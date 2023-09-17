package com.example.hibernatetest2.onetomany.integration.resource;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.hibernatetest2.onetomany.integration.util.IntegrationTestBase;
import com.example.hibernatetest2.tables.onetomanytoone.entities.Table1;

import lombok.RequiredArgsConstructor;

@AutoConfigureMockMvc
@RequiredArgsConstructor
public class Table1ResourceIT extends IntegrationTestBase {

    /**
     * Special mock for Resouce
     */
    private final MockMvc mockMvc;

    @org.junit.jupiter.api.Test
    void findAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/table1/tim@gmail.com") //? URL where to send HTTP-GET request
        // .with(SecurityMockMvcRequestPostProcessors.user("test@gmail.com").authorities(Role.ADMIN)) //? For testing with signed in user. Works only if there is Spring Security dependency
        )
               .andExpect(MockMvcResultMatchers.status()
                                               .is2xxSuccessful())
               .andExpect(MockMvcResultMatchers.jsonPath("$")
                                               .value(Table1.builder()
                                                            .id(7L)
                                                            .name("tim@gmail.com")
                                                            .build()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.id") //? In returned json file, in variable id...
                                               .value(7L)) //? ... there is a value 7L
               .andExpect(MockMvcResultMatchers.jsonPath("$.name")
                                               .value("tim@gmail.com"));
    }
    
}
