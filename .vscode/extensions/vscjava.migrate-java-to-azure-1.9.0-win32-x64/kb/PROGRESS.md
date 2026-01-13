# Java Modernization Map Project Deliverables Checklist

This document tracks the progress of all deliverables for the Java Modernization Map project.

After created new kb file, mark the corresponding item as completed by checking the checkbox `- [x] ` of the list item

## Prevalence Score Legend

The numbers in square brackets [1-10] after each source technology (X) indicate how commonly this technology is encountered in systems that need modernization:

- [1]: Extremely rare - Almost never encountered in systems needing migration
- [2]: Very rare - Seldom encountered in systems needing migration
- [3]: Rare - Found in specialized legacy systems
- [4]: Uncommon - Occasionally found in legacy systems
- [5]: Somewhat common - Appears in a notable subset of modernization projects
- [6]: Moderately common - Regularly appears in modernization projects
- [7]: Common - Frequently found in systems needing modernization
- [8]: Very common - Expected in most legacy Java systems
- [9]: Highly common - Present in the vast majority of legacy Java systems
- [10]: Extremely common - Nearly ubiquitous in legacy Java systems

These scores help prioritize migration paths based on how likely you are to encounter the source technology in real-world modernization scenarios.

### Prevalence Score Timeline Considerations (Last Updated: June 2025)

The prevalence scores reflect the estimated frequency of encountering these technologies in modernization projects **as of June 2025**. These scores take into account:

1. **Technology Lifecycle Stage**: Where technologies fall in their adoption, maturity, and obsolescence curve.
   - Example: JDK 7 is rated [5] (somewhat common) as most organizations have moved to JDK 8+ by 2025.
   - Example: Quarkus is rated [6] (moderately common) reflecting its increased adoption since 2019.

2. **Historical Dominance Period**: When a technology was at its peak adoption.
   - Example: Java EE and Spring Web Applications are both rated [9] (highly common) reflecting that both
     represent major waves of enterprise Java development, with Java EE dominating earlier (pre-2015)
     and Spring becoming more prevalent after that.

3. **Modernization Lag Period**: The typical time between a technology becoming "legacy" and actively being targeted for modernization (typically 5-10 years).

4. **Enterprise Adoption Patterns**: Differences between early adopter organizations and more conservative enterprises.

As time passes, these scores will need to be reassessed as technologies continue to evolve and adoption patterns change.

## Modernization Scenarios

NOTE: **the file path in each scenario is relative to the path of this `PROGRESS.md` file.**

### 1. Azure Integration & Onboarding

*(Specific code and configuration changes within the repository to connect and leverage the Azure cloud platform.)*

#### 1.1. Azure Compute & Hosting Configuration

This subcategory details migrations of Java applications and workloads from on-premise infrastructure to various Azure compute and hosting services. It covers lift-and-shift scenarios for traditional application servers like Tomcat and JBoss to Azure App Service, containerizing applications for Azure Kubernetes Service (AKS) and Azure Container Apps, and migrating existing container orchestrators to AKS. It also includes modernizing web hosting patterns by moving to services like Azure Static Web Apps and Azure Front Door.

- [ ] Tomcat Server on-premise [9] → Tomcat on Azure App Service (azure/compute-hosting/tomcat-onprem-to-tomcat-on-appservice.md)
- [ ] WildFly/JBoss on-premise [7] → WildFly/JBoss on Azure App Service (azure/compute-hosting/wildfly-onprem-to-wildfly-on-appservice.md)
- [ ] Java EE on-premise [7] → Java EE on Azure App Service (azure/compute-hosting/java-ee-onprem-to-java-ee-app-service.md)
- [ ] Java WAR on-premise [10] → Java WAR on Azure Web Apps (azure/compute-hosting/java-war-onprem-to-war-on-webapps.md)
- [ ] Spring Boot on-premise [9] → Spring Boot on Azure Container Apps (azure/compute-hosting/spring-boot-onprem-to-spring-boot-container-apps.md)
- [ ] Quarkus on-premise [6] → Quarkus on Azure Container Apps (azure/compute-hosting/quarkus-onprem-to-quarkus-container-apps.md)
- [ ] Java Monolith on VM [9] → Java Monolith on Azure Kubernetes Service (azure/compute-hosting/java-monolith-vm-to-java-monolith-aks.md)
- [ ] Docker Swarm [6] → Azure Kubernetes Service (azure/compute-hosting/docker-swarm-to-aks.md)
- [ ] On-premise Kubernetes [8] → Azure Kubernetes Service (azure/compute-hosting/onprem-k8s-to-aks.md)
- [ ] Java Microservices [8] → Azure Service Fabric (azure/compute-hosting/java-microservices-to-service-fabric.md)
- [ ] Java Web Apps [7] → Azure Static Web Apps (azure/compute-hosting/java-webapps-to-static-webapps.md)
- [ ] Java Local Caching [8] → Azure Front Door for CDN (azure/compute-hosting/java-local-caching-to-azure-front-door.md)

#### 1.2. Azure Data Services Integration

This subcategory focuses on migrating Java applications from using on-premise or self-hosted data stores to managed Azure data services. It covers a variety of scenarios, including transitioning relational databases (PostgreSQL, MySQL, MariaDB) to their Azure Database equivalents and modernizing data access layers (JDBC, Hibernate, Spring Data JPA) to connect to them. It also includes migrations from NoSQL databases like MongoDB to Azure Cosmos DB and moving self-hosted search indexes to the Azure Elasticsearch Service.

- [ ] JDBC with On-premise DB [9] → JDBC with Azure SQL Database (azure/data-services/jdbc-onprem-db-to-jdbc-azure-sql.md)
- [ ] Hibernate with On-premise DB [8] → Hibernate with Azure SQL Database (azure/data-services/hibernate-onprem-db-to-hibernate-azure-sql.md)
- [ ] Spring Data JPA with On-premise DB [9] → Spring Data JPA with Azure SQL Database (azure/data-services/spring-data-jpa-onprem-db-to-spring-data-jpa-azure-sql.md)
- [ ] Connection Pooling with On-premise DB [8] → Connection Pooling with Azure Databases (azure/data-services/connection-pooling-onprem-db-to-connection-pooling-azure.md)
- [ ] PostgreSQL Self-hosted [8] → Azure Database for PostgreSQL (azure/data-services/postgresql-self-hosted-to-azure-postgresql.md)
- [ ] MySQL Self-hosted [9] → Azure Database for MySQL (azure/data-services/mysql-self-hosted-to-azure-mysql.md)
- [ ] MariaDB Self-hosted [5] → Azure Database for MariaDB (azure/data-services/mariadb-self-hosted-to-azure-mariadb.md)
- [ ] JPA with RDBMS [9] → JPA with Azure Cosmos DB (azure/data-services/jpa-rdbms-to-jpa-cosmos-db.md)
- [ ] MongoDB Java Driver [7] → MongoDB Java Driver with Azure Cosmos DB API (azure/data-services/mongo-driver-to-mongo-driver-cosmos-db.md)
- [ ] Spring Data MongoDB with Self-hosted MongoDB [7] → Spring Data MongoDB with Azure Cosmos DB (azure/data-services/spring-data-mongo-to-spring-data-mongo-cosmos-db.md)
- [ ] Elasticsearch Self-hosted [6] → Azure Elasticsearch Service (azure/data-services/elasticsearch-self-hosted-to-azure-elasticsearch.md)

#### 1.3. Azure Messaging & Integration Services

This subcategory covers modernizing integration patterns by migrating from self-hosted or on-premise messaging systems and API approaches to managed Azure services. It includes adopting Azure API Management for securing and managing APIs, replacing message brokers like ActiveMQ and RabbitMQ with Azure Service Bus, transitioning from Apache Kafka to Azure Event Hubs for real-time data streaming, and moving from code-based integration frameworks like Apache Camel to low-code solutions like Azure Logic Apps.

- [ ] Java REST API [9] → Azure API Management with Policies (azure/messaging-services/java-rest-api-to-apim-with-policies.md)
- [ ] GraphQL Java [5] → Azure API Management with GraphQL support (azure/messaging-services/graphql-java-to-apim-graphql.md)
- [ ] Java RPC Services [4] → Java RPC Services with Azure API Management (azure/messaging-services/java-rpc-with-apim.md)
- [ ] Direct API Calls [7] → Azure Service Bus (azure/messaging-services/direct-api-calls-to-azure-service-bus.md)
- [ ] JMS with ActiveMQ [7] → JMS with Azure Service Bus (azure/messaging-services/jms-activemq-to-jms-servicebus.md)
- [ ] RabbitMQ [6] → Azure Service Bus (azure/messaging-services/rabbitmq-to-servicebus.md)
- [ ] Apache Kafka [9] → Azure Event Hubs (azure/messaging-services/kafka-to-event-hubs.md)
- [ ] Spring Cloud Stream with Kafka [6] → Spring Cloud Stream with Azure Event Hub (azure/messaging-services/spring-stream-kafka-to-spring-stream-eventhub.md)
- [ ] Java Message Listeners [7] → Azure Event Grid Subscribers (azure/messaging-services/message-listeners-to-event-grid.md)
- [ ] Apache Camel [5] → Azure Logic Apps with Integration Account (azure/messaging-services/apache-camel-to-azure-logic-apps.md)
- [ ] Spring Integration [6] → Azure Logic Apps (azure/messaging-services/spring-integration-to-azure-logic-apps.md)

#### 1.4. Azure Security, Identity, & Configuration

This subcategory focuses on enhancing security and modernizing configuration and identity management by integrating with managed Azure services. It includes migrating from file-based or self-hosted solutions to centralized services like Azure App Configuration for application settings and Azure Key Vault for secrets management. It also covers updating authentication and authorization mechanisms from local or legacy systems (like LDAP) to Azure Active Directory, and adopting Azure's robust role-based access control (RBAC).

- [ ] Spring Cloud Config [6] → Azure App Configuration (with Feature Flags) (azure/security-identity-config/spring-cloud-config-to-app-config-with-feature-flags.md)
- [ ] Spring Security with Local Auth [8] → Spring Security with Azure Active Directory (azure/security-identity-config/spring-security-local-auth-to-spring-security-aad.md)
- [ ] JAAS with Local Auth [4] → JAAS with Azure Active Directory (azure/security-identity-config/jaas-local-auth-to-jaas-with-aad.md)
- [ ] Java LDAP Auth [7] → Azure Active Directory (azure/security-identity-config/java-ldap-auth-to-azure-ad.md)
- [ ] Java OAuth2/OIDC with Custom IDP [6] → Java OAuth2/OIDC with Azure AD B2C (azure/security-identity-config/oauth-custom-idp-to-oauth-azure-b2c.md)
- [ ] Custom Role Management [7] → Azure RBAC (azure/security-identity-config/custom-role-mgmt-to-azure-rbac.md)
- [ ] Secrets in properties files [7] → Azure Key Vault (azure/security-identity-config/properties-secrets-to-azure-key-vault.md)
- [ ] Java KeyStore [8] → Azure Key Vault (azure/security-identity-config/java-keystore-to-azure-key-vault.md)
- [ ] Spring Vault with Local Secrets [4] → Spring Vault with Azure Key Vault (azure/security-identity-config/spring-vault-local-to-spring-vault-key-vault.md)
- [ ] Java Key Management [5] → Azure Managed HSM (azure/security-identity-config/java-key-mgmt-to-azure-managed-hsm.md)
- [ ] Java Certificate Management [5] → Azure App Service Certificates (azure/security-identity-config/java-cert-mgmt-to-azure-app-certificates.md)

