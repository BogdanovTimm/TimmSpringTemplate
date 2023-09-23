package com.example.hibernatetest2.tables.manytomany.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.example.hibernatetest2.tables.manytomany.entities.QTable4;
import com.example.hibernatetest2.tables.manytomany.entities.Table4;
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
public class Table4RepositoryCustomQueryDslImpl implements Table4RepositoryCustomQueryDsl {

    private final EntityManager entityManager;





    @Override
    public List<Table4> findAllByFilter(Pageable givenPageRequest, Predicate predicate) {
        return new JPAQuery<Table4>(entityManager).select(QTable4.table4)
                                                  .from(QTable4.table4)
                                                  .where(predicate)
                                                  .fetch();
    }

}
