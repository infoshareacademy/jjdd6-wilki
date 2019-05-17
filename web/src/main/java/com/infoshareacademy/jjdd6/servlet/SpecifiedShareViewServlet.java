package com.infoshareacademy.jjdd6.servlet;

import com.infoshareacademy.jjdd6.freemarker.TemplateProvider;
import com.infoshareacademy.jjdd6.service.StatsService;
import com.infoshareacademy.jjdd6.service.UserService;
import com.infoshareacademy.jjdd6.validation.Validators;
import com.infoshareacademy.jjdd6.wilki.DownloadCurrentData;
import com.infoshareacademy.jjdd6.wilki.Share;
import com.infoshareacademy.jjdd6.wilki.User;
import com.infoshareacademy.jjdd6.wilki.Wallet;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class SpecifiedShareViewServlet extends HttpServlet {

    @Inject
    private TemplateProvider templateProvider;

    @Inject
    private UserService userService;

    @Inject
    private StatsService statsService;

    @Inject
    private Validators validators;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String ticker = req.getParameter("ticker");

        if(null != ticker){

            User user = userService.loggedUser(req);
            Wallet userWallet = user.getWallet();

            if(!(validators.isTickerNotValid(ticker)) && userWallet.checkIfShareIsPresent(ticker)) {
                showSpecifiedShare(req, resp, ticker);
            }
            else{
                resp.sendRedirect("/share-sell");
            }
        }
        showSpecifiedShare(req, resp, ticker);
    }

    private void showSpecifiedShare(HttpServletRequest req, HttpServletResponse resp, String ticker) throws IOException {
        User user = userService.loggedUser(req);
        Wallet userWallet = user.getWallet();
        Share share = userWallet.scanWalletForShare(ticker);
        BigDecimal roe = userWallet.getROE();
        BigDecimal freeCash = userWallet.getFreeCash();
        String profilePicURL = userService.userProfilePicURL(user);
        DownloadCurrentData.updateWalletData(userWallet);
        Map<String, Object> model = new HashMap<>();
        Map<String, String> bestPerforming = statsService.getMostProfitableShare(userWallet);
        Map<String, String> worstPerforming = statsService.getLeastProfitableShare(userWallet);
        model.put("mpTicker", bestPerforming.get("ticker"));
        model.put("mpReturn", bestPerforming.get("return"));
        model.put("wpTicker", worstPerforming.get("ticker"));
        model.put("wpReturn", worstPerforming.get("return"));
        model.put("roe", roe);
        model.put("freeCash", freeCash);
        model.put("content", "specified_share");
        model.put("profilePicURL", profilePicURL);
        model.put("userName", user.getName());

        Template template = templateProvider.getTemplate(getServletContext(), "menu.ftlh");

        try {
            template.process(model, resp.getWriter());
        } catch (TemplateException e) {
            resp.getWriter().println("Something went wrong");
        }
    }
}
