package com.infoshareacademy.jjdd6.servlet;

import com.infoshareacademy.jjdd6.dao.WalletDao;
import com.infoshareacademy.jjdd6.validation.Validators;
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
import java.math.BigDecimal;

@Transactional
@WebServlet(urlPatterns = "/increase-reduce-free-cash")
public class IncreaseReduceFreeCashServlet extends HttpServlet {

    private static Logger logger = LoggerFactory.getLogger(IncreaseReduceFreeCashServlet.class);

    @Inject
    private WalletDao walletDao;

    @Inject
    private Validators validators;

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
        if (validators.isNotIntegerOrIsSmallerThanZero(idStr)) {
            resp.getWriter().println("Wallet walletId should be an integer greater than 0");
            logger.info("Incorrect wallet walletId = {}", idStr);
            return;
        }

        if (validators.isWalletNotPresent(idStr)) {
            resp.getWriter().println("No wallet found for walletId = {" + idStr + "}");
            logger.info("No wallet found for walletId = {}, nothing to be updated", idStr);
            return;
        }

        final Long id = Long.parseLong(req.getParameter("wallet_id"));
        final Wallet existingWallet = walletDao.findById(id);

        String cash = req.getParameter("cash");
        if (validators.isNotDoubleOrIsSmallerThanZero(cash)) {
            resp.getWriter().println("Cash should be a number");
            return;
        }

        BigDecimal cashBigDecimal = BigDecimal.valueOf(Double.parseDouble(cash));
        existingWallet.increaseBaseCash(cashBigDecimal);

        walletDao.update(existingWallet);
        logger.info("Wallet object updated: {}", existingWallet);

        resp.getWriter().println("Free cash increased by: " + cash);
    }

    private void reduceFreeCash(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String idStr = req.getParameter("wallet_id");
        if (validators.isNotIntegerOrIsSmallerThanZero(idStr)) {
            resp.getWriter().println("Wallet walletId should be an integer greater than 0");
            logger.info("Incorrect wallet walletId = {}", idStr);
            return;
        }

        if (validators.isWalletNotPresent(idStr)) {
            resp.getWriter().println("No wallet found for walletId = {" + idStr + "}");
            logger.info("No wallet found for walletId = {}, nothing to be updated", idStr);
            return;
        }

        final Long id = Long.parseLong(req.getParameter("wallet_id"));
        final Wallet existingWallet = walletDao.findById(id);

        String cash = req.getParameter("cash");
        if (validators.isNotDoubleOrIsSmallerThanZero(cash)) {
            resp.getWriter().println("Cash should be a number");
            return;
        }

        BigDecimal cashBigDecimal = BigDecimal.valueOf(Double.parseDouble(cash));
        existingWallet.increaseBaseCash(cashBigDecimal);

        if (validators.isEnoughCashToReduceFreeCash(existingWallet, cash)) {
            resp.getWriter().println("You don't have enough money! Your current balance is: "
                    + existingWallet.getFreeCash());
            logger.info("Not enough money to reduce free cash");
            return;
        }

        existingWallet.reduceBaseCash(cashBigDecimal);
        resp.getWriter().println("Total free cash: " + existingWallet.getFreeCash());

        walletDao.update(existingWallet);
        logger.info("Wallet object updated: {}", existingWallet);

        resp.getWriter().println("Free cash reduced by: " + cash);
    }
}
