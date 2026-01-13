Consider the following knowledge when handle httpclient4 to httpclient5 migration when upgrade to spring boot3 with open rewrite. 
- If you meet `cannot find symbol symbol: class HttpRequestBase`, then replace it with HttpUriRequestBase
- If you meet `cannot find symbol symbol: method setSocketConfig(org.apache.hc.core5.http.HttpHost,org.apache.hc.core5.http.io.SocketConfig) location: variable connectionManager of type org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager, then replaced it manually with SetDefaultSocketConfig wo/ host argument
- If you meet `cannot find symbol symbol: method getAllHeaders() location: variable response of type org.apache.hc.client5.http.impl.classic.CloseableHttpResponse`, then replace it with getHeaders()
- If you meet `cannot find symbol symbol: method shutdown() location: variable connectionManager of type org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager`, then removed call to shutdown() manually
- If you meet `incompatible types: java.lang.String cannot be converted to org.apache.hc.core5.http.Header` change the type to String
- If you meet `cannot find symbol symbol: method getValue() location: variable contentType of type java.lang.String`, then removed call to getValue()
- If you meet `package org.apache.hc.core5.http.client.fluent does not exist`, then change to import org.apache.hc.client5.http.fluent
- If you meet `package org.apache.hc.core5.http.io.entity.mime does not exist` then change to import org.apache.hc.client5.http.entity.mime
- If you meet `cannot find symbol symbol: method Put(java.lang.String) location: class org.apache.hc.client5.http.fluent.Request` then change all Get/Put/Post/Delete calls to lowercase
- If you meet `cannot find symbol symbol: method getEntity()` then cast response to ClassicHttpResponse, Below is an example
    - Before
    ```
        HttpEntity entity = httpResponse.getEntity();
    ```
    - After
    ```
        HttpEntity entity = httpResponse instanceof ClassicHttpResponse ? ((ClassicHttpResponse) httpResponse).getEntity() : null;
    ```
- If you meet `unreported exception org.apache.hc.core5.http.ParseException; must be caught or declared to be thrown` then import org.apache.hc.core5.http.ParseException and catch exception
- If you meet `cannot find symbol symbol: class Consts location: package org.apache.hc.core5.http` then import java.nio.charset.StandardCharsets and replace Consts.UTF_8 with StandardCharsets.UTF_8
- If you meet `no suitable constructor found for UsernamePasswordCredentials(java.lang.String,java.lang.String) constructor org.apache.hc.client5.http.auth.UsernamePasswordCredentials.UsernamePasswordCredentials(java.security.Principal,char[]) is not applicable (argument mismatch; java.lang.String cannot be converted to java.security.Principal) constructor org.apache.hc.client5.http.auth.UsernamePasswordCredentials.UsernamePasswordCredentials(java.lang.String,char[]) is not applicable (argument mismatch; java.lang.String cannot be converted to char[])`, then manually changed password parameter to password.toCharArray()
- If you meet `cannot find symbol symbol: method setCredentials(org.apache.hc.client5.http.auth.AuthScope,org.apache.hc.client5.http.auth.UsernamePasswordCredentials) location: variable credsProvider of type org.apache.hc.client5.http.auth.CredentialsProvider` then manually changed to BasicCredentialsProvider
- Remove old org.apache.http imports and re-import HttpClient classes from org.apache.hc.httpclient5 package namespace. Most old interfaces and classes should resolve automatically. One notable exception is HttpEntityEnclosingRequest interface In HttpClient 5.x one can enclose a request entity with any HTTP method even if violates semantic of the method.
- There will be compilation errors due to API incompatibilities between version series 4.x and 5.x mostly related to SSL/TLS and timeout settings and CloseableHttpClient instance creation. Several modifications are likely to be necessary.
- Use PoolingHttpClientConnectionManagerBuilder class to create connection managers with custom parameters
- Use SSLConnectionSocketFactoryBuilder class to create SSL connection socket factories with custom parameters
- While HttpClient 5 automatically disables all SSL versions and weak TLS versions it may still be advisable to explicitly specify TLSv1.3 as the only enabled version.
- Use Timeout class to define timeouts.
- Use TimeValue class to define time values (duration).
- Optionally choose a connection pool concurrency policy: STRICT for strict connection max limit guarantees; LAX for higher concurrency but with lax connection maximum limit guarantees. With LAX policy HttpClient can exceed the per route maximum limit under high load and does not enforce the total maximum limit.
- Optionally choose a connection pool re-use policy: LIFO to re-use as few connections as possible making it possible for connections to become idle and expire; FIFO to re-use all connections equally preventing them from becoming idle and expiring.
- Optionally choose a finite total time to live for connections.
    ```
    PoolingHttpClientConnectionManager connectionManager = PoolingHttpClientConnectionManagerBuilder.create()
          .setSSLSocketFactory(SSLConnectionSocketFactoryBuilder.create()
                  .setSslContext(SSLContexts.createSystemDefault())
                  .setTlsVersions(TLS.V_1_3)
                  .build())
          .setDefaultSocketConfig(SocketConfig.custom()
                  .setSoTimeout(Timeout.ofMinutes(1))
                  .build())
          .setPoolConcurrencyPolicy(PoolConcurrencyPolicy.STRICT)
          .setConnPoolPolicy(PoolReusePolicy.LIFO)
          .setDefaultConnectionConfig(ConnectionConfig.custom()
                  .setSocketTimeout(Timeout.ofMinutes(1))
                  .setConnectTimeout(Timeout.ofMinutes(1))
                  .setTimeToLive(TimeValue.ofMinutes(10))
                  .build())
          .build();
    ```
- Favor the strict cookie policy when using HttpClient 5.0.
- Use response timeout to define the maximum period of inactivity until receipt of response data.
- All base principles and good practices of HttpClient programing still apply. Always re-use client instances. Client instances are expensive to create and are thread safe in both HttpClient 4.x and 5.x series.
    ```
    CloseableHttpClient client = HttpClients.custom()
          .setConnectionManager(connectionManager)
          .setDefaultRequestConfig(RequestConfig.custom()
                  .setCookieSpec(StandardCookieSpec.STRICT)
                  .build())
          .build();

    CookieStore cookieStore = new BasicCookieStore();

    CredentialsProvider credentialsProvider = new BasicCredentialsProvider();

    HttpClientContext clientContext = HttpClientContext.create();
    clientContext.setCookieStore(cookieStore);
    clientContext.setCredentialsProvider(credentialsProvider);
    clientContext.setRequestConfig(RequestConfig.custom()
          .setCookieSpec(StandardCookieSpec.STRICT)
          .build());

    JsonFactory jsonFactory = new JsonFactory();
    ObjectMapper objectMapper = new ObjectMapper(jsonFactory);

    ClassicHttpRequest httpPost = ClassicRequestBuilder.post("https://httpbin.org/post")
           .setEntity(HttpEntities.create(outstream -> {
               objectMapper.writeValue(outstream, Arrays.asList(
                       new BasicNameValuePair("name1", "value1"),
                       new BasicNameValuePair("name2", "value2")));
               outstream.flush();
           }, ContentType.APPLICATION_JSON))
           .build();
    ```
- HTTP response messages in HttpClient 5.x no longer have a status line. Use response code directly.
    ```
    JsonNode responseData = client.execute(httpPost, response -> {
      if (response.getCode() >= 300) {
          throw new ClientProtocolException(new StatusLine(response).toString());
      }
      final HttpEntity responseEntity = response.getEntity();
      if (responseEntity == null) {
          return null;
      }
      try (InputStream inputStream = responseEntity.getContent()) {
          return objectMapper.readTree(inputStream);
      }
    });
    System.out.println(responseData);
    ```
- CloseableHttpClient instances should be closed when no longer needed or about to go out of scope.
    ```
    client.close();
    ```
- The 4.x RequestConfig property normalizeUri has been removed, and URIUtils.normalizeSyntax is no longer public. In 4.x, these only supported limited normalization from RFC 3986 including removal of dot segments (section 5.2.4), and syntax-based normalization (section 6.2.2). The 5.x URIBuilder in httpcomponents-core has a new public normalizeSyntax method, but it strives for more thorough support of RFC 3986, specifically percent-encoding all components. Since 5.3, normalizeSyntax has been deprecated and renamed to optimize to emphasize the difference in behaviour.
- HttpClient 5 no longer provides a setConfig method on ClassicRequestBuilder. To apply a custom RequestConfig for an individual request, set it on the HttpClientContext, then pass that context to execute.
    - Before:
    ```
    RequestConfig config = RequestConfig.build();
    requestBuilder.setConfig(config);
    ```
    - After
    ```
    // Remove or comment request config related codes in request builder part
    ...
    // Update the codes where send the request 
    ClassicHttpRequest request = requestBuilder.build();
    RequestConfig config = RequestConfig.build();
    HttpClientContext context = HttpClientContext.create();
    context.setRequestConfig(requestConfig);
    httpclient.execute(request, context))
    ```
- The return type of HttpEntity.getContentType has changed to String in httpclient5, so we could simplify the codes like beloe
    - Before
    ```
    entity.getContentType().getValue()
    ```
    - After
    ```
    entity.getContentType()
    ```
- ContentType.get(HttpEntity) has been removed in httpclient5, to get the content type of http entity, just call HttpEntity.getContentType and parse it
    - Before
    ```
    ContentType.get(entity)
    ```
    - After
    ```
    ContentType..parse(entity.getContentType())
    ```
- HttpRequestRetryHandler was replcaed by HttpRequestRetryStrategy with httpclient5, but need to change the method signature and add up missing methods after upgrade
    - Before
    ```
    HttpRequestRetryHandler retryHander = new HttpRequestRetryHandler() {
        @Override
        public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
            // customer logic
        }
    }
    ```
    - After
    ```
    HttpRequestRetryStrategy retryHander = new HttpRequestRetryStrategy() {
        @Override
        public boolean retryRequest(HttpRequest request, IOException exception, int execCount, HttpContext context) {
            // original logic
        }

        @Override
        public boolean retryRequest(HttpResponse response, int execCount, HttpContext context) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'retryRequest'");
        }

        @Override
        public TimeValue getRetryInterval(HttpResponse response, int execCount, HttpContext context) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'getRetryInterval'");
        }
    };
    ```
- There are breaking changes in DefaultRedirectStrategy, if users overwrite method `public HttpUriRequest getRedirect(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException`, please comment related codes directly as this is no longer support in httpclient5
- `org.apache.http.conn.ssl.X509HostnameVerifier`, this was deprecated and finally removed in httpclient5, please use `javax.net.ssl.HostnameVerifier` to replace it
    - Before 
    ```
    new X509HostnameVerifier() {
        @Override
        public void verify(String host, SSLSocket ssl)
                throws IOException {
        }
        @Override
        public void verify(String host, X509Certificate cert)
                throws SSLException {
        }
        @Override
        public void verify(String host, String[] cns,
                           String[] subjectAlts) throws SSLException {
        }
        @Override
        public boolean verify(String s, SSLSession sslSession) {
            // logic here
        }
    }
    ```
    - After
    ```
    new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            // original logic here
        }
    }
    ```
- `HttpCoreContext.HTTP_TARGET_HOST` was removed in httpclient5, please replace it with its value "http.target_host"