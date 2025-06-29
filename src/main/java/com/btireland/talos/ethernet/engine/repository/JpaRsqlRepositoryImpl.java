package com.btireland.talos.ethernet.engine.repository;

import io.github.perplexhub.rsql.RSQLSupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;

public class JpaRsqlRepositoryImpl<T, I extends Serializable> extends SimpleJpaRepository<T, I> implements JpaRsqlRepository<T, I> {

    public JpaRsqlRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
    }

    @Override
    public List<T> findAllByRsqlQuery(String rsqlQuery) {
        return this.findAll(RSQLSupport.toSpecification(rsqlQuery));
    }

    @Override
    public Page<T> findAllByRsqlQuery(String rsqlQuery, Pageable pageable) {
        return this.findAll(RSQLSupport.toSpecification(rsqlQuery), pageable);
    }

    @Override
    public List<T> findAllByRsqlQuery(String rsqlQuery, Sort sort) {
        return this.findAll(RSQLSupport.toSpecification(rsqlQuery), sort);
    }
}
