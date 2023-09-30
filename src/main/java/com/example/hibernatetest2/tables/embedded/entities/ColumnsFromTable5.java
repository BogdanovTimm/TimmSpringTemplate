package com.example.hibernatetest2.tables.embedded.entities;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// @Table (name = "table1", schema = "public")
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ColumnsFromTable5 {

    private String firstname; //? Represents main_table.firstname

    private String lastname; //? Represents main_table.lastname
}
