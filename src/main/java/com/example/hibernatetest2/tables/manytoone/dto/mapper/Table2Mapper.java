package com.example.hibernatetest2.tables.manytoone.dto.mapper;

import org.mapstruct.Mapper;

import com.example.hibernatetest2.tables.manytoone.dto.Table2Dto;
import com.example.hibernatetest2.tables.manytoone.entities.Table2;


@Mapper(componentModel = "spring" //? For Spring's Dependency Injection to work
//uses = Table2Mapper.class) //? Allows you to use another mapper inside this one
)
public interface Table2Mapper {

    //@Mapping(target = "table1DtoIdVariableName", source = "table1IdVariableName") //? If names of variables are different
    //@Mapping(target = "table1DtoVariableName", source = "table1VariableName", dateFormat = "dd-MM-yyyy HH:mm:ss") //? Converts Date to String
    //@Mapping(target = "id", source = "person.id", defaultExpression = "java(java.util.UUID.randomUUID().toString())") //? Define variable within Mapper
    Table2Dto table2ToDto(Table2 table1);

    //@Mapping(target = "table1VariableName", source = "table1DtoVariableName", dateFormat = "dd-MM-yyyy HH:mm:ss") //? Converts String to Date
    Table2 table2DtoToTable2(Table2Dto table1Dto);

}