#### 1.5. Azure Storage Integration

This subcategory addresses the modernization of data storage by migrating applications from local file systems, on-premise object storage, or other cloud providers to Azure's scalable and managed storage services. It covers transitions to Azure Blob Storage for unstructured object data, Azure Files for shared file access, and Azure Queue Storage for simple, reliable messaging. This includes both infrastructure shifts and code-level changes, such as replacing standard file I/O operations with the Azure Storage SDK.

- [ ] Local Object Storage [7] → Azure Blob Storage (azure/storage/local-object-storage-to-azure-blob.md)
- [ ] AWS S3 [8] → Azure Blob Storage (azure/storage/aws-s3-to-azure-blob.md)
- [ ] Java NIO File Operations [7] → Azure Blob Storage SDK (azure/storage/java-nio-to-azure-blob-sdk.md)
- [ ] Local File System [10] → Azure Files (azure/storage/local-fs-to-azure-files.md)
- [ ] Local Queue Processing [7] → Azure Queue Storage (azure/storage/local-queue-processing-to-azure-queue.md)

#### 1.6. Azure Observability Integration

This subcategory focuses on integrating Java applications with Azure's native observability platform to gain deep insights into performance, health, and usage. It covers migrating from traditional logging frameworks, JMX-based monitoring, and open-source metric collectors like Prometheus to centralized solutions like Azure Monitor and Azure Application Insights. It also includes modernizing distributed tracing by shifting from self-hosted backends like Jaeger to the fully managed tracing capabilities within Application Insights.

- [ ] Log4j/Logback [10] → Azure Application Insights (azure/observability/java-logging-to-app-insights.md)
- [ ] JMX Monitoring [6] → Azure Monitor (azure/observability/jmx-monitoring-to-azure-monitor.md)
- [ ] Micrometer with Prometheus [7] → Micrometer with Azure Monitor (azure/observability/micrometer-prometheus-to-micrometer-azure.md)
- [ ] Spring Boot Actuator [7] → Azure Monitor (azure/observability/spring-boot-actuator-to-azure-monitor.md)
- [ ] OpenTelemetry Java with Jaeger [6] → OpenTelemetry Java with Azure Application Insights (azure/observability/opentelemetry-jaeger-to-opentelemetry-app-insights.md)

#### 1.7. Azure Caching Integration

This subcategory focuses on modernizing application performance by migrating from self-hosted caching solutions to Azure's managed, high-performance caching services. It includes lift-and-shift migrations for applications already using Redis, as well as replacing other in-memory data grids like Hazelcast with Azure Cache for Redis to leverage a fully managed environment.

- [ ] Java Redis Client with Self-hosted Redis [7] → Java Redis Client with Azure Cache for Redis (azure/caching/redis-client-to-azure-cache-for-redis.md)
- [ ] Spring Data Redis with Self-hosted Redis [6] → Spring Data Redis with Azure Cache for Redis (azure/caching/spring-data-redis-to-azure-cache-for-redis.md)
- [x] Hazelcast [5] → Azure Cache for Redis for Distributed Caching (cache-tasks/hazelcast-to-azure-cache-for-redis.md)
- [x] Spring Cache Backends [] → Spring Cache for Azure Cache for Redis (cache-tasks/spring-cache-backends-to-spring-cache-with-azure-cache-for-redis.md)

#### 1.8. Azure AI & Advanced Services Integration

- [ ] Java AI Libraries [6] → Azure AI SDK (azure/ai-services/java-ai-libs-to-azure-ai-sdk.md)
- [ ] Local LLM Models [6] → Azure OpenAI Service Integration (azure/ai-services/local-llm-to-azure-openai.md)

#### 1.9. Azure Serverless & Processing Integration

This subcategory covers the transition of traditional Java workloads and processing patterns to Azure's serverless and event-driven platforms. It focuses on re-architecting applications by migrating scheduled jobs, batch processes, and even entire web applications to Azure Functions. It also includes replacing code-based workflow engines with managed solutions like Azure Logic Apps, enabling more scalable, cost-effective, and event-triggered application logic.

- [ ] Java Background Processes [9] → Azure Functions (azure/serverless-processing/java-background-processes-to-azure-functions.md)
- [ ] Tomcat Deployment [10] → Azure Functions (azure/serverless-processing/tomcat-deployment-to-azure-functions.md)
- [ ] Java Scheduled Tasks [8] → Azure Functions with Timer Trigger (azure/serverless-processing/java-scheduled-tasks-to-azure-functions.md)
- [ ] Quartz Scheduler [7] → Azure Functions (azure/serverless-processing/quartz-scheduler-to-azure-functions.md)
- [ ] Spring Batch Jobs [7] → Azure Functions (azure/serverless-processing/spring-batch-to-azure-functions.md)
- [ ] Java File Processing [10] → Azure Blob Storage + Functions (azure/serverless-processing/java-file-processing-to-blob-functions.md)
- [ ] Java Workflow Engine [7] → Azure Logic Apps (azure/serverless-processing/java-workflow-engine-to-logic-apps.md)

#### 1.10. General Azure SDK Integration

- [ ] Self-hosted [9] → Azure SDK for Java (azure/general-sdk/self-hosted-to-azure-sdk-for-java.md)


### 2. Foundational Application Modernization
*(Core code improvements to pay down technical debt and modernize frameworks, independent of the cloud target.)*

#### 2.1. Core Platform & Libraries
This category focuses on modernizing the core Java platform and its standard libraries. It includes scenarios for upgrading JDK versions and distributions, adopting modern language features like records and lambdas, migrating from legacy concurrency models to modern ones like virtual threads, and replacing outdated third-party utility libraries with standard Java APIs.

- [ ] JDK 6 [3] → JDK 7 (foundational/core-platform/jdk6-to-jdk7.md)
- [ ] JDK 7 [5] → JDK 8 (foundational/core-platform/jdk7-to-jdk8.md)
- [ ] JDK 8 [10] → JDK 11 (foundational/core-platform/jdk8-to-jdk11.md)
- [ ] JDK 11 [9] → JDK 17 (foundational/core-platform/jdk11-to-jdk17.md)
- [ ] JDK 17 [8] → JDK 21 (foundational/core-platform/jdk17-to-jdk21.md)
- [ ] JDK 21 [5] → JDK 25 (foundational/core-platform/jdk21-to-jdk25.md)
- [ ] Oracle JDK 8 → Temurin JDK 8 (foundational/core-platform/oracle-jdk8-to-temurin-jdk8.md)
- [ ] Oracle JDK 11 → Temurin JDK 11 (foundational/core-platform/oracle-jdk11-to-temurin-jdk11.md)
- [ ] Oracle JDK 17 [6] → Temurin JDK 17 (foundational/core-platform/oracle-jdk17-to-temurin-jdk17.md)
- [ ] Oracle JDK 21 → Temurin JDK 21 (foundational/core-platform/oracle-jdk21-to-temurin-jdk21.md)
- [ ] Anonymous Classes [9] → Java Lambda Expressions (foundational/core-platform/anonymous-classes-to-lambdas.md)
- [ ] Legacy Java Collections [9] → Java Stream API (foundational/core-platform/legacy-collections-to-stream-api.md)
- [ ] Legacy For-Loops [9] → Java Enhanced For-Each (foundational/core-platform/legacy-loops-to-for-each.md)
- [ ] Class Data Transfer Objects [7] → Java Records (foundational/core-platform/class-dto-to-records.md)
- [ ] Project Lombok [10] → Java 16+ Records (foundational/core-platform/project-lombok-to-java-records.md)
- [ ] Verbose Type Declarations [8] → Java 10+ var Type Inference (foundational/core-platform/verbose-types-to-var.md)
- [ ] Pattern Matching if-else [8] → Java 17+ Switch Expressions (foundational/core-platform/if-else-to-switch-expressions.md)
- [ ] Pre-JPMS Java [8] → Java Module System (JPMS) (foundational/core-platform/pre-jpms-to-jpms.md)
- [ ] Thread/Runnable [10] → Executors Framework (foundational/core-platform/thread-runnable-to-executors-framework.md)
- [ ] Executor Service [8] → Virtual Threads (foundational/core-platform/executor-service-to-virtual-threads.md)
- [ ] Classic Exception Handling [6] → Java CompletableFuture (foundational/core-platform/classic-exception-handling-to-completablefuture.md)
- [ ] CountDownLatch/CyclicBarrier [6] → CompletableFuture (foundational/core-platform/latches-barriers-to-completablefuture.md)
- [ ] Blocking Queue [7] → Reactive Streams (foundational/core-platform/blocking-queue-to-reactive-streams.md)
- [ ] Apache Commons Lang [10] → Java 8+ APIs (foundational/core-platform/apache-commons-lang-to-java-apis.md)
- [ ] Apache Commons Collections [10] → Java Collections + Stream API (foundational/core-platform/apache-commons-collections-to-java-collections.md)
- [ ] Apache Commons JCI [4] → Java Compiler API (foundational/core-platform/apache-commons-jci-to-compiler-api.md)
- [ ] Google Guava Utilities [8] → Java 8+ APIs (foundational/core-platform/guava-utilities-to-java-apis.md)
- [ ] Google Guava Collections [8] → Java Collections (foundational/core-platform/guava-collections-to-java-collections.md)
- [ ] Apache XML [7] → JAXB / Jakarta XML Binding (foundational/core-platform/apache-xml-to-jakarta-xml-binding.md)

#### 2.2. Application & Web Frameworks

This category covers the modernization of application runtimes, web frameworks, and their core components. It includes scenarios for upgrading major ecosystems like Spring and Jakarta EE, migrating from legacy or obsolete frameworks (e.g., Struts, JSF-centric stacks) to modern alternatives, and evolving application packaging and component models.

##### Spring Ecosystem Modernization

- [ ] Spring Framework 4.x [8] → Spring Framework 5.x (foundational/application-frameworks/spring-framework-4-to-5.md)
- [ ] Spring Framework 5.x [9] → Spring Framework 6.x (foundational/application-frameworks/spring-framework-5-to-6.md)
- [ ] Spring Boot 1.x [8] → Spring Boot 2.x (foundational/application-frameworks/spring-boot-1-to-2.md)
- [ ] Spring Boot 2.x [10] → Spring Boot 3.x (foundational/application-frameworks/spring-boot-2-to-3.md)
- [ ] Spring XML Config [10] → Spring Java Config (foundational/application-frameworks/spring-xml-to-java-config.md)
- [ ] Spring XML Config [10] → Spring Boot Auto-configuration (foundational/application-frameworks/spring-xml-to-boot-autoconfig.md)
- [ ] Spring MVC [9] → Spring WebFlux (foundational/application-frameworks/spring-mvc-to-webflux.md)
- [ ] Servlet-based Spring [9] → Spring Native (foundational/application-frameworks/servlet-spring-to-spring-native.md)
- [ ] Servlet Filters [10] → Spring WebFilter (foundational/application-frameworks/servlet-filters-to-spring-webfilter.md)

