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
import org.apache.commons.lang3.math.NumberUtils;
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

        showWalletWithSellForm(resp, "");
    }

    private void showWalletWithSellForm(HttpServletResponse resp, String status) throws IOException {
        Map<String, Object> model = new HashMap<>();

        List<Share> shares = shareDao.findAll();

        BigDecimal roe = walletDao.findById(1L).getROE();

        BigDecimal freeCash = walletDao.findById(1L).getFreeCash();

        model.put("shares", shares);
        model.put("roe", roe);
        model.put("freeCash", freeCash);
        model.put("content", 3);
        if(null != status){
            model.put("status", status);
        }
        Template template = templateProvider.getTemplate(getServletContext(), "menu.ftlh");
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

//        String idStr = req.getParameter("wallet_id");
//        if (!NumberUtils.isDigits(idStr)) {
//            resp.getWriter().println("Wallet id should be an integer");
//            return;
//        }
//        final Long id = Long.parseLong(req.getParameter("wallet_id"));
//        logger.info("Updating wallet with id = {}", id);

        final Wallet existingWallet = walletDao.findById(1L);
//        if (existingWallet == null) {
//            logger.info("No wallet found for id = {}, nothing to be updated", id);
//            return;
//        }

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
        resp.getWriter().println(ticker);
        resp.getWriter().println(amount);
        resp.getWriter().println(price);

//        existingWallet.sellShare(ticker, amount, price);
//        Transaction transaction = existingWallet.scanWalletForShare(ticker).getTransactionHistory().get(existingWallet.scanWalletForShare(ticker).getTransactionHistory().size() - 1);
//        Share share = existingWallet.scanWalletForShare(ticker);
//        transaction.setShare(share);
//        transaction.setWallet(existingWallet);
//
//        transactionDao.save(transaction);
//        shareDao.update(share);
//        walletDao.update(existingWallet);
//
//        logger.info("Wallet object updated: {}", existingWallet);
//
//        resp.getWriter().println("Transaction success.");
    }
}
