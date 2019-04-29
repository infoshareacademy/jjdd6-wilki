package com.infoshareacademy.jjdd6.dao;

import com.infoshareacademy.jjdd6.wilki.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class UserDao {

    @PersistenceContext
    EntityManager entityManager;

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

    public List<User> findAll() {
        final Query query = entityManager.createQuery("SELECT u FROM User u");

        return query.getResultList();
    }
}
