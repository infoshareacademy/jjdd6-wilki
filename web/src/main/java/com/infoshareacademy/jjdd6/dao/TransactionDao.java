package com.infoshareacademy.jjdd6.dao;

import com.infoshareacademy.jjdd6.wilki.Transaction;
import org.hibernate.param.CollectionFilterKeyParameterSpecification;

import javax.ejb.Stateless;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestScoped
public class TransactionDao {

    @PersistenceContext
    EntityManager entityManager;

    public Long save(Transaction transaction) {
        entityManager.persist(transaction);
        return transaction.getId();
    }

    public Transaction update(Transaction transaction) {
        return entityManager.merge(transaction);
    }

    public void delete(Long id) {
        final Transaction transaction = entityManager.find(Transaction.class, id);
        if (transaction != null) {
            entityManager.remove(transaction);
        }
    }

    public Transaction findById(Long id) {
        return entityManager.find(Transaction.class, id);
    }

    public List<Transaction> findAll() {
        final Query query = entityManager.createNamedQuery("Transaction.findAll");

        return query.getResultList();
    }

   }
