package com.infoshareacademy.jjdd6.dao;

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
    private EntityManager entityManager;

    public List<String> getMostBoughtStocks() {
        final Query query = entityManager
                .createQuery("SELECT COUNT(*), s.ticker FROM Transaction t " +
                        "INNER JOIN Share s ON (t.share = s.id) " +
                        "WHERE t.type = 0 " +
                        "GROUP BY s.ticker " +
                        "ORDER BY COUNT(*) DESC");
        return mapObjectsFromNativeQuery(query.getResultList());
    }

    public List<String> getMostSoldStocks() {
        final Query query = entityManager
                .createQuery("SELECT COUNT(*), s.ticker FROM Transaction t " +
                        "INNER JOIN Share s ON (t.share = s.id) " +
                        "WHERE t.type = 1" +
                        "GROUP BY s.ticker " +
                        "ORDER BY COUNT(*) DESC");
        return mapObjectsFromNativeQuery(query.getResultList());
    }

    public List<String> mapObjectsFromNativeQuery(List<Object[]> queryResult) {
        List<String> list = new ArrayList<>();
        for (Object[] arr : queryResult) {
            Long count = (Long) arr[0];
            String ticker = (String) arr[1];
            list.add(ticker + " (" + count.toString() + ")");
        }
        return list;
    }
}
