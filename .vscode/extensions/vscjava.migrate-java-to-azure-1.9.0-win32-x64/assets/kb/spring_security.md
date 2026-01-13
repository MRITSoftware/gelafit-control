Consider the following knowledge base for spring security upgrade: 
1. add or upgrade dependency 
    - ` org.springframework.security.oauth:spring-security-oauth2:2.5.2.RELEASE `
    - ` org.springframework.security:spring-security-oauth2-resource-server `
2. org.springframework.security.oauth2.provider.OAuth2Authentication is deprecated,  
    - look into the new classes and interfaces provided in the org.springframework.security.oauth2.server.resource and org.springframework.security.oauth2.client packages. 
    - For example, use org.springframework.security.oauth2.server.resource.authentication.OAuth2AuthenticationToken, org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken. 