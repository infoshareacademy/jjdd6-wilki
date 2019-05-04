package com.infoshareacademy.jjdd6.servlet;

import com.infoshareacademy.jjdd6.dao.ShareDao;
import com.infoshareacademy.jjdd6.dao.TransactionDao;
import com.infoshareacademy.jjdd6.dao.WalletDao;
import com.infoshareacademy.jjdd6.validation.Validators;
import com.infoshareacademy.jjdd6.wilki.Wallet;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/share-buy")
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

        walletDao.update(existingWallet);
        logger.info("Wallet object updated: {}", existingWallet);

        resp.getWriter().println("Transaction success.");
    }


}
