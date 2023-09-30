package com.example.hibernatetest2.tables.onetoone.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.hibernatetest2.tables.manytomany.repositories.Table4RepositoryCustomQueryDsl;
import com.example.hibernatetest2.tables.onetoone.entities.MasterTable;

import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;

@Repository
public interface MasterTableRepository
                                       extends
                                       JpaRepository<MasterTable, Long>, //? Auto-creating of functions
                                       QuerydslPredicateExecutor<MasterTable>,
                                       MasterTableRepositoryCustomQueryDsl {
    //! Also there is a Page<Table1> findAllBy(com.querydsl.core.types.Predicate predicate, Pageable givenPageRequest); in QuerydslPredicateExecutor //! Lazy because Page works badly with Eager get

    //? Find for ManyToMany Tables --v
    //@EntityGraph(attributePaths = {"listOfTable1Table2s", "listOfTable1Table2s.myOneAndOnlyTable2"})
    //Optional<Table1> findByName(String name);
    //? Find for ManyToMany Tables --^

    /**
     * Gets row with all references to other tables
     */
    @EntityGraph(attributePaths = {"mySlaveTable"})  //? Gets myOnlyOneTalbe1
    Optional<MasterTable> findById(Long id);

    @EntityGraph(attributePaths = {"mySlaveTable"})
    Optional<MasterTable> findByName(String name);



    @Modifying(clearAutomatically = true, //? Refreshes Hibernate's cache before running this function (non-flushed changes will be deleted)
               flushAutomatically = true //? Applies all changes before running this function
    ) //! Always use it on update, delete or create queries.
    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE) //? If some session is working with some rows - blocks them and force another session to wait untill first session finishes its work or until some timeout has passed.
    Integer deleteByName(String name);



    @Modifying(clearAutomatically = true, //? Refreshes Hibernate's cache before running this function (non-flushed changes will be deleted)
               flushAutomatically = true //? Applies all changes before adding this query to queue.
    ) //! Always use it on update, delete or create queries.
    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE) //? If some session is working with some rows - blocks them and force another session to wait untill first session finishes its work or until some timeout has passed.
    MasterTable saveAndFlush(MasterTable givenTable1); //! It is also an update()



    @Query("""
           SELECT t2
           FROM MasterTable AS t2
               JOIN FETCH t2.mySlaveTable AS t1
           WHERE t2.name LIKE %:name2%
           """
    )//! You need to use Java classes and their variables here instead of SQL tables and columns
    Optional<MasterTable> customFunction(
                                         @Param("name2") //? This parameter will be used in SQL-Query as [name2]
                                         String name);
}
