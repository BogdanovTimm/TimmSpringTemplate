package com.example.hibernatetest2.tables.onetomanytoone.repositories;


import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.hibernatetest2.tables.onetomanytoone.entities.Table1;

import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;

@Repository
public interface Table1Repository
                                  extends
                                  JpaRepository<Table1, Long>, //? Auto-creating of functions
                                  QuerydslPredicateExecutor<Table1>,
                                  Table1RepositoryCustomQueryDsl { //? Custom fucntions, created by hand

    //! Also there is a Page<Table1> findAllBy(com.querydsl.core.types.Predicate predicate, Pageable givenPageRequest); in QuerydslPredicateExecutor //! Lazy because Page works badly with Eager get

    //? Find for ManyToMany Tables --v
    //@EntityGraph(attributePaths = {"listOfTable1Table2s", "listOfTable1Table2s.myOneAndOnlyTable2"})
    //Optional<Table1> findByName(String name);
    //? Find for ManyToMany Tables --^

    /**
     * Gets row with all references to other tables
     */
    @EntityGraph(attributePaths = {"mapOfTable2s"}) //? Gets mapOfTable2s
    Optional<Table1> findById(Long id);



    @Modifying (clearAutomatically = true, //? Refreshes Hibernate's cache before running this function (non-flushed changes will be deleted)
                flushAutomatically = true //? Applies all changes before running this function
    ) //! Always use it on update, delete or create queries.
    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE) //? If some session is working with some rows - blocks them and force another session to wait untill first session finishes its work or until some timeout has passed.
    Integer deleteByName(String name);



    @Modifying(clearAutomatically = true, //? Refreshes Hibernate's cache before running this function (non-flushed changes will be deleted)
               flushAutomatically = true //? Applies all changes before adding this query to queue.
    ) //! Always use it on update, delete or create queries.
    @Transactional
    @Lock (LockModeType.PESSIMISTIC_WRITE) //? If some session is working with some rows - blocks them and force another session to wait untill first session finishes its work or until some timeout has passed.
    Table1 saveAndFlush(Table1 givenTable1); //! It is also an update()

    

    @Query("""
           SELECT t1
           FROM Table1 AS t1
               JOIN FETCH t1.mapOfTable2s AS t2
           WHERE t1.name LIKE %:name2%
           """
    )//! You need to use Java classes and their variables here instead of SQL tables and columns
    Optional<Table1> customFunction(
                                    @Param("name2") //? This parameter will be used in SQL-Query as [name2]
                                    String name);

}
