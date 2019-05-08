package com.infoshareacademy.jjdd6.validation;

import com.infoshareacademy.jjdd6.dao.UserDao;
import com.infoshareacademy.jjdd6.dao.WalletDao;
import com.infoshareacademy.jjdd6.wilki.DownloadCurrentData;
import com.infoshareacademy.jjdd6.wilki.Wallet;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.math.BigDecimal;

import static org.apache.commons.lang3.math.NumberUtils.isCreatable;
import static org.apache.commons.lang3.math.NumberUtils.isDigits;
import static org.apache.commons.validator.GenericValidator.isEmail;

@RequestScoped
public class Validators {

    @Inject
    UserDao userDao;

    @Inject
    WalletDao walletDao;

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

    public boolean isEnoughCash(Wallet existingWallet, int amount, double price) {

        return BigDecimal.valueOf(amount*price).
                compareTo(existingWallet.getFreeCash()) >= 0;

    }
}
