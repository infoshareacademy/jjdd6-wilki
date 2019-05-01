package com.infoshareacademy.jjdd6.servlet;

import com.infoshareacademy.jjdd6.dao.ShareDao;
import com.infoshareacademy.jjdd6.dao.TransactionDao;
import com.infoshareacademy.jjdd6.dao.WalletDao;
import com.infoshareacademy.jjdd6.wilki.Share;
import com.infoshareacademy.jjdd6.wilki.Transaction;
import com.infoshareacademy.jjdd6.wilki.TransactionType;
import com.infoshareacademy.jjdd6.wilki.Wallet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@WebServlet(urlPatterns = "/transaction")
public class TransactionServlet extends HttpServlet {
    private Logger LOG = LoggerFactory.getLogger(TransactionServlet.class);

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
            addTransaction(req, resp);
        } else if (action.equals("delete")) {
            deleteTransaction(req, resp);
        } else if (action.equals("update")) {
            updateTransaction(req, resp);
        } else {
            resp.getWriter().write("Unknown action.");
        }
    }

    private void updateTransaction(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        final Long id = Long.parseLong(req.getParameter("id"));
        LOG.info("Updating Transaction with id = {}", id);

        final Transaction existingTransaction = transactionDao.findById(id);
        if (existingTransaction == null) {
            LOG.info("No Transaction found for id = {}, nothing to be updated", id);
        } else {
            String amountStr = req.getParameter("amount");
            Integer amount = Integer.parseInt(amountStr);
            existingTransaction.setAmount(amount);

            String priceStr = req.getParameter("price");
            Long price = Long.parseLong(priceStr);
            existingTransaction.setPrice(BigDecimal.valueOf(price));

            String profitStr = req.getParameter("profit");
            Long profit = Long.parseLong(profitStr);
            existingTransaction.setProfit(BigDecimal.valueOf(profit));

            String dateStr = req.getParameter("date");
            LocalDate date = LocalDate.parse(dateStr);
            existingTransaction.setDate(date);

            String feeValueStr = req.getParameter("fee-value");
            Long feeValue = Long.parseLong(feeValueStr);
            existingTransaction.setTransactionFeeValue(BigDecimal.valueOf(feeValue));

            String transactionType = req.getParameter("type");
            TransactionType type = TransactionType.valueOf(transactionType);
            existingTransaction.setType(type);

            transactionDao.update(existingTransaction);
            LOG.info("Transaction object updated: {}", existingTransaction);
        }

        findAll(req, resp);
    }

    private void addTransaction(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        final Transaction transaction = new Transaction();
        String amountStr = req.getParameter("amount");
        Integer amount = Integer.parseInt(amountStr);
        transaction.setAmount(amount);

        String priceStr = req.getParameter("amount");
        Long price = Long.parseLong(priceStr);
        transaction.setPrice(BigDecimal.valueOf(price));

        String profitStr = req.getParameter("profit");
        Long profit = Long.parseLong(profitStr);
        transaction.setProfit(BigDecimal.valueOf(profit));

        String dateStr = req.getParameter("date");
        LocalDate date = LocalDate.parse(dateStr);
        transaction.setDate(date);

        String feeValStr = req.getParameter("fee-value");
        Long feeValue = Long.parseLong(feeValStr);
        transaction.setTransactionFeeValue(BigDecimal.valueOf(feeValue));

        String transactionTypeStr = req.getParameter("type");
        TransactionType type = TransactionType.valueOf(transactionTypeStr);
        transaction.setType(type);

        transactionDao.save(transaction);
        LOG.info("Saved a new Transaction object: {}", transaction);

        findAll(req, resp);
    }

    private void deleteTransaction(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        final Long id = Long.parseLong(req.getParameter("id"));
        LOG.info("Removing Transaction with id = {}", id);
        transactionDao.delete(id);

        findAll(req, resp);
    }

    private void findAll(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final List<Transaction> result = transactionDao.findAll();
        LOG.info("Found {} objects", result.size());
        for (Transaction transaction : result) {
            resp.getWriter().write(transaction.toString() + "\n");
        }
    }
}
