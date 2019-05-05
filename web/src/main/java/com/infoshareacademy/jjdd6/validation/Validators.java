package com.infoshareacademy.jjdd6.validation;

import com.infoshareacademy.jjdd6.dao.TransactionDao;
import com.infoshareacademy.jjdd6.dao.UserDao;
import com.infoshareacademy.jjdd6.dao.WalletDao;
import com.infoshareacademy.jjdd6.servlet.UserServlet;
import com.infoshareacademy.jjdd6.wilki.DownloadCurrentData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;

import static org.apache.commons.lang3.math.NumberUtils.isCreatable;
import static org.apache.commons.lang3.math.NumberUtils.isDigits;
import static org.apache.commons.validator.GenericValidator.isEmail;

@RequestScoped
public class Validators {

    private static Logger logger = LoggerFactory.getLogger(UserServlet.class);

    @PersistenceContext
    EntityManager entityManager;

    @Inject
    UserDao userDao;

    @Inject
    WalletDao walletDao;

    @Inject
    TransactionDao transactionDao;

    public boolean isEmailIncorrect(String email) {
        return (!isEmail(email));
    }

    public boolean isIntegerGreaterThanZero(String id) {
        return (!isDigits(id)
                || (Integer.valueOf(id) < 0));
    }

    public boolean isDoubleGreaterThanZero(String id) {
        return (!isCreatable(id)
                || (Double.valueOf(id) < 0));
    }

    public boolean isEmailPresent(String email) {
        return !userDao.findUserByEmail(email).isEmpty();
    }

    public boolean isIdNotPresent(String id) {
        return userDao.findById(Long.valueOf(id)) == null;
    }

    public boolean isWalletNotPresent(String walletId) {
        return walletDao.findById(Long.valueOf(walletId)) == null;
    }

    public boolean isTickerNotValid(String ticker) {
        DownloadCurrentData downloadCurrentData = new DownloadCurrentData();
        return !downloadCurrentData.validateTicker(ticker);
    }

    public boolean isEnoughCash(Long walletId, int amount, double price) {

        return BigDecimal.valueOf(amount*price).
                compareTo(transactionDao.freeCash(walletId)) >= 0;

    }
}
