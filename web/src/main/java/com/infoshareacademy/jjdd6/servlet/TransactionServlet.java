package com.infoshareacademy.jjdd6.servlet;

import com.infoshareacademy.jjdd6.dao.ShareDao;
import com.infoshareacademy.jjdd6.dao.TransactionDao;
import com.infoshareacademy.jjdd6.dao.WalletDao;
import com.infoshareacademy.jjdd6.wilki.Transaction;
import com.infoshareacademy.jjdd6.wilki.TransactionType;
import org.apache.commons.lang3.math.NumberUtils;
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
import java.util.List;

@WebServlet(urlPatterns = "/transaction")
public class TransactionServlet extends HttpServlet {
    private static Logger logger = LoggerFactory.getLogger(TransactionServlet.class);

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
            findAllTransactions(req, resp);
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

    private String getAction(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final String action = req.getParameter("action");
        logger.info("Requested action: {}", action);
        if (action == null || action.isEmpty()) {
            resp.getWriter().write("Empty action parameter.");
        }
        return action;
    }

    private void updateTransaction(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String idStr = req.getParameter("id");
        if (!NumberUtils.isDigits(idStr)) {
            resp.getWriter().println("Transaction id should bean integer");
            return;
        }
        final Long id = Long.parseLong(idStr);
        logger.info("Updating Transaction with id = {}", id);

        final Transaction existingTransaction = transactionDao.findById(id);
        if (existingTransaction == null) {
            logger.info("No Transaction found for id = {}, nothing to be updated", id);
            return;
        }
        String amountStr = req.getParameter("amount");
        if (!NumberUtils.isDigits(amountStr)) {
            resp.getWriter().println("Amount should be an integer");
            return;
        }
        Integer amount = Integer.parseInt(amountStr);
        existingTransaction.setAmount(amount);

        String priceStr = req.getParameter("price");
        if (!NumberUtils.isParsable(priceStr)) {
            resp.getWriter().println("Price should be a number");
            return;
        }
        Double price = Double.parseDouble(priceStr);
        existingTransaction.setPrice(BigDecimal.valueOf(price));

        String profitStr = req.getParameter("profit");
        if (!NumberUtils.isParsable(profitStr)) {
            resp.getWriter().println("Profit should be a number");
            return;
        }
        Double profit = Double.parseDouble(profitStr);
        existingTransaction.setProfit(BigDecimal.valueOf(profit));

        String dateStr = req.getParameter("date");
        LocalDate date = LocalDate.parse(dateStr);
        existingTransaction.setDate(date);

        String feeValueStr = req.getParameter("fee-value");
        if (!NumberUtils.isParsable(feeValueStr)) {
            resp.getWriter().println("Fee value should be a number");
            return;
        }
        Double feeValue = Double.parseDouble(feeValueStr);
        existingTransaction.setTransactionFeeValue(BigDecimal.valueOf(feeValue));

        String transactionType = req.getParameter("type");
        TransactionType type = TransactionType.valueOf(transactionType);
        existingTransaction.setType(type);

        transactionDao.update(existingTransaction);
        logger.info("Transaction object updated: {}", existingTransaction);

        findAllTransactions(req, resp);

    }

    private void addTransaction(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        final Transaction transaction = new Transaction();
        String amountStr = req.getParameter("amount");
        if (!NumberUtils.isDigits(amountStr)) {
            resp.getWriter().println("Amount should be a whole number");
            return;
        }
        Integer amount = Integer.parseInt(amountStr);
        transaction.setAmount(amount);

        String priceStr = req.getParameter("amount");
        if (!NumberUtils.isParsable(priceStr)) {
            resp.getWriter().println("Price should have a numerical value");
            return;
        }
        Double price = Double.parseDouble(priceStr);
        transaction.setPrice(BigDecimal.valueOf(price));

        String profitStr = req.getParameter("profit");
        if (!NumberUtils.isParsable(profitStr)) {
            resp.getWriter().println("Profit should have numerical value");
            return;
        }
        Double profit = Double.parseDouble(profitStr);
        transaction.setProfit(BigDecimal.valueOf(profit));

        String dateStr = req.getParameter("date");
        LocalDate date = LocalDate.parse(dateStr);
        transaction.setDate(date);

        String feeValStr = req.getParameter("fee-value");
        if (!NumberUtils.isParsable(feeValStr)) {
            resp.getWriter().println("Fee value should have numerical value");
            return;
        }
        Double feeValue = Double.parseDouble(feeValStr);
        transaction.setTransactionFeeValue(BigDecimal.valueOf(feeValue));

        String transactionTypeStr = req.getParameter("type");
        TransactionType type = TransactionType.valueOf(transactionTypeStr);
        transaction.setType(type);

        transactionDao.save(transaction);
        logger.info("Saved a new Transaction object: {}", transaction);

        findAllTransactions(req, resp);
    }

    private void deleteTransaction(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String idStr = req.getParameter("id");
        if (!NumberUtils.isDigits(idStr)) {
            resp.getWriter().println("Id should be a whole number");
            return;
        }
        final Long id = Long.parseLong(req.getParameter("id"));
        if (transactionDao.findById(id) != null) {
            logger.info("Removing Transaction with id = {}", id);
            transactionDao.delete(id);
        } else {
            resp.getWriter().println("There is no transaction with id = " + id);
        }
        findAllTransactions(req, resp);
    }

    private void findAllTransactions(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final List<Transaction> result = transactionDao.findAll();
        logger.info("Found {} objects", result.size());
        for (Transaction transaction : result) {
            resp.getWriter().write(transaction.toString() + "\n");
        }
    }
}
