package com.example.hibernatetest2.tables.embedable;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MapKeyColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

// ? @Table (name = "table_name") //? For tables with complex names
// ? @JsonInclude(NON_DEFAULT) //? You need Spring-Web for this to work
@Entity
@ToString(exclude = "rowsFromEmbeddableTable") //? You need to exclude every List, Map, etc
@EqualsAndHashCode(exclude = "rowsFromEmbeddableTable")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
public class Table5 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // This vraible must be placed in a table for which you want to create an embeddable table--v
    //[ ] Don't forget to exclude this vraiable from @ToString and @EqualsAndHashCode
    @ElementCollection //? Says that this map points to a table that annotated with @Embeddable
    @CollectionTable(name = "table5_embeddabletable", //? Name of a table to create
                     joinColumns = @jakarta.persistence.JoinColumn(name = "table5_id")) //? Name of an id column to create
    @MapKeyColumn(name = "language") //? Column in TableEmbeddable that will be used as a key to this Map
    @Column(name = "translation") //? Column in TableEmbeddable that will be used as a value to this Map
    @Builder.Default //? It is needed for creating of TreeMap, ArrayList, etc, instead of one randomly choosed by Hibernate
    private java.util.Map<String, String> rowsFromEmbeddableTable = new java.util.HashMap<>(); //! This map doesn't represent any column in this table in the database
    // This vraible must be placed in a table for which you want to create an embeddable table--^

}
