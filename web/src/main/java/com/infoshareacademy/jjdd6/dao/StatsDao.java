package com.infoshareacademy.jjdd6.dao;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class StatsDao {

    @PersistenceContext
    private EntityManager entityManager;

    public List<String[]> getMostBoughtStocks() {
        final Query query = entityManager
                .createQuery("SELECT COUNT(*), s.ticker FROM Transaction t " +
                        "INNER JOIN Share s ON (t.share = s.id) " +
                        "WHERE t.type = 0 " +
                        "GROUP BY s.ticker " +
                        "ORDER BY COUNT(*) DESC");
        return mapObjectsFromNativeQuery(query.getResultList());
    }

    public List<String[]> getMostSoldStocks() {
        final Query query = entityManager
                .createQuery("SELECT COUNT(*), s.ticker FROM Transaction t " +
                        "INNER JOIN Share s ON (t.share = s.id) " +
                        "WHERE t.type = 1" +
                        "GROUP BY s.ticker " +
                        "ORDER BY COUNT(*) DESC");
        return mapObjectsFromNativeQuery(query.getResultList());
    }

    private List<String[]> mapObjectsFromNativeQuery(List<Object[]> queryResult) {
        List<String[]> list = new ArrayList<>();
        for (Object[] arr : queryResult) {
            String[] mapping = new String[2];
            mapping[1] = arr[0].toString();
            mapping[0] = arr[1].toString();
            list.add(mapping);
        }
        return list;
    }
}
