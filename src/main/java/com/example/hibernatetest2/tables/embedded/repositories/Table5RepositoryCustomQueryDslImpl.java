package com.example.hibernatetest2.tables.embedded.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.example.hibernatetest2.tables.embedded.entities.QTable5;
import com.example.hibernatetest2.tables.embedded.entities.Table5;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class Table5RepositoryCustomQueryDslImpl implements Table5RepositoryCustomQueryDsl {

    private final EntityManager entityManager;


    @Override
    public List<Table5> customQueryDslFunction(Pageable givenPageRequest, Predicate predicate) {
        return new JPAQuery<Table5>(entityManager).select(QTable5.table5)
                                                  .from(QTable5.table5)
                                                  .where(predicate)
                                                  .fetch();
    }

}
