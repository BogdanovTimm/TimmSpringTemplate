package com.example.hibernatetest2.tables.embedded.dto;

import com.example.hibernatetest2.tables.embedded.entities.ColumnsFromTable5;
import com.example.hibernatetest2.tables.manytoone.entities.Table2.Enumeration1;

import lombok.Value;
import lombok.experimental.SuperBuilder;

@Value
@SuperBuilder
public class Table5Dto {
    
    private Long id;

    private String name;

    private Enumeration1 enumeration;

    private ColumnsFromTable5 myColumns;
}
