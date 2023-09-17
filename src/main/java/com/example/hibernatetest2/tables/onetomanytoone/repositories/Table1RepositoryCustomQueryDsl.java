package com.example.hibernatetest2.tables.onetomanytoone.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import com.example.hibernatetest2.tables.onetomanytoone.entities.Table1;
import com.querydsl.core.types.Predicate;

/**
 * Custom QueryDsl functions
  */
public interface Table1RepositoryCustomQueryDsl {

    public java.util.List<Table1> findAllByFilter(Pageable givenPageRequest,
                                                  Predicate predicate);

}
