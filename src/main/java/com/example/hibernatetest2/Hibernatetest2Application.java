package com.example.hibernatetest2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.example.hibernatetest2.tables.onetomanytoone.repositories.Table1Repository;
import com.example.hibernatetest2.tables.onetomanytoone.repositories.Table1RepositoryCustomQueryDslImpl;
import lombok.extern.slf4j.Slf4j;


// [v] Create a create function
// [v] 1 to many
// [v] many to 1
// [v] many to many
// [ ] 1 to 1
// [v] Get row from more than 1 tables
// [v] Create Resource level
// [v] Integration Testing
// [v] Unity Testing Post
// [v] Docker Test Containers
// [v] Liquibase
// [ ] Authorization and authentication
// [ ] Oauth2
// [v] Validation
// [v] Logs
// [ ] N + 1 problem
// [v] Actually, findByName must return Page
// [v] QueryDsl
// [ ] MySQL Table Indexing
// [ ] Mappers
// [ ] Service
// [ ] Create Folders for OneToMany/ManyToOne, ManyToMany, Embedable
// [ ] Create Folders for Spring Security

@Slf4j
@SpringBootApplication
public class Hibernatetest2Application {

    public static void main(String[] args) {
        var application = SpringApplication.run(Hibernatetest2Application.class, args);
        var table1 = application.getBean("table1Repository", Table1Repository.class);
        //var table5 = application.getBean("table5Repository", Table5Repository.class);
        var table1Custom = application.getBean("table1RepositoryCustomQueryDslImpl",
                                               Table1RepositoryCustomQueryDslImpl.class);
        /**
         * EntityManagerFactory emf = Persistence.createEntityManagerFactory("com.baeldung.querydsl.intro");
         * EntityManager em = emf.createEntityManager();
         * JPAQueryFactory queryFactory = new JPAQueryFactory(JPQLTemplates.DEFAULT, em);
         * queryFactory.selectFrom(QTable1.table1);
         * var emptyMapOfPredicates = new HashMap<>();
         * var mapOfPredicates = java.util.Map.of("KeyString1", "ValueString1");
         * var predicate = QTable1.table1.name.like("t");
         * predicate.or("null");
         **/
        //?-------------------------------------------------------------------------------------------------------------
        log.info("Hi! Can you see this?");
        log.warn("Hi! Can you see this?");
        log.error("Hi! Can you see this?");
    }

}
