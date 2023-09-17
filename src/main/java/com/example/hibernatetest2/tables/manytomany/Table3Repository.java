package com.example.hibernatetest2.tables.manytomany;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.example.hibernatetest2.tables.onetomanytoone.entities.Table1;
import com.example.hibernatetest2.tables.onetomanytoone.entities.Table2;

import jakarta.transaction.Transactional;

@Component
public interface Table3Repository extends JpaRepository<Table3, Long> {

    @EntityGraph(attributePaths = {"listOfTable3Table4s", "listOfTable3Table4s.myOneAndOnlyTable4"})
    Optional<Table3> findByName(String name);

    Page<Table3> findAllBy(Pageable givenPageRequest);

    @Transactional
    void deleteByName(String name);

    @Transactional
    Table3 saveAndFlush(Table3 givenTable3); // It is also an update(). Also, 

}
