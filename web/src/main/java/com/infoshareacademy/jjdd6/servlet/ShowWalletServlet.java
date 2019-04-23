package com.infoshareacademy.jjdd6.servlet;

import com.infoshareacademy.jjdd6.wilki.Share;
import com.infoshareacademy.jjdd6.wilki.Wallet;
import com.infoshareacademy.jjdd6.wilki.WalletInitializer;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.List;

@WebServlet("/show-wallet")
public class ShowWalletServlet extends HttpServlet {

    @EJB
    private WalletInitializer walletInitializer;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        Wallet wallet = walletInitializer.init();

        List<Share> shares = wallet.getShares();

        PrintWriter printWriter = resp.getWriter();

        for (Share share : shares) {
            DecimalFormat df = new DecimalFormat("0.00");

            printWriter.println(share.getTicker());
            printWriter.println(share.getSharesTotalAmount());
            printWriter.println(share.getAvgBuyPrice() + " pln");
            printWriter.println(share.getBaseValue() + " pln");
            printWriter.println(share.getCurrentPrice() + " pln");
            printWriter.println(share.getCurrentValue() + " pln");
            printWriter.println(df.format(((share.getCurrentValue().doubleValue() / (share.getBaseValue()).doubleValue()) - 1) * 100) + " %");
            printWriter.println(share.getStopLossPrice() + " pln");
            printWriter.println(share.getTakeProfitPrice() + " pln");
            printWriter.println("---------");
        }
    }
}
