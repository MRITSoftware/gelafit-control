Consider the following knowledge when the project contains keycloak references when upgrade to spring boot3.
- keycloak adapters (in package org.keycloak.adapters) are not supported in spring-boot 3.2, need remove related codes. 
    1. remove unsupported keycloak adapter
    1. remove dependency org.keycloak.bom:keycloak-adapter-bom
    1. remove classes from package org.keycloak.adapters.*