##### Jakarta EE / Java EE Ecosystem Modernization

- [ ] JavaEE 7 [8] → Jakarta EE 8 (foundational/application-frameworks/java-ee-7-to-jakarta-ee-8.md)
- [ ] JavaEE 8 [8] → Jakarta EE 9 (foundational/application-frameworks/java-ee-8-to-jakarta-ee-9.md)
- [ ] Jakarta EE 9 [6] → Jakarta EE 10 (foundational/application-frameworks/jakarta-ee-9-to-10.md)
- [ ] Javax [9] → Jakarta Namespace (foundational/application-frameworks/javax-to-jakarta.md)
- [ ] JSF 1.x [6] → JSF 2.3 (foundational/application-frameworks/jsf1-to-jsf23.md)
- [ ] JSF 2.3 [8] → Jakarta Faces 4.0 (foundational/application-frameworks/jsf23-to-jakarta-faces4.md)
- [ ] PrimeFaces 6.x [7] → PrimeFaces 12.x (foundational/application-frameworks/primefaces6-to-primefaces12.md)
- [ ] PrimeFaces JSF 2.x [7] → PrimeFaces Jakarta Faces 4.0 (foundational/application-frameworks/primefaces-jsf-to-jakarta-faces.md)
- [ ] RichFaces [5] → PrimeFaces (foundational/application-frameworks/richfaces-to-primefaces.md)
- [ ] ICEfaces [6] → PrimeFaces (foundational/application-frameworks/icefaces-to-primefaces.md)
- [ ] OmniFaces 3.x [5] → OmniFaces 4.x (foundational/application-frameworks/omnifaces3-to-omnifaces4.md)
- [ ] JSF Managed Beans [9] → CDI Beans (foundational/application-frameworks/jsf-managed-beans-to-cdi.md)
- [ ] EJB [8] → CDI (foundational/application-frameworks/ejb-to-cdi.md)
- [ ] CDI 2 [7] → CDI 4 (foundational/application-frameworks/cdi-2-to-4.md)
- [ ] Java EE Interceptors [6] → CDI Interceptors (foundational/application-frameworks/javaee-interceptors-to-cdi-interceptors.md)
- [ ] JSR-303 Bean Validation [8] → Jakarta Bean Validation 3.0 (foundational/application-frameworks/jsr303-bean-validation-to-jakarta-validation.md)
- [ ] Bean Validation 2 [6] → Bean Validation 3 (foundational/application-frameworks/bean-validation-2-to-3.md)
- [ ] JAX-RS 2 [7] → JAX-RS 3 (foundational/application-frameworks/jax-rs-2-to-3.md)
- [ ] Servlet 4 [7] → Servlet 6 (foundational/application-frameworks/servlet-4-to-6.md)
- [ ] Jakarta Core Profile [5] → Jakarta Web Profile (foundational/application-frameworks/jakarta-core-profile-to-web-profile.md)
- [ ] Jakarta Web Profile [6] → Jakarta Full Platform (foundational/application-frameworks/jakarta-web-profile-to-full-platform.md)
- [ ] JBoss EAP [7] → WildFly (foundational/application-frameworks/jboss-eap-to-wildfly.md)
- [ ] Tomcat [9] → TomEE (foundational/application-frameworks/tomcat-to-tomee.md)
- [ ] WebLogic [7] → Open Liberty (foundational/application-frameworks/weblogic-to-openliberty.md)
- [ ] WebSphere [7] → Open Liberty (foundational/application-frameworks/websphere-to-openliberty.md)

##### Framework Interoperability & Migration

- [ ] Spring Framework [7] → Quarkus (foundational/application-frameworks/spring-to-quarkus.md)
- [ ] Spring Framework [7] → Micronaut (foundational/application-frameworks/spring-to-micronaut.md)
- [ ] Spring Framework [6] → Helidon (foundational/application-frameworks/spring-to-helidon.md)
- [ ] Spring Boot [7] → Micronaut (foundational/application-frameworks/spring-boot-to-micronaut.md)
- [ ] Spring Boot [7] → Quarkus (foundational/application-frameworks/spring-boot-to-quarkus.md)
- [ ] Jakarta EE [6] → Helidon (foundational/application-frameworks/jakarta-to-helidon.md)
- [ ] Jakarta EE [6] → Micronaut (foundational/application-frameworks/jakarta-to-micronaut.md)
- [ ] Jakarta EE [6] → Quarkus (foundational/application-frameworks/jakarta-to-quarkus.md)
- [ ] JBoss Seam 2.x [6] → CDI 2.0 (foundational/application-frameworks/seam-to-cdi.md)
- [ ] JBoss Seam 2.x [6] → Spring Framework (foundational/application-frameworks/seam-to-spring.md)
- [ ] Apache DeltaSpike [4] → Spring Framework (foundational/application-frameworks/deltaspike-to-spring.md)
- [ ] Helidon Version [5] → Upgrade (foundational/application-frameworks/helidon-version-upgrade.md)
- [ ] Micronaut Version [5] → Upgrade (foundational/application-frameworks/micronaut-version-upgrade.md)
- [ ] Quarkus Version [5] → Upgrade (foundational/application-frameworks/quarkus-version-upgrade.md)

##### Legacy Web & Enterprise Framework Migrations

- [ ] Apache Struts 1 [9] → Spring Boot (foundational/application-frameworks/struts1-to-springboot.md)
- [ ] Apache Struts 2 [8] → Spring Boot (foundational/application-frameworks/struts2-to-springboot.md)
- [ ] Apache Tapestry [4] → Spring MVC (foundational/application-frameworks/tapestry-to-spring-mvc.md)
- [ ] Apache Wicket 8.x [5] → Apache Wicket 9.x (foundational/application-frameworks/wicket8-to-wicket9.md)
- [ ] Apache Wicket [5] → Spring MVC + Thymeleaf (foundational/application-frameworks/wicket-to-spring-thymeleaf.md)
- [ ] Grails 2.x [6] → Grails 5.x (foundational/application-frameworks/grails2-to-grails5.md)
- [ ] Grails [6] → Spring Boot (foundational/application-frameworks/grails-to-spring-boot.md)
- [ ] Play Framework 2.5 [6] → Play Framework 3.0 (foundational/application-frameworks/play-framework-25-to-30.md)
- [ ] Play Framework [6] → Spring WebFlux (foundational/application-frameworks/play-framework-to-spring-webflux.md)
- [ ] Vaadin 8.x [5] → Vaadin 24.x (foundational/application-frameworks/vaadin8-to-vaadin24.md)
- [ ] GWT 2.x [4] → GWT 2.10.0 (foundational/application-frameworks/gwt2-to-gwt210.md)
- [ ] ZK Framework [5] → Spring MVC + Thymeleaf (foundational/application-frameworks/zk-framework-to-spring-thymeleaf.md)
- [ ] Apache Click [5] → Spring Boot (foundational/application-frameworks/click-to-springboot.md)
- [ ] Apache Click [5] → Vaadin (foundational/application-frameworks/click-to-vaadin.md)
- [ ] Apache Shale [4] → JSF 2.3 (foundational/application-frameworks/shale-to-jsf23.md)
- [ ] Apache Shale [4] → Jakarta Faces 3.0 (foundational/application-frameworks/shale-to-jakarta-faces3.md)
- [ ] Apache Shale [4] → Spring MVC (foundational/application-frameworks/shale-to-spring-mvc.md)
- [ ] Apache Cocoon [5] → Spring MVC (foundational/application-frameworks/cocoon-to-spring-mvc.md)
- [ ] Apache Cocoon [5] → Jakarta Faces (foundational/application-frameworks/cocoon-to-jakarta-faces.md)
- [ ] Apache Tuscany [4] → Spring Cloud (foundational/application-frameworks/tuscany-to-spring-cloud.md)
- [ ] Apache Tuscany [4] → Quarkus (foundational/application-frameworks/tuscany-to-quarkus.md)
- [ ] Apache River [3] → Spring Cloud (foundational/application-frameworks/river-to-spring-cloud.md)
- [ ] Apache River [3] → Micronaut (foundational/application-frameworks/river-to-micronaut.md)
- [ ] Apache Beehive [4] → Spring Framework (foundational/application-frameworks/beehive-to-spring.md)
- [ ] Apache Excalibur [3] → Spring Framework (foundational/application-frameworks/excalibur-to-spring.md)
- [ ] Apache Excalibur [3] → CDI (foundational/application-frameworks/excalibur-to-cdi.md)
- [ ] Apache HiveMind [3] → Spring Framework (foundational/application-frameworks/hivemind-to-spring.md)
- [ ] Apache HiveMind [3] → Google Guice (foundational/application-frameworks/hivemind-to-guice.md)
- [ ] Apache Onami [2] → Spring Framework (foundational/application-frameworks/onami-to-spring.md)
- [ ] Apache Onami [2] → Google Guice (foundational/application-frameworks/onami-to-guice.md)
- [ ] Apache Lenya [3] → Magnolia CMS (foundational/application-frameworks/lenya-to-magnolia.md)
- [ ] Apache Lenya [3] → Hippo CMS (foundational/application-frameworks/lenya-to-hippo.md)
- [ ] Eclipse RAP [5] → Spring Boot + Thymeleaf (foundational/application-frameworks/eclipse-rap-to-springboot-thymeleaf.md)
- [ ] Eclipse Riena [3] → Spring Boot (foundational/application-frameworks/eclipse-riena-to-springboot.md)

##### UI Template & Component Migrations

- [ ] JSP + JSTL [10] → Thymeleaf (foundational/application-frameworks/jsp-jstl-to-thymeleaf.md)
- [ ] JSP + JSTL [10] → JSP with Expression Language 4.0 (foundational/application-frameworks/jsp-jstl-to-jakarta-jsp.md)
- [ ] JSP [7] → Facelets (foundational/application-frameworks/jsp-to-facelets.md)
- [ ] JSP Tag Libraries [9] → Thymeleaf Dialect (foundational/application-frameworks/jsp-taglibs-to-thymeleaf-dialect.md)
- [ ] Apache Velocity [7] → FreeMarker (foundational/application-frameworks/velocity-to-freemarker.md)
- [ ] Apache Velocity [7] → Pebble (foundational/application-frameworks/velocity-to-pebble.md)
- [ ] Velocity [6] → Thymeleaf (foundational/application-frameworks/velocity-to-thymeleaf.md)
- [ ] Apache Tiles [7] → Thymeleaf Layout Dialect (foundational/application-frameworks/tiles-to-thymeleaf-layout.md)
- [ ] Apache Tiles [7] → Spring MVC + Bootstrap (foundational/application-frameworks/tiles-to-spring-mvc-bootstrap.md)
- [ ] JSF with jQuery [7] → JSF with PrimeFaces (foundational/application-frameworks/jsf-jquery-to-primefaces.md)
- [ ] JSF + Custom JavaScript [7] → JSF + TypeScript (foundational/application-frameworks/jsf-js-to-typescript.md)

##### Packaging & Deployment Model Modernization

- [ ] WAR Packaging [8] → Spring Boot Executable JAR (foundational/application-frameworks/war-packaging-to-springboot-jar.md)

#### 2.3. Data Access & Persistence Patterns

