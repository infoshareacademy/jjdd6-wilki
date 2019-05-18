package com.infoshareacademy.jjdd6.dao;

import com.infoshareacademy.jjdd6.wilki.HistoricalData;
import com.infoshareacademy.jjdd6.wilki.Ticker;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Singleton
public class HistoricalDataDao {

    @PersistenceContext
    private EntityManager entityManager;

    public Long save(HistoricalData data) {
        entityManager.persist(data);
        entityManager.flush();
        return data.getId();
    }

    public HistoricalData update(HistoricalData data) {
        return entityManager.merge(data);
    }

    public void delete(Long id) {
        final HistoricalData data = entityManager.find(HistoricalData.class, id);
        if (id != null) {
            entityManager.remove(data);
        }
    }

    public HistoricalData findById(Long id) {
        return entityManager.find(HistoricalData.class, id);
    }

    public List<HistoricalData> getDataforTicker(String ticker) {
        final Query query = entityManager.createQuery("SELECT d FROM HistoricalData d WHERE HistoricalData.ticker = '" + ticker + "' ORDER BY HistoricalData.date ASC");
        return query.getResultList();
    }

    public List<HistoricalData> getDataforTickerFromTo(String ticker, LocalDate from, LocalDate to) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        final Query query = entityManager.createQuery("SELECT d FROM HistoricalData d WHERE HistoricalData.ticker = '" + ticker + "' AND HistoricalData.date IS BETWEEN '" + df.format(from) + "' AND '" + df.format(to) + "' ORDER BY HistoricalData.date ASC");
        return query.getResultList();
    }

    public Long getCount() {
        final Query query = entityManager.createNamedQuery("HistoricalData.getCount");
        List<Long> list = query.getResultList();
        return list.get(0);
    }
}
