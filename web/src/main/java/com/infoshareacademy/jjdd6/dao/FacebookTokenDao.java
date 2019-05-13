package com.infoshareacademy.jjdd6.dao;

import com.infoshareacademy.jjdd6.wilki.FacebookToken;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@RequestScoped
public class FacebookTokenDao {

    @PersistenceContext
    private EntityManager entityManager;

    public Long save(FacebookToken token) {
        entityManager.persist(token);
        return token.getId();
    }

    public FacebookToken update(FacebookToken token) {
        return entityManager.merge(token);
    }

    public void delete(Long id) {
        final FacebookToken token = entityManager.find(FacebookToken.class, id);
        if (token != null) {
            entityManager.remove(token);
        }
    }

    public FacebookToken findById(Long id) {
        return entityManager.find(FacebookToken.class, id);
    }

    public List<FacebookToken> findAll() {
        final Query query = entityManager.createNamedQuery("Tokens.findAll");
        return query.getResultList();
    }
}
