package com.example.hibernatetest2.tables.manytomany.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import com.example.hibernatetest2.tables.manytomany.entities.Table4;
import com.querydsl.core.types.Predicate;

/**
 * Custom QueryDsl functions
 */
public interface Table4RepositoryCustomQueryDsl {

  public List<Table4> findAllByFilter(Pageable givenPageRequest,
                                      Predicate predicate);

}