This category deals with modernizing the data access layer of Java applications. Scenarios include migrating from low-level JDBC to higher-level abstractions, upgrading ORM versions, adopting modern database schema migration tools like Flyway or Liquibase, shifting to reactive data access models, and evolving the underlying data model from relational to NoSQL.

##### Core Abstraction & Specification Modernization

- [ ] Raw JDBC [10] → Spring JdbcTemplate (foundational/data-access-persistence/jdbc-to-spring-jdbctemplate.md)
- [ ] Raw JDBC [9] → Hibernate ORM (foundational/data-access-persistence/jdbc-to-hibernate.md)
- [ ] JDBC [8] → JPA (foundational/data-access-persistence/jdbc-to-jpa.md)
- [ ] Entity EJB [6] → JPA (foundational/data-access-persistence/entity-ejb-to-jpa.md)
- [ ] JPA [10] → Spring Data JPA (foundational/data-access-persistence/jpa-to-spring-data-jpa.md)
- [ ] JPA [8] → jOOQ (foundational/data-access-persistence/jpa-to-jooq.md)
- [ ] Hibernate Native API [9] → JPA API (foundational/data-access-persistence/hibernate-native-api-to-jpa-api.md)
- [ ] JPA 2 [7] → JPA 3 (foundational/data-access-persistence/jpa-2-to-3.md)
- [ ] LocalTransaction [9] → JTA Transactions (foundational/data-access-persistence/local-transaction-to-jta.md)
- [ ] JTA 1.2 [6] → JTA 2.0 (foundational/data-access-persistence/jta-1-2-to-2-0.md)

##### ORM & Mapper Modernization

- [ ] Hibernate 3.x [6] → Hibernate 4.x (foundational/data-access-persistence/hibernate3-to-hibernate4.md)
- [ ] Hibernate 4.x [7] → Hibernate 5.x (foundational/data-access-persistence/hibernate4-to-hibernate5.md)
- [ ] Hibernate 5.x [8] → Hibernate 6.x (foundational/data-access-persistence/hibernate5-to-hibernate6.md)
- [ ] OpenJPA [6] → Hibernate ORM (foundational/data-access-persistence/openjpa-to-hibernate.md)
- [ ] EclipseLink [6] → Hibernate ORM (foundational/data-access-persistence/eclipselink-to-hibernate.md)
- [ ] QueryDSL 4.x [6] → QueryDSL 5.x (foundational/data-access-persistence/querydsl4-to-querydsl5.md)
- [ ] QueryDSL with JPA [6] → Spring Data JPA Specifications (foundational/data-access-persistence/querydsl-jpa-to-spring-data-specifications.md)
- [ ] Spring Data JPA + Specifications [7] → Spring Data JPA + QueryDSL (foundational/data-access-persistence/spring-data-specifications-to-querydsl.md)
- [ ] Apache iBATIS [9] → MyBatis (foundational/data-access-persistence/ibatis-to-mybatis.md)
- [ ] Apache iBATIS [9] → Spring Data JPA (foundational/data-access-persistence/ibatis-to-spring-data-jpa.md)
- [ ] Apache iBATIS [9] → Hibernate (foundational/data-access-persistence/ibatis-to-hibernate.md)

##### Reactive Data Access

- [ ] Synchronous JDBC [10] → Reactive R2DBC (foundational/data-access-persistence/sync-jdbc-to-reactive-r2dbc.md)
- [ ] Spring Data JPA [8] → Spring Data R2DBC (foundational/data-access-persistence/spring-data-jpa-to-spring-data-r2dbc.md)
- [ ] Synchronous MongoDB [8] → Reactive MongoDB (foundational/data-access-persistence/sync-mongo-to-reactive-mongo.md)

##### Database Schema Migration

- [ ] Manual DB Migration [10] → Flyway (foundational/data-access-persistence/manual-db-migration-to-flyway.md)
- [ ] Liquibase 3.x [7] → Liquibase 4.x (foundational/data-access-persistence/liquibase3-to-liquibase4.md)

##### Database & Data Model Migration

- [ ] MySQL [9] → PostgreSQL (foundational/data-access-persistence/mysql-to-postgresql.md)
- [ ] Oracle DB [9] → PostgreSQL (foundational/data-access-persistence/oracle-db-to-postgresql.md)
- [ ] Relational DB [9] → MongoDB (foundational/data-access-persistence/relational-db-to-mongodb.md)
- [ ] Relational DB [9] → Cassandra (foundational/data-access-persistence/relational-db-to-cassandra.md)

##### Data Access Techniques & Extensions

- [ ] JDBC Batch Processing [9] → Reactive Batches (foundational/data-access-persistence/jdbc-batch-to-reactive-batch-considerations.md)
- [ ] Stored Procedures [8] → JPA @NamedStoredProcedure (foundational/data-access-persistence/stored-proc-to-jpa-procedure-considerations.md)
- [ ] Oracle PL/SQL [7] → PostgreSQL PL/pgSQL (foundational/data-access-persistence/oracle-plsql-to-postgres-plpgsql-considerations.md)
- [ ] MyBatis XML Mappers [6] → MyBatis Dynamic SQL (foundational/data-access-persistence/mybatis-xml-to-dynamic-sql-considerations.md)

##### Legacy Data Framework Migration

- [ ] Custom ORM [7] → JPA/Hibernate (foundational/data-access-persistence/custom-orm-to-jpa-hibernate.md)
- [ ] Apache OJB [3] → Hibernate (foundational/data-access-persistence/ojb-to-hibernate.md)
- [ ] Apache OJB [3] → Spring Data JPA (foundational/data-access-persistence/ojb-to-spring-data-jpa.md)
- [ ] Apache Gora [3] → Spring Data (foundational/data-access-persistence/gora-to-spring-data.md)
- [ ] Apache Gora [3] → Hibernate (foundational/data-access-persistence/gora-to-hibernate.md)
- [ ] Eclipse MDT EODM [2] → Jakarta EE (foundational/data-access-persistence/eclipse-eodm-to-jakarta-ee.md)
- [ ] Eclipse MDT EODM [2] → Spring Data (foundational/data-access-persistence/eclipse-eodm-to-spring-data.md)
- [ ] Apache XML Xindice [3] → MongoDB (foundational/data-access-persistence/xindice-to-mongodb.md)
- [ ] Apache XML Xindice [3] → BaseX (foundational/data-access-persistence/xindice-to-basex.md)
- [ ] Apache XML Xindice [3] → eXist-db (foundational/data-access-persistence/xindice-to-existdb.md)
- [ ] Apache Chemistry [3] → Apache JCR (foundational/data-access-persistence/chemistry-to-jcr.md)
- [ ] Apache Chemistry [3] → Spring Content (foundational/data-access-persistence/chemistry-to-spring-content.md)

#### 2.4. Security Frameworks & Logic

This category covers the modernization of application security at the code and framework level. It includes scenarios for replacing outdated authentication mechanisms like Basic or Form-based auth with modern protocols like OAuth 2.0 and OIDC, migrating from custom or legacy security frameworks to standards like Spring Security, upgrading security framework versions, and improving secrets and session management practices.

- [ ] Basic Authentication [10] → Spring Security OAuth2 (foundational/security-frameworks/basic-auth-to-spring-security-oauth2.md)
- [ ] Basic Authentication [10] → OAuth 2.0 (foundational/security-frameworks/basic-auth-to-oauth2.md)
- [ ] Basic Authentication [10] → Keycloak (foundational/security-frameworks/basic-auth-to-keycloak.md)
- [ ] Form Authentication [9] → OIDC Authentication (foundational/security-frameworks/form-auth-to-oidc.md)
- [ ] Custom Security [8] → Spring Security (foundational/security-frameworks/custom-security-to-spring-security.md)
- [ ] Custom Security [7] → Apache Shiro (foundational/security-frameworks/custom-security-to-apache-shiro.md)
- [ ] Apache Shiro [5] → Spring Security (foundational/security-frameworks/apache-shiro-to-spring-security.md)
- [ ] JAAS [5] → Spring Security (foundational/security-frameworks/jaas-to-spring-security.md)
- [ ] Apache Oltu [5] → Spring Security OAuth2 (foundational/security-frameworks/oltu-to-spring-security-oauth2.md)
- [ ] Apache Oltu [5] → Keycloak (foundational/security-frameworks/oltu-to-keycloak.md)
- [ ] Apache Shindig [5] → Spring Social (foundational/security-frameworks/shindig-to-spring-social.md)
- [ ] Apache Shindig [5] → Keycloak (foundational/security-frameworks/shindig-to-keycloak.md)
- [ ] Custom JWT Implementation [7] → JJWT Library (foundational/security-frameworks/custom-jwt-to-jjwt.md)
- [ ] Spring Security 5.x [8] → Spring Security 6.x (foundational/security-frameworks/spring-security-5-to-6.md)
- [ ] Spring Security OAuth [8] → Spring Authorization Server (foundational/security-frameworks/spring-security-oauth-to-authorization-server.md)
- [ ] Spring Security XML [7] → Spring Security Java Config (foundational/security-frameworks/spring-security-xml-to-java-config.md)
- [ ] Java EE Security [7] → Jakarta Security (foundational/security-frameworks/javaee-security-to-jakarta-security.md)
- [ ] JDBC Session [9] → Spring Session (foundational/security-frameworks/jdbc-session-to-spring-session.md)
- [ ] In-memory Session [9] → Redis Session Store (foundational/security-frameworks/in-memory-session-to-redis-session-store.md)
- [ ] Secrets in Properties Files [7] → Vault (foundational/security-frameworks/properties-secrets-to-vault.md)

#### 2.5. User Interface (UI) Decoupling

This category focuses on modernizing the user interface layer of Java applications. It covers scenarios for migrating from older desktop UI toolkits like AWT and Swing to modern alternatives like JavaFX, as well as the complete architectural shift from desktop or server-side rendered UIs to decoupled web applications with modern JavaScript frontends.

