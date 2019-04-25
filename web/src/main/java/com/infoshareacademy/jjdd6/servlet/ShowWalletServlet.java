package com.infoshareacademy.jjdd6.servlet;

import com.infoshareacademy.jjdd6.freemarker.TemplateProvider;
import com.infoshareacademy.jjdd6.wilki.Share;
import com.infoshareacademy.jjdd6.wilki.Wallet;
import com.infoshareacademy.jjdd6.wilki.WalletInitializer;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/wallet")
public class ShowWalletServlet extends HttpServlet {

    @EJB
    private WalletInitializer walletInitializer;

    @Inject
    TemplateProvider templateProvider;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        Wallet wallet = walletInitializer.init();

        List<Share> shares = wallet.getShares();

        Map<String, Object> model = new HashMap<>();
        model.put("shares", shares);

        Template template = templateProvider.getTemplate(getServletContext(), "show-owned-shares.ftlh");

        try {
            template.process(model, resp.getWriter());
        } catch (TemplateException e) {
            resp.getWriter().println("Something went wrong");
        }

    }
}
