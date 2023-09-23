package com.example.hibernatetest2.onetomany.unit.service;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.example.hibernatetest2.tables.onetomany.dto.Table1Dto;
import com.example.hibernatetest2.tables.onetomany.dto.mapper.Table1Mapper;
import com.example.hibernatetest2.tables.onetomany.entities.QTable1;
import com.example.hibernatetest2.tables.onetomany.entities.Table1;
import com.example.hibernatetest2.tables.onetomany.repositories.Table1Repository;
import com.example.hibernatetest2.tables.onetomany.service.Table1Service;
import com.example.hibernatetest2.util.UnitTestBase;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;

@ExtendWith(MockitoExtension.class)
public class Table1ServiceTest extends UnitTestBase {

    @Mock //? Every class that we don't want to test must has this annotation
    private Table1Repository table1Repository;

    @Mock //? Every class that we don't want to test must has this annotation
    private Table1Mapper table1Mapper;

    @InjectMocks  //? In unit tests, variable that you want to test must have this Annotation
    private Table1Service table1Service;

    private static final Optional<Table1> optionalRowFromDatabase = Optional.of(Table1.builder()
                                                                                      .id(7L)
                                                                                      .name("tim@gmail.com")
                                                                                      .build());

    private static final Table1 rowFromDatabase = Table1.builder().id(7L).name("tim@gmail.com").build();

    private static final Table1 newRowFromDatabase = Table1.builder().id(7L).name("newName").build();

    private static final Table1Dto dto = Table1Dto.builder().id(7L).name("tim@gmail.com").build();

    private static final Table1Dto newDto = Table1Dto.builder().id(7L).name("newName").build();


    PageRequest pageRequest = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "id"));

    private static final Predicate predicate = ExpressionUtils.anyOf(QTable1.table1.name.containsIgnoreCase("th"),
                                                                     QTable1.table1.name.containsIgnoreCase("me"));


    private static final Table1 rowFromTable1 = Table1.builder()
                                                      .id(4L)
                                                      .name("megadeth@gmail.com")
                                                      .build();

    private static final Table1Dto rowFromTable1Dto = Table1Dto.builder()
                                                               .id(4L)
                                                               .name("megadeth@gmail.com")
                                                               .build();

    private static final Table1 row2FromTable1 = Table1.builder()
                                                       .id(3L)
                                                       .name("blacksabbath@gmail.com")
                                                       .build();

    private static final Table1Dto row2FromTable1Dto = Table1Dto.builder()
                                                                .id(3L)
                                                                .name("blacksabbath@gmail.com")
                                                                .build();

    private static final Page<Table1> pageToReturnForThMe = new PageImpl<>(List.of());

    private static final Page<Table1Dto> pageToReturnForThMeWithDto = new PageImpl<>(List.of(Table1Dto.builder()
                                                                                                      .id(4L)
                                                                                                      .name("megadeth@gmail.com")
                                                                                                      .build(),
                                                                                             Table1Dto.builder()
                                                                                                      .id(3L)
                                                                                                      .name("blacksabbath@gmail.com")
                                                                                                      .build()));





    @org.junit.jupiter.api.Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    public void search() {
        Mockito.when(table1Repository.findAll(predicate, pageRequest)).thenReturn(pageToReturnForThMe);
        Mockito.when(table1Mapper.table1ToDto(rowFromTable1)).thenReturn(rowFromTable1Dto);
        Mockito.when(table1Mapper.table1ToDto(row2FromTable1)).thenReturn(row2FromTable1Dto);
        var result = table1Service.search(0, 2, "id", "th me");
        Assertions.assertThat(result).isNotNull();
    }



    @org.junit.jupiter.api.Test
    public void findById() {
        Mockito.when(table1Repository.findById(7L)).thenReturn(optionalRowFromDatabase);
        Mockito.when(table1Mapper.table1ToDto(optionalRowFromDatabase.get())).thenReturn(dto);
        var resultToTest = table1Service.findById(7L);
        Assertions.assertThat(resultToTest).isNotNull();
        Assertions.assertThat(resultToTest.getId()).isEqualTo(7L);
        Assertions.assertThat(resultToTest.getName()).isEqualTo("tim@gmail.com");
    }



    @org.junit.jupiter.api.Test
    public void createNewRow() {
        Mockito.when(table1Repository.saveAndFlush(rowFromDatabase)).thenReturn(rowFromDatabase);
        Mockito.when(table1Mapper.table1ToDto(rowFromDatabase)).thenReturn(dto);
        var resultToTest = table1Service.createNewRow(rowFromDatabase);
        Assertions.assertThat(resultToTest).isNotNull();
        Assertions.assertThat(resultToTest.getId()).isEqualTo(7L);
        Assertions.assertThat(resultToTest.getName()).isEqualTo("tim@gmail.com");
    }



    @org.junit.jupiter.api.Test
    public void changeRow() {
        Mockito.when(table1Repository.findById(7L)).thenReturn(optionalRowFromDatabase);
        Mockito.when(table1Repository.saveAndFlush(newRowFromDatabase)).thenReturn(newRowFromDatabase);
        Mockito.when(table1Mapper.table1ToDto(newRowFromDatabase)).thenReturn(newDto);
        var resultToTest = table1Service.changeRow("newName", 7L);
        Assertions.assertThat(resultToTest).isNotNull();
        Assertions.assertThat(resultToTest.getId()).isEqualTo(7L);
        Assertions.assertThat(resultToTest.getName()).isEqualTo("newName");
    }



    @org.junit.jupiter.api.Test
    public void deleteRow() {
        Mockito.when(table1Repository.deleteByName("tim@gmail.com")).thenReturn(1);
        var resultToTest = table1Service.deleteRow("tim@gmail.com");
        Assertions.assertThat(resultToTest).isNotNull();
        Assertions.assertThat(resultToTest).isEqualTo(1);
    }

}
