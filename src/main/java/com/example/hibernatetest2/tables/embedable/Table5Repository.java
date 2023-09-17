package com.example.hibernatetest2.tables.embedable;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface Table5Repository extends JpaRepository<Table5, Long> { //? Auto-creating of functions
    
     
    @EntityGraph(attributePaths = {"rowsFromEmbeddableTable"}) //?If this table points to other tables
    Page<Table5> findByName(String name, org.springframework.data.domain.Pageable givenPageRequest);
    
    Page<Table5> findAllBy(org.springframework.data.domain.Pageable givenPageRequest); //? Lazy because Page works badly with Eager get
    
    @jakarta.transaction.Transactional
    void deleteByName(String name);
    
    @jakarta.transaction.Transactional
    Table5 saveAndFlush(Table5 givenTable1); //? It is also an update()
}
