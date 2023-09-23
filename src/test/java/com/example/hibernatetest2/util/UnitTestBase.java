package com.example.hibernatetest2.util;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;

@ActiveProfiles("test")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL) //? For Dependency Injection to work
@WithMockUser (username = "test@gmail.com", //? It needs for tests to work with Spring-Security
               password = "test",
               authorities = {"DELETE:USER"}
)
public class UnitTestBase {
    
}
