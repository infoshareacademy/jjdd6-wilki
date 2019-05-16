package com.infoshareacademy.jjdd6.servlet;

import com.infoshareacademy.jjdd6.freemarker.TemplateProvider;
import com.infoshareacademy.jjdd6.service.StatsService;
import com.infoshareacademy.jjdd6.service.UserService;
import com.infoshareacademy.jjdd6.wilki.DownloadCurrentData;
import com.infoshareacademy.jjdd6.wilki.Share;
import com.infoshareacademy.jjdd6.wilki.User;
import com.infoshareacademy.jjdd6.wilki.Wallet;
import freemarker.template.Template;
import freemarker.template.TemplateException;
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
@WebServlet("/wallet")
public class ShowWalletServlet extends HttpServlet {

    @Inject
    private TemplateProvider templateProvider;

    @Inject
    private UserService userService;

    @Inject
    private StatsService statsService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

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
        model.put("mpTicker", bestPerforming.get("ticker"));
        model.put("mpProfit", bestPerforming.get("profit"));
        model.put("mpReturn", bestPerforming.get("return"));
        model.put("wpTicker", worstPerforming.get("ticker"));
        model.put("wpProfit", worstPerforming.get("profit"));
        model.put("wpReturn", worstPerforming.get("return"));
        model.put("shares", shares);
        model.put("roe", roe);
        model.put("freeCash", freeCash);
        model.put("content", "show_wallet");
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
