package com.infoshareacademy.jjdd6.servlet;

import com.infoshareacademy.jjdd6.dao.ShareDao;
import com.infoshareacademy.jjdd6.dao.TransactionDao;
import com.infoshareacademy.jjdd6.dao.WalletDao;
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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@WebServlet(urlPatterns = "/share")
@Transactional
public class ShareServlet extends HttpServlet {
    private Logger LOG = LoggerFactory.getLogger(ShareServlet.class);

    @Inject
    private ShareDao shareDao;

    @Inject
    private WalletDao walletDao;

    @Inject
    private TransactionDao transactionDao;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        final String action = req.getParameter("action");
        LOG.info("Requested action: {}", action);
        if (action == null || action.isEmpty()) {
            resp.getWriter().write("Empty action parameter.");
            return;
        }

        if (action.equals("findAll")) {
            findAll(req, resp);
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

    private void updateShare(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        final Long id = Long.parseLong(req.getParameter("id"));
        LOG.info("Updating share with id = {}", id);

        final Share existingShare = shareDao.findById(id);
        if (existingShare == null) {
            LOG.info("No share found for id = {}, nothing to be updated", id);
        } else {
            String ticker = req.getParameter("ticker");
            existingShare.setTicker(ticker);

            String compName = req.getParameter("company-name");
            existingShare.setFullCompanyName(compName);

            String currentPriceStr = req.getParameter("current-price");
            Long currentPrice = Long.parseLong(currentPriceStr);
            existingShare.setCurrentPrice(BigDecimal.valueOf(currentPrice));

            String takeProfitPriceStr = req.getParameter("take-profit");
            Long takeProfitPrice = Long.parseLong(takeProfitPriceStr);
            existingShare.setTakeProfitPrice(BigDecimal.valueOf(takeProfitPrice));

            String stopLossPriceStr = req.getParameter("stop-loss");
            Long stopLossPrice = Long.parseLong(stopLossPriceStr);
            existingShare.setStopLossPrice(BigDecimal.valueOf(stopLossPrice));

            String currentPEstr = req.getParameter("current-PE");
            Double currentPE = Double.parseDouble(currentPEstr);
            existingShare.setCurrentPE(currentPE);

            String volumeStr = req.getParameter("volume");
            Long volume = Long.parseLong(volumeStr);
            existingShare.setVolume(volume);

            String highestPriceStr = req.getParameter("highest-price");
            Long highestPrice = Long.parseLong(highestPriceStr);
            existingShare.setHighestPrice(BigDecimal.valueOf(highestPrice));

            String lowestPriceStr = req.getParameter("lowest-price");
            Long lowestPrice = Long.parseLong(lowestPriceStr);
            existingShare.setLowestPrice(BigDecimal.valueOf(lowestPrice));

            String dateStr = req.getParameter("data-date");
            LocalDate date = LocalDate.parse(dateStr);
            existingShare.setDataDate(date);

            String timeStr = req.getParameter("time");
            LocalTime time = LocalTime.parse(timeStr);
            existingShare.setDataTime(time);

            String walletIdStr = req.getParameter("wallet-id");
            Long walletId = Long.parseLong(walletIdStr);
            Wallet wallet = walletDao.findById(walletId);
            existingShare.getWallets().add(wallet);

            String transactionIdStr = req.getParameter("transaction-id");
            Long transactionId = Long.parseLong(transactionIdStr);
            Transaction transaction = transactionDao.findById(transactionId);
            existingShare.getTransactionLinkedList().add(transaction);
            existingShare.getTransactionHistory().add(transaction);

            shareDao.update(existingShare);
            LOG.info("Share object updated: {}", existingShare);
        }

        findAll(req, resp);
    }

    private void addShare(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        final Share share = new Share();
        String ticker = req.getParameter("ticker");
        share.setTicker(ticker);
        shareDao.save(share);
        LOG.info("Saved a new Share object: {}", share);

        findAll(req, resp);
    }

    private void deleteShare(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        final Long id = Long.parseLong(req.getParameter("id"));
        LOG.info("Removing Share with id = {}", id);
        shareDao.delete(id);

        findAll(req, resp);
    }

    private void findAll(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final List<Share> result = shareDao.findAll();
        LOG.info("Found {} objects", result.size());
        for (Share share : result) {
            resp.getWriter().write(share.toString() + "\n");
        }
    }
}
