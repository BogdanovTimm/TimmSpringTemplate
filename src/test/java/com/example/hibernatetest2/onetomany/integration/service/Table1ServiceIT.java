package com.example.hibernatetest2.onetomany.integration.service;

import java.util.NoSuchElementException;

import org.assertj.core.api.Assertions;

import com.example.hibernatetest2.tables.onetomany.dto.Table1Dto;
import com.example.hibernatetest2.tables.onetomany.entities.Table1;
import com.example.hibernatetest2.tables.onetomany.service.Table1Service;
import com.example.hibernatetest2.util.IntegrationTestBase;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class Table1ServiceIT extends IntegrationTestBase {

    //! Don't forget to run Docker on the PC when you do integration tests

    private final Table1Service table1Service; //! It will not work if it is not private final

    private final String searchPhrase = "th me";

    @org.junit.jupiter.api.Test
    void findAllWithEmptyPredicate() {
        Assertions.assertThat(table1Service.search(0, 2, "id", "")).isNotEmpty();
    }



    @org.junit.jupiter.api.Test
    void findAllWithSomePredicate() {
        Assertions.assertThat(table1Service.search(0, 2, "id", searchPhrase)).isNotEmpty();
    }



    @org.junit.jupiter.api.Test
    void ckeckfindById() {
        var rowDtoFromTable1 = table1Service.findById(1L);
        Assertions.assertThat(rowDtoFromTable1)
                  .isEqualTo(Table1Dto.builder()
                                      .id(1L)
                                      .name("metallica@gmail.com")
                                      .build());
    }

    @org.junit.jupiter.api.Test
    void saveAndFlush() {
        var newRow = Table1.builder()
                           .name("bigben@gmail.com")
                           .build();
        table1Service.createNewRow(newRow);
        var newRowFromDatabase = table1Service.findById(7L);
        Assertions.assertThat(newRowFromDatabase)
                  .isEqualTo(Table1Dto.builder()
                                      .id(7L)
                                      .name("bigben@gmail.com")
                                      .build());
    }

    @org.junit.jupiter.api.Test
    void delete() {
        Assertions.assertThat(table1Service.deleteRow("metallica@gmail.com")).isEqualTo(1);
        try {
            Assertions.assertThat(table1Service.findById(1L)).hasAllNullFieldsOrProperties();
        } catch (Exception exception) {
            Assertions.assertThat(exception).isInstanceOf(NoSuchElementException.class);
        }
    }

}
