package com.infoshareacademy.jjdd6.servlet;

import com.infoshareacademy.jjdd6.dao.ShareDao;
import com.infoshareacademy.jjdd6.dao.TransactionDao;
import com.infoshareacademy.jjdd6.dao.WalletDao;
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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@WebServlet(urlPatterns = "/share")
@Transactional
public class ShareServlet extends HttpServlet {
    private static Logger logger = LoggerFactory.getLogger(ShareServlet.class);

    @Inject
    private ShareDao shareDao;

    @Inject
    private WalletDao walletDao;

    @Inject
    private TransactionDao transactionDao;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        final String action = getAction(req, resp);
        if (action == null) {
            return;
        }

        if (action.equals("findAll")) {
            findAllShares(req, resp);
        } else if (action.equals("add")) {
            addShare(req, resp);
        } else if (action.equals("delete")) {
            deleteShare(req, resp);
        } else if (action.equals("update")) {
            updateShare(req, resp);
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

    private void updateShare(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String idStr = req.getParameter("id");
        if (!NumberUtils.isDigits(idStr)) {
            resp.getWriter().println("Share id should be an integer");
            return;
        }
        final Long id = Long.parseLong(idStr);
        logger.info("Updating share with id = {}", id);

        final Share existingShare = shareDao.findById(id);
        if (existingShare == null) {
            logger.info("No share found for id = {}, nothing to be updated", id);
            return;
        }

        String ticker = req.getParameter("ticker");
        existingShare.setTicker(ticker);

        String compName = req.getParameter("company-name");
        existingShare.setFullCompanyName(compName);

        String currentPriceStr = req.getParameter("current-price");
        if (!NumberUtils.isParsable(currentPriceStr)) {
            resp.getWriter().println("Current price should be a number");
            return;
        }
        Double currentPrice = Double.parseDouble(currentPriceStr);
        existingShare.setCurrentPrice(BigDecimal.valueOf(currentPrice));

        String takeProfitPriceStr = req.getParameter("take-profit");
        if (!NumberUtils.isParsable(takeProfitPriceStr)) {
            resp.getWriter().println("Take profit price should be a number");
            return;
        }
        Double takeProfitPrice = Double.parseDouble(takeProfitPriceStr);
        existingShare.setTakeProfitPrice(BigDecimal.valueOf(takeProfitPrice));

        String stopLossPriceStr = req.getParameter("stop-loss");
        if (!NumberUtils.isParsable(stopLossPriceStr)) {
            resp.getWriter().println("Stop loss price should be a number");
            return;
        }
        Double stopLossPrice = Double.parseDouble(stopLossPriceStr);
        existingShare.setStopLossPrice(BigDecimal.valueOf(stopLossPrice));

        String currentPEstr = req.getParameter("current-PE");
        if (!NumberUtils.isParsable(currentPEstr)) {
            resp.getWriter().println("Current PE should be a number");
            return;
        }
        Double currentPE = Double.parseDouble(currentPEstr);
        existingShare.setCurrentPE(currentPE);

        String volumeStr = req.getParameter("volume");
        if (!NumberUtils.isDigits(volumeStr)) {
            resp.getWriter().println("Volume should be a number");
            return;
        }
        Long volume = Long.parseLong(volumeStr);
        existingShare.setVolume(volume);

        String highestPriceStr = req.getParameter("highest-price");
        if (!NumberUtils.isParsable(highestPriceStr)) {
            resp.getWriter().println("Highest price should be a number");
            return;
        }
        Double highestPrice = Double.parseDouble(highestPriceStr);
        existingShare.setHighestPrice(BigDecimal.valueOf(highestPrice));

        String lowestPriceStr = req.getParameter("lowest-price");
        if (!NumberUtils.isParsable(lowestPriceStr)) {
            resp.getWriter().println("Lowest price should be a number");
            return;
        }
        Double lowestPrice = Double.parseDouble(lowestPriceStr);
        existingShare.setLowestPrice(BigDecimal.valueOf(lowestPrice));

        String dateStr = req.getParameter("data-date");
        LocalDate date = LocalDate.parse(dateStr);
        existingShare.setDataDate(date);

        String timeStr = req.getParameter("time");
        LocalTime time = LocalTime.parse(timeStr);
        existingShare.setDataTime(time);

        String walletIdStr = req.getParameter("wallet-id");
        if (!NumberUtils.isDigits(walletIdStr)) {
            resp.getWriter().println("Wallet id should be a whole number");
            return;
        }
        Long walletId = Long.parseLong(walletIdStr);
        Wallet wallet = walletDao.findById(walletId);
        if (wallet != null) {
            existingShare.getWallets().add(wallet);
        } else {
            resp.getWriter().println("There is no wallet with such id");
        }

        String transactionIdStr = req.getParameter("transaction-id");
        if (!NumberUtils.isDigits(transactionIdStr)) {
            resp.getWriter().println("Transaction id should be a whole number");
            return;
        }
        Long transactionId = Long.parseLong(transactionIdStr);
        Transaction transaction = transactionDao.findById(transactionId);
        if (transaction == null) {
            logger.info("No transaction found for id = {}, nothing to be added to the list", id);
        } else {
            existingShare.getTransactionHistory().add(transaction);
        }

        shareDao.update(existingShare);
        logger.info("Share object updated: {}", existingShare);

        findAllShares(req, resp);
    }

    private void addShare(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        final Share share = new Share();
        String ticker = req.getParameter("ticker");
        share.setTicker(ticker);
        shareDao.save(share);
        logger.info("Saved a new Share object: {}", share);

        findAllShares(req, resp);
    }

    private void deleteShare(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String idString = req.getParameter("id");
        if (!NumberUtils.isDigits(idString)) {
            resp.getWriter().println("Share id should be a whole number");
            return;
        }
        final Long id = Long.parseLong(idString);
        if (shareDao.findById(id) != null) {
            logger.info("Removing Share with id = {}", id);
            shareDao.delete(id);
        } else {
            resp.getWriter().println("There is no share with id = " + id);
        }

        findAllShares(req, resp);
    }

    private void findAllShares(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final List<Share> result = shareDao.findAll();
        logger.info("Found {} objects", result.size());
        for (Share share : result) {
            resp.getWriter().write(share.toString() + "\n");
        }
    }
}
