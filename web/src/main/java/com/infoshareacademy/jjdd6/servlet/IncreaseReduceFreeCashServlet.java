package com.infoshareacademy.jjdd6.servlet;

import com.infoshareacademy.jjdd6.dao.WalletDao;
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
import java.math.BigDecimal;

@WebServlet(urlPatterns = "/increase-reduce-free-cash")
public class IncreaseReduceFreeCashServlet extends HttpServlet {

    private Logger logger = LoggerFactory.getLogger(IncreaseReduceFreeCashServlet.class);

    @Inject
    WalletDao walletDao;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        final String action = getAction(req, resp);
        if (action == null) {
            return;
        }

        if (action.equals("increaseFreeCash")) {
            increaseFreeCash(req, resp);
        } else if (action.equals("reduceFreeCash")) {
            reduceFreeCash(req, resp);
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

    private void increaseFreeCash(HttpServletRequest req, HttpServletResponse resp)
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
        String cash = req.getParameter("cash");
        if (!NumberUtils.isParsable(cash)) {
            resp.getWriter().println("Cash should be a number");
        } else {
            Double cashDouble = Double.valueOf(cash);
            BigDecimal cashBigDecimal = BigDecimal.valueOf(cashDouble);
            existingWallet.increaseBaseCash(cashBigDecimal);
        }

        walletDao.update(existingWallet);
        logger.info("Wallet object updated: {}", existingWallet);

        resp.getWriter().println("Free cash increased by: " + cash);
    }

    private void reduceFreeCash(HttpServletRequest req, HttpServletResponse resp)
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
        String cash = req.getParameter("cash");
        if (!NumberUtils.isParsable(cash)) {
            resp.getWriter().println("Cash should be a number");
        } else {
            Double cashDouble = Double.valueOf(cash);
            BigDecimal cashBigDecimal = BigDecimal.valueOf(cashDouble);
            existingWallet.reduceBaseCash(cashBigDecimal);
        }

        walletDao.update(existingWallet);
        logger.info("Wallet object updated: {}", existingWallet);

        resp.getWriter().println("Free cash reduced by: " + cash);
    }
}
