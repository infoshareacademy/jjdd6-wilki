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
        if (!user.isAdmin()) {
            resp.sendRedirect("/wallet");
            return;
        }

        String selectedUserStr = req.getParameter("selectedUser");

        if (selectedUserStr != null && !selectedUserStr.isEmpty()) {
            User selectedUser = userService.findById(Long.parseLong(selectedUserStr));
            String profilePicURL = userService.userProfilePicURL(selectedUser);
            Map<String, Object> model = new HashMap<>();
            Integer admin = 0;
            if (selectedUser.isAdmin()) {
                admin = 1;
            }
            model.put("user", selectedUser);
            model.put("profilePicURL", profilePicURL);
            model.put("isAdmin", admin);

            Template template = templateProvider.getTemplate(getServletContext(), "userDetailsPanel.ftlh");

            try {
                template.process(model, resp.getWriter());
            } catch (TemplateException e) {
                resp.getWriter().println("Something went wrong");
            }
        } else {

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
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = userService.findById(Long.parseLong(req.getParameter("userId")));
        user.setName(req.getParameter("name"));
        user.setSurname(req.getParameter("surname"));
        if (req.getParameter("email") != null && !req.getParameter("email").equals("")) {
            user.setEmail(req.getParameter("email"));
        }
        if (req.getParameter("isAdmin") != null && req.getParameter("isAdmin").equals("admin")) {
            user.setIsAdmin(true);
        } else {
            user.setIsAdmin(false);
        }
        userService.updateUser(user);
        doGet(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }
}
