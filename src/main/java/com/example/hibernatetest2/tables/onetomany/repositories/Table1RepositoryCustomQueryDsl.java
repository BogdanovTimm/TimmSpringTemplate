package com.example.hibernatetest2.tables.onetomany.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.example.hibernatetest2.tables.onetomany.entities.Table1;
import com.querydsl.core.types.Predicate;

/**
 * Custom QueryDsl functions
 */
public interface Table1RepositoryCustomQueryDsl {

  public List<Table1> findAllByFilter(Pageable givenPageRequest,
                                      Predicate predicate);

}
