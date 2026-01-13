---
id: auth-by-mi-for-azure-redis-in-micronaut-project
title: Via Micronaut
description: Secure Azure Cache for Redis with Managed Identity via Micronaut
hierarchy: Security Tasks / Secure Azure Cache for Redis with Managed Identity
---

Your task is to migrate a Micronaut Java project to use managed identity for authentication in Azure Cache for Redis / Azure Managed Redis instead of a password.

1. Add dependency.
    Example 1: Diff in build.gradle.
    ```diff
    + implementation("com.azure:azure-identity:1.16.2")
    ```
    Example 2: Diff in pom.xml.
    ```diff
    + <dependency>
    +     <groupId>com.azure</groupId>
    +     <artifactId>azure-identity</artifactId>
    +     <version>1.16.2</version>
    + </dependency>
    ```
    Note: Please check the latest compatible version of the dependency, and upgrade the version if possible.

2. Update property file.
    ```diff
    redis:
    -  uri: redis://localhost:6379
    -  username:
    +  # AZURE_REDIS_URI should be set to your Azure Redis instance URI, e.g., `rediss://example.redis.cache.windows.net:6380`.
    +  uri: ${AZURE_REDIS_URI}
    +  # AZURE_MANAGED_IDENTITY_OBJECT_ID should be set to the Object ID of your Managed Identity or Service Principal, e.g., `12345678-90ab-cdef-1234-567890abcdef`.
    +  username: ${AZURE_MANAGED_IDENTITY_OBJECT_ID}
    +
    +azure:
    +  redis:
    +    # Optional: AZURE_MANAGED_IDENTITY_CLIENT_ID for user-assigned managed identity, e.g., `12345678-90ab-cdef-1234-567890abcdef`.
    +    # If not set, system-assigned managed identity or default credential chain will be used.
    +    client-id: ${AZURE_MANAGED_IDENTITY_CLIENT_ID:}
    ```

3. Create AzureRedisCredentialsConfiguration.
   ```java
   package com.example.config;

   import com.azure.core.credential.TokenRequestContext;
   import com.azure.identity.DefaultAzureCredential;
   import com.azure.identity.DefaultAzureCredentialBuilder;
   import io.lettuce.core.RedisCredentials;
   import io.lettuce.core.RedisCredentialsProvider;
   import io.micronaut.configuration.lettuce.AbstractRedisConfiguration;
   import io.micronaut.context.annotation.Requires;
   import io.micronaut.context.annotation.Value;
   import io.micronaut.context.event.BeanCreatedEvent;
   import io.micronaut.context.event.BeanCreatedEventListener;
   import jakarta.inject.Singleton;
   import org.slf4j.Logger;
   import org.slf4j.LoggerFactory;
   import reactor.core.publisher.Mono;


   /**
    * Configuration for Azure Redis with Managed Identity authentication.
    * It enables Managed Identity authentication for Azure Cache for Redis and Azure Managed Redis.
    * <p>
    * This configuration uses a {@link BeanCreatedEventListener} to intercept the creation of
    * {@link AbstractRedisConfiguration} and inject a {@link RedisCredentialsProvider} that uses
    * Azure Managed Identity for authentication. This approach preserves all existing
    * Micronaut Redis Lettuce features including:
    * <ul>
    *   <li>Connection pooling configuration ({@code redis.pool.*})</li>
    *   <li>Master-replica setup ({@code redis.replica-uris})</li>
    *   <li>Read-from configuration ({@code redis.read-from})</li>
    *   <li>Multiple Redis servers ({@code redis.servers.*})</li>
    *   <li>Client resources configuration (IO/computation thread pools)</li>
    * </ul>
    * <p>
    * The provider uses {@link DefaultAzureCredential} to obtain access tokens from Azure Active Directory
    * and automatically refreshes tokens before they expire.
    * <p>
    * <strong>Prerequisites:</strong>
    * <ul>
    *   <li>The Managed Identity must have the authority to access Redis (e.g., "Redis Cache Data Owner" or "Redis Cache Data Contributor" role).</li>
    *   <li>Environment variable/property {@code redis.username} should be set to the Object ID of the Managed Identity.</li>
    *   <li>Optional: For user-assigned managed identity, set {@code azure.redis.client-id} to the Client ID of the user-assigned managed identity.</li>
    * </ul>
    * <p>
    * <strong>Configuration Example:</strong>
    * <pre>
    * redis:
    *   uri: rediss://example.redis.cache.windows.net:6380
    *   username: ${AZURE_MANAGED_IDENTITY_OBJECT_ID}
    *   pool:
    *     enabled: true
    *     max-total: 8
    * azure:
    *   redis:
    *     client-id: ${AZURE_MANAGED_IDENTITY_CLIENT_ID:}  # Optional, for user-assigned MI
    * </pre>
    *
    * @see DefaultAzureCredential
    * @see RedisCredentialsProvider
    * @see BeanCreatedEventListener
    */
   @Singleton
   @Requires(property = "redis.username")
   public class AzureRedisCredentialsConfiguration implements BeanCreatedEventListener<AbstractRedisConfiguration> {

       private static final Logger LOG = LoggerFactory.getLogger(AzureRedisCredentialsConfiguration.class);
       private static final String REDIS_SCOPE = "https://redis.azure.com/.default";

       private final String username;
       private final DefaultAzureCredential credential;
       private final TokenRequestContext tokenContext;

       public AzureRedisCredentialsConfiguration(
               @Value("${redis.username}") String username,
               @Value("${azure.redis.client-id:}") String clientId) {
           if (username == null || username.trim().isEmpty()) {
               throw new IllegalArgumentException("Azure Redis username must be set and non-empty (property: redis.username). " +
                   "This should be the Object ID of your Managed Identity or Service Principal.");
           }
           this.username = username.trim();

           // Build credential with optional client ID for user-assigned managed identity
           DefaultAzureCredentialBuilder builder = new DefaultAzureCredentialBuilder();
           if (clientId != null && !clientId.trim().isEmpty()) {
               builder.managedIdentityClientId(clientId.trim());
               LOG.info("Initialized Azure Managed Identity credentials provider with user-assigned managed identity (client ID: {}) for Redis authentication", clientId.trim());
           } else {
               LOG.info("Initialized Azure Managed Identity credentials provider with default credential chain for Redis authentication");
           }
           this.credential = builder.build();

           this.tokenContext = new TokenRequestContext().addScopes(REDIS_SCOPE);
           LOG.info("Using Redis username (object ID): {}", this.username);
       }

       @Override
       public AbstractRedisConfiguration onCreated(BeanCreatedEvent<AbstractRedisConfiguration> event) {
           AbstractRedisConfiguration config = event.getBean();

           // Set credentials provider on the RedisURI if present
           config.getUri().ifPresent(uri -> {
               uri.setCredentialsProvider(this::resolveAzureCredentials);
               LOG.info("Injected Azure Managed Identity credentials provider into RedisURI: {}", uri.getHost());
           });

           // Also set on the configuration itself (as it extends RedisURI)
           config.setCredentialsProvider(this::resolveAzureCredentials);
           LOG.info("Injected Azure Managed Identity credentials provider into Redis configuration");

           return config;
       }

       private Mono<RedisCredentials> resolveAzureCredentials() {
           return Mono.defer(() -> {
               LOG.debug("Resolving Azure Managed Identity credentials for Redis");
               return credential.getToken(tokenContext)
                   .doOnNext(token -> LOG.debug("Successfully obtained/refreshed Azure access token for Redis authentication"))
                   .map(token -> RedisCredentials.just(username, token.getToken()));
           });
       }

   }
   ```
