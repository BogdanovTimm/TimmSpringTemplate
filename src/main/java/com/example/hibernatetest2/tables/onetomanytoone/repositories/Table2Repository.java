package com.example.hibernatetest2.tables.onetomanytoone.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.example.hibernatetest2.tables.onetomanytoone.entities.Table2;

import jakarta.transaction.Transactional;

@Component
public interface Table2Repository extends JpaRepository<Table2, Long> { // Auto-creating of functions

    @EntityGraph(attributePaths = {"myOnlyOneTalbe1"}) // Gets myOnlyOneTable1
    Optional<Table2> findByName(String name);

    Page<Table2> findAllBy(Pageable givenPageRequest);

    @Transactional
    int deleteByName(String name);

    @Transactional
    Table2 saveAndFlush(Table2 givenTable2); // It is also an update(). Also, 

}
