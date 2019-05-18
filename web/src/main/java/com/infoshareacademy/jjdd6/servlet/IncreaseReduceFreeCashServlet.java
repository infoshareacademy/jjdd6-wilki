package com.infoshareacademy.jjdd6.servlet;

import com.infoshareacademy.jjdd6.dao.WalletDao;
import com.infoshareacademy.jjdd6.freemarker.TemplateProvider;
import com.infoshareacademy.jjdd6.service.StatsService;
import com.infoshareacademy.jjdd6.service.UserService;
import com.infoshareacademy.jjdd6.validation.Validators;
import com.infoshareacademy.jjdd6.wilki.User;
import com.infoshareacademy.jjdd6.wilki.Wallet;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Transactional
@WebServlet(urlPatterns = "/increase-reduce-free-cash")
public class IncreaseReduceFreeCashServlet extends HttpServlet {

    private static Logger logger = LoggerFactory.getLogger(IncreaseReduceFreeCashServlet.class);

    @Inject
    private WalletDao walletDao;

    @Inject
    private Validators validators;

    @Inject
    private UserService userService;

    @Inject
    private TemplateProvider templateProvider;

    @Inject
    private StatsService statsService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        showManageFreeCash(req, resp, "");

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String action = req.getParameter("action");
        if(action.equals("increase")){
            increaseFreeCash(req, resp);
        }
        else{
            reduceFreeCash(req, resp);
        }

    }

    private void showManageFreeCash(HttpServletRequest req, HttpServletResponse resp, String status) throws IOException {

        User user = userService.loggedUser(req);
        Wallet userWallet = user.getWallet();
        BigDecimal roe = userWallet.getROE();
        BigDecimal freeCash = userWallet.getFreeCash();
        String profilePicURL = userService.userProfilePicURL(user);
        Map<String, Object> model = new HashMap<>();
        if (null != status) {
            model.put("status", status);
        }
        Map<String, String> bestPerforming = statsService.getMostProfitableShare(userWallet);
        Map<String, String> worstPerforming = statsService.getLeastProfitableShare(userWallet);
        int userAdmin = 0;
        if (user.isAdmin()) {
            userAdmin = 1;
        }
        model.put("isAdmin", userAdmin);
        model.put("mpTicker", bestPerforming.get("ticker"));
        model.put("mpProfit", bestPerforming.get("profit"));
        model.put("mpReturn", bestPerforming.get("return"));
        model.put("wpTicker", worstPerforming.get("ticker"));
        model.put("wpProfit", worstPerforming.get("profit"));
        model.put("wpReturn", worstPerforming.get("return"));
        model.put("roe", roe);
        model.put("freeCash", freeCash);
        model.put("content", "manage");
        model.put("userName", user.getName());
        model.put("profilePicURL", profilePicURL);

        Template template = templateProvider.getTemplate(getServletContext(), "menu.ftlh");

        try {
            template.process(model, resp.getWriter());
        } catch (TemplateException e) {
            resp.getWriter().println("Something went wrong");
        }

    }

    private void increaseFreeCash(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        User user = userService.loggedUser(req);
        Wallet userWallet = user.getWallet();
        Long id = userWallet.getId();

        String cash = req.getParameter("cash");

        if (validators.isSmallerThanZero(id)) {
            showManageFreeCash(req, resp, "Wallet ID should be an integer greater than 0");
            logger.info("Incorrect wallet walletId = {}", id);
            return;
        }

        if (validators.isWalletNotPresent(id)) {
            showManageFreeCash(req, resp, "No wallet found for walletId = {" + id + "}");
            logger.info("No wallet found for walletId = {}, nothing to be updated", id);
            return;
        }

        if (validators.isNotDoubleOrIsSmallerThanZero(cash)) {
            showManageFreeCash(req, resp, "Cash should be a number");
            return;
        }

        BigDecimal cashBigDecimal = BigDecimal.valueOf(Double.parseDouble(cash));

        userWallet.increaseBaseCash(cashBigDecimal);

        walletDao.update(userWallet);
        logger.info("Wallet object updated: {}", userWallet);

        showManageFreeCash(req, resp, "Free cash increased by: " + cash + " PLN");
    }

    private void reduceFreeCash(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        User user = userService.loggedUser(req);
        Wallet userWallet = user.getWallet();
        Long id = userWallet.getId();

        if (validators.isSmallerThanZero(id)) {
            showManageFreeCash(req, resp, "Wallet ID should be an integer greater than 0");
            logger.info("Incorrect wallet walletId = {}", id);
            return;
        }

        if (validators.isWalletNotPresent(id)) {
            showManageFreeCash(req, resp, "No wallet found for walletId = {" + id + "}");
            logger.info("No wallet found for walletId = {}, nothing to be updated", id);
            return;
        }

        String cash = req.getParameter("cash");

        if (validators.isNotDoubleOrIsSmallerThanZero(cash)) {
            showManageFreeCash(req, resp, "Cash should be a number");
            return;
        }

        BigDecimal cashBigDecimal = BigDecimal.valueOf(Double.parseDouble(cash));

        if (validators.isEnoughCashToReduceFreeCash(userWallet, cash)) {
            showManageFreeCash(req, resp, "You don't have enough money!");
            logger.info("Not enough money to reduce free cash");
            return;
        }

        userWallet.reduceBaseCash(cashBigDecimal);

        walletDao.update(userWallet);
        logger.info("Wallet object updated: {}", userWallet);

        showManageFreeCash(req, resp, "Free cash reduced by: " + cash + " PLN");
    }
}
