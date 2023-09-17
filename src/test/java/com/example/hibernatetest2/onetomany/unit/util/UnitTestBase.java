package com.example.hibernatetest2.onetomany.unit.util;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;

@ActiveProfiles("test")
/**
 * @WithMockUser (username = "test@gmail.com", //? It needs for tests to work with Spring-Security
 * password = "test",
 * authorities = {"ADMIN",
 * "USER"}
 * )
 */
public abstract class UnitTestBase {

    //? Setting up Docker from Test-Containers dependency --v

    private static final MySQLContainer<?> container = new MySQLContainer<>("mysql:8.0");

    @BeforeAll
    static void contextLoads() {
        container.start();
    }

    /**
     * Changes spring:datasource:url: to dynamically created URL for mysql-database in a Docker from Test-Containers dependency
     */
    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry applicationDotYaml) {
        applicationDotYaml.add("spring.datasource.url", container::getJdbcUrl);
    }

    //? Setting up Docker from Test-Containers dependency --^

}
