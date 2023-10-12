package com.example.hibernatetest2.tables.manytoone.repositories;

import com.example.hibernatetest2.tables.manytoone.entities.QTable2;
import com.example.hibernatetest2.tables.manytoone.entities.Table2;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 Custom QueryDsl Functions implemnetntation
 */
@Slf4j
@RequiredArgsConstructor
public class Table2RepositoryCustomQueryDslImpl
        implements Table2RepositoryCustomQueryDsl {

    private final EntityManager entityManager;



    @Override
    public List<Table2> findAllByFilter(Pageable givenPageRequest, Predicate predicate) {
        return new JPAQuery<Table2>(entityManager).select(QTable2.table2)
                                                  .from(QTable2.table2)
                                                  .where(predicate)
                                                  .fetch();
    }
}
