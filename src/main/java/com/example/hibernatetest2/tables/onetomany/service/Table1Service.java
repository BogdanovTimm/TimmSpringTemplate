package com.example.hibernatetest2.tables.onetomany.service;

import java.util.ArrayList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.hibernatetest2.tables.onetomany.dto.Table1Dto;
import com.example.hibernatetest2.tables.onetomany.dto.mapper.Table1Mapper;
import com.example.hibernatetest2.tables.onetomany.entities.QTable1;
import com.example.hibernatetest2.tables.onetomany.entities.Table1;
import com.example.hibernatetest2.tables.onetomany.repositories.Table1Repository;
import com.example.hibernatetest2.tables.onetomany.repositories.Table1RepositoryCustomQueryDslImpl;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
public class Table1Service {

    private final Table1Repository table1Repository;

    private final Table1RepositoryCustomQueryDslImpl table1RepositoryCustom;

    private final Table1Mapper table1Mapper;



    public Page<Table1Dto> search(
                                  Integer pageNumber,
                                  Integer numberOfRowsInOnePage,
                                  String sortedBy,
                                  String searchPhrase) {
        var pageRequest = PageRequest.of(pageNumber,
                                         numberOfRowsInOnePage,
                                         Sort.by(Sort.Direction.DESC, sortedBy));
        var listOfBooleanExpressions = new ArrayList<Predicate>(); //! You need QueryDsl dependency for this to work
        String[] arr = searchPhrase.split(" "); //! http://website.com/?searchPhrase=me+th parses internally as searchPhrase = "me th"
        for (String currentWord : arr) {
            listOfBooleanExpressions.add(QTable1.table1.name.containsIgnoreCase(currentWord));//[ ] QTable1 is a class generated by QueryDsl. You need to change it to class that represents your table, like QMyTable
        }
        var predicate = ExpressionUtils.anyOf(listOfBooleanExpressions); //? Connects QueryDslPredicate1s with logivcanl OR
        return table1Repository.findAll(predicate, pageRequest).map(table1Mapper::table1ToDto);
    }

    public Table1Dto findById(Long id) {
        var optionalRowFromTable1 = table1Repository.findById(id);
        return table1Mapper.table1ToDto(optionalRowFromTable1.get());
    }

    public Table1Dto createNewRow(Table1 creatingForm) {
        return table1Mapper.table1ToDto(table1Repository.saveAndFlush(creatingForm));
    }

    public Table1Dto changeRow(String newName,
                               Long id) {
        var rowToChange = table1Repository.findById(id).get();
        rowToChange.setName(newName);
        return table1Mapper.table1ToDto(table1Repository.saveAndFlush(rowToChange));
    }

    public int deleteRow(String name) {
        return table1Repository.deleteByName(name);
    }

}
