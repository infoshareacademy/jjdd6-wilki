package com.infoshareacademy.jjdd6.servlet;

import com.infoshareacademy.jjdd6.dao.ShareDao;
import com.infoshareacademy.jjdd6.dao.StatsDao;
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

@WebServlet(urlPatterns = {"/sl-and-tp", "/sl", "/tp"})
@Transactional
public class SetSlAndTpPriceServlet extends HttpServlet {

    private static Logger logger = LoggerFactory.getLogger(SetSlAndTpPriceServlet.class);

    @Inject
    private WalletDao walletDao;

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

    @Inject
    private TickerDao tickerDao;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        showManageTpAndSl(req, resp, "");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final String action = req.getParameter("action");
        logger.info("Requested action: {}", action);
        if (action == null) {
            return;
        }
        if (action.equals("sl")) {
            setStopLoos(req, resp);
        } else if (action.equals("tp")) {
            setTakeProfit(req, resp);
        } else {
            showManageTpAndSl(req, resp, "Unknown action.");
        }
    }

    private void showManageTpAndSl(HttpServletRequest req, HttpServletResponse resp, String status) throws IOException {

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
        model.put("content", "setTpSl");
        model.put("userName", user.getName());
        model.put("profilePicURL", profilePicURL);

        Template template = templateProvider.getTemplate(getServletContext(), "menu.ftlh");

        try {
            template.process(model, resp.getWriter());
        } catch (TemplateException e) {
            resp.getWriter().println("Something went wrong");
        }

    }

    private void setStopLoos(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        User user = userService.loggedUser(req);
        Wallet userWallet = user.getWallet();
        Long id = userWallet.getId();


 //       String userId = String.valueOf(req.getSession().getAttribute("user"));
        if (validators.isUserNotAllowedToWalletModification(user.getId().toString(), id.toString())) {
            showManageTpAndSl(req, resp, "Unauthorized try to modify wallet!");
            logger.info("Unauthorized try to modify wallet with id = {} by user with id = {}", id.toString(), user.getId());
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String ticker = req.getParameter("ticker");

        if (validators.isTickerNotValid(ticker)) {
            showManageTpAndSl(req, resp, "Ticker = {" + ticker + "} is not valid");
            logger.info("Ticker = {} is not valid.", ticker);
            return;
        }

        String priceStr = req.getParameter("price");

        if (validators.isNotDoubleOrIsSmallerThanZero(priceStr)) {
            showManageTpAndSl(req, resp, "Price should have a numerical value greater than 0");
            logger.info("Incorrect price = {}", priceStr);
            return;
        }
        final Wallet existingWallet = walletDao.findById(id);

        List<Share> listFromExistingWallet = existingWallet.getShares();

        for (Share share : listFromExistingWallet) {
            if (share.getTicker().contains(ticker.toUpperCase())) {
                share.setStopLossPrice(BigDecimal.valueOf(Double.valueOf(priceStr)));
                logger.info("Set stop-loss price for share with id: {}", share.getId());
                shareDao.update(share);
                logger.info("Share with id: {} updated!", share.getId());
            }
        }
        showManageTpAndSl(req, resp, "Stop-loss price is now set to: " + priceStr + " PLN");
    }

    private void setTakeProfit(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        User user = userService.loggedUser(req);
        Wallet userWallet = user.getWallet();
        Long id = userWallet.getId();

//        String userId = String.valueOf(req.getSession().getAttribute("user"));

        if (validators.isUserNotAllowedToWalletModification(user.getId().toString(), id.toString())) {
            showManageTpAndSl(req, resp, "Unauthorized try to modify wallet!");
            logger.info("Unauthorized try to modify wallet with id = {} by user with id = {}", id, user.getId());
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String ticker = req.getParameter("ticker");

        if (validators.isTickerNotValid(ticker)) {
            showManageTpAndSl(req, resp, "Ticker = " + ticker + " is not valid");
            logger.info("Ticker = {} is not valid.", ticker);
            return;
        }

        String priceStr = req.getParameter("price");

        if (validators.isNotDoubleOrIsSmallerThanZero(priceStr)) {
            showManageTpAndSl(req, resp, "Price should have a numerical value greater than 0");
            logger.info("Incorrect price = {}", priceStr);
            return;
        }

        final Wallet existingWallet = walletDao.findById(id);

        List<Share> listFromExistingWallet = existingWallet.getShares();

        for (Share share : listFromExistingWallet) {
            if (share.getTicker().contains(ticker.toUpperCase())) {
                share.setTakeProfitPrice(BigDecimal.valueOf(Double.valueOf(priceStr)));
                logger.info("Set take-profit price for share with id: {}", share.getId());
                shareDao.update(share);
                logger.info("Share with id: {} updated!", share.getId());
            }
        }
        showManageTpAndSl(req, resp, "Take-profit price is now set to: " + priceStr + " PLN");
    }
}
