package com.infoshareacademy.jjdd6.servlet;

import com.infoshareacademy.jjdd6.freemarker.TemplateProvider;
import com.infoshareacademy.jjdd6.service.ChartGenerator;
import com.infoshareacademy.jjdd6.service.StatsService;
import com.infoshareacademy.jjdd6.service.UserService;
import com.infoshareacademy.jjdd6.wilki.DownloadCurrentData;
import com.infoshareacademy.jjdd6.wilki.Share;
import com.infoshareacademy.jjdd6.wilki.User;
import com.infoshareacademy.jjdd6.wilki.Wallet;
import freemarker.template.Template;
import freemarker.template.TemplateException;

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
import java.util.List;
import java.util.Map;

@Transactional
@WebServlet("/stats")
public class StatsServlet extends HttpServlet {

    @Inject
    private ChartGenerator chartGenerator;

    @Inject
    private UserService userService;

    @Inject
    private StatsService statsService;

    @Inject
    private TemplateProvider templateProvider;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String chart = req.getParameter("chart");
        String view = req.getParameter("view");
        String forward;

        if (chart != null && chart.equals("appbuy")) {
            forward = "/images/" + chartGenerator.getMostTradedBuyChart();
            resp.sendRedirect(forward);

        } else if (chart != null && chart.equals("appsell")) {
            forward = "/images/" + chartGenerator.getMostTradedSellChart();
            resp.sendRedirect(forward);

        } else if (chart != null && chart.equals("profits")) {
            forward = "/images/" + chartGenerator.getMostProfitable(userService.loggedUser(req));
            resp.sendRedirect(forward);

        } else if (chart != null && chart.equals("losses")) {
            forward = "/images/" + chartGenerator.getSharesWithLosses(userService.loggedUser(req));
            resp.sendRedirect(forward);

        } else if (chart != null && chart.equals("wse")) {
            forward = "/images/" + chartGenerator.getMostTradedWSEChart();
            resp.sendRedirect(forward);
        }

        User user = userService.loggedUser(req);
        Wallet userWallet = user.getWallet();
        List<Share> shares = userWallet.walletToDisplay();
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
        model.put("shares", shares);
        model.put("roe", roe);
        model.put("freeCash", freeCash);
        model.put("profilePicURL", profilePicURL);
        model.put("userName", user.getName());

        if (view != null && view.equals("app")) {
            model.put("content", "app_stats");
        } else if (view != null && view.equals("wallet")) {
            model.put("content", "wallet_stats");
        } else if (view != null && view.equals("wse")) {
            model.put("content", "wse_stats");
        } else {
            resp.sendRedirect("/wallet");
        }

        Template template = templateProvider.getTemplate(getServletContext(), "menu.ftlh");

        try {
            template.process(model, resp.getWriter());
        } catch (TemplateException e) {
            resp.getWriter().println("Something went wrong");
        }
    }
}