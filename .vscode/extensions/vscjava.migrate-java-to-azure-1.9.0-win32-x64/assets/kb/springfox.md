Consider the following knowledge when handle springfox to swagger3 migration when upgrade to spring boot3 with open rewrite. 
1. Remove springfox and swagger 2 dependencies. Add springdoc-openapi-starter-webmvc-ui dependency instead.
    `org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.1`
    
1. Replace swagger 2 annotations with swagger 3 annotations (it is already included with springdoc-openapi-starter-webmvc-ui dependency). Package for swagger 3 annotations is io.swagger.v3.oas.annotations.
    - Use `@Tag` to replace `@Api`
    - Use `@Parameter(hidden = true)` to replace `@ApiIgnore` if it was used as an annotation of parameters
    - Use `@Hidden` or `@Operation(hidden = true)` to replace `@ApiIgnore` if it was used as an annotation of a class or method
    - Use `@Parameter` to replace `@ApiImplicitParam`
    - Use `@Parameters` to replace `@ApiImplicitParams`
    - Use `@Schema` to replace `@ApiModel`
    - Use `@Schema(nullable = true)` to replace `@ApiModelProperty(allowEmptyValue = true) `
    - Use `@Schema` to replace `@ApiModelProperty`
    - Use `@Operation(summary = "foo", description = "bar")` to replace `@ApiOperation(value = "foo", notes = "bar")`
    - Use `@Parameter` to replace `@ApiParam`
    - Use `@ApiResponse(responseCode = "404", description = "foo")` to replace `@ApiResponse(code = 404, message = "foo")`
1. If you have multiple Docket beans replace them with GroupedOpenApi beans.
    Before:
    ```    
    @Bean
    public Docket publicApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage ("org.github.springshop.web.public"))
                .paths(PathSelectors.regex("/public.*"))
                .build()
                .groupName("springshop-public")
                .apiInfo(apiInfo());
    }
    
    @Bean
    public Docket adminApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("org.github.springshop.web.admin"))
                .paths(PathSelectors.regex("/admin.*"))
                .apis(RequestHandlerSelectors.withMethodAnnotation(Admin.class))
                .build()
                .groupName("springshop-admin")
                .apiInfo(apiInfo());
    }
    ```
    Now:
    ```
    import org.springdoc.core.models.GroupedOpenApi;

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("springshop-public")
                .pathsToMatch("/public/**")
                .build();
    }
    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("springshop-admin")
                .pathsToMatch("/admin/**")
                .addOpenApiMethodFilter(method -> method.isAnnotationPresent(Admin.class))
                .build();
    }
    ```
1. If you have only one Docket — remove it and instead add properties to your application.properties:
    ```
    springdoc.packagesToScan=package1, package2
    springdoc.pathsToMatch=/v1, /api/balance/**
    ```
1. Add bean of OpenAPI type. See example:
    ```
    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("SpringShop API")
                .description("Spring shop sample application")
                .version("v0.0.1")
                .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                .description("SpringShop Wiki Documentation")
                .url("https://springshop.wiki.github.org/docs"));
    }
    ```
1. If you met issue 'cannot find symbol symbol class ApiIgnore', please use @Parameter(hidden = true) or @Operation(hidden = true) or @Hidden to replace them
    - For annotation target parameter, use @Operation(hidden = true) to replace it
        - Before
        ```
        MessageResponse demo(@ApiIgnore Object object) {
        }
        ```
        - After
        ```
        MessageResponse demo(@Parameter(hidden = true) Object object) {
        }
        ```
    - For annotation target method or type, use @Hidden to replace it 
         - Before
        ```
        @RequestMapping(value = "/", method = RequestMethod.GET)
        @ApiIgnore
        public ModelAndView demo() {
        }
        ```
        - After
        ```
        @RequestMapping(value = "/", method = RequestMethod.GET)
        @Hidden
        public ModelAndView demo() {
        }
        ```