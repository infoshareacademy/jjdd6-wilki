package com.infoshareacademy.jjdd6.validation;

import com.infoshareacademy.jjdd6.dao.UserDao;
import com.infoshareacademy.jjdd6.servlet.UserServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isAlpha;
import static org.apache.commons.lang3.StringUtils.isNumeric;
import static org.apache.commons.lang3.math.NumberUtils.isDigits;
import static org.apache.commons.validator.GenericValidator.isEmail;


@RequestScoped
public class Validators {

    private Logger logger = LoggerFactory.getLogger(UserServlet.class);


    @PersistenceContext
    EntityManager entityManager;

    @Inject
    UserDao userDao;

    public boolean isEmailCorrect(String email) {
        return (isEmail(email));
    }

    public boolean isIdCorrect(String id) {
        return (isDigits(id)
        && (Integer.valueOf(id) > 0));
    }

    public boolean isEmailPresent(String email) {
        return userDao.findByEmail(email) == null;
    }

    public boolean isIdPresent(String id) {
        return userDao.findById(Long.valueOf(id)) == null;
    }


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
