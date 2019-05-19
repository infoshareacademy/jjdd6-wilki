package com.infoshareacademy.jjdd6.servlet;

import com.infoshareacademy.jjdd6.dao.ShareDao;
import com.infoshareacademy.jjdd6.dao.TransactionDao;
import com.infoshareacademy.jjdd6.freemarker.TemplateProvider;
import com.infoshareacademy.jjdd6.service.StatsService;
import com.infoshareacademy.jjdd6.service.UserService;
import com.infoshareacademy.jjdd6.wilki.*;
import freemarker.template.Template;
import freemarker.template.TemplateException;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@WebServlet("/transactions")
public class TransactionsServlet extends HttpServlet {

    private static Logger logger = LoggerFactory.getLogger(TransactionsServlet.class);

    @Inject
    private TemplateProvider templateProvider;

    @Inject
    private UserService userService;

    @Inject
    private StatsService statsService;

    @Inject
    private TransactionDao transactionDao;

    @Inject
    private ShareDao shareDao;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        User user = userService.loggedUser(req);
        Wallet userWallet = user.getWallet();
        List<Transaction> transactionList = transactionDao.findAllByWalletId(userWallet.getId());
        List<Share> shares = shareDao.findAll();
        BigDecimal roe = userWallet.getROE();
        BigDecimal freeCash = userWallet.getFreeCash();
        String profilePicURL = userService.userProfilePicURL(user);
        DownloadCurrentData.updateWalletData(userWallet);
        Map<String, Object> model = new HashMap<>();
        Map<String, String> bestPerforming = statsService.getMostProfitableShare(userWallet);
        Map<String, String> worstPerforming = statsService.getLeastProfitableShare(userWallet);
        int userAdmin = 0;
        if (user.isAdmin()) {
            userAdmin = 1;
        }
        model.put("isAdmin", userAdmin);
        model.put("mpTicker", bestPerforming.get("ticker"));
        model.put("mpReturn", bestPerforming.get("return"));
        model.put("wpTicker", worstPerforming.get("ticker"));
        model.put("wpReturn", worstPerforming.get("return"));
        model.put("transactions", transactionList);
        model.put("shares", shares);
        model.put("roe", roe);
        model.put("freeCash", freeCash);
        model.put("content", "transaction");
        model.put("profilePicURL", profilePicURL);
        model.put("userName", user.getName());

        Template template = templateProvider.getTemplate(getServletContext(), "menu.ftlh");
        logger.info("Template loaded.");

        try {
            template.process(model, resp.getWriter());
        } catch (TemplateException e) {
            logger.info("Something went wrong");
            resp.getWriter().println("Something went wrong");
        }

    }
}
