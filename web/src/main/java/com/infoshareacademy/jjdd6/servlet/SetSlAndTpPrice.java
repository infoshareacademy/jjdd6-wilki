package com.infoshareacademy.jjdd6.servlet;

import com.infoshareacademy.jjdd6.dao.ShareDao;
import com.infoshareacademy.jjdd6.dao.WalletDao;
import com.infoshareacademy.jjdd6.validation.Validators;
import com.infoshareacademy.jjdd6.wilki.Share;
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
import java.util.List;

@WebServlet("/sl-and-tp")
@Transactional
public class SetSlAndTpPrice extends HttpServlet {

    private static Logger logger = LoggerFactory.getLogger(SetSlAndTpPrice.class);

    @Inject
    private WalletDao walletDao;

    @Inject
    private ShareDao shareDao;

    @Inject
    private Validators validators;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        final String action = getAction(req, resp);
        if (action == null) {
            return;
        }

        if (action.equals("sl")) {
            setStopLoos(req, resp);
        } else if (action.equals("tp")) {
            setTakeProfit(req, resp);
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

    private void setStopLoos(HttpServletRequest req, HttpServletResponse resp)
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

        String ticker = req.getParameter("ticker");

        if (validators.isTickerNotValid(ticker)) {
            resp.getWriter().println("Ticker = {" + ticker + "} is not valid");
            logger.info("Ticker = {} is not valid.", ticker);
            return;
        }

        String priceStr = req.getParameter("price");

        if (validators.isNotDoubleOrIsSmallerThanZero(priceStr)) {
            resp.getWriter().println("Price should have a numerical value greater than 0");
            logger.info("Incorrect price = {}", priceStr);
            return;
        }

        final Long walletId = Long.valueOf(idStr);
        final Wallet existingWallet = walletDao.findById(walletId);

        List<Share> listFromExistingWallet = existingWallet.getShares();

        for (Share share : listFromExistingWallet) {
            if (share.getTicker().contains(ticker.toUpperCase())) {
                share.setStopLossPrice(BigDecimal.valueOf(Double.valueOf(priceStr)));
                logger.info("Set stop-loose price for share with id: {}", share.getId());
                shareDao.update(share);
                logger.info("Share with id: {} updated!", share.getId());
            }
        }
    }

    private void setTakeProfit(HttpServletRequest req, HttpServletResponse resp)
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

        String ticker = req.getParameter("ticker");

        if (validators.isTickerNotValid(ticker)) {
            resp.getWriter().println("Ticker = {" + ticker + "} is not valid");
            logger.info("Ticker = {} is not valid.", ticker);
            return;
        }

        String priceStr = req.getParameter("price");

        if (validators.isNotDoubleOrIsSmallerThanZero(priceStr)) {
            resp.getWriter().println("Price should have a numerical value greater than 0");
            logger.info("Incorrect price = {}", priceStr);
            return;
        }

        final Long walletId = Long.valueOf(idStr);
        final Wallet existingWallet = walletDao.findById(walletId);

        List<Share> listFromExistingWallet = existingWallet.getShares();

        for (Share share : listFromExistingWallet) {
            if (share.getTicker().contains(ticker.toUpperCase())) {
                share.setTakeProfitPrice(BigDecimal.valueOf(Double.valueOf(priceStr)));
                logger.info("Set take-profit price for share with id: {}", share.getId());
                shareDao.update(share);
                logger.info("Share with id: {} updated!", share.getId());
            }
        }
    }
}
