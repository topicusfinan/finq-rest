package nl.finan.finq.dao.impl;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@FunctionalInterface
public interface WhereBuilder<T> {

    Predicate buildWhere(CriteriaBuilder builder, Root<T> root);
}
