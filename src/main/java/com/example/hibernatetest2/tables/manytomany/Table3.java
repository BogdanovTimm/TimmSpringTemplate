package com.example.hibernatetest2.tables.manytomany;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.Entity;
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

// @Table (name = "table_name") // For tables with complex names
@Entity
@ToString(exclude = "listOfTable3Table4s") // You need to exclude every List, Map, etc
@EqualsAndHashCode(exclude = "listOfTable3Table4s")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
public class Table3 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "myOneAndOnlyTable3", // Variable name in Table3Table4 that points to Talbe3.id
               orphanRemoval = true) // If true, deletes entire row in Table3Table4 when Table3 is deleted
    @Builder.Default
    private List<Table3Table4> listOfTable3Table4s = new ArrayList<>(); // It doesn't represent any column. It is only for Java

}
