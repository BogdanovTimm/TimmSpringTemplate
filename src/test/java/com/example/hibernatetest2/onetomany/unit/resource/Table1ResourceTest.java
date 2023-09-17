package com.example.hibernatetest2.onetomany.unit.resource;

import java.util.List;
import java.util.Optional;

import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.hibernatetest2.tables.onetomanytoone.entities.QTable1;
import com.example.hibernatetest2.tables.onetomanytoone.entities.Table1;
import com.example.hibernatetest2.tables.onetomanytoone.repositories.Table1Repository;
import com.example.hibernatetest2.tables.onetomanytoone.repositories.Table1RepositoryCustomQueryDslImpl;
import com.example.hibernatetest2.tables.onetomanytoone.resources.Table1Resource;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ActiveProfiles("test")
@WebMvcTest(Table1Resource.class) // [ ] Name of a Resource class you want to unit-test
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL) //? For Dependency Injection to work
public class Table1ResourceTest {

    /**
     * Special mock for Resouce
     */
    private final MockMvc mockMvc;

    @MockBean //? Class on which our class that we want to test depends on
    private Table1Repository table1Repository;

    @MockBean //? Class on which our class that we want to test depends on
    private Table1RepositoryCustomQueryDslImpl table1RepositoryCustomQueryDslImpl;

    private static final PageRequest pageRequest = PageRequest.of(0, //? Page number
                                                                  2, //? Size of one page
                                                                  Sort.by("id"));

    private static final Predicate predicate = ExpressionUtils.anyOf(QTable1.table1.name.containsIgnoreCase("th"),
                                                                     QTable1.table1.name.containsIgnoreCase("me"));

    private static final Optional<Table1> rowFromDatabase = Optional.of(Table1.builder().id(7L).name("tim@gmail.com").build());

    private static final Table1 rowToCreate = Table1.builder().name("new@gmail.com").build();

    private static final String creationBody = "{\"name\": \"new@gmail.com\"}";

    private static final Table1 creationRow = Table1.builder().id(8L).name("new@gmail.com").build();

    private static final List<Table1> listToReturnForThMe = List.of(Table1.builder().id(4L).name("megadeth@gmail.com").build(),
                                                                    Table1.builder().id(3L).name("blacksabbath@gmail.com").build());

    private static final Page<Table1> pageToReturnForThMe = new PageImpl<>(listToReturnForThMe);










    @org.junit.jupiter.api.Test
    public void findById() throws Exception {
        Mockito.when(table1Repository.findById(7L)).thenReturn(rowFromDatabase);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/table1" + "/7") //? URL where to send HTTP-GET request
        // .with(SecurityMockMvcRequestPostProcessors.user("test@gmail.com").authorities(Role.ADMIN)) //? For testing with signed in user. Works only if there is Spring Security dependency
        )
               .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
               .andExpect(MockMvcResultMatchers.jsonPath("$").value(Table1.builder().id(7L).name("tim@gmail.com").build()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(7L)) //? In returned json file, in variable id there is a value 7L
               .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("tim@gmail.com"));
    }



    @org.junit.jupiter.api.Test
    public void search() throws Exception {
        Mockito.when(table1Repository.findAll(predicate, pageRequest)).thenReturn(pageToReturnForThMe);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/table1" + "?searchPhrase=th") //? URL where to send HTTP-GET request
        // .with(SecurityMockMvcRequestPostProcessors.user("test@gmail.com").authorities(Role.ADMIN)) //? For testing with signed in user. Works only if there is Spring Security dependency
        )
               .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }



    @org.junit.jupiter.api.Test
    public void createNewRow() throws Exception {
        Mockito.when(table1Repository.saveAndFlush(rowToCreate)).thenReturn(creationRow);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/table1" + "/add")
                                              .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                                              .content(creationBody) //? URL where to send HTTP-POST request and content for creation
        // .with(SecurityMockMvcRequestPostProcessors.user("test@gmail.com").authorities(Role.ADMIN)) //? For testing with signed in user. Works only if there is Spring Security dependency
        )
               .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }



    @org.junit.jupiter.api.Test
    public void deleteRow() throws Exception {
        Mockito.when(table1Repository.deleteByName("tim@gmail.com")).thenReturn(1);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/table1" + "/tim@gmail.com" + "/delete") //? URL where to send HTTP-DELETE request and content for creation
        // .with(SecurityMockMvcRequestPostProcessors.user("test@gmail.com").authorities(Role.ADMIN)) //? For testing with signed in user. Works only if there is Spring Security dependency
        )
               .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

}