- [ ] AWT [6] → Swing (foundational/ui-decoupling/awt-to-swing.md)
- [ ] AWT [6] → JavaFX (foundational/ui-decoupling/awt-to-javafx.md)
- [ ] Java Swing [9] → JavaFX (foundational/ui-decoupling/swing-to-javafx.md)
- [ ] SWT (Eclipse) [6] → JavaFX (foundational/ui-decoupling/swt-to-javafx.md)
- [ ] Eclipse Riena [3] → JavaFX (foundational/ui-decoupling/eclipse-riena-to-javafx.md)
- [ ] Apache Pivot [4] → JavaFX (foundational/ui-decoupling/pivot-to-javafx.md)
- [ ] JavaFX 8 [6] → JavaFX 20+ (foundational/ui-decoupling/javafx8-to-javafx20.md)
- [ ] Swing JTable [10] → JavaFX TableView (foundational/ui-decoupling/swing-jtable-to-javafx-tableview.md)
- [ ] Swing JFreeChart [6] → JavaFX Charts (foundational/ui-decoupling/swing-jfreechart-to-javafx-charts.md)
- [ ] Swing Layout Managers [7] → JavaFX Layout Containers (foundational/ui-decoupling/swing-layout-managers-to-javafx-layouts.md)
- [ ] Swing Custom Components [7] → JavaFX Custom Controls (foundational/ui-decoupling/swing-custom-components-to-javafx-controls.md)
- [ ] JIDE Components [5] → JavaFX Components (foundational/ui-decoupling/jide-components-to-javafx-components.md)
- [ ] JGoodies Forms/Binding [5] → JavaFX Binding API (foundational/ui-decoupling/jgoodies-to-javafx-binding-api.md)
- [ ] Swing Look and Feel [6] → CSS Styling in JavaFX (foundational/ui-decoupling/swing-look-and-feel-to-javafx-css.md)
- [ ] Swing Event Model [7] → JavaFX Event Handling (foundational/ui-decoupling/swing-event-model-to-javafx-events.md)
- [ ] Swing MVC [7] → JavaFX MVVM/MVC (foundational/ui-decoupling/swing-mvc-to-javafx-mvvm.md)
- [ ] Swing + Action Framework [6] → JavaFX + MVVM Pattern (foundational/ui-decoupling/swing-action-framework-to-javafx-mvvm.md)
- [ ] Swing + SwingWorker [7] → JavaFX + Task API (foundational/ui-decoupling/swing-swingworker-to-javafx-task-api.md)
- [ ] Swing + Spring [5] → JavaFX + Spring (foundational/ui-decoupling/swing-spring-to-javafx-spring.md)
- [ ] Swing + XML Config [5] → JavaFX + FXML (foundational/ui-decoupling/swing-xml-config-to-javafx-fxml.md)
- [ ] Swing Graphics2D [6] → JavaFX Canvas (foundational/ui-decoupling/swing-graphics2d-to-javafx-canvas.md)
- [ ] Swing + Java2D Animations [5] → JavaFX Animations (foundational/ui-decoupling/swing-java2d-animations-to-javafx-animations.md)
- [ ] Java 2D/3D [6] → JavaFX 3D (foundational/ui-decoupling/java-2d-3d-to-javafx-3d.md)
- [ ] Swing with Legacy Printing API [5] → JavaFX with Modern Printing (foundational/ui-decoupling/swing-legacy-printing-to-javafx-printing.md)
- [ ] Swing Desktop App [8] → Web Application (foundational/ui-decoupling/swing-desktop-to-web-app.md)
- [ ] Swing Forms [7] → Spring Boot + Thymeleaf Forms (foundational/ui-decoupling/swing-forms-to-springboot-thymeleaf-forms.md)
- [ ] Swing + Hibernate Desktop App [7] → Spring Boot Web App (foundational/ui-decoupling/swing-hibernate-to-springboot-web-app.md)
- [ ] Swing Desktop App [8] → Electron + Java Backend (foundational/ui-decoupling/swing-desktop-to-electron-java-backend.md)
- [ ] Swing Client + RMI [5] → RESTful Web API + JS Frontend (foundational/ui-decoupling/swing-rmi-to-web-api-frontend.md)
- [ ] Netbeans Platform RCP [5] → OSGi/Eclipse RCP (foundational/ui-decoupling/netbeans-rcp-to-osgi-eclipse-rcp.md)
- [ ] Java Web Start [6] → Java Packager/jpackage (foundational/ui-decoupling/java-web-start-to-jpackage.md)
- [ ] Java Applets [4] → Progressive Web Apps (foundational/ui-decoupling/java-applets-to-pwa.md)
- [ ] Server-side Rendering [10] → Client-side Rendering (foundational/ui-decoupling/server-side-rendering-to-client-side-rendering.md)
- [ ] Eclipse RAP [5] → Angular/React (foundational/ui-decoupling/eclipse-rap-to-angular-react.md)
- [ ] Vaadin Flow [5] → Spring + React (foundational/ui-decoupling/vaadin-flow-to-spring-react.md)
- [ ] GWT [4] → React/Angular (foundational/ui-decoupling/gwt-to-react-angular.md)
- [ ] GWT UI Binder [5] → React Components (foundational/ui-decoupling/gwt-uibinder-to-react-components.md)
- [ ] jQuery UI [6] → Modern JavaScript Framework (foundational/ui-decoupling/jquery-ui-to-modern-js-framework.md)
- [ ] Dojo Toolkit [4] → Modern JavaScript Framework (foundational/ui-decoupling/dojo-toolkit-to-modern-js-framework.md)
- [ ] Apache Pivot [4] → Vaadin (foundational/ui-decoupling/pivot-to-vaadin.md)

#### 2.6. Integration & Messaging Patterns

This subcategory covers migrations that modernize how different services and application components communicate. It includes shifting from synchronous to asynchronous patterns, replacing direct calls with messaging systems or API gateways, and upgrading API styles from legacy formats like SOAP to modern alternatives like REST, GraphQL, or gRPC. It also addresses migrations between specific messaging technologies (e.g., JMS, Kafka), integration frameworks (e.g., Apache Camel to Spring Integration), and resilience components (e.g., Hystrix to Resilience4j).

- [ ] Synchronous Calls [10] → CompletableFuture (foundational/integration-patterns/synchronous-calls-to-completablefuture.md)
- [ ] Request/Response [10] → Apache Kafka Event Streaming (foundational/integration-patterns/request-response-to-kafka-events.md)
- [ ] Direct API Calls [10] → Apache Kafka (foundational/integration-patterns/direct-api-calls-to-kafka.md)
- [ ] Direct API Calls [10] → RabbitMQ (foundational/integration-patterns/direct-api-calls-to-rabbitmq.md)
- [ ] Direct API Access [10] → Spring Cloud Gateway (foundational/integration-patterns/direct-api-access-to-spring-cloud-gateway.md)
- [ ] Direct API Access [10] → Kong API Gateway (foundational/integration-patterns/direct-api-access-to-kong-api-gateway.md)
- [ ] SOAP Web Services [10] → GraphQL Java (foundational/integration-patterns/soap-to-graphql.md)
- [ ] SOAP Web Services [10] → gRPC-Java (foundational/integration-patterns/soap-to-grpc.md)
- [ ] SOAP Web Services [10] → JAX-RS REST (foundational/integration-patterns/soap-to-jaxrs-rest.md)
- [ ] SOAP Web Services [10] → Spring MVC REST (foundational/integration-patterns/soap-to-springmvc-rest.md)
- [ ] REST APIs [9] → GraphQL (foundational/integration-patterns/rest-apis-to-graphql.md)
- [ ] REST APIs [10] → gRPC-Java (foundational/integration-patterns/rest-apis-to-grpc.md)
- [ ] Apache ActiveMQ [7] → Apache Kafka (foundational/integration-patterns/activemq-to-kafka.md)
- [ ] RabbitMQ [6] → Apache Kafka (foundational/integration-patterns/rabbitmq-to-kafka.md)
- [ ] JMS [9] → Spring Cloud Stream (foundational/integration-patterns/jms-to-spring-cloud-stream.md)
- [ ] JMS 1 [10] → JMS 2 (foundational/integration-patterns/jms1-to-jms2.md)
- [ ] JMS 2 [6] → JMS 3 (foundational/integration-patterns/jms-2-to-3.md)
- [ ] Apache Camel [7] → Spring Integration (foundational/integration-patterns/apache-camel-to-spring-integration.md)
- [ ] Eclipse SCA Tools (part of STP) [3] → Spring Integration (foundational/integration-patterns/eclipse-sca-tools-to-spring-integration.md)
- [ ] JCA Connectors [6] → Spring Integration Adapters (foundational/integration-patterns/jca-connectors-to-spring-integration-adapters.md)
- [ ] Netflix Zuul [7] → Spring Cloud Gateway (foundational/integration-patterns/netflix-zuul-to-spring-cloud-gateway.md)
- [ ] Netflix Ribbon [7] → Spring Cloud LoadBalancer (foundational/integration-patterns/netflix-ribbon-to-spring-cloud-loadbalancer.md)
- [ ] Netflix Hystrix [8] → Resilience4j (foundational/integration-patterns/netflix-hystrix-to-resilience4j.md)
- [ ] Apache jUDDI [3] → Spring Cloud Service Registry (foundational/integration-patterns/juddi-to-spring-cloud-registry.md)
- [ ] Apache jUDDI [3] → Consul (foundational/integration-patterns/juddi-to-consul.md)
- [ ] JAX-RPC [5] → JAX-WS (foundational/integration-patterns/jax-rpc-to-jax-ws.md)
- [ ] JAX-WS [7] → JAX-RS (foundational/integration-patterns/jax-ws-to-jax-rs.md)
- [ ] Web Services with CXF [6] → Spring Web Services (foundational/integration-patterns/cxf-web-services-to-spring-ws.md)
- [ ] Metro SOAP Stack [7] → Spring Web Services (foundational/integration-patterns/metro-soap-stack-to-spring-ws.md)
- [ ] Apache Etch [2] → gRPC (foundational/integration-patterns/etch-to-grpc.md)
- [ ] Apache Etch [2] → Apache Thrift (foundational/integration-patterns/etch-to-thrift.md)
- [ ] Apache Abdera [3] → Jakarta RESTful Web Services (foundational/integration-patterns/abdera-to-jakarta-rest.md)
- [ ] Apache Abdera [3] → Spring WebFlux (foundational/integration-patterns/abdera-to-spring-webflux.md)
- [ ] Apache Wink [4] → Spring WebFlux (foundational/integration-patterns/wink-to-spring-webflux.md)
- [ ] HTTP Clients [8] → Spring WebClient (foundational/integration-patterns/http-clients-to-spring-webclient.md)
- [ ] Apache HttpClient [9] → Java HttpClient (foundational/integration-patterns/apache-httpclient-to-java-httpclient.md)
- [ ] WebSocket 1.1 [6] → WebSocket 2.0 (foundational/integration-patterns/websocket-1-1-to-2-0.md)

#### 2.7. Obsolete & Legacy Code Removal

This subcategory focuses on modernizing applications by replacing obsolete, unsupported, or legacy code with current, standardized, and more efficient alternatives. The migrations cover a wide range of foundational technologies, including unifying logging frameworks, upgrading web and web service frameworks, adopting modern API specifications, and replacing outdated core libraries for tasks like handling date/time, HTTP communication, and data mapping.

