package com.example.hibernatetest2.tables.onetomanytoone.entities;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

// @Table (name = "table_name") //? For tables with complex names
@Entity
@JsonInclude(NON_DEFAULT) //! You need Spring-Web dependency for this to work
@ToString (exclude = "myOnlyOneTalbe1") //! You need to exclude every List, Map, etc
@EqualsAndHashCode (exclude = "myOnlyOneTalbe1") //! You need to exclude every List, Map, etc
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
public class Table2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,
            nullable = false/*,
                            name = "column_name"*/) //? For columns with complex names
    private String name;

    //! Don't forget to exclude this vraiable from @ToString and @EqualsAndHashCode
    @JoinColumn(name = "table1_id") //? Name of this column in the database
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore //? It is need to not creating a loop. You need Spring-Web dependency for this to work
    private Table1 myOnlyOneTalbe1; //! Every time you want to add or change this, the table1 instance must exist yet.
}
