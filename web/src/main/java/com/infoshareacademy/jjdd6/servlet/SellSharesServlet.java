package com.infoshareacademy.jjdd6.servlet;

import com.infoshareacademy.jjdd6.dao.ShareDao;
import com.infoshareacademy.jjdd6.dao.TransactionDao;
import com.infoshareacademy.jjdd6.dao.WalletDao;
import com.infoshareacademy.jjdd6.freemarker.TemplateProvider;
import com.infoshareacademy.jjdd6.validation.Validators;
import com.infoshareacademy.jjdd6.wilki.Share;
import com.infoshareacademy.jjdd6.wilki.Transaction;
import com.infoshareacademy.jjdd6.wilki.Wallet;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/share-sell")
@Transactional
public class SellSharesServlet extends HttpServlet {

    private static Logger logger = LoggerFactory.getLogger(SellSharesServlet.class);

    @Inject
    WalletDao walletDao;

    @Inject
    ShareDao shareDao;

    @Inject
    TransactionDao transactionDao;

    @Inject
    private Validators validators;

    @Inject
    TemplateProvider templateProvider;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String action = req.getParameter("action");

        if("sell_selected".equals(action)){
            Map<String, Object> model = new HashMap<>();

            Wallet existingWallet = walletDao.findById(1L);
            String ticker = req.getParameter("ticker");

            Share share = existingWallet.scanWalletForShare(ticker);
            showSellSpecifiedShare(resp, "", model);
        }
        else {
            showWalletWithSellForm(resp, "");
        }
    }

    private void showSellSpecifiedShare(HttpServletResponse resp, String status, Map<String, Object> model) throws IOException {

        Template template = templateProvider.getTemplate(getServletContext(), "menu.ftlh");

        try {
            template.process(model, resp.getWriter());
        } catch (TemplateException e) {
            resp.getWriter().println("Something went wrong");
        }

    }

    private void showWalletWithSellForm(HttpServletResponse resp, String status) throws IOException {
        Map<String, Object> model = new HashMap<>();

        Template template = templateProvider.getTemplate(getServletContext(), "menu.ftlh");

        Wallet existingWallet = walletDao.findById(1L);

        List<Share> shares = existingWallet.getShares();

        BigDecimal roe = existingWallet.getROE();

        BigDecimal freeCash = existingWallet.getFreeCash();

        model.put("shares", shares);
        model.put("roe", roe);
        model.put("freeCash", freeCash);
        model.put("content", "sell_selected");

        if(null != status){
            model.put("status", status);
        }
        try {
            template.process(model, resp.getWriter());
        } catch (TemplateException e) {
            resp.getWriter().println("Something went wrong");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        sellShare(req, resp);
    }

    private void sellShare(HttpServletRequest req, HttpServletResponse resp)
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

        String amountStr = req.getParameter("amount");

        if (validators.isNotIntegerOrIsSmallerThanZero(amountStr)) {
            resp.getWriter().println("Amount should be an integer greater than 0");
            logger.info("Incorrect amount = {}", amountStr);
            return;
        }

        String priceStr = req.getParameter("price");

        if (validators.isDoubleGreaterThanZero(priceStr)) {
            resp.getWriter().println("Price should be a number greater than 0 - format 0.00");
            logger.info("Incorrect price = {}", amountStr);
            return;
        }

        int amount = Integer.parseInt(amountStr);
        double price = Double.parseDouble(priceStr);
        final Long walletId = Long.parseLong(req.getParameter("wallet_id"));
        final Wallet existingWallet = walletDao.findById(walletId);

        existingWallet.sellShare(ticker, amount, price);
        Transaction transaction = existingWallet.scanWalletForShare(ticker).getTransactionHistory().get(existingWallet.scanWalletForShare(ticker).getTransactionHistory().size()-1);
        Share share = existingWallet.scanWalletForShare(ticker);
        transaction.setShare(share);
        transaction.setWallet(existingWallet);

        transactionDao.save(transaction);
        shareDao.update(share);
        walletDao.update(existingWallet);

        logger.info("Wallet object updated: {}", existingWallet);

        resp.getWriter().println("Transaction success."
                + "\nFree Cash: " + existingWallet.getFreeCash());
    }
}
