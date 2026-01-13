Consider bellow knowledge to migrate Jhipster projects to spring boot 3.2: 
1. Some configuration properties are remove from class JHipsterProperties, use a default value to fix this. Here are some examples:
    - `jHipsterProperties.getMetrics().getLogs().isEnabled()` can be replaced with `true`, because Metrics property is removed 
    - `jHipsterProperties.getHttp().getVersion().equals(JHipsterProperties.Http.Version.V_2_0)` can be replaced with `true`, because Http property is removed
1. AjaxLogoutSuccessHandler is deprecated, just use SimpleUrlLogoutSuccessHandler 
1. If you met exception like `Failed to execute goal org.apache.maven.plugins:maven-enforcer-plugin:enforce (enforce-versions) on project : Some Enforcer rules have failed. Look above for specific messages explaining why the rule failed`, please check the rules configuration of maven enforcer plugin, update or remove the invalid rules. Here is an example:
    Before:
    ```xml
         <configuration>
                    <rules>
                        <requireMavenVersion>
                            <message>You are running an older version of Maven. JHipster requires at least Maven ${maven.version}</message>
                            <version>[${maven.version},)</version>
                        </requireMavenVersion>
                        <requireJavaVersion>
                            <!-- Until JHipster supports JDK 9 -->
                            <message>You are running an incompatible version of Java. JHipster requires JDK ${java.version}</message>
                            <version>[1.8,23)</version>
                        </requireJavaVersion>
                    </rules>
                </configuration>
    ```
    After:
    ```xml
         <configuration>
                    <rules>
                        <requireMavenVersion>
                            <message>You are running an older version of Maven. JHipster requires at least Maven ${maven.version}</message>
                            <version>[${maven.version},)</version>
                        </requireMavenVersion>
                        <requireJavaVersion>
                            <message>You are running an incompatible version of Java. JHipster requires JDK ${java.version}</message>
                            <version>[1.8,23)</version>
                        </requireJavaVersion>
                    </rules>
                </configuration>
    ```
