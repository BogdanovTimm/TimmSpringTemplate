package com.example.hibernatetest2.tables.onetomanytoone.repositories;

import org.springframework.data.domain.Pageable;

import com.example.hibernatetest2.tables.onetomanytoone.entities.QTable1;
import com.example.hibernatetest2.tables.onetomanytoone.entities.Table1;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Custom QueryDsl Functions implemnetntation
 */
@Slf4j
@RequiredArgsConstructor
public class Table1RepositoryCustomQueryDslImpl implements Table1RepositoryCustomQueryDsl {

    private final EntityManager entityManager;





    @Override
    public java.util.List<Table1> findAllByFilter(Pageable givenPageRequest, Predicate predicate) {
        return new JPAQuery<Table1>(entityManager).select(QTable1.table1)
                                                  .from(QTable1.table1)
                                                  .where(predicate)
                                                  .fetch();
    }

}