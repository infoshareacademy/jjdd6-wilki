package com.infoshareacademy.jjdd6.dao;

import com.infoshareacademy.jjdd6.wilki.Transaction;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Singleton
public class TransactionDao {

    @PersistenceContext
    private EntityManager entityManager;

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

    public List<Transaction> findAllByWalletId(Long walletId) {
        final Query query = entityManager.createNamedQuery("Transaction.findAllByWalletId");
        query.setParameter("walletId", walletId);

        return query.getResultList();
    }

    public List<Transaction> findAllByWalletIdAndTicker(Long walletId, String ticker){
        final Query query = entityManager.createNamedQuery("Transaction.findAllByWalletIdAndTicker");
        query.setParameter("walletId", walletId);
        query.setParameter("ticker", ticker);
        return query.getResultList();
    }

}
