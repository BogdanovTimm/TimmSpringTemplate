package com.example.hibernatetest2.tables.manytoone.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import com.example.hibernatetest2.tables.manytoone.entities.Table2;
import com.querydsl.core.types.Predicate;

/**
 * Custom QueryDsl functions
 */
public interface Table2RepositoryCustomQueryDsl {

  public List<Table2> findAllByFilter(Pageable givenPageRequest,
                                      Predicate predicate);

}
