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

        final String action = req.getParameter("action");
        LOG.info("Requested action: {}", action);
        if (action == null || action.isEmpty()) {
            resp.getWriter().write("Empty action parameter.");
            return;
        }

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

    private void updateWallet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        final Long id = Long.parseLong(req.getParameter("id"));
        LOG.info("Updating wallet with id = {}", id);

        final Wallet existingWallet = walletDao.findById(id);
        if (existingWallet == null) {
            LOG.info("No wallet found for id = {}, nothing to be updated", id);
        } else {
            String bcashstr = req.getParameter("baseCash");
            Long baseCashL = Long.valueOf(bcashstr);
            BigDecimal baseCash = BigDecimal.valueOf(baseCashL);
            existingWallet.setBaseCash(baseCash);

            String shareIdStr = req.getParameter("shareid");
            Long shareid = Long.valueOf(shareIdStr);

            Share share = shareDao.findById(shareid);
            existingWallet.getShares().add(share);
            walletDao.update(existingWallet);
            LOG.info("Wallet object updated: {}", existingWallet);
        }

        findAll(req, resp);
    }

    private void addWallet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        final Wallet wallet = new Wallet();
        String bcashstr = req.getParameter("baseCash");
        Long baseCashL = Long.valueOf(bcashstr);
        BigDecimal baseCash = BigDecimal.valueOf(baseCashL);
        wallet.setBaseCash(baseCash);

        walletDao.save(wallet);
        LOG.info("Saved a new wallet object: {}", wallet);

        findAll(req, resp);
    }

    private void deleteWallet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        final Long id = Long.parseLong(req.getParameter("id"));
        LOG.info("Removing wallett with id = {}", id);

        walletDao.delete(id);

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