- [ ] Axis [6] → JAX-RS (foundational/obsolete-legacy-code/axis-to-jaxrs.md)
- [ ] Axis [6] → Spring WS (foundational/obsolete-legacy-code/axis-to-spring-ws.md)
- [ ] EJB 2 [7] → Spring (foundational/obsolete-legacy-code/ejb2-to-spring.md)
- [ ] Struts 1 [8] → Spring MVC (foundational/obsolete-legacy-code/struts1-to-spring-mvc.md)
- [ ] Struts 2 [7] → Spring MVC (foundational/obsolete-legacy-code/struts2-to-spring-mvc.md)
- [ ] Wink [5] → RESTEasy (foundational/obsolete-legacy-code/wink-to-resteasy.md)
- [ ] Commons Logging [9] → SLF4J (foundational/obsolete-legacy-code/jcl-to-slf4j.md)
- [ ] java.util.logging [7] → SLF4J + Logback (foundational/obsolete-legacy-code/jul-to-slf4j-logback.md)
- [ ] Log4j 1 [8] → Log4j 2 (foundational/obsolete-legacy-code/log4j1-to-log4j2.md)
- [ ] Log4j 1 [9] → Logback (foundational/obsolete-legacy-code/log4j1-to-logback.md)
- [ ] Mixed Logging Frameworks [10] → Unified Logging with SLF4J (foundational/obsolete-legacy-code/mixed-logging-to-slf4j.md)
- [ ] OpenTracing [6] → OpenTelemetry (foundational/obsolete-legacy-code/opentracing-to-opentelemetry.md)
- [ ] Swagger [8] → OpenAPI 3 (foundational/obsolete-legacy-code/swagger-to-openapi3.md)
- [ ] Commons HTTP [7] → Java 11 HTTP (foundational/obsolete-legacy-code/commons-http-to-java11-http.md)
- [ ] Flyway 5 [7] → Flyway 9 (foundational/obsolete-legacy-code/flyway5-to-flyway9.md)
- [ ] Jackson 1 [7] → Jackson 2 (foundational/obsolete-legacy-code/jackson1-to-jackson2.md)
- [ ] Joda [8] → Java Time (foundational/obsolete-legacy-code/joda-time-to-java-time.md)
- [ ] ModelMapper [6] → MapStruct (foundational/obsolete-legacy-code/modelmapper-to-mapstruct.md)
- [ ] Velocity [6] → Thymeleaf (foundational/obsolete-legacy-code/velocity-to-thymeleaf.md)

#### 2.8. Logging & Observability Patterns

This subcategory focuses on modernizing application logging and observability practices. It includes migrating from traditional text-based logging to structured formats like JSON for better searchability and analysis, replacing custom trace implementations with standardized solutions like OpenTelemetry for distributed tracing, updating log collection infrastructure, and adopting asynchronous logging patterns for improved application performance.

- [ ] Logback XML [9] → Structured Logging with JSON (foundational/logging-observability/logback-xml-to-json-considerations.md)
- [ ] Custom Trace IDs [6] → OpenTelemetry Context Propagation (foundational/logging-observability/custom-traces-to-opentelemetry-considerations.md)
- [ ] Splunk Heavy Forwarders [5] → OpenTelemetry Collectors (foundational/logging-observability/splunk-to-otel-collectors-considerations.md)
- [ ] Log4j 1.x [8] → Log4j 2.x with Async Appenders (foundational/logging-observability/log4j1-to-log4j2-async-considerations.md)

#### 2.9. Caching & In-Memory Data Grids

This subcategory focuses on upgrading and modernizing caching and in-memory data grid solutions. It includes migrations from older or obsolete caching libraries to newer, more performant alternatives, version upgrades of existing caching frameworks, and architectural shifts such as moving from embedded cache modes to client-server deployments.

- [ ] Apache DirectMemory [3] → Caffeine (foundational/caching-in-memory-grids/directmemory-to-caffeine.md)
- [ ] Apache DirectMemory [3] → Ehcache 3 (foundational/caching-in-memory-grids/directmemory-to-ehcache3.md)
- [ ] Coherence [6] → Hazelcast IMDG (foundational/caching-in-memory-grids/coherence-to-hazelcast-imdg.md)
- [ ] Ehcache 2.x [8] → Ehcache 3.x (foundational/caching-in-memory-grids/ehcache2-to-ehcache3.md)
- [ ] EhCache [7] → Caffeine (foundational/caching-in-memory-grids/ehcache-to-caffeine.md)
- [ ] Ehcache [8] → JCache (JSR-107) (foundational/caching-in-memory-grids/ehcache-to-jcache.md)
- [ ] JBoss Cache [5] → Infinispan (foundational/caching-in-memory-grids/jboss-cache-to-infinispan.md)
- [ ] Infinispan Embedded [6] → Infinispan Client-Server (foundational/caching-in-memory-grids/infinispan-embedded-to-client-server.md)

#### 2.10. Workflow & Batch Processing

This subcategory addresses the modernization of automated business processes, batch jobs, and rules engines. It includes migrating from older or legacy workflow and Business Process Management (BPM) systems to modern alternatives like Camunda or Flowable, upgrading batch processing frameworks, and replacing existing rules engines. It also covers transitions to contemporary solutions like state machines and spec upgrades such as moving from Java Batch to Jakarta Batch.

- [ ] Apache Oozie [6] → Spring Batch (foundational/workflow-batch/oozie-to-spring-batch.md)
- [ ] Apache Oozie [6] → Airflow (foundational/workflow-batch/oozie-to-airflow.md)
- [ ] Eclipse Stardust [3] → Camunda BPM (foundational/workflow-batch/eclipse-stardust-to-camunda.md)
- [ ] Eclipse STP BPMN [4] → Camunda BPM (foundational/workflow-batch/eclipse-stp-bpmn-to-camunda.md)
- [ ] Eclipse JWT (part of STP) [3] → Flowable (foundational/workflow-batch/eclipse-jwt-to-flowable.md)
- [ ] JBoss jBPM 5.x [6] → jBPM 7.x (foundational/workflow-batch/jbpm5-to-jbpm7.md)
- [ ] JBoss jBPM [6] → Camunda BPM (foundational/workflow-batch/jbpm-to-camunda.md)
- [ ] JBoss jBPM [6] → Flowable (foundational/workflow-batch/jbpm-to-flowable.md)
- [ ] Drools 5.x [6] → Drools 8.x (foundational/workflow-batch/drools5-to-drools8.md)
- [ ] Drools [6] → Red Hat Decision Manager (foundational/workflow-batch/drools-to-rhdm.md)
- [ ] Drools Rules [6] → Spring Rules Engine (foundational/workflow-batch/drools-rules-to-spring-rules.md)
- [ ] Apache SCXML [5] → Spring State Machine (foundational/workflow-batch/apache-scxml-to-spring-statemachine.md)
- [ ] Java Batch [6] → Jakarta Batch (foundational/workflow-batch/java-batch-to-jakarta-batch.md)

#### 2.11. Business Intelligence & Reporting

- [ ] Eclipse BIRT [6] → JasperReports (foundational/bi-reporting/eclipse-birt-to-jasperreports.md)
- [ ] Eclipse BIRT [6] → Power BI/Tableau (foundational/bi-reporting/eclipse-birt-to-powerbi-tableau.md)

#### 2.12. Mobile & Client-Side Java

This subcategory focuses on modernizing mobile and client-side applications built with Java or related technologies. It covers a range of updates, including migrating legacy mobile platforms like J2ME to modern Android, adopting Kotlin as the preferred language for Android development, and replacing older Android patterns (XML Layouts, AsyncTask, Activities) with modern Jetpack components like Compose, Coroutines, and Navigation. It also includes migrations from cross-platform solutions to newer alternatives and updates to native code integration.

- [ ] J2ME Mobile Apps [3] → Modern Android Apps (foundational/mobile-client-side/j2me-to-android.md)
- [ ] Android Native Java [10] → Android Kotlin (foundational/mobile-client-side/android-java-to-kotlin.md)
- [ ] Android XML Layouts [9] → Jetpack Compose (foundational/mobile-client-side/android-xml-layouts-to-jetpack-compose.md)
- [ ] Android Activities [9] → Jetpack Navigation Components (foundational/mobile-client-side/android-activities-to-navigation-components.md)
- [ ] Android AsyncTask [9] → Kotlin Coroutines (foundational/mobile-client-side/android-asynctask-to-kotlin-coroutines.md)
- [ ] Apache Cordova [5] → React Native (foundational/mobile-client-side/apache-cordova-to-react-native.md)
- [ ] RoboVM [3] → Kotlin Multiplatform Mobile (foundational/mobile-client-side/robovm-to-kotlin-multiplatform.md)
- [ ] JNI Native Code [6] → Android NDK/C++ (foundational/mobile-client-side/jni-to-android-ndk.md)

#### 2.13. XML & Data Format Processing

This subcategory focuses on modernizing the handling of data formats, primarily by upgrading XML processing techniques and shifting towards more modern formats like JSON and YAML. Migrations include moving from legacy, memory-intensive XML parsing models (like DOM/SAX) to efficient stream-based APIs, replacing older libraries with standard or more capable alternatives (like Jackson), and updating data-binding specifications.

- [ ] DOM/SAX Parsing [10] → JAXP with StAX (foundational/xml-data-processing/dom-sax-to-jaxp-stax.md)
- [ ] DOM4J [7] → JAXP (foundational/xml-data-processing/dom4j-to-jaxp.md)
- [ ] JDOM [6] → JDOM2 (foundational/xml-data-processing/jdom-to-jdom2.md)
- [ ] XML Serialization [8] → Jackson XML (foundational/xml-data-processing/xml-serialization-to-jackson-xml.md)
- [ ] XStream [5] → JAXB/Jakarta XML Binding (foundational/xml-data-processing/xstream-to-jaxb.md)
- [ ] JAXB XML [9] → Jackson JSON (foundational/xml-data-processing/jaxb-xml-to-jackson-json.md)
- [ ] XML Configuration [10] → YAML Configuration (foundational/xml-data-processing/xml-config-to-yaml-config.md)
- [ ] JSON-B 1 [6] → JSON-B 2 (foundational/xml-data-processing/json-b-1-to-2.md)
- [ ] JSON-P 1.1 [6] → JSON-P 2.0 (foundational/xml-data-processing/json-p-1-1-to-2-0.md)

#### 2.14. Java Game Development

This subcategory addresses the modernization of games and graphical applications developed in Java. It covers migrating from obsolete technologies like Java Applets to web standards like HTML5, upgrading from legacy 2D/3D graphics libraries (Java2D, Java3D, Slick2D) to powerful, modern game development frameworks such as LibGDX and JMonkeyEngine, and replacing outdated UI-based game loops with more robust and current alternatives.

- [ ] Java Applet Games [3] → HTML5 Canvas Games (foundational/game-development/java-applet-games-to-html5.md)
- [ ] Java3D [2] → JMonkeyEngine (foundational/game-development/java3d-to-jmonkeyengine.md)
- [ ] Java2D Game [5] → LibGDX (foundational/game-development/java2d-game-to-libgdx.md)
- [ ] Slick2D [4] → LibGDX (foundational/game-development/slick2d-to-libgdx.md)
- [ ] AWT/Swing Games [5] → Processing (foundational/game-development/awt-swing-games-to-processing.md)
- [ ] Swing Game Loop [6] → JavaFX Animation Timer (foundational/game-development/swing-gameloop-to-javafx-animationtimer.md)
- [ ] LWJGL 2.x [6] → LWJGL 3.x (foundational/game-development/lwjgl2-to-lwjgl3.md)

#### 2.15. Scientific & Research Libraries

This subcategory focuses on modernizing scientific computing and research applications in Java. It involves replacing older or less efficient numerical and scientific libraries with contemporary, high-performance alternatives like EJML and ND4J. It also covers important version upgrades for widely-used frameworks in data analysis, machine learning, and bioinformatics, such as Weka, Processing, and BioJava.

- [ ] Apache Commons Math [6] → ND4J/DeepLearning4J (foundational/scientific-research-libraries/commons-math-to-dl4j.md)
- [ ] JAMA (Java Matrix) [6] → EJML (Efficient Java Matrix Library) (foundational/scientific-research-libraries/jama-to-ejml.md)
- [ ] JScience [6] → Eclipse Collections/JDK Specialized Collections (foundational/scientific-research-libraries/jscience-to-modern-collections.md)
- [ ] BioJava [6] → BioJava 5+ (foundational/scientific-research-libraries/biojava-legacy-to-modern.md)
- [ ] Processing 2.x [5] → Processing 4.x (foundational/scientific-research-libraries/processing2-to-processing4.md)
- [ ] Weka 3.6 [5] → Weka 3.9+ (foundational/scientific-research-libraries/weka36-to-weka39.md)

