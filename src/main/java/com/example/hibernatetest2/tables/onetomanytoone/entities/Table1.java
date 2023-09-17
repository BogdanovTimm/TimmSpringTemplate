package com.example.hibernatetest2.tables.onetomanytoone.entities;

import java.util.Map;
import java.util.TreeMap;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MapKey;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

// @Table (name = "table_name") //? For tables with complex names
@Entity
@JsonInclude(NON_DEFAULT) //! You need Spring-Web for this to work
@ToString(exclude = "mapOfTable2s") //! You need to exclude every List, Map, etc
@EqualsAndHashCode(exclude = "mapOfTable2s")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
public class Table1 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @Column(unique = true,
            nullable = false/*,
                            name = "column_name"*/) //? For columns with complex names
    // @NotEmpty(message = "Email cannot be empty")  //! You need Spring-Validation dependency for this to work
    // @Email(message = "Invalid email. Please enter a valid email address") //! You need Spring-Validation dependency for this to work
    @Size(min = 14, max = 32) //! You need Spring-Validation dependency for this to work
    private String name;



    @Enumerated(EnumType.STRING)
    private Enumeration1 enumeration;

    public enum Enumeration1 { VALUE1, VALUE2, VALUE3 }



    //! Don't forget to exclude this vraiable from @ToString and @EqualsAndHashCode
    @OneToMany(mappedBy = "myOnlyOneTalbe1", //? Variable name in Table2 that points to Talbe1.id
               cascade = CascadeType.ALL,
               orphanRemoval = false) //? Doesn't delete users if they were deleted from this Map, but not deleted from database (true is rarely used)
    @MapKey(name = "name") //? Table2.name will be the key to this Map
    @Builder.Default //? It is needed for creating of TreeMap, ArrayList, etc, instead of one randomly choosed by Hibernate
    @JsonIgnore //? If you don't want this variable to be added to JSON
    private Map<String, Table2> mapOfTable2s = new TreeMap<String, Table2>(); //! It doesn't represent any column. It is only for Java

}
