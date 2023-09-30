package com.example.hibernatetest2.tables.onetoone.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
@ToString(exclude = "myMasterTable") //! You need to exclude every List, Map, etc
@EqualsAndHashCode(exclude = "myMasterTable")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
public class SlaveTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Enumeration1 enumeration;

    public enum Enumeration1 { VALUE1, VALUE2, VALUE3 }

    //! Don't forget to exclude this vraiable from @ToString and @EqualsAndHashCode
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "master_table_id") //? Name of this column in the database
    @JsonIgnore //? It is need to not creating a loop. You need Spring-Web dependency for this to work
    private MasterTable myMasterTable;
}
