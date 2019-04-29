package com.infoshareacademy.jjdd6.dao;


import com.infoshareacademy.jjdd6.wilki.Share;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class ShareDao {

    @PersistenceContext
    EntityManager entityManager;

    public Long save(Share share) {
        entityManager.persist(share);
        return share.getId();
    }

    public Share update(Share share) {
        return entityManager.merge(share);
    }

    public void delete(Long id) {
        final Share share = entityManager.find(Share.class, id);
        if (share != null) {
            entityManager.remove(share);
        }
    }

    public Share findById(Long id) {
        return entityManager.find(Share.class, id);
    }

    public List<Share> findAll() {
        final Query query = entityManager.createQuery("SELECT s FROM Share s");

        return query.getResultList();
    }
}