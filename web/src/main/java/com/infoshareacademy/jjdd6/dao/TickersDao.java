package com.infoshareacademy.jjdd6.dao;

import com.infoshareacademy.jjdd6.wilki.Ticker;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Singleton
public class TickersDao {

    @PersistenceContext
    private EntityManager entityManager;

    public String save(Ticker ticker) {
        entityManager.persist(ticker);
        entityManager.flush();
        return ticker.getTickerName();
    }

    public Ticker update(Ticker ticker) {
        return entityManager.merge(ticker);
    }

    public void delete(String tickerName) {
        final Ticker ticker = entityManager.find(Ticker.class, tickerName);
        if (ticker != null) {
            entityManager.remove(ticker);
        }
    }

    public Ticker findById(String tickerName) {
        return entityManager.find(Ticker.class, tickerName);
    }

    public List<Ticker> findAll() {
        final Query query = entityManager.createNamedQuery("Tickers.findAll");
        return query.getResultList();
    }

    public Long getCount() {
        final Query query = entityManager.createNamedQuery("Tickers.getCount");
        List<Long> list = query.getResultList();
        return list.get(0);
    }
}
