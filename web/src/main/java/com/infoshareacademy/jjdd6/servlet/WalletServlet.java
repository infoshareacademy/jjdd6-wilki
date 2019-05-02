package com.infoshareacademy.jjdd6.servlet;

import com.infoshareacademy.jjdd6.dao.ShareDao;
import com.infoshareacademy.jjdd6.dao.TransactionDao;
import com.infoshareacademy.jjdd6.dao.WalletDao;
import com.infoshareacademy.jjdd6.wilki.Share;
import com.infoshareacademy.jjdd6.wilki.Wallet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;


@WebServlet(urlPatterns = "/wallet-op")
@Transactional
public class WalletServlet extends HttpServlet {

    private Logger LOG = LoggerFactory.getLogger(WalletServlet.class);

    @Inject
    private ShareDao shareDao;

    @Inject
    private WalletDao walletDao;

    @Inject
    private TransactionDao transactionDao;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        Wallet wallet = new Wallet();
        wallet.setBaseCash(BigDecimal.valueOf(10000));
        walletDao.save(wallet);

        Share share1 = new Share("kgh");
        shareDao.save(share1);

    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        final String action = getString(req, resp);
        if (action == null) return;

        if (action.equals("findAll")) {
            findAll(req, resp);
        } else if (action.equals("add")) {
            addWallet(req, resp);
        } else if (action.equals("delete")) {
            deleteWallet(req, resp);
        } else if (action.equals("update")) {
            updateWallet(req, resp);
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

    private void updateWallet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        try {
            final Long id = Long.parseLong(req.getParameter("id"));
            LOG.info("Updating wallet with id = {}", id);

            final Wallet existingWallet = walletDao.findById(id);
            if (existingWallet == null) {
                LOG.info("No wallet found for id = {}, nothing to be updated", id);
                return;
            }
            String bcashstr = req.getParameter("baseCash");
            try {
                Double baseCashL = Double.valueOf(bcashstr);
                BigDecimal baseCash = BigDecimal.valueOf(baseCashL);
                existingWallet.setBaseCash(baseCash);
            } catch (NumberFormatException e) {
                resp.getWriter().println("Base cash should be a number");
            }

            String shareIdStr = req.getParameter("shareid");
            try {
                Long shareid = Long.valueOf(shareIdStr);
                Share share = shareDao.findById(shareid);
                if (share != null) {
                    existingWallet.getShares().add(share);
                } else {
                    resp.getWriter().println("There is no share with such id");
                }
            } catch (NumberFormatException e) {
                resp.getWriter().println("Share id should be a whole number");
            }
            walletDao.update(existingWallet);
            LOG.info("Wallet object updated: {}", existingWallet);
        } catch (NumberFormatException e) {
            resp.getWriter().println("Wallet id should be a whole number");
        }

        findAll(req, resp);

    }

    private void addWallet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        final Wallet wallet = new Wallet();
        String bcashstr = req.getParameter("baseCash");
        try {
            Double baseCashL = Double.valueOf(bcashstr);
            BigDecimal baseCash = BigDecimal.valueOf(baseCashL);
            wallet.setBaseCash(baseCash);
        } catch (NumberFormatException e) {
            resp.getWriter().println("Base cash should be a number");
        }

        walletDao.save(wallet);
        LOG.info("Saved a new wallet object: {}", wallet);

        findAll(req, resp);
    }

    private void deleteWallet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        try {
            final Long id = Long.parseLong(req.getParameter("id"));

            if (walletDao.findById(id) != null) {
                LOG.info("Removing wallet with id = {}", id);
                walletDao.delete(id);
            } else {
                resp.getWriter().println("There is no wallet with id = " + id);
            }
        } catch (NumberFormatException e) {
            resp.getWriter().println("Wallet id should be a number");
        }

        findAll(req, resp);
    }


    private void findAll(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final List<Wallet> result = walletDao.findAll();
        LOG.info("Found {} objects", result.size());
        for (Wallet wallet : result) {
            resp.getWriter().write(wallet.toString() + "\n");
        }
    }

}
