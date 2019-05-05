package com.infoshareacademy.jjdd6.dao;

import com.infoshareacademy.jjdd6.wilki.Transaction;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.List;

@Stateless
public class TransactionDao {

    @PersistenceContext
    EntityManager entityManager;

    @Inject
    WalletDao walletDao;

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

    public BigDecimal freeCash(Long walletId) {

        BigDecimal baseCash = walletDao.findById(walletId).getBaseCash();

        final Query query = entityManager.createNamedQuery("Transaction.totalTransactionValue");
        query.setParameter("walletId", walletId);
        BigDecimal totalValue = (BigDecimal) query.getSingleResult();

        return baseCash.add(totalValue);

    }
}
