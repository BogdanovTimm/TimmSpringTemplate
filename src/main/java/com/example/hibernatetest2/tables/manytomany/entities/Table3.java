package com.example.hibernatetest2.tables.manytomany.entities;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

import java.util.ArrayList;
import java.util.List;

// @Table (name = "table_name") //? For tables with complex names
@Entity
@JsonInclude(NON_DEFAULT) //! You need Spring-Web dependency for this to work
@ToString(exclude = "listOfTable3Table4s") //[ ] You need to exclude every List, Map, etc
@EqualsAndHashCode(exclude = "listOfTable3Table4s") //[ ]! You need to exclude every List, Map, etc
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
public class Table3 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,
            nullable = false/*,
                            name = "column_name"*/) //? For columns with complex names
    private String name;

    @Enumerated(EnumType.STRING)
    private Enumeration1 enumeration;

    public enum Enumeration1 { VALUE1, VALUE2, VALUE3 }

    @OneToMany(mappedBy = "myOneAndOnlyTable3", //[ ]] Variable name in Table1Table2 that points to OtherTable.id
               orphanRemoval = true) //? If true, deletes entire row in Table1Table2 when Table1 is deleted
    @Builder.Default
    private List<Table3Table4> listOfTable3Table4s = new ArrayList<>(); //! It doesn't represent any column. It is only for Java
}
