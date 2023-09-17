package com.example.hibernatetest2.onetomany.integration.util;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.MySQLContainer;

import jakarta.transaction.Transactional;

@ActiveProfiles("test")
@Transactional  //? Auto dealing with opening and closing of transactions
@SpringBootTest //(classes = CustomBeansForTest.class) //? Allows you to use custom Beans for tests that is defined in [CustomBeansForTests1]
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL) //? For Dependency Injection to work
@Sql({ //? Path to custom sql scripts that will be runned every time every test class was started
      "classpath:sql/data.sql", //? This file is in: %this_application%/src/test/resources/sql/createtables.sql
      "classpath:sql/rows.sql"
})
/**
 * @WithMockUser (username = "test@gmail.com", //? It needs for tests to work with Spring-Security
                password = "test",
                authorities = {"ADMIN",
                               "USER"}
 )
 */
public abstract class IntegrationTestBase { //! Every Java class that does Integration testing must extend this class

    //? Setting up Docker from Test-Containers dependency --v

    private static final MySQLContainer<?> container = new MySQLContainer<>("mysql:8.1");

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
