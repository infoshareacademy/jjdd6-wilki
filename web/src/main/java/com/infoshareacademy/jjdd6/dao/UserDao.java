package com.infoshareacademy.jjdd6.dao;

import com.infoshareacademy.jjdd6.wilki.User;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Singleton
public class UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    public Long save(User user) {
        entityManager.persist(user);
        return user.getId();
    }

    public User update(User user) {
        return entityManager.merge(user);
    }

    public void delete(Long id) {
        final User user = entityManager.find(User.class, id);
        if (user != null) {
            entityManager.remove(user);
        }
    }

    public User findById(Long id) {
        return entityManager.find(User.class, id);
    }

    public List<User> findByFbUserId(String fbUserId) {
        final Query query = entityManager.createNamedQuery("User.findUserByFbUserId");
        query.setParameter("fbUserId", fbUserId);
        return query.getResultList();
    }


    public List<User> findUserByEmail(String email) {
        final Query query = entityManager.createNamedQuery("User.findUserByEmail");
        query.setParameter("email", email);
        return query.getResultList();
    }

    public List<User> findAll() {
        final Query query = entityManager.createNamedQuery("User.findAll");
        return query.getResultList();
    }
}
