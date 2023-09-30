package com.example.hibernatetest2.tables.onetoone.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.example.hibernatetest2.tables.onetoone.entities.MasterTable;
import com.querydsl.core.types.Predicate;

public interface MasterTableRepositoryCustomQueryDsl {

    public List<MasterTable> findAllByFilter(Pageable givenPageRequest,
                                             Predicate predicate);
}
