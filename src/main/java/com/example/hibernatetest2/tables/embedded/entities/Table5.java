package com.example.hibernatetest2.tables.embedded.entities;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Entity
@JsonInclude(NON_DEFAULT) //! You need Spring-Web for this to work
@ToString(exclude = "myColumns") //! You need to exclude every List, Map, etc
@EqualsAndHashCode(exclude = "myColumns")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
public class Table5 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Enumeration1 enumeration;

    public enum Enumeration1 { VALUE1, VALUE2, VALUE3 }

    private String name;

    @Embedded //? Doesn't represent any table, but has main_table.firstname and main_table.lastname columns in itself
    private ColumnsFromTable5 myColumns;
}