#### 2.16. Architectural Pattern Modernization

This subcategory focuses on high-level, strategic modernizations of application architecture. It addresses fundamental shifts in software design, such as decomposing monolithic applications into more manageable modular monoliths or distributed microservices. It also covers the transition from synchronous, request-response communication patterns to asynchronous, event-driven architectures to improve scalability and resilience.

- [ ] Monolithic Application [10] → Microservices (foundational/architectural-patterns/monolith-to-microservices.md)
- [ ] Monolithic Spring [10] → Modular Monolith (foundational/architectural-patterns/spring-monolith-to-modular-monolith.md)
- [ ] Modular Monolith [9] → Microservices (foundational/architectural-patterns/spring-modular-monolith-to-microservices.md)
- [ ] Synchronous Communication [10] → Event-Driven Architecture (foundational/architectural-patterns/sync-communication-to-event-driven.md)
- [ ] Framework Version Upgrade [10] → Best Practices (foundational/architectural-patterns/framework-version-upgrade-best-practices.md)

#### 2.17. AI & Machine Learning Libraries

This subcategory focuses on modernizing Java-based AI and machine learning implementations. It covers migrations from older or less maintained Java ML libraries to more active, high-performance alternatives that better support modern deep learning and computer vision capabilities. These migrations help organizations adopt libraries with better integration into the broader ML ecosystem, more efficient GPU utilization, and improved model training and inference capabilities.

- [ ] Scikit-learn Java Wrappers [5] → Deeplearning4j (foundational/ai-ml/sklearn-java-to-dl4j-considerations.md)
- [ ] Weka 3.x [6] → Tribuo ML Library (foundational/ai-ml/weka-to-tribuo-considerations.md)
- [ ] Mahout Classic [4] → Spark MLLib (foundational/ai-ml/mahout-to-spark-mllib-considerations.md)
- [ ] OpenCV Java [7] → Deep Java Library (DJL) (foundational/ai-ml/opencv-java-to-djl-considerations.md)

#### 2.18. Quantum Computing Preparation

This subcategory focuses on preparing Java applications for the quantum computing era. It covers migration from classical cryptographic algorithms to quantum-resistant alternatives, adapting mathematical libraries to use quantum-safe primitives, and integrating Java code with specialized quantum programming languages. These migrations help organizations future-proof their applications against potential quantum computing threats and take advantage of quantum computing capabilities.

- [ ] Classic Crypto Libs [7] → Post-Quantum Algorithms (foundational/quantum/classic-crypto-to-pqc.md)
- [ ] Math Libraries [6] → Quantum-Safe Math Primitives (foundational/quantum/math-to-quantum-safe.md)
- [ ] Java Simulation Code [5] → Q# Integration (foundational/quantum/java-simulation-to-qsharp.md)

#### 2.19. Green Computing & Energy Efficiency

This subcategory focuses on modernizing Java applications to be more energy-efficient and environmentally friendly. It covers replacing energy-intensive algorithms with eco-friendly alternatives, improving thread efficiency with virtual threads to reduce power consumption, and optimizing memory usage with off-heap management to minimize the environmental impact of Java applications. These migrations help organizations reduce their carbon footprint and optimize resource utilization while maintaining or improving application performance.

- [ ] Energy-Intensive Algorithms [6] → Eco-Friendly Patterns (foundational/green-computing/energy-intensive-to-eco-algorithms.md)
- [ ] High-Power Threading [7] → Virtual Thread Efficiency (foundational/green-computing/threading-to-virtual-threads.md)
- [ ] Memory-Heap Waste [8] → Off-Heap Memory Management (foundational/green-computing/heap-to-offheap.md)

### 3. Cloud-Native Enablement (DevOps & Packaging)
*(Changes to the project repository that prepare the application to be built, deployed, and operated effectively in any modern cloud environment.)*

#### 3.1. Build & Dependency Management

