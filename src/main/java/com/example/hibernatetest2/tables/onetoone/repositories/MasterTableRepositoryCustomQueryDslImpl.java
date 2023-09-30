package com.example.hibernatetest2.tables.onetoone.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.example.hibernatetest2.tables.onetoone.entities.MasterTable;
import com.example.hibernatetest2.tables.onetoone.entities.QMasterTable;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class MasterTableRepositoryCustomQueryDslImpl implements MasterTableRepositoryCustomQueryDsl {

    private final EntityManager entityManager;


    @Override
    public List<MasterTable> findAllByFilter(Pageable givenPageRequest, Predicate predicate) {
        return new JPAQuery<MasterTable>(entityManager).select(QMasterTable.masterTable)
                                                       .from(QMasterTable.masterTable)
                                                       .where(predicate)
                                                       .fetch();
    }
}
