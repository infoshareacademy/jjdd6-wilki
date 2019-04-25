package com.infoshareacademy.jjdd6.servlet;

import com.infoshareacademy.jjdd6.wilki.Share;
import com.infoshareacademy.jjdd6.wilki.Wallet;
import com.infoshareacademy.jjdd6.wilki.WalletInitializer;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@WebServlet("/wallet")
public class ShowWalletServlet extends HttpServlet {

    @EJB
    private WalletInitializer walletInitializer;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {

        Wallet wallet = walletInitializer.init();

        List<Share> shares = wallet.getShares();

    }
}
