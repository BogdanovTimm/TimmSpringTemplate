package com.example.hibernatetest2.tables.manytomany.dto;


import com.example.hibernatetest2.tables.manytomany.entities.Table3.Enumeration1;

import lombok.Value;
import lombok.experimental.SuperBuilder;

@Value
@SuperBuilder
public class Table3Dto {

    private Long id;

    private String name;

    private Enumeration1 enumeration;

    //! We do not create Collections in DTOs

}
