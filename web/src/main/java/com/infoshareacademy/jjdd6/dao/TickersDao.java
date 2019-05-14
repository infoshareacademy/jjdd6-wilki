package com.infoshareacademy.jjdd6.dao;

import com.infoshareacademy.jjdd6.wilki.Tickers;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@RequestScoped
public class TickersDao {

    @PersistenceContext
    private EntityManager entityManager;

    public String save(Tickers ticker) {
        entityManager.persist(ticker);
        return ticker.getTickerName();
    }

    public Tickers update(Tickers ticker) {
        return entityManager.merge(ticker);
    }

    public void delete(String tickerName) {
        final Tickers ticker = entityManager.find(Tickers.class, tickerName);
        if (ticker != null) {
            entityManager.remove(ticker);
        }
    }

    public Tickers findById(String tickerName) {
        return entityManager.find(Tickers.class, tickerName);
    }

    public List<Tickers> findAll() {
        final Query query = entityManager.createNamedQuery("Tickers.findAll");
        return query.getResultList();
    }

    public Long getCount() {
        final Query query = entityManager.createNamedQuery("Tickers.getCount");
        List<Long> list = query.getResultList();
        return list.get(0);
    }
}
