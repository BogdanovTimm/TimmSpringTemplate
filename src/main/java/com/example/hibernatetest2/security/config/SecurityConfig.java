package com.example.hibernatetest2.security.config;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.hibernatetest2.security.exception.CustomAccessDeniedHandler;
import com.example.hibernatetest2.security.exception.CustomAuthenticationEntryPoint;
import com.example.hibernatetest2.security.filter.CustomAuthorizationFilter;
import com.example.hibernatetest2.security.repository.UserRepositoryImpl;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final BCryptPasswordEncoder encoder;

    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    /**
     * Spring just finds class that implements UserDetailsService (UserRepositoryImpl in the case of this application).
     */
    private final UserDetailsService userDetailsService;

    private final CustomAuthorizationFilter customAuthorizationFilter;

    private final UserRepositoryImpl userRepositoryImpl;

    private static final String[] PUBLIC_URLS = {"/api/v1/user/verify/password/**",
                                                 "/api/v1/user/login/**",
                                                 "/api/v1/user/verify/code/**",
                                                 "/api/v1/user/register/**",
                                                 "/api/v1/user/resetpassword/**",
                                                 "/api/v1/user/verify/account/**",
                                                 "/api/v1/user/refresh/token/**",
                                                 "/api/v1/user/image/**",
                                                 "/api/v1/user/new/password/**",
                                                 "/api/v1/manytomany/**",
                                                 "/home.html",
                                                 "/login.html",
                                                 "/register.html",
                                                 // #
                                                 "/#/api/v1/user/verify/password/**",
                                                 "/#/api/v1/user/login/**",
                                                 "/#/api/v1/user/verify/code/**",
                                                 "/#/api/v1/user/register/**",
                                                 "/#/api/v1/user/resetpassword/**",
                                                 "/#/api/v1/user/verify/account/**",
                                                 "/#/api/v1/user/refresh/token/**",
                                                 "/#/api/v1/user/image/**",
                                                 "/#/api/v1/user/new/password/**",
                                                 "/#/**",
                                                 "/#/login/**",
                                                 "/#/register/**",
                                                 "/login/**",
                                                 "/oauth2/authorization/google",
                                                 "/register/**"};



    /**
     * Sets:
     * <ul>
     * <li>CSRF
     * <li>CORS
     * <li>Webpages that can be seen without signing in
     * <li>Permissions for HTTP-Methods for some webpages
     * <li>Custom handler for Access Denied Error
     * <li>Custom handler for non-logged user
     * <li>Custom filter that checks every HTTP-request
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // We disable csrf because in REST API we don't need ti
            .cors(Customizer.withDefaults());
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.logout(logout -> logout.logoutUrl("/logout") // URL of logout page
                                    .logoutSuccessUrl("/login")
                                    .deleteCookies("JSESSIONID"))
            .formLogin(Customizer.withDefaults()) // Where to redirect if user successfully has singed in
            .oauth2Login(config -> config.loginPage("/login")
                                         .defaultSuccessUrl("/users")
                                         .userInfoEndpoint(userInfo -> userInfo.oidcUserService(oidcUserService())));
        http.authorizeHttpRequests(request -> request.requestMatchers(PUBLIC_URLS).permitAll());
        http.authorizeHttpRequests(request -> request.requestMatchers(HttpMethod.OPTIONS).permitAll());
        http.authorizeHttpRequests(request -> request.requestMatchers(HttpMethod.DELETE,
                                                                      "/api/v1/user/delete/**",
                                                                      "/api/v1/table1/delete/**",
                                                                      "/api/v1/table2/delete/**"
        )
                                                     .hasAuthority("DELETE:USER")
        );
        http.authorizeHttpRequests(request -> request.requestMatchers(HttpMethod.DELETE,
                                                                      "/api/v1/customer/delete/**")
                                                     .hasAuthority("DELETE:CUSTOMER")
        );
        http.exceptionHandling(exception -> exception.accessDeniedHandler(customAccessDeniedHandler) // Sets custom handler for Access Denied Error
                                                     .authenticationEntryPoint(customAuthenticationEntryPoint)); // Sets custom handler for non-logged user
        http.addFilterBefore(customAuthorizationFilter, UsernamePasswordAuthenticationFilter.class); // Adds filter that checks user's permissions for every HTTP-Request
        http.authorizeHttpRequests(request -> request.anyRequest().authenticated()); // All others requests must be authenticated
        return http.build();
    }

    /**
     * Sets encoder for User's passwords
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder);
        return new ProviderManager(authProvider);
    }

    private OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
        return userRequest -> {
            String email = userRequest.getIdToken().getClaim("email");
            UserDetails userDetails = userRepositoryImpl.loadUserByUsername(email);
            //            new OidcUserService().loadUser()
            DefaultOidcUser oidcUser = new DefaultOidcUser(userDetails.getAuthorities(),
                                                           userRequest.getIdToken()
            );
            Set<Method> userDetailsMethods = Set.of(UserDetails.class.getMethods());
            return (OidcUser)Proxy.newProxyInstance(SecurityConfig.class.getClassLoader(),
                                                    new Class[] {UserDetails.class,
                                                                 OidcUser.class
                                                    },
                                                    (proxy, method, args) -> userDetailsMethods.contains(method) ?// 
                                                            method.invoke(userDetails, args) ://
                                                            method.invoke(oidcUser, args)
            );
        };
    }
}

