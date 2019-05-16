package com.infoshareacademy.jjdd6.servlet;

import com.infoshareacademy.jjdd6.freemarker.TemplateProvider;
import com.infoshareacademy.jjdd6.service.UserService;
import com.infoshareacademy.jjdd6.wilki.User;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@WebServlet("/admin")
public class AdminServlet extends HttpServlet {

    @Inject
    private UserService userService;

    @Inject
    private TemplateProvider templateProvider;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = userService.loggedUser(req);
//        if (!user.isAdmin()) {
//            resp.setStatus(403);
//            return;
//        }
        List<User> userList = userService.getAllUsers();

        Map<String, Object> model = new HashMap<>();
        model.put("users", userList);

        Template template = templateProvider.getTemplate(getServletContext(), "adminpanel.ftlh");

        try {
            template.process(model, resp.getWriter());
        } catch (TemplateException e) {
            resp.getWriter().println("Something went wrong");
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }
}
