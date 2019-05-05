package com.infoshareacademy.jjdd6.servlet;

import com.infoshareacademy.jjdd6.dao.BuySharesDao;
import com.infoshareacademy.jjdd6.dao.ShareDao;
import com.infoshareacademy.jjdd6.dao.TransactionDao;
import com.infoshareacademy.jjdd6.dao.WalletDao;
import com.infoshareacademy.jjdd6.validation.Validators;
import com.infoshareacademy.jjdd6.wilki.Share;
import com.infoshareacademy.jjdd6.wilki.Transaction;
import com.infoshareacademy.jjdd6.wilki.Wallet;
import org.apache.commons.lang3.math.NumberUtils;
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

    @Inject
    BuySharesDao buySharesDao;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        final String action = getAction(req, resp);
        if (action == null) {
            return;
        }

        if (action.equals("buy")) {
            buyShare(req, resp);
        } else {
            resp.getWriter().write("Unknown action.");
        }
    }

    private String getAction(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final String action = req.getParameter("action");
        logger.info("Requested action: {}", action);
        if (action == null || action.isEmpty()) {
            resp.getWriter().write("Empty action parameter.");
        }
        return action;
    }

    private void buyShare(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String idStr = req.getParameter("wallet_id");
        if (!NumberUtils.isDigits(idStr)) {
            resp.getWriter().println("Wallet id should be an integer");
            return;
        }
        final Long id = Long.parseLong(req.getParameter("wallet_id"));
        logger.info("Updating wallet with id = {}", id);

        final Wallet existingWallet = walletDao.findById(id);
        if (existingWallet == null) {
            logger.info("No wallet found for id = {}, nothing to be updated", id);
            return;
        }

        String ticker = req.getParameter("ticker");

        String amountStr = req.getParameter("amount");
        if (!NumberUtils.isDigits(amountStr)) {
            resp.getWriter().println("Amount should be a whole number");
            return;
        }
        int amount = Integer.parseInt(amountStr);

        String priceStr = req.getParameter("price");
        if (!NumberUtils.isParsable(priceStr)) {
            resp.getWriter().println("Price should have a numerical value");
            return;
        }
        double price = Double.parseDouble(priceStr);

        existingWallet.buyShare(ticker, amount, price);
        Transaction transaction = existingWallet.scanWalletForShare(ticker).getTransactionHistory().get(existingWallet.scanWalletForShare(ticker).getTransactionHistory().size()-1);
        Share share = existingWallet.scanWalletForShare(ticker);
        transaction.setShare(share);
        transaction.setWallet(existingWallet);

        transactionDao.save(transaction);
        shareDao.save(share);
        walletDao.update(existingWallet);

        existingWallet.sellShare(ticker, amount-10, price+20);
        Transaction transaction2 = existingWallet.scanWalletForShare(ticker).getTransactionHistory().get(existingWallet.scanWalletForShare(ticker).getTransactionHistory().size()-1);
        Share share2 = existingWallet.scanWalletForShare(ticker);
        transaction2.setShare(share2);
        transaction2.setWallet(existingWallet);

        transactionDao.save(transaction2);
        shareDao.update(share2);
        walletDao.update(existingWallet);

        logger.info("Wallet object updated: {}", existingWallet);

        resp.getWriter().println("Transaction success.");
    }


}
