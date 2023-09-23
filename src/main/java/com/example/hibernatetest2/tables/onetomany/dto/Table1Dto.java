package com.example.hibernatetest2.tables.onetomany.dto;

import com.example.hibernatetest2.tables.onetomany.entities.Table1.Enumeration1;
import lombok.Value;
import lombok.experimental.SuperBuilder;

@Value
@SuperBuilder
public class Table1Dto {

    private Long id;

    private String name;

    private Enumeration1 enumeration;

    //! We do not create Collections in DTOs

}
