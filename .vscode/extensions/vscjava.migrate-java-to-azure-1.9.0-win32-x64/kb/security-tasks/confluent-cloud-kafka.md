---
id: confluent-cloud-kafka
title: Migrate Kafka to Apache Kafka on Confluent Cloud with Entra ID Auth
description: Migrate Kafka to Apache Kafka on Confluent Cloud with Entra ID authentication
hierarchy: Security Tasks
showTask: false
---


Apache Kafka → Apache Kafka for Confluent Cloud with Passwordless Auth

---
### Context
Your Java project currently uses Apache Kafka as event streaming middleware
Key Points:
1. You need to migrate the project from Apache Kafka to Apache Kafka for Confluent Cloud.
2. Auth to Apache Kafka for Confluent Cloud should be passwordless.
3. Please change files directly, don't leave incomplete code or config.
4. For params you don't know, such as server URL or port, please use examples like server-url:port.
5. Do not try to reformat the project files. Just do in-place migration with minimum changes.
6. Do not try to fix any bugs even if you know they're incorrect.
---

### Actionable Steps

#### Update Dependencies
- Update the `pom.xml` or `build.gradle` file to include the Azure Identity package. Use the latest version available.
  ```xml
    <!-- For Maven -->
    <dependency>
      <groupId>com.azure</groupId>
      <artifactId>azure-identity</artifactId>
      <version>1.16.2</version> <!-- Use the latest version -->
    </dependency>
  ```

  ```groovy
    // For Gradle
    implementation 'com.azure:azure-identity:${THE_LATEST_VERSION}' // Use the latest version
  ```

#### Supplement Customized OAuth Bearer Login Callback Handler
  - Add a new class in a proper location like the config folder for a customized oauth bearer login callback handler. Below is the sample code for such class.
  ```java
    package ${PATH_TO_CUSTOMIZED_OAUTH_BEARER_LOGIN_CALLBACK_HANDLER}; // replace with the actual package path

    import com.azure.core.credential.AccessToken;
    import com.azure.core.credential.TokenCredential;
    import com.azure.core.credential.TokenRequestContext;
    import com.azure.identity.DefaultAzureCredential;
    import com.azure.identity.DefaultAzureCredentialBuilder;
    import org.apache.kafka.common.security.oauthbearer.OAuthBearerLoginCallbackHandler;
    import org.apache.kafka.common.security.oauthbearer.internals.secured.AccessTokenRetriever;
    import org.apache.kafka.common.security.oauthbearer.internals.secured.AccessTokenValidator;

    import javax.security.auth.login.AppConfigurationEntry;
    import java.util.List;
    import java.util.Map;


    /**
    * The official {@link OAuthBearerLoginCallbackHandler} uses {@link org.apache.kafka.common.security.oauthbearer.internals.secured.HttpAccessTokenRetriever}
    * by default, which will send POST request to the token endpoint with {@code grant_type=client_credentials} in payload.
    * This results in the following error from the Azure Metadata Service endpoint:
    *
    * <pre>
    *     The response code 405 and error response {"invalid_request" - "Request Method not supported for the API"} was encountered reading the token endpoint response; will not attempt further retries
    * </pre>
    *
    * Here we create a custom {@link OAuthBearerLoginCallbackHandler} that uses the Azure SDK's {@link DefaultAzureCredential}
    * to fetch the token.
    */
    public class AzureManagedIdentityCallbackHandler extends OAuthBearerLoginCallbackHandler {
        private static final String SASL_OAUTHBEARER_TOKEN_RESOURCE = "sasl.oauthbearer.token.resource";

        private final TokenCredential credential;
        private String resource;

        public AzureManagedIdentityCallbackHandler() {
            this(new DefaultAzureCredentialBuilder().build());
        }

        public AzureManagedIdentityCallbackHandler(TokenCredential credential) {
            this.credential = credential;
        }

        @Override
        public void configure(Map<String, ?> configs, String saslMechanism, List<AppConfigurationEntry> jaasConfigEntries) {
            Object resource = configs.get(SASL_OAUTHBEARER_TOKEN_RESOURCE);
            if (resource == null) {
                throw new IllegalArgumentException(SASL_OAUTHBEARER_TOKEN_RESOURCE + " was not set.");
            }
            if (!(resource instanceof String)) {
                throw new IllegalArgumentException(SASL_OAUTHBEARER_TOKEN_RESOURCE + " must be a String.");
            }
            this.resource = (String) resource;
            super.configure(configs, saslMechanism, jaasConfigEntries);
        }

        @Override
        public void init(AccessTokenRetriever accessTokenRetriever, AccessTokenValidator accessTokenValidator) {
            AccessTokenRetriever retriever = () -> {
                AccessToken token = credential.getTokenSync(new TokenRequestContext().addScopes(resource));
                return token.getToken();
            };
            super.init(retriever, accessTokenValidator);
        }
    }
  ```

#### Update Config Files (.properties/.yaml)
- Update Kafka server URL with Confluent Cloud server URL. Here use .properties file as example.
  ```
    # Before (Apache Kafka)
    # Other properties
    bootstrap.servers=localhost:9092,another-local-ip-address:9092
    # Other properties

    # After (Confluent Cloud with Passwordless Auth via Azure Managed Identity)

    # Other properties

    # Confluent Cloud
    # Set this to your Kafka cluster's bootstrap server address.
    spring.kafka.bootstrap-servers=some-name.some-region.azure.confluent.cloud:9092

    # Confluent Cloud with Passwordless Auth via Azure Managed Identity
    # Replace with actual values
    spring.kafka.properties.security.protocol=SASL_SSL
    spring.kafka.properties.sasl.oauthbearer.token.endpoint.url=http://169.254.169.254/metadata/identity/oauth2/token?api-version=2018-02-01&resource=${ENTRA_ID_ENTERPRISE_APPLICATION_ID}
    spring.kafka.properties.sasl.oauthbearer.token.resource=${ENTRA_ID_ENTERPRISE_APPLICATION_ID}
    spring.kafka.properties.sasl.login.callback.handler.class=${PATH_TO_CUSTOMIZED_OAUTH_BEARER_LOGIN_CALLBACK_HANDLER} // this is the path of the class you created above
    spring.kafka.properties.sasl.mechanism=OAUTHBEARER
    spring.kafka.properties.sasl.jaas.config= \
      org.apache.kafka.common.security.oauthbearer.OAuthBearerLoginModule required \
          clientId='ignored' \
          clientSecret='ignored' \
          extension_logicalCluster='${LOGIC_CLUSTER_ID}' \
          extension_identityPoolId='${IDENTITY_POOL_ID}';

    # Other properties
  ```


---

### Key Takeaways
1. Make sure to use a Confluent Cloud server URL after migration.
2. Make sure to remove any Kafka relevant credential configs in project files.
3. For properties migrations:
  - Files need to be migrated are files with bootstrap.server(s), bootstrapServer(s) or similar properties. Do not migrate files without these properties.
  - Make sure to update the config field only once in the file which the config take effect in it.
  - **DO NOT** touch configs that are not relevant to the Kafka or Confluent Cloud even you know they're invalid or incorrect.
