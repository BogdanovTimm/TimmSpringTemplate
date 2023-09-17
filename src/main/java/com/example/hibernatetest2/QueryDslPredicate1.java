package com.example.hibernatetest2;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QueryDslPredicate1 {

    private final List<Predicate> listOfPredicates = new ArrayList<>();





    public static QueryDslPredicate1 builder() {
        return new QueryDslPredicate1();
    }

    public <T> QueryDslPredicate1 add(T object,
                                      Function<T, Predicate> function) {
        if (object != null) {
            listOfPredicates.add(function.apply(object));
        }
        return this;
    }

    public Predicate build() { //? All statements must be true (AND)
        return Optional.ofNullable(ExpressionUtils.allOf(listOfPredicates))
                                 .orElseGet(() -> Expressions.asBoolean(true).isTrue());
    }

    public Predicate buildOr() { //? 1 true statement is enough (OR)
        return Optional.ofNullable(ExpressionUtils.anyOf(listOfPredicates))
                                 .orElseGet(() -> Expressions.asBoolean(true).isTrue());
    }

}
