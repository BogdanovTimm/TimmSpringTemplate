package com.example.hibernatetest2.tables.manytomany;

import jakarta.persistence.Entity;
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

// @Table (name = "table_name") // For tables with complex names
@Entity
@ToString(exclude = {"myOneAndOnlyTable3", "myOneAndOnlyTable4"}) // You need to exclude every List, Map, etc
@EqualsAndHashCode(exclude = {"myOneAndOnlyTable3", "myOneAndOnlyTable4"})
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
public class Table3Table4 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Don't forget to exclude this vraiable from @ToString and @EqualsAndHashCode
    @ManyToOne
    @JoinColumn(name = "table3_id")  // Name for this column
    private Table3 myOneAndOnlyTable3;

    // Don't forget to exclude this vraiable from @ToString and @EqualsAndHashCode
    @ManyToOne
    @JoinColumn(name = "table4_id")  // Name for this column
    private Table4 myOneAndOnlyTable4;

    public void setMyOneAndOnlyTable3(Table3 myOneAndOnlyTable3) {
        this.myOneAndOnlyTable3 = myOneAndOnlyTable3;
        this.myOneAndOnlyTable3.getListOfTable3Table4s().add(this);
    }

    public void setMyOneAndOnlyTable4(Table4 myOneAndOnlyTable4) {
        this.myOneAndOnlyTable4 = myOneAndOnlyTable4;
        this.myOneAndOnlyTable4.getListOfTable3Table4s().add(this);
    }

}
