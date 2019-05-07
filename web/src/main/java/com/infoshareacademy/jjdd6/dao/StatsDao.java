package com.infoshareacademy.jjdd6.dao;

import javax.ejb.Stateless;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@RequestScoped
@Transactional
public class StatsDao {

    @PersistenceContext
    EntityManager entityManager;

    public List<String> getMostBoughtStocks() {
        final Query query = entityManager
                .createQuery("SELECT COUNT(*), s.ticker FROM Transaction t " +
                        "INNER JOIN Share s ON (t.share = s.id) " +
                        "WHERE t.type = 0 " +
                        "GROUP BY s.ticker " +
                        "ORDER BY COUNT(*) DESC");
        List<String> list = new ArrayList<>();
        List<Object[]> result = query.getResultList();
        for (Object[] arr : result) {
            Long count = (Long) arr[0];
            String ticker = (String) arr[1];
            list.add(ticker + " (" + count.toString() + ")");
        }
        return list;
    }

    public List<String> getMostSoldStocks() {
        final Query query = entityManager
                .createQuery("SELECT COUNT(*), s.ticker FROM Transaction t " +
                        "INNER JOIN Share s ON (t.share = s.id) " +
                        "WHERE t.type = 1" +
                        "GROUP BY s.ticker " +
                        "ORDER BY COUNT(*) DESC");
        List<String> list = new ArrayList<>();
        List<Object[]> result = query.getResultList();
        for (Object[] arr : result) {
            Long count = (Long) arr[0];
            String ticker = (String) arr[1];
            list.add(ticker + " (" + count.toString() + ")");
        }
        return list;
    }
}
