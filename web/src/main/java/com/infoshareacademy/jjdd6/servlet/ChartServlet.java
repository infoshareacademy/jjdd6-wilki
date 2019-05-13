package com.infoshareacademy.jjdd6.servlet;

import com.infoshareacademy.jjdd6.service.ChartGenerator;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/chart")
public class ChartServlet extends HttpServlet {

    @Inject
    ChartGenerator chartGenerator;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String ticker = req.getParameter("ticker").toLowerCase();
        if (!ticker.isEmpty()) {
            resp.getWriter().print("<html>\n" +
                    "<head>\n" +
                    "  <meta charset=\"utf-8\">\n" +
                    "  <title>Wallet App</title>\n" +
                    "  <link rel=\"stylesheet\" type=\"text/css\" href=\"/css/style.css\"/>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<div id=\"container\" align=\"center\">\n" +
                    "<img src=\"" + req.getRequestURI() + "/images/" + chartGenerator.getChart(ticker) + "\" width=\"600\" height=\"300\"/>\n" +
                    "</div>\n" +
                    "</body>\n" +
                    "</html>");
        } else {
            resp.getWriter().print("Parameter ticker is required");
        }
    }
}
