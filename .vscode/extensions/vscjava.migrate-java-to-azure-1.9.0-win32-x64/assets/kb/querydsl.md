Consider the following knowledge when handle querydsl upgrade, you should do this as early as possible to fix the issues bring by the deprecated query dsl dependencies;
1. Using a high version of querydsl, like 5.0.0. Update property <querydsl.version> to 5.0.0 for maven projects. 
2. Update dependencies com.querydsl:querydsl-jpa and com.querydsl:querydsl-apt to version 5.0.0 and add classifier jakarta.
    - `com.querydsl:querydsl-jpa:5.0.0:jakarta`
    - `com.querydsl:querydsl-apt:5.0.0:jakarta`
3. Update JPAAnnotationProcessor configuration.
    - For maven project, check each pom.xml file to see if querydsl.apt.jpa.JPAAnnotationProcessor is used. 
        - If it's used, use the bellow configuration as an example to fix the maven-plugin. **Important**, you need remove the outputDirectory property if it is already defined inside the configuration part. 
            - Before
            ``` xml
            <plugin> 
                <groupId>com.mysema.maven</groupId> 
                <artifactId>apt-maven-plugin</artifactId> 
                <version>1.1.3</version> 
                <executions> 
                    <execution> 
                        <goals> 
                            <goal>process</goal> 
                        </goals> 
                        <configuration> 
                            <outputDirectory>target/generated-sources/java</outputDirectory>
                            <processor>com.querydsl.apt.jpa.JPAAnnotationProcessor</processor> 
                        </configuration> 
                    </execution> 
                </executions> 
            </plugin> 
            ```
            - After: 
            ``` xml
            <plugin> 
                <groupId>com.mysema.maven</groupId> 
                <artifactId>apt-maven-plugin</artifactId> 
                <version>1.1.3</version> 
                <executions> 
                    <execution> 
                        <goals> 
                            <goal>process</goal> 
                        </goals> 
                        <configuration> 
                            <processor>com.querydsl.apt.jpa.JPAAnnotationProcessor</processor> 
                        </configuration> 
                    </execution> 
                </executions> 
            </plugin> 
            ```
    - For gradle project, check if annotationProcessor `com.querydsl:querydsl-apt` is used in the build.gradle file.
        - If it's used, use the bellow configuration as an example to fix the gradle-plugin. 
            ```
            dependencies {
                implementation 'com.querydsl:querydsl-jpa:5.0.0:jakarta'
                annotationProcessor 'com.querydsl:querydsl-apt:5.0.0:jakarta'
            }

            compileJava {
                options.annotationProcessorPath = configurations.annotationProcessor
            }
            ```
        - **Important**, you need to remove the outputDirectory option if it is defined.
4.  If you see compile errors like, 'cannot find symbol xxx.Qxxx, try fix the dsl maven plugin in step 3 
