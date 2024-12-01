package com.example.hibernatetest2.tables.onetoone.dto.mapper;

import org.mapstruct.Mapper;

import com.example.hibernatetest2.tables.manytomany.dto.Table3Dto;
import com.example.hibernatetest2.tables.manytomany.entities.Table3;
import com.example.hibernatetest2.tables.onetoone.dto.MasterTableDto;
import com.example.hibernatetest2.tables.onetoone.dto.SlaveTableDto;
import com.example.hibernatetest2.tables.onetoone.entities.MasterTable;
import com.example.hibernatetest2.tables.onetoone.entities.SlaveTable;

@Mapper(componentModel = "spring" //? For Spring's Dependency Injection to work
//uses = Table2Mapper.class) //? Allows you to use another mapper inside this one
)
public interface MasterTableMapper {

    //@Mapping(target = "table1DtoIdVariableName", source = "table1IdVariableName") //? If names of variables are different
    //@Mapping(target = "table1DtoVariableName", source = "table1VariableName", dateFormat = "dd-MM-yyyy HH:mm:ss") //? Converts Date to String
    //@Mapping(target = "id", source = "person.id", defaultExpression = "java(java.util.UUID.randomUUID().toString())") //? Define variable within Mapper
    MasterTableDto masterTableToDto(MasterTable table1);

    //@Mapping(target = "table1VariableName", source = "table1DtoVariableName", dateFormat = "dd-MM-yyyy HH:mm:ss") //? Converts String to Date
    MasterTable masterTableDtoToMasterTable(MasterTableDto table1Dto);
}
