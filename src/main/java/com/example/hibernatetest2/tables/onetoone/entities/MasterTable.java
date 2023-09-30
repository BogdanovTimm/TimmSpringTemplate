package com.example.hibernatetest2.tables.onetoone.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

// @Table (name = "table_name") //? For tables with complex names
@Entity
@JsonInclude(NON_DEFAULT) //! You need Spring-Web for this to work
@ToString(exclude = "mySlaveTable") //! You need to exclude every List, Map, etc
@EqualsAndHashCode(exclude = "mySlaveTable")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
public class MasterTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Enumeration1 enumeration;

    public enum Enumeration1 { VALUE1, VALUE2, VALUE3 }

    private String name;

    @OneToOne(mappedBy = "myMasterTable",  //? Variable name in SlaveTable that points to MasterTable.id
              cascade = CascadeType.ALL)
    @JsonIgnore //? It is need to not creating a loop. You need Spring-Web dependency for this to work
    private SlaveTable mySlaveTable;
}
