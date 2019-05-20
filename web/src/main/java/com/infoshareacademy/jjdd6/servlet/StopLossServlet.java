package com.infoshareacademy.jjdd6.servlet;

import com.infoshareacademy.jjdd6.dao.ShareDao;
import com.infoshareacademy.jjdd6.dao.TickerDao;
import com.infoshareacademy.jjdd6.dao.WalletDao;
import com.infoshareacademy.jjdd6.freemarker.TemplateProvider;
import com.infoshareacademy.jjdd6.service.StatsService;
import com.infoshareacademy.jjdd6.service.UserService;
import com.infoshareacademy.jjdd6.validation.Validators;
import com.infoshareacademy.jjdd6.wilki.Share;
import com.infoshareacademy.jjdd6.wilki.User;
import com.infoshareacademy.jjdd6.wilki.Wallet;
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

@WebServlet("/sl")
@Transactional
public class StopLossServlet extends HttpServlet {

    private static Logger logger = LoggerFactory.getLogger(StopLossServlet.class);

    @Inject
    private ShareDao shareDao;

    @Inject
    private Validators validators;

    @Inject
    private UserService userService;

    @Inject
    private StatsService statsService;

    @Inject
    private TemplateProvider templateProvider;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String ticker = req.getParameter("ticker");

        if (null != ticker) {

            User user = userService.loggedUser(req);
            Wallet userWallet = user.getWallet();

            if (!(validators.isTickerNotValid(ticker)) && userWallet.checkIfShareIsPresent(ticker)) {
                showManageStopLoss(req, resp, "");
            } else {
                resp.sendRedirect("/wallet");
            }
        } else {
            resp.sendRedirect("/wallet");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setStopLoss(req, resp);
    }

    private void showManageStopLoss(HttpServletRequest req, HttpServletResponse resp, String status) throws IOException {

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
        model.put("wpProfit", worstPerforming.get("profit"));
        model.put("wpReturn", worstPerforming.get("return"));
        model.put("mpProfit", bestPerforming.get("profit"));
        model.put("mpReturn", bestPerforming.get("return"));
        model.put("wpTicker", worstPerforming.get("ticker"));

        model.put("roe", roe);
        model.put("freeCash", freeCash);
        model.put("content", "set_sl");
        model.put("userName", user.getName());
        model.put("profilePicURL", profilePicURL);

        Template template = templateProvider.getTemplate(getServletContext(), "menu.ftlh");

        try {
            template.process(model, resp.getWriter());
        } catch (TemplateException e) {
            resp.getWriter().println("Something went wrong");
        }

    }

    private void setStopLoss(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        User user = userService.loggedUser(req);
        Wallet userWallet = user.getWallet();

        String ticker = req.getParameter("ticker");

//        if (validators.isTickerNotValid(ticker)) {
//            showManageStopLoss(req, resp, "Ticker = {" + ticker + "} is not valid");
//            logger.info("Ticker = {} is not valid.", ticker);
//            return;
//        }

        String priceStr = req.getParameter("price");

        if (validators.isNotDoubleOrIsSmallerThanZero(priceStr)) {
            showManageStopLoss(req, resp, "Price should have a numerical value greater than 0");
            logger.info("Incorrect price = {}", priceStr);
            return;
        }

        List<Share> shareList = userWallet.getShares();

        for (Share share : shareList) {
            if (share.getTicker().contains(ticker.toUpperCase())) {
                share.setStopLossPrice(BigDecimal.valueOf(Double.valueOf(priceStr)));
                logger.info("Set stop-loss price for share with id: {}", share.getId());
                shareDao.update(share);
                logger.info("Share with id: {} updated!", share.getId());
            }
        }
        showManageStopLoss(req, resp, "Stop-loss price is now set to: " + priceStr + " PLN");
    }

//    private void setTakeProfit(HttpServletRequest req, HttpServletResponse resp)
//            throws IOException {
//
//        User user = userService.loggedUser(req);
//        Wallet userWallet = user.getWallet();
//        Long id = userWallet.getId();
//
//        String userId = String.valueOf(req.getSession().getAttribute("user"));
//
//        if (validators.isUserNotAllowedToWalletModification(user.getId().toString(), id.toString())) {
//            showManageStopLoss(req, resp, "Unauthorized try to modify wallet!");
//            logger.info("Unauthorized try to modify wallet with id = {} by user with id = {}", id, user.getId());
//            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            return;
//        }
//
//        String ticker = req.getParameter("ticker");
//
//        if (validators.isTickerNotValid(ticker)) {
//            showManageStopLoss(req, resp, "Ticker = " + ticker + " is not valid");
//            logger.info("Ticker = {} is not valid.", ticker);
//            return;
//        }
//
//        String priceStr = req.getParameter("price");
//
//        if (validators.isNotDoubleOrIsSmallerThanZero(priceStr)) {
//            showManageStopLoss(req, resp, "Price should have a numerical value greater than 0");
//            logger.info("Incorrect price = {}", priceStr);
//            return;
//        }
//
//        final Wallet existingWallet = walletDao.findById(id);
//
//        List<Share> listFromExistingWallet = existingWallet.getShares();
//
//        for (Share share : listFromExistingWallet) {
//            if (share.getTicker().contains(ticker.toUpperCase())) {
//                share.setTakeProfitPrice(BigDecimal.valueOf(Double.valueOf(priceStr)));
//                logger.info("Set take-profit price for share with id: {}", share.getId());
//                shareDao.update(share);
//                logger.info("Share with id: {} updated!", share.getId());
//            }
//        }
//        showManageStopLoss(req, resp, "Take-profit price is now set to: " + priceStr + " PLN");
//    }
}
