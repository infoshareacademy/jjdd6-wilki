package com.infoshareacademy.jjdd6.servlet;

import com.infoshareacademy.jjdd6.freemarker.TemplateProvider;
import com.infoshareacademy.jjdd6.service.UserService;
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
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/wallet")
public class ShowWalletServlet extends HttpServlet {

    @Inject
    TemplateProvider templateProvider;

    @Inject
    UserService userService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        User user = userService.loggedUser(req);

        Wallet userWallet = user.getWallet();

        List<Share> shares = userWallet.getShares();

        BigDecimal roe = userWallet.getROE();

        BigDecimal freeCash = userWallet.getFreeCash();

        Map<String, Object> model = new HashMap<>();
        model.put("shares", shares);
        model.put("roe", roe);
        model.put("freeCash", freeCash);
        model.put("content", 1);

        Template template = templateProvider.getTemplate(getServletContext(), "menu.ftlh");

        try {
            template.process(model, resp.getWriter());
        } catch (TemplateException e) {
            resp.getWriter().println("Something went wrong");
        }

    }
}
