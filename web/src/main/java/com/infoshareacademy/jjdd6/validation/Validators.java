package com.infoshareacademy.jjdd6.validation;

import com.infoshareacademy.jjdd6.dao.UserDao;
import com.infoshareacademy.jjdd6.dao.WalletDao;
import com.infoshareacademy.jjdd6.servlet.UserServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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

    public boolean isEmailIncorrect(String email) {
        return (!isEmail(email));
    }

    public boolean isIdIncorrect(String id) {
        return (!isDigits(id)
                || (Integer.valueOf(id) <= 0));
    }

    public boolean isEmailPresent(String email) {
        return !userDao.findUserByEmail(email).isEmpty();
    }

    public boolean isIdNotPresent(String id) {
        return userDao.findById(Long.valueOf(id)) == null;
    }

    public boolean isWalletPresent(String walletId) {
        return walletDao.findById(Long.valueOf(walletId)) != null;
    }

}
