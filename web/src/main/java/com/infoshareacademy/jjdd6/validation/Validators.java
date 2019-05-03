package com.infoshareacademy.jjdd6.validation;

import com.infoshareacademy.jjdd6.dao.UserDao;
import com.infoshareacademy.jjdd6.dao.WalletDao;
import com.infoshareacademy.jjdd6.servlet.UserServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import static org.apache.commons.lang3.math.NumberUtils.isDigits;
import static org.apache.commons.validator.GenericValidator.isEmail;

@RequestScoped
public class Validators {

    private Logger logger = LoggerFactory.getLogger(UserServlet.class);

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
        return userDao.findByEmail(email) != null;
    }

    public boolean isIdNotPresent(String id) {
        return userDao.findById(Long.valueOf(id)) == null;
    }

    public boolean isWalletPresent(String wallet_id) {
        return walletDao.findById(Long.valueOf(wallet_id)) != null;
    }

}
