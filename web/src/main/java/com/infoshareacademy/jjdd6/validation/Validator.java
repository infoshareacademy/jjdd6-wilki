package com.infoshareacademy.jjdd6.validation;

import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isAlpha;
import static org.apache.commons.lang3.StringUtils.isNumeric;

@RequestScoped
public class Validator {

    @PersistenceContext
    EntityManager entityManager;

    public boolean isTickerValid(String ticker) {
        String tickerToUppercase = ticker.toUpperCase();
        return (isEmpty(tickerToUppercase)
                && isAlpha(tickerToUppercase));
//                && findAll().contains(tickerToUppercase));
    }

    public boolean isNotEmptyIsNumeric(String text) {
        return (isNumeric(text)
                && isEmpty(text));
    }

    public boolean isPositiveNumber(String text) {
        double number = Double.parseDouble(text);
        return number >= 0;
    }

    // TODO: 25.04.19 Uncomment comment content and replace "amountFromDatabase"
//    public boolean isEnoughShares(String amount) {
//        int amountInteger = Integer.parseInt(amount);
//        return amountInteger <= amountFromDatabase;
//    }

    // TODO: 25.04.19 Uncomment comment content and replace "valueFromDatabase"
//    public boolean isEnoughCash(Double value) {
//        return value > valueFromDatabase;
//    }

    // TODO: 25.04.19 Uncomment comment content and input Ticker table name
    //  into List<> and correct query
//    public List<Ticker> findAll() {
//        final Query query = entityManager.createQuery("SELECT t FROM TICKERS t");
//        return query.getResultList();
//    }


}