This subcategory focuses on modernizing the build, dependency, and artifact management toolchain. It includes migrating from legacy build systems like Ant to modern tools such as Maven and Gradle, upgrading tool versions, and adopting better dependency management practices like Maven BOMs and Gradle Version Catalogs. It also covers shifts between build ecosystems (e.g., Maven to Gradle), adopting modern DSLs (e.g., Gradle's Kotlin DSL), and moving from self-hosted artifact repositories to cloud-native solutions.

- [ ] Ant [9] → Maven (cloud-native/build-dependency-management/ant-to-maven.md)
- [ ] Ant [7] → Gradle (cloud-native/build-dependency-management/ant-to-gradle.md)
- [ ] Ant + Ivy [6] → Gradle (cloud-native/build-dependency-management/ant-ivy-to-gradle.md)
- [ ] Eclipse Buckminster [4] → Maven/Gradle (cloud-native/build-dependency-management/eclipse-buckminster-to-maven-gradle.md)
- [ ] Maven [6] → Gradle (cloud-native/build-dependency-management/maven-to-gradle.md)
- [ ] Maven Multi-module [10] → Gradle Multi-project (cloud-native/build-dependency-management/maven-multimodule-to-gradle-multiproject.md)
- [ ] Maven 2.x [7] → Maven 3.x (cloud-native/build-dependency-management/maven2-to-maven3.md)
- [ ] Gradle Groovy DSL [9] → Gradle Kotlin DSL (cloud-native/build-dependency-management/gradle-groovy-dsl-to-kotlin-dsl.md)
- [ ] Manual Dependency Management [8] → Maven BOM (cloud-native/build-dependency-management/manual-deps-to-maven-bom.md)
- [ ] Manual Dependency Management [7] → Gradle Version Catalogs (cloud-native/build-dependency-management/manual-deps-to-gradle-catalogs.md)
- [ ] Maven BOM [8] → Gradle Version Catalog (cloud-native/build-dependency-management/maven-bom-to-gradle-version-catalog.md)
- [ ] Maven Profiles [7] → Spring Profiles (cloud-native/build-dependency-management/maven-profiles-to-spring-profiles.md)
- [ ] Maven Profiles [8] → Gradle Build Variants (cloud-native/build-dependency-management/maven-profiles-to-gradle-build-variants.md)
- [ ] Self-hosted Artifact Repository [7] → Azure Artifacts (cloud-native/build-dependency-management/self-hosted-artifact-repo-to-azure-artifacts.md)
- [ ] Self-hosted Artifact Repository [7] → GitHub Packages (cloud-native/build-dependency-management/self-hosted-artifact-repo-to-github-packages.md)

#### 3.2. Containerization

This subcategory focuses on packaging Java applications into containers for portability, scalability, and consistency across environments. It covers migrating traditional deployment artifacts like WARs and JARs into Docker images, as well as replacing obsolete deployment technologies like Java WebStart. It also includes adopting modern, efficient tools like Jib for building container images directly and migrating between different container runtimes.

- [ ] WAR Deployment [10] → Docker Containers (cloud-native/containerization/war-deployment-to-docker.md)
- [ ] JAR Deployment [10] → Docker Containers (cloud-native/containerization/jar-deployment-to-docker.md)
- [ ] WebStart [6] → Docker (cloud-native/containerization/webstart-to-docker.md)
- [ ] Eclipse Libra [3] → Docker (cloud-native/containerization/eclipse-libra-to-docker.md)
- [ ] Manual JAR Creation [6] → Jib Container Images (cloud-native/containerization/manual-jar-to-jib.md)
- [ ] Rkt [3] → containerd (cloud-native/containerization/rkt-to-containerd.md)
- [ ] Rkt [3] → CRI-O (cloud-native/containerization/rkt-to-cri-o.md)

#### 3.3. Testing & Quality Assurance

This subcategory focuses on modernizing software testing and quality assurance practices for cloud-native development. It covers upgrading core testing frameworks like JUnit, adopting modern mocking and assertion libraries, and shifting towards advanced testing paradigms such as Behavior-Driven Development (BDD), contract testing, and automated integration testing with tools like Testcontainers. It also includes migrations to modern UI test automation and integrating code quality analysis into CI/CD pipelines.

- [ ] JUnit 3 [9] → JUnit 5 (cloud-native/testing-qa/junit3-to-junit5.md)
- [ ] JUnit 4 [9] → JUnit 5 (cloud-native/testing-qa/junit4-to-junit5.md)
- [ ] JUnit Rules [9] → JUnit 5 Extensions (cloud-native/testing-qa/junit-rules-to-junit5-extensions.md)
- [ ] TestNG [7] → JUnit 5 (cloud-native/testing-qa/testng-to-junit5.md)
- [ ] JMock [6] → Mockito 5.x (cloud-native/testing-qa/jmock-to-mockito5.md)
- [ ] EasyMock [6] → Mockito 5.x (cloud-native/testing-qa/easymock-to-mockito5.md)
- [ ] PowerMock [7] → Mockito + JUnit 5 Extensions (cloud-native/testing-qa/powermock-to-mockito-junit5.md)
- [ ] Hamcrest Matchers [9] → AssertJ (cloud-native/testing-qa/hamcrest-to-assertj.md)
- [ ] Traditional Testing [7] → Cucumber BDD (cloud-native/testing-qa/traditional-testing-to-cucumber.md)
- [ ] Traditional Testing [10] → Spock Framework (cloud-native/testing-qa/traditional-testing-to-spock.md)
- [ ] Concordion [5] → Cucumber (cloud-native/testing-qa/concordion-to-cucumber.md)
- [ ] JBehave [6] → Cucumber (cloud-native/testing-qa/jbehave-to-cucumber.md)
- [ ] Manual Integration Tests [8] → Testcontainers (cloud-native/testing-qa/manual-integration-tests-to-testcontainers.md)
- [ ] DBUnit [5] → Test Containers (cloud-native/testing-qa/dbunit-to-testcontainers.md)
- [ ] Arquillian [6] → Spring Test (cloud-native/testing-qa/arquillian-to-spring-test.md)
- [ ] Manual Testing [10] → Selenium WebDriver (cloud-native/testing-qa/manual-testing-to-selenium.md)
- [ ] Selenium WebDriver [8] → Playwright Java (cloud-native/testing-qa/selenium-to-playwright.md)
- [ ] Pact [7] → Spring Cloud Contract (cloud-native/testing-qa/pact-to-spring-cloud-contract.md)
- [ ] WireMock [8] → Spring Cloud Contract (cloud-native/testing-qa/wiremock-to-spring-cloud-contract.md)
- [ ] Traditional Testing [9] → Spring Cloud Contract (cloud-native/testing-qa/traditional-testing-to-spring-cloud-contract.md)
- [ ] Manual Performance Tests [10] → JMH (cloud-native/testing-qa/manual-performance-tests-to-jmh.md)
- [ ] Manual Performance Tests [10] → Gatling (cloud-native/testing-qa/manual-performance-tests-to-gatling.md)
- [ ] Legacy Code Coverage [9] → JaCoCo (cloud-native/testing-qa/legacy-code-coverage-to-jacoco.md)
- [ ] Local Code Quality Analysis [7] → GitHub Advanced Security (cloud-native/testing-qa/local-code-quality-to-github-advanced-security.md)
- [ ] SonarQube [8] → GitHub Code Scanning (cloud-native/testing-qa/sonarqube-to-github-code-scanning.md)

#### 3.4. Local Development Environment

#### 3.5. Developer Tooling & Documentation

This subcategory is dedicated to modernizing the developer environment, tooling, and documentation processes. It includes major migrations between Integrated Development Environments (IDEs), such as from Eclipse or NetBeans to IntelliJ IDEA, along with updating specific IDE features like debuggers and refactoring tools. It also covers shifts to modern documentation platforms, specialized graphical modeling tools, memory analysis utilities, and the adoption of AI-assisted tools like GitHub Copilot to enhance productivity.

- [ ] Eclipse [10] → IntelliJ IDEA (cloud-native/developer-tooling-docs/eclipse-to-intellij.md)
- [ ] NetBeans [6] → IntelliJ IDEA (cloud-native/developer-tooling-docs/netbeans-to-intellij.md)
- [ ] Eclipse Debugging [9] → IntelliJ Debugging (cloud-native/developer-tooling-docs/eclipse-debugging-to-intellij-debugging.md)
- [ ] Eclipse JDT [8] → IntelliJ IDEA Java Tools (cloud-native/developer-tooling-docs/eclipse-jdt-to-intellij-java-tools.md)
- [ ] Eclipse Plugins [6] → IntelliJ Plugins (cloud-native/developer-tooling-docs/eclipse-plugins-to-intellij-plugins.md)
- [ ] Eclipse Refactor [4] → IntelliJ IDEA Refactoring (cloud-native/developer-tooling-docs/eclipse-refactor-to-intellij.md)
- [ ] Eclipse Memory Analyzer [6] → VisualVM/JProfiler (cloud-native/developer-tooling-docs/mat-to-visualvm-jprofiler.md)
- [ ] Eclipse GMF Tooling [5] → Eclipse Sirius (cloud-native/developer-tooling-docs/eclipse-gmf-to-sirius.md)
- [ ] Eclipse GMF Tooling [5] → JetBrains MPS (cloud-native/developer-tooling-docs/eclipse-gmf-to-mps.md)
- [ ] Eclipse VE [5] → JavaFX Scene Builder (cloud-native/developer-tooling-docs/eclipse-ve-to-javafx-scene-builder.md)
- [ ] Eclipse Form Editors [4] → Custom JavaFX Forms (cloud-native/developer-tooling-docs/eclipse-form-editors-to-javafx-forms.md)
- [ ] Eclipse Orion [4] → VS Code/Eclipse Theia (cloud-native/developer-tooling-docs/eclipse-orion-to-vscode-theia.md)
- [ ] Eclipse ATL (AM3) [5] → QVT (cloud-native/developer-tooling-docs/eclipse-atl-to-qvt.md)
- [ ] Java Project Documentation [7] → GitHub Pages (cloud-native/developer-tooling-docs/java-docs-to-github-pages.md)
- [ ] Java Code Analysis [7] → GitHub Copilot (cloud-native/developer-tooling-docs/java-code-analysis-to-github-copilot.md)
- [ ] Local LLM Models [6] → GitHub Copilot for Docs (cloud-native/developer-tooling-docs/local-llm-to-github-copilot-docs.md)
- [ ] NATS Box [3] → NATS CLI (cloud-native/developer-tooling-docs/nats-box-to-nats-cli.md)

#### 3.6. CI/CD & Automation

This subcategory is dedicated to modernizing Continuous Integration and Continuous Deployment (CI/CD) pipelines and related automation. It covers migrating from legacy CI systems or manual shell scripts to modern, declarative platforms like GitHub Actions, Azure DevOps, and Jenkins Pipelines. It also includes the strategic shift from on-premise, self-hosted infrastructure—such as Git servers, container registries, and secrets management—to managed, cloud-native services to improve reliability, scalability, and security.

- [ ] Shell Script Builds [7] → GitHub Actions (cloud-native/ci-cd-automation/shell-script-builds-to-github-actions.md)
- [ ] Shell Script Builds [7] → Jenkins Pipeline (cloud-native/ci-cd-automation/shell-script-builds-to-jenkins-pipeline.md)
- [ ] Apache Continuum [5] → Jenkins (cloud-native/ci-cd-automation/continuum-to-jenkins.md)
- [ ] Apache Continuum [5] → GitHub Actions (cloud-native/ci-cd-automation/continuum-to-github-actions.md)
- [ ] Brigade (CI/CD) [4] → GitHub Actions (cloud-native/ci-cd-automation/brigade-to-github-actions.md)
- [ ] Brigade (CI/CD) [4] → Jenkins Pipeline (cloud-native/ci-cd-automation/brigade-to-jenkins-pipeline.md)
- [ ] Brigade (CI/CD) [4] → Tekton (cloud-native/ci-cd-automation/brigade-to-tekton.md)
- [ ] Jenkins [10] → Azure DevOps Pipelines (cloud-native/ci-cd-automation/jenkins-to-azure-devops.md)
- [ ] Jenkins [10] → GitHub Actions (cloud-native/ci-cd-automation/jenkins-to-github-actions.md)
- [ ] GitLab CI/CD [7] → Azure DevOps Pipelines (cloud-native/ci-cd-automation/gitlab-cicd-to-azure-devops.md)
- [ ] GitLab CI/CD [7] → GitHub Actions (cloud-native/ci-cd-automation/gitlab-cicd-to-github-actions.md)
- [ ] Java CI Workflows [8] → GitHub Actions Workflows (cloud-native/ci-cd-automation/java-ci-workflows-to-github-actions.md)
- [ ] Self-hosted Git Server [7] → GitHub (cloud-native/ci-cd-automation/self-hosted-git-to-github.md)
- [ ] On-premise Docker Registry [7] → Azure Container Registry (cloud-native/ci-cd-automation/onprem-docker-registry-to-acr.md)
- [ ] On-premise Secrets Management [6] → GitHub Secrets (cloud-native/ci-cd-automation/onprem-secrets-to-github-secrets.md)

#### 3.7. Cloud-Native Configuration

- [ ] Properties Files [10] → Kubernetes ConfigMaps (cloud-native/configuration/properties-files-to-kubernetes-configmaps.md)
- [ ] Static Configuration [9] → Spring Cloud Config (cloud-native/configuration/static-config-to-spring-cloud-config.md)

#### 3.8. Deployment & Orchestration

This subcategory focuses on modernizing application deployment and runtime management, primarily by shifting from traditional virtual machine-based deployments to container orchestration with Kubernetes. It also covers migrations within the cloud-native ecosystem, such as upgrading or replacing service meshes, ingress controllers, and configuration management tools like Helm. Additionally, it addresses the adoption of automated, declarative practices like Horizontal Pod Autoscaling (HPA) to replace manual processes.

- [ ] VM Deployment [9] → Kubernetes Deployment (cloud-native/deployment-orchestration/vm-deployment-to-kubernetes.md)
- [ ] Manual Scaling [9] → Kubernetes HPA (cloud-native/deployment-orchestration/manual-scaling-to-kubernetes-hpa.md)
- [ ] Eclipse Libra [3] → Kubernetes (cloud-native/deployment-orchestration/eclipse-libra-to-kubernetes.md)
- [ ] Contour (Ingress/Gateway) [5] → Ingress Nginx (cloud-native/deployment-orchestration/contour-to-ingress-nginx.md)
- [ ] Contour (Ingress/Gateway) [5] → Istio Gateway (cloud-native/deployment-orchestration/contour-to-istio-gateway.md)
- [ ] Istio 0.x/1.0 [7] → Istio 1.x+ (cloud-native/deployment-orchestration/istio-0x-to-1x.md)
- [ ] Istio 0.x/1.0 [7] → Linkerd (cloud-native/deployment-orchestration/istio-to-linkerd.md)
- [ ] Envoy Mobile [3] → Consul Service Mesh (cloud-native/deployment-orchestration/envoy-mobile-to-consul.md)
- [ ] Helm Classic [4] → Helm v3 (cloud-native/deployment-orchestration/helm-classic-to-helm3.md)
- [ ] Helm Classic [4] → Kustomize (cloud-native/deployment-orchestration/helm-classic-to-kustomize.md)
- [ ] etcd Operator [4] → Kubernetes Operator Framework (cloud-native/deployment-orchestration/etcd-operator-to-operator-framework.md)
- [ ] etcd Operator [4] → Statefulset-based etcd (cloud-native/deployment-orchestration/etcd-operator-to-statefulset.md)
- [ ] NATS Operator [3] → NATS Helm Chart (cloud-native/deployment-orchestration/nats-operator-to-helm-chart.md)

#### 3.9. Cloud-Native Observability

This subcategory focuses on modernizing the tools and practices for monitoring, logging, and tracing applications in cloud-native environments. It covers migrating from traditional application-level logging and JMX metrics to centralized, scalable observability platforms like the ELK stack and Prometheus with Grafana. It also includes upgrading or replacing components of the observability stack, such as log shippers (e.g., Fluentd) and distributed tracing systems (e.g., Jaeger), often moving towards vendor-neutral standards like OpenTelemetry.

- [ ] Log4j/Logback [10] → Elasticsearch (ELK) (cloud-native/observability/logging-to-elk.md)
- [ ] JMX Monitoring [7] → Prometheus + Grafana (cloud-native/observability/jmx-to-prometheus-grafana.md)
- [ ] Fluentd v0.x [6] → Fluentd v1.x (cloud-native/observability/fluentd-v0-to-v1.md)
- [ ] Fluentd v0.x [6] → Logstash (cloud-native/observability/fluentd-to-logstash.md)
- [ ] Fluent Bit [6] → Vector (cloud-native/observability/fluentbit-to-vector.md)
- [ ] Jaeger v1.x [6] → Jaeger v2.x (cloud-native/observability/jaeger-v1-to-v2.md)
- [ ] Jaeger v1.x [6] → OpenTelemetry Tracing (cloud-native/observability/jaeger-to-opentelemetry.md)
- [ ] Eclipse TPTP [5] → JProfiler (cloud-native/observability/eclipse-tptp-to-jprofiler.md)
- [ ] Eclipse TPTP [5] → VisualVM/JConsole (cloud-native/observability/eclipse-tptp-to-visualvm-jconsole.md)

#### 3.10. Cloud-Native Security

- [ ] Falco [5] → Tracee (cloud-native/security/falco-to-tracee.md)
- [ ] Falco [5] → Sysdig Secure (cloud-native/security/falco-to-sysdig-secure.md)
- [ ] Open Policy Agent 0.x [4] → Open Policy Agent 1.x (cloud-native/security/opa-0x-to-1x.md)
- [ ] TUF Python [3] → go-tuf (cloud-native/security/tuf-python-to-go-tuf.md)

#### 3.11. Cloud-Native Data Services

- [ ] Vitess v6.x [5] → Vitess v7.x+ (cloud-native/data-services/vitess-v6-to-v7.md)
- [ ] Vitess v6.x [5] → CockroachDB (cloud-native/data-services/vitess-to-cockroachdb.md)
