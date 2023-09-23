# Add spring-boot-starter-mail

# Insert this into application.yaml:

```YAML
spring:
    mail:
        host: smtp.gmail.com
        port: 587
        username: bogdanovtimm@gmail.com
        password: asdp ovdy nhpp ekig # rxuirozapnegszbz
        properties:
            mail:
                smtp:
                    writetimeout: 5000
                    connectiontimeout: 5000
                    timeout: 5000
                    auth: true
                    starttls:
                        enable: true
                        required: true
    security:
        oauth2:
            client:
                registration:
                    #google:
                        # clientId:
                        #clientSecret:
                        #redirectUri: http://localhost/login/oauth2/code/google #? IDK how to set this right
                        #scope: openid,email,profile  #? IDK how to set this right
                    github:
                        clientId:
                        clientSecret:
                        redirectUri: http://localhost/login/oauth2/code/github #? This code you need to paste into github in settings
                        #scope: openid,email,profile  #? What to get from user's github account
```

# Insert this into Application.java:

```JAVA
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    /**
     * Sets:
     * <ol>
     * <li>from which websites our application can consume HTTP-Requests
     * <li>which HTTP-Headers is allowed from websites that is allowed in 1.
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:4200",
                                                    "http://localhost:3000",
                                                    "http://localhost:80", // nginx server
                                                    "http://localhost", // allows every HTTP-Request from all ports from localhost
                                                    "https://localhost",
                                                    "http://192.168.0.102",
                                                    "https://192.168.0.102",
                                                    "http://185.43.5.52",
                                                    "https://185.43.5.52",
                                                    "http://timofeimen.fvds.ru",
                                                    "https://timofeimen.fvds.ru"));
        corsConfiguration.setAllowedHeaders(Arrays.asList("Origin",
                                                          "Access-Control-Allow-Origin",
                                                          "Content-Type",
                                                          "Accept",
                                                          "Jwt-Token",
                                                          "Authorization",
                                                          "Origin",
                                                          "Accept",
                                                          "X-Requested-With",
                                                          "Access-Control-Request-Method",
                                                          "Access-Control-Request-Headers"));
        corsConfiguration.setExposedHeaders(Arrays.asList("Origin",
                                                          "Content-Type",
                                                          "Accept",
                                                          "Jwt-Token",
                                                          "Authorization",
                                                          "Access-Control-Allow-Origin",
                                                          "Access-Control-Allow-Origin",
                                                          "Access-Control-Allow-Credentials",
                                                          "File-Name"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET",
                                                          "POST",
                                                          "PUT",
                                                          "PATCH",
                                                          "DELETE",
                                                          "OPTIONS"));
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }
```