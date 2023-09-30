package com.example.hibernatetest2.tables.embedded.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.example.hibernatetest2.tables.embedded.entities.Table5;
import com.querydsl.core.types.Predicate;

public interface Table5RepositoryCustomQueryDsl {

    public List<Table5> customQueryDslFunction(Pageable givenPageRequest, Predicate predicate);
}
