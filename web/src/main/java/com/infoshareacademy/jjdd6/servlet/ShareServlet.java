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
import java.time.DateTimeException;
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

        final String action = getString(req, resp);
        if (action == null) return;

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

    private String getString(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final String action = req.getParameter("action");
        LOG.info("Requested action: {}", action);
        if (action == null || action.isEmpty()) {
            resp.getWriter().write("Empty action parameter.");
        }
        return action;
    }

    private void updateShare(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        try {
            final Long id = Long.parseLong(req.getParameter("id"));
            LOG.info("Updating share with id = {}", id);

            final Share existingShare = shareDao.findById(id);
            if (existingShare == null) {
                LOG.info("No share found for id = {}, nothing to be updated", id);
                return;
            }

            String ticker = req.getParameter("ticker");
            existingShare.setTicker(ticker);

            String compName = req.getParameter("company-name");
            existingShare.setFullCompanyName(compName);

            String currentPriceStr = req.getParameter("current-price");
            try {
                Double currentPrice = Double.parseDouble(currentPriceStr);
                existingShare.setCurrentPrice(BigDecimal.valueOf(currentPrice));
            } catch (NumberFormatException e) {
                resp.getWriter().println("Current price should be a number");
            }

            String takeProfitPriceStr = req.getParameter("take-profit");
            try {
                Double takeProfitPrice = Double.parseDouble(takeProfitPriceStr);
                existingShare.setTakeProfitPrice(BigDecimal.valueOf(takeProfitPrice));
            } catch (NumberFormatException e) {
                resp.getWriter().println("Take profit price should have a number value");
            }

            String stopLossPriceStr = req.getParameter("stop-loss");
            try {
                Double stopLossPrice = Double.parseDouble(stopLossPriceStr);
                existingShare.setStopLossPrice(BigDecimal.valueOf(stopLossPrice));
            } catch (NumberFormatException e) {
                resp.getWriter().println("Stop loss price should have a number value");
            }
            String currentPEstr = req.getParameter("current-PE");
            try {
                Double currentPE = Double.parseDouble(currentPEstr);
                existingShare.setCurrentPE(currentPE);
            } catch (NumberFormatException e) {
                resp.getWriter().println("Current PE should have a number value");
            }
            String volumeStr = req.getParameter("volume");
            try {
                Long volume = Long.parseLong(volumeStr);
                existingShare.setVolume(volume);
            } catch (NumberFormatException e) {
                resp.getWriter().println("Volume value should be na integer");
            }
            String highestPriceStr = req.getParameter("highest-price");
            try {
                Double highestPrice = Double.parseDouble(highestPriceStr);
                existingShare.setHighestPrice(BigDecimal.valueOf(highestPrice));
            } catch (NumberFormatException e) {
                resp.getWriter().println("Highest price should be a number");
            }
            String lowestPriceStr = req.getParameter("lowest-price");
            try {
                Double lowestPrice = Double.parseDouble(lowestPriceStr);
                existingShare.setLowestPrice(BigDecimal.valueOf(lowestPrice));
            } catch (NumberFormatException e) {
                resp.getWriter().println("Lowest price should be a number");
            }
            String dateStr = req.getParameter("data-date");
            try {
                LocalDate date = LocalDate.parse(dateStr);
                existingShare.setDataDate(date);
            } catch (DateTimeException e) {
                resp.getWriter().println("Date should be: yyyy-mm-dd");
            }
            String timeStr = req.getParameter("time");
            try {
                LocalTime time = LocalTime.parse(timeStr);
                existingShare.setDataTime(time);
            } catch (DateTimeException e) {
                resp.getWriter().println("Time should be hh:mm");
            }
            String walletIdStr = req.getParameter("wallet-id");
            try {
                Long walletId = Long.parseLong(walletIdStr);
                Wallet wallet = walletDao.findById(walletId);
                if (wallet != null) {
                    existingShare.getWallets().add(wallet);
                } else {
                    resp.getWriter().println("There is no wallet with such id");
                }
            } catch (NumberFormatException e) {
                resp.getWriter().println("Walletid should be an integer");
            }

            String transactionIdStr = req.getParameter("transaction-id");
            try {
                Long transactionId = Long.parseLong(transactionIdStr);
                Transaction transaction = transactionDao.findById(transactionId);
                if (transaction == null) {
                    LOG.info("No transaction found for id = {}, nothing to be added to the list", id);
                } else {
                    existingShare.getTransactionLinkedList().add(transaction);
                    existingShare.getTransactionHistory().add(transaction);
                }
            } catch (NumberFormatException e) {
                resp.getWriter().println("Transaction id should be an integer");
            }
            shareDao.update(existingShare);
            LOG.info("Share object updated: {}", existingShare);
        } catch (NumberFormatException e) {
            resp.getWriter().println("Share id should be an integer");
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

        try {
            final Long id = Long.parseLong(req.getParameter("id"));
            if (shareDao.findById(id) != null) {
                LOG.info("Removing Share with id = {}", id);
                shareDao.delete(id);
            } else {
                resp.getWriter().println("There is no share with id = " + id);
            }
        } catch (NumberFormatException e) {
            resp.getWriter().println("Share id should be an integer");
        }

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
