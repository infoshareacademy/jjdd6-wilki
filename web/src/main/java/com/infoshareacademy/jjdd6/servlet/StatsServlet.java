package com.infoshareacademy.jjdd6.servlet;

import com.infoshareacademy.jjdd6.service.StatsService;
import com.infoshareacademy.jjdd6.service.UserService;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;

@Transactional
@WebServlet("/stats")
public class StatsServlet extends HttpServlet {

    @Inject
    StatsService statsService;

    @Inject
    UserService userService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().println("Most traded shares in App (buy transactions):");
        resp.getWriter().println(statsService.getMostBoughtStocks());

        resp.getWriter().println("\nMost traded shares in App(sell transactions):");
        resp.getWriter().println(statsService.getMostSoldStocks());

        resp.getWriter().println("\nMost 25 traded shares today on WSE (volume) [change]:");
        resp.getWriter().println(statsService.getMostTradedOnWse());

        resp.getWriter().println("\nMost profitable share in wallet:");
        resp.getWriter().println(statsService.getMostProfitableShare(userService.loggedUser(req).getWallet()));

        resp.getWriter().println("\nLeast profitable share in wallet:");
        resp.getWriter().println(statsService.getLeastProfitableShare(userService.loggedUser(req).getWallet()));
    }
}