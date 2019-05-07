package com.infoshareacademy.jjdd6.servlet;

import com.infoshareacademy.jjdd6.dao.StatsDao;
import com.infoshareacademy.jjdd6.wilki.DataFromFile;
import com.infoshareacademy.jjdd6.wilki.DownloadCurrentData;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/stats")
@Transactional
public class StatsServlet extends HttpServlet {

    @Inject
    StatsDao statsDao;

    @Inject
    DownloadCurrentData downloadCurrentData;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().println("Most traded shares in App (buy transactions):");
        for (String mostBoughtStock : statsDao.getMostBoughtStocks()) {
            if (mostBoughtStock == "" || mostBoughtStock == null) {
                resp.getWriter().println("None");
            } else {
                resp.getWriter().println(mostBoughtStock);
            }
        }
        resp.getWriter().println("\nMost traded shares in App(sell transactions):");
        for (String mostSoldStock : statsDao.getMostSoldStocks()) {
            if (mostSoldStock == "" || mostSoldStock == null) {
                resp.getWriter().println("None");
            } else {
                resp.getWriter().println(mostSoldStock);
            }
        }

        resp.getWriter().println("\nMost 25 traded shares today on WSE (volume) [change]:");
        List<DataFromFile> statsWSE = downloadCurrentData.getMostTradedVolume();

        for (int i = 25; i < statsWSE.size(); i++) {
            resp.getWriter().println(statsWSE.get(i).getSymbol() + " ("
                    + statsWSE.get(i).getVolume() + ") ["
                    + statsWSE.get(i).getChange() + "]");

        }


    }
}
