package com.example.hibernatetest2;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.example.hibernatetest2.tables.onetoone.repositories.MasterTableRepository;


@SpringBootApplication
public class Hibernatetest2Application {

    public static void main(String[] args) {
        var application = SpringApplication.run(Hibernatetest2Application.class, args);
        var masterTable = application.getBean("masterTableRepository", MasterTableRepository.class);
        var rowFromMasterTable = masterTable.findById(1L);
        System.out.println(rowFromMasterTable.get().getMySlaveTable());
        
    }

    //? Spring Security --v
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
    //? Spring Security --^

}
