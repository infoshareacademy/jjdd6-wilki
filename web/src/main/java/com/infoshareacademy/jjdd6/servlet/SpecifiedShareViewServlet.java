package com.infoshareacademy.jjdd6.servlet;

import com.infoshareacademy.jjdd6.freemarker.TemplateProvider;
import com.infoshareacademy.jjdd6.service.StatsService;
import com.infoshareacademy.jjdd6.service.UserService;
import com.infoshareacademy.jjdd6.validation.Validators;
import com.infoshareacademy.jjdd6.wilki.*;
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
import java.util.Map;

@WebServlet("/details")
@Transactional
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
                resp.sendRedirect("/wallet");
            }
        }else{
            resp.sendRedirect("/wallet");
        }
    }

    private void showSpecifiedShare(HttpServletRequest req, HttpServletResponse resp, String ticker) throws IOException {

        User user = userService.loggedUser(req);
        Wallet userWallet = user.getWallet();
        Share share = userWallet.scanWalletForShare(ticker);

        String tickerFromWallet = share.getTicker();
        Integer amount = share.getSharesTotalAmount();
        BigDecimal avgBuyPrice = share.getAvgBuyPrice();
        BigDecimal value = share.getBaseValue();
        BigDecimal currentPrice = share.getCurrentPrice();
        BigDecimal currentValue = share.getCurrentValue();
        BigDecimal currentReturn = share.getCurrentReturn();
        BigDecimal stopLoss = share.getStopLossPrice();
        BigDecimal takeProfit = share.getTakeProfitPrice();
        String fullName = share.getFullCompanyName();
        BigDecimal takeProfitValue = share.getTakeProfitValue();
        BigDecimal stopLossValue = share.getStopLossValue();
        Double currentPE = share.getCurrentPE();
        Double targetPE = share.getTargetPE();
        BigDecimal feeAmount = share.getFeeAmount();
        Double riskReward = share.getRiskRewardRatio();
        Long volume = share.getVolume();


        BigDecimal roe = userWallet.getROE();
        BigDecimal freeCash = userWallet.getFreeCash();
        String profilePicURL = userService.userProfilePicURL(user);

        DownloadCurrentData.updateWalletData(userWallet);

        Map<String, String> bestPerforming = statsService.getMostProfitableShare(userWallet);
        Map<String, String> worstPerforming = statsService.getLeastProfitableShare(userWallet);

        Map<String, Object> model = new HashMap<>();

        model.put("takeProfitValue", takeProfitValue);
        model.put("stopLossValue", stopLossValue);
        model.put("currentPE", currentPE);
        model.put("targetPE", targetPE);
        model.put("feeAmount", feeAmount);
        model.put("riskReward", riskReward);
        model.put("volume", volume);
        model.put("ticker", tickerFromWallet);
        model.put("amount", amount);
        model.put("avgBuyPrice", avgBuyPrice);
        model.put("value", value);
        model.put("currentPrice", currentPrice);
        model.put("currentValue", currentValue);
        model.put("currentReturn", currentReturn);
        model.put("stopLoss", stopLoss);
        model.put("takeProfit", takeProfit);
        model.put("fullName", fullName);
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
