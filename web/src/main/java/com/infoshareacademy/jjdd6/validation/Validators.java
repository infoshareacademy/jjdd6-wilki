package com.infoshareacademy.jjdd6.validation;

import com.infoshareacademy.jjdd6.dao.UserDao;
import com.infoshareacademy.jjdd6.dao.WalletDao;
import com.infoshareacademy.jjdd6.wilki.DownloadCurrentData;
import com.infoshareacademy.jjdd6.wilki.Share;
import com.infoshareacademy.jjdd6.wilki.User;
import com.infoshareacademy.jjdd6.wilki.Wallet;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;

import static org.apache.commons.lang3.math.NumberUtils.isCreatable;
import static org.apache.commons.lang3.math.NumberUtils.isDigits;
import static org.apache.commons.validator.GenericValidator.isEmail;

@RequestScoped
public class Validators {

    @Inject
    private UserDao userDao;

    @Inject
    private WalletDao walletDao;

    public boolean isEmailIncorrect(String email) {
        return (!isEmail(email));
    }

    public boolean isNotIntegerOrIsSmallerThanZero(String id) {
        return (!isDigits(id)
                || (Integer.valueOf(id) <= 0));
    }

    public boolean isNotDoubleOrIsSmallerThanZero(String price) {
        return (!isCreatable(price)
                || (Double.valueOf(price) <= 0));
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

    public boolean isEnoughCashToBuyShares(Wallet existingWallet, int amount, double price) {
        return BigDecimal.valueOf(amount*price).
                compareTo(existingWallet.getFreeCash()) >= 0;
    }

    public boolean isEnoughCashToReduceFreeCash(Wallet existingWallet, String value) {
        return BigDecimal.valueOf(Double.valueOf(value)).
                compareTo(existingWallet.getFreeCash()) > 0;
    }

    public boolean isEnoughSharesToSell(Wallet existingWallet, int amount, String ticker) {

        List<Share> listFromExistingWallet = existingWallet.getShares();
        Integer totalShareAmount = null;
        for (Share share : listFromExistingWallet) {
            if (share.getTicker().contains(ticker.toUpperCase())) {
               totalShareAmount = share.getSharesTotalAmount();
            }
        }
        return totalShareAmount >= amount;
    }

    public boolean isUserNotAllowedToWalletModification(String userId, String walletId) {
        User user = userDao.findById(Long.valueOf(userId));
        return !Long.valueOf(walletId).equals(user.getWallet().getId());

    }

    public boolean isTypeIncorrect(String type, String firsValue, String secondValue) {
        return !type.equals(firsValue) && !type.equals(secondValue);
    }
}
