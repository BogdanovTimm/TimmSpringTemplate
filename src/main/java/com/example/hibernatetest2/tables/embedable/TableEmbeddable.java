package com.example.hibernatetest2.tables.embedable;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@SuperBuilder
/**
 * Every table can create an iterable variable that points to that class. 
 * Hibernate will create a new table for every table that points for this class.
 * It is good for translations.
 */
public class TableEmbeddable {

    //! There is no Id Column

    //! Column that points to other table will be automatically created

    private String language;

    private String translation;

}
