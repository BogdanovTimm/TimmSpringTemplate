package com.example.hibernatetest2.onetomany.unit.resource;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.example.hibernatetest2.tables.onetomany.dto.Table1Dto;

import com.example.hibernatetest2.tables.onetomany.entities.Table1;
import com.example.hibernatetest2.tables.onetomany.repositories.Table1Repository;
import com.example.hibernatetest2.tables.onetomany.repositories.Table1RepositoryCustomQueryDslImpl;
import com.example.hibernatetest2.tables.onetomany.rest.Table1Resource;
import com.example.hibernatetest2.tables.onetomany.service.Table1Service;
import com.example.hibernatetest2.util.UnitTestBase;
import com.example.hibernatetest2.security.jwttoken.CustomJWTTokenHandler;
import lombok.RequiredArgsConstructor;

@WebMvcTest(Table1Resource.class) // [ ] Name of a Resource class you want to unit-test
@RequiredArgsConstructor
class Table1ResourceTest extends UnitTestBase {

    /**
     * Special mock for Resouce
     */
    private final MockMvc mockMvc;

    @MockBean //? Class on which our class that we want to test depends on
    private Table1Service table1Service;

    @MockBean //? Class on which our class that we want to test depends on
    private Table1Repository table1Repository;

    @MockBean //? Class on which our class that we want to test depends on
    private Table1RepositoryCustomQueryDslImpl table1RepositoryCustomQueryDslImpl;

    @MockBean //? Class on which our class that we want to test depends on
    private CustomJWTTokenHandler CustomJWTTokenHandler;

    private static final Table1Dto rowFromDatabase = Table1Dto.builder().id(7L).name("tim@gmail.com").build();

    private static final Table1 rowToCreate = Table1.builder().name("new@gmail.com").build();

    private static final String creationBody = "{\"name\": \"new@gmail.com\"}";

    private static final Table1Dto creationRow = Table1Dto.builder().id(8L).name("new@gmail.com").build();

    private static final List<Table1Dto> listToReturnForThMe = List.of(Table1Dto.builder()
                                                                                .id(4L)
                                                                                .name("megadeth@gmail.com")
                                                                                .build(),
                                                                       Table1Dto.builder()
                                                                                .id(3L)
                                                                                .name("blacksabbath@gmail.com")
                                                                                .build());

    private static final Page<Table1Dto> pageToReturnForThMe = new PageImpl<>(listToReturnForThMe);


    @org.junit.jupiter.api.Test
    void findById() throws Exception {
        Mockito.when(table1Service.findById(7L)).thenReturn(rowFromDatabase);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/table1" + "/7")) //? URL where to send HTTP-GET request
               .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
               .andExpect(MockMvcResultMatchers.jsonPath("$").value(Table1.builder().id(7L).name("tim@gmail.com").build()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(7L)) //? In returned json file, in variable id there is a value 7L
               .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("tim@gmail.com"));
    }

    @org.junit.jupiter.api.Test
    void search() throws Exception {
        Mockito.when(table1Service.search(null, null, null, "th")).thenReturn(pageToReturnForThMe);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/table1" + "?searchPhrase=th")) //? URL where to send HTTP-GET request
               .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Disabled
    @org.junit.jupiter.api.Test
    void createNewRow() throws Exception {
        Mockito.when(table1Service.createNewRow(rowToCreate)).thenReturn(creationRow);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/table1" + "/add")
                                              .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                                              .content(creationBody) //? URL where to send HTTP-POST request and content for creation
                                              .with(SecurityMockMvcRequestPostProcessors.user("test@gmail.com").authorities(new SimpleGrantedAuthority("DELETE:USER"))) //? For testing with signed in user. Works only if there is Spring Security dependency
        )
               .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Disabled
    @org.junit.jupiter.api.Test
    void deleteRow() throws Exception {
        Mockito.when(table1Service.deleteRow("tim@gmail.com")).thenReturn(1);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/table1/delete/tim@gmail.com") //? URL where to send HTTP-DELETE request and content for creation
                                              .with(SecurityMockMvcRequestPostProcessors.user("test@gmail.com")
                                                                                        .authorities(new SimpleGrantedAuthority("DELETE:USER"))) //? For testing with signed in user. Works only if there is Spring Security dependency
        )
               .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
