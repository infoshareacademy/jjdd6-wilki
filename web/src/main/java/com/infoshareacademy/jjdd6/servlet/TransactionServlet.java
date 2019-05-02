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
import java.time.DateTimeException;
import java.time.LocalDate;
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

        final String action = getString(req, resp);
        if (action == null) return;

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

    private String getString(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final String action = req.getParameter("action");
        LOG.info("Requested action: {}", action);
        if (action == null || action.isEmpty()) {
            resp.getWriter().write("Empty action parameter.");
        }
        return action;
    }

    private void updateTransaction(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        try {
            final Long id = Long.parseLong(req.getParameter("id"));
            LOG.info("Updating Transaction with id = {}", id);

            final Transaction existingTransaction = transactionDao.findById(id);
            if (existingTransaction == null) {
                LOG.info("No Transaction found for id = {}, nothing to be updated", id);
                return;
            }
            String amountStr = req.getParameter("amount");
            try {
                Integer amount = Integer.parseInt(amountStr);
                existingTransaction.setAmount(amount);
            } catch (NumberFormatException e) {
                resp.getWriter().println("Amount should be an integer");
            }

            String priceStr = req.getParameter("price");
            try {
                Double price = Double.parseDouble(priceStr);
                existingTransaction.setPrice(BigDecimal.valueOf(price));
            } catch (NumberFormatException e) {
                resp.getWriter().println("Price should be a number");
            }

            String profitStr = req.getParameter("profit");
            try {
                Double profit = Double.parseDouble(profitStr);
                existingTransaction.setProfit(BigDecimal.valueOf(profit));
            } catch (NumberFormatException e) {
                resp.getWriter().println("Profit should be a number");
            }
            String dateStr = req.getParameter("date");
            try {
                LocalDate date = LocalDate.parse(dateStr);
                existingTransaction.setDate(date);
            } catch (DateTimeException e) {
                resp.getWriter().println("Date should be: yyyy-mm-dd");
            }

            String feeValueStr = req.getParameter("fee-value");
            try {
                Double feeValue = Double.parseDouble(feeValueStr);
                existingTransaction.setTransactionFeeValue(BigDecimal.valueOf(feeValue));
            } catch (NumberFormatException e) {
                resp.getWriter().println("Fee value should be a number");
            }
            String transactionType = req.getParameter("type");
            TransactionType type = TransactionType.valueOf(transactionType);
            existingTransaction.setType(type);

            transactionDao.update(existingTransaction);
            LOG.info("Transaction object updated: {}", existingTransaction);
        } catch (NumberFormatException e) {
            resp.getWriter().println("Transaction id should be an integer");
        }

        findAll(req, resp);
    }

    private void addTransaction(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        final Transaction transaction = new Transaction();
        String amountStr = req.getParameter("amount");
        try {
            Integer amount = Integer.parseInt(amountStr);
            transaction.setAmount(amount);
        } catch (NumberFormatException e) {
            resp.getWriter().println("Amount should be a whole number");
        }
        String priceStr = req.getParameter("amount");
        try {
            Double price = Double.parseDouble(priceStr);
            transaction.setPrice(BigDecimal.valueOf(price));
        } catch (NumberFormatException e) {
            resp.getWriter().println("Price should have a number value");
        }
        String profitStr = req.getParameter("profit");
        try {
            Double profit = Double.parseDouble(profitStr);
            transaction.setProfit(BigDecimal.valueOf(profit));
        } catch (NumberFormatException e) {
            resp.getWriter().println("Profit should have a number value");
        }
        String dateStr = req.getParameter("date");
        try {
            LocalDate date = LocalDate.parse(dateStr);
            transaction.setDate(date);
        } catch (DateTimeException e) {
            resp.getWriter().println("Date should be yyyy-mm-dd");
        }
        String feeValStr = req.getParameter("fee-value");
        try {
            Double feeValue = Double.parseDouble(feeValStr);
            transaction.setTransactionFeeValue(BigDecimal.valueOf(feeValue));
        } catch (NumberFormatException e) {
            resp.getWriter().println("Fee value should have a number value");
        }
        String transactionTypeStr = req.getParameter("type");
        TransactionType type = TransactionType.valueOf(transactionTypeStr);
        transaction.setType(type);

        transactionDao.save(transaction);
        LOG.info("Saved a new Transaction object: {}", transaction);

        findAll(req, resp);
    }

    private void deleteTransaction(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        final Long id = Long.parseLong(req.getParameter("id"));
        if (transactionDao.findById(id) != null) {
            LOG.info("Removing Transaction with id = {}", id);
            transactionDao.delete(id);
        } else {
            resp.getWriter().println("There is no transaction with id = " + id);
        }
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
