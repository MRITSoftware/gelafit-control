Consider the following knowledge when handle hibernates orm upgrade to spring boot3. 
1. For dependency org.hibernate:hibernate-core, 7.0.0.Beta3 is the latest version.
   - Use `org.hibernate:hibernate-core:7.0.0.Beta3`

2. For dependency org.hibernate:hibernate-ehcache, 6.0.0.Alpha7 is the latest version.
   - Use `org.hibernate:hibernate-ehcache:6.0.0.Alpha7`

3. Many method names have been changed in hibernate orm, below are some examples:
    - remapSqlTypeDescriptor => resolveSqlTypeDescriptor 
    - registerColumnType => registerColumnTypes 