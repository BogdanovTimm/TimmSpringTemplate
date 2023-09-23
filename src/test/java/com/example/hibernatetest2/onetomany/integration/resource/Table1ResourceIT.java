package com.example.hibernatetest2.onetomany.integration.resource;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.hibernatetest2.security.entities.RoleType;
import com.example.hibernatetest2.tables.onetomany.entities.Table1;
import com.example.hibernatetest2.util.IntegrationTestBase;

import lombok.RequiredArgsConstructor;

@AutoConfigureMockMvc
@RequiredArgsConstructor
public class Table1ResourceIT extends IntegrationTestBase {

    /**
     * Special mock for Resouce
     */
    private final MockMvc mockMvc;

    private static final String creationBody = "{\"name\": \"new@gmail.com\"}";



    @org.junit.jupiter.api.Test
    void findById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/table1/6")) //? URL where to send HTTP-GET request
               .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
               .andExpect(MockMvcResultMatchers.jsonPath("$")
                                               .value(Table1.builder()
                                                            .id(6L)
                                                            .name("tim@gmail.com")
                                                            .build()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.id") //? In returned json file, in variable id...
                                               .value(6L)) //? ... there is a value 7L
               .andExpect(MockMvcResultMatchers.jsonPath("$.name")
                                               .value("tim@gmail.com"));
    }

    @org.junit.jupiter.api.Test
    void search() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/table1")) //? URL where to send HTTP-GET request
               .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
               .andExpect(MockMvcResultMatchers.jsonPath("$.content").exists())
               .andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray());
    }

    @org.junit.jupiter.api.Test
    void createNewRow() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/table1/add") //? URL where to send HTTP-GET request
                                              .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                                              .content(creationBody) //? URL where to send HTTP-POST request and content for creation
                                              .with(SecurityMockMvcRequestPostProcessors.user("test@gmail.com") //? For testing with signed in user. Works only if there is Spring Security dependency
                                                                                        .authorities(new SimpleGrantedAuthority("DELETE:USER")))
        )
               .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
               .andExpect(MockMvcResultMatchers.jsonPath("$").exists());
    }

    @org.junit.jupiter.api.Test
    void deleteRow() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/table1/delete/tim@gmail.com") //? URL where to send HTTP-DELETE request and content for creation
                                              .with(SecurityMockMvcRequestPostProcessors.user("test@gmail.com")
                                              .authorities(new SimpleGrantedAuthority("DELETE:USER"))) //? For testing with signed in user. Works only if there is Spring Security dependency
        )
               .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

}
