package com.infoshareacademy.jjdd6.servlet;

import com.infoshareacademy.jjdd6.dao.ShareDao;
import com.infoshareacademy.jjdd6.dao.TransactionDao;
import com.infoshareacademy.jjdd6.dao.WalletDao;
import com.infoshareacademy.jjdd6.validation.Validators;
import com.infoshareacademy.jjdd6.wilki.Share;
import com.infoshareacademy.jjdd6.wilki.Transaction;
import com.infoshareacademy.jjdd6.wilki.Wallet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;

@WebServlet("/share-buy")
@Transactional
public class BuySharesServlet extends HttpServlet {

    private static Logger logger = LoggerFactory.getLogger(BuySharesServlet.class);

    @Inject
    WalletDao walletDao;

    @Inject
    ShareDao shareDao;

    @Inject
    TransactionDao transactionDao;

    @Inject
    private Validators validators;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        buyShare(req, resp);

    }

    private void buyShare(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String idStr = req.getParameter("wallet_id");
        if (validators.isIntegerGreaterThanZero(idStr)) {
            resp.getWriter().println("Wallet walletId should be an integer greater than 0");
            logger.info("Incorrect wallet walletId = {}", idStr);
            return;
        }

        if (validators.isWalletNotPresent(idStr)) {
            resp.getWriter().println("No wallet found for walletId = {" + idStr + "}");
            logger.info("No wallet found for walletId = {}, nothing to be updated", idStr);
            return;
        }

        String ticker = req.getParameter("ticker");

        if (validators.isTickerNotValid(ticker)) {
            resp.getWriter().println("Ticker = {" + ticker + "} is not valid");
            logger.info("Ticker = {} is not valid.", ticker);
            return;
        }

        String amountStr = req.getParameter("amount");

        if (validators.isIntegerGreaterThanZero(amountStr)) {
            resp.getWriter().println("Amount should be an integer greater than 0");
            logger.info("Incorrect amount = {}", amountStr);
            return;
        }

        String priceStr = req.getParameter("price");

        if (validators.isDoubleGreaterThanZero(priceStr)) {
            resp.getWriter().println("Price should be a number greater than 0");
            logger.info("Incorrect price = {}", amountStr);
            return;
        }

        int amount = Integer.parseInt(amountStr);
        double price = Double.parseDouble(priceStr);
        final Long walletId = Long.parseLong(req.getParameter("wallet_id"));
        final Wallet existingWallet = walletDao.findById(walletId);

//        if (validators.isEnoughCash(walletId, amount, price)) {
//            resp.getWriter().println("You don't have enough money! Your current balance is: "
//            + existingWallet.getFreeCash());
//            logger.info("Not enough money to buy shares");
//            return;
//        }

        existingWallet.buyShare(ticker, amount, price);
        Transaction transaction = existingWallet.scanWalletForShare(ticker).getTransactionHistory().get(existingWallet.scanWalletForShare(ticker).getTransactionHistory().size()-1);
        Share share = existingWallet.scanWalletForShare(ticker);
        transaction.setShare(share);
        transaction.setWallet(existingWallet);

        transactionDao.save(transaction);
        shareDao.save(share);
        walletDao.update(existingWallet);

        logger.info("Wallet object updated: {}", existingWallet);

        resp.getWriter().println("Transaction success." + transactionDao.freeCash(walletId));
    }
}
