//package com.infoshareacademy.jjdd6.servlet;
//
//import com.infoshareacademy.jjdd6.dao.ShareDao;
//import com.infoshareacademy.jjdd6.dao.TransactionDao;
//import com.infoshareacademy.jjdd6.dao.WalletDao;
//import com.infoshareacademy.jjdd6.wilki.Share;
//import com.infoshareacademy.jjdd6.wilki.Wallet;
//import org.apache.commons.lang3.math.NumberUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import javax.inject.Inject;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.transaction.Transactional;
//import java.io.IOException;
//import java.math.BigDecimal;
//import java.util.List;
//
//
//@WebServlet(urlPatterns = "/wallet-op")
//@Transactional
//public class WalletServlet extends HttpServlet {
//
//    private static Logger logger = LoggerFactory.getLogger(WalletServlet.class);
//
//    @Inject
//    private ShareDao shareDao;
//
//    @Inject
//    private WalletDao walletDao;
//
//    @Inject
//    private TransactionDao transactionDao;
//
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
//            throws IOException {
//
//        final String action = getAction(req, resp);
//        if (action == null) {
//            return;
//        }
//
//        if (action.equals("findAll")) {
//            findAllWallets(req, resp);
//        } else if (action.equals("add")) {
//            addWallet(req, resp);
//        } else if (action.equals("delete")) {
//            deleteWallet(req, resp);
//        } else if (action.equals("update")) {
//            updateWallet(req, resp);
//        } else {
//            resp.getWriter().write("Unknown action.");
//        }
//    }
//
//    private String getAction(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        final String action = req.getParameter("action");
//        logger.info("Requested action: {}", action);
//        if (action == null || action.isEmpty()) {
//            resp.getWriter().write("Empty action parameter.");
//        }
//        return action;
//    }
//
//    private void updateWallet(HttpServletRequest req, HttpServletResponse resp)
//            throws IOException {
//
//        String idStr = req.getParameter("id");
//        if (!NumberUtils.isDigits(idStr)) {
//            resp.getWriter().println("Wallet id should be an integer");
//            return;
//        }
//        final Long id = Long.parseLong(req.getParameter("id"));
//        logger.info("Updating wallet with id = {}", id);
//
//        final Wallet existingWallet = walletDao.findById(id);
//        if (existingWallet == null) {
//            logger.info("No wallet found for id = {}, nothing to be updated", id);
//            return;
//        }
//        String bcashstr = req.getParameter("baseCash");
//        if (!NumberUtils.isParsable(bcashstr)) {
//            resp.getWriter().println("Base cash should be a number");
//        } else {
//            Double baseCashL = Double.valueOf(bcashstr);
//            BigDecimal baseCash = BigDecimal.valueOf(baseCashL);
//            existingWallet.setBaseCash(baseCash);
//        }
//        String shareIdStr = req.getParameter("shareid");
//        if (!NumberUtils.isDigits(shareIdStr)) {
//            resp.getWriter().println("Share id should be an integer");
//            return;
//        }
//        Long shareid = Long.valueOf(shareIdStr);
//        Share share = shareDao.findById(shareid);
//        if (share != null) {
//            existingWallet.getShares().add(share);
//        } else {
//            resp.getWriter().println("There is no share with such id");
//        }
//        walletDao.update(existingWallet);
//        logger.info("Wallet object updated: {}", existingWallet);
//
//        findAllWallets(req, resp);
//    }
//
//    private void addWallet(HttpServletRequest req, HttpServletResponse resp)
//            throws IOException {
//
//        final Wallet wallet = new Wallet();
//        wallet.setBaseCash(BigDecimal.valueOf(0.00));
//
//        walletDao.save(wallet);
//        logger.info("Saved a new wallet object: {}", wallet);
//
//        findAllWallets(req, resp);
//    }
//
//    private void deleteWallet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//
//        String idStr = req.getParameter("id");
//        if (!NumberUtils.isDigits(idStr)) {
//            resp.getWriter().println("Wallet id should be an integer");
//            return;
//        }
//        Long id = Long.parseLong(idStr);
//
//        if (walletDao.findById(id) != null) {
//            logger.info("Removing wallet with id = {}", id);
//            walletDao.delete(id);
//        } else {
//            resp.getWriter().println("There is no wallet with id = " + id);
//        }
//
//        findAllWallets(req, resp);
//    }
//
//
//    private void findAllWallets(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        final List<Wallet> result = walletDao.findAll();
//        logger.info("Found {} objects", result.size());
//        for (Wallet wallet : result) {
//            resp.getWriter().write(wallet.toString() + "\n");
//        }
//    }
//
//}
