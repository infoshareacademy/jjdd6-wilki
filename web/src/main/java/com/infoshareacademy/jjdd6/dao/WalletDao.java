package com.infoshareacademy.jjdd6.dao;

import com.infoshareacademy.jjdd6.wilki.Wallet;

import javax.ejb.Stateless;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;


@RequestScoped
public class WalletDao {

    @PersistenceContext
    EntityManager entityManager;

    public Long save(Wallet wallet) {
        entityManager.persist(wallet);
        return wallet.getId();
    }

    public Wallet update(Wallet wallet) {
        return entityManager.merge(wallet);
    }

    public void delete(Long id) {
        final Wallet wallet = entityManager.find(Wallet.class, id);
        if (wallet != null) {
            entityManager.remove(wallet);
        }
    }

    public Wallet findById(Long id) {
        return entityManager.find(Wallet.class, id);
    }

    public List<Wallet> findAll() {
        final Query query = entityManager.createNamedQuery("Wallet.findAll");
        return query.getResultList();
    }
}


