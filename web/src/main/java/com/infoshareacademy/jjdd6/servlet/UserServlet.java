package com.infoshareacademy.jjdd6.servlet;

import com.infoshareacademy.jjdd6.dao.UserDao;
import com.infoshareacademy.jjdd6.dao.WalletDao;
import com.infoshareacademy.jjdd6.wilki.User;
import com.infoshareacademy.jjdd6.wilki.Wallet;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = "/user")
@Transactional
public class UserServlet extends HttpServlet {

    private Logger LOG = LoggerFactory.getLogger(UserServlet.class);
    @Inject
    UserDao userDao;

    @Inject
    WalletDao walletDao;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        final String action = getAction(req, resp);
        if (action == null) {
            return;
        }

        if (action.equals("findAll")) {
            findAllUsers(req, resp);
        } else if (action.equals("add")) {
            addUser(req, resp);
        } else if (action.equals("delete")) {
            deleteUser(req, resp);
        } else if (action.equals("update")) {
            updateUser(req, resp);
        } else {
            resp.getWriter().write("Unknown action.");
        }
    }

    private String getAction(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final String action = req.getParameter("action");
        LOG.info("Requested action: {}", action);
        if (action == null || action.isEmpty()) {
            resp.getWriter().write("Empty action parameter.");
        }
        return action;
    }

    private void updateUser(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String idStr = req.getParameter("id");
        if (!NumberUtils.isDigits(idStr)) {
            resp.getWriter().println("User id should be an integer");
            return;
        }
        final Long id = Long.parseLong(idStr);
        LOG.info("Updating user with id = {}", id);

        final User existingUser = userDao.findById(id);
        if (existingUser == null) {
            LOG.info("No User found for id = {}, nothing to be updated", id);
            return;
        }

        existingUser.setEmail(req.getParameter("email"));

        String walletIdStr = req.getParameter("wallet-id");
        if (!NumberUtils.isDigits(walletIdStr)) {
            resp.getWriter().println("Wallet id should be an integer");
            return;
        }
        Long walletId = Long.parseLong(walletIdStr);
        Wallet wallet = walletDao.findById(walletId);
        if (wallet == null) {
            LOG.info("No wallet found for id = {}, nothing to be updated", id);
        } else {
            existingUser.setWallet(wallet);
        }

        userDao.update(existingUser);
        LOG.info("User updated: {}", existingUser);

        findAllUsers(req, resp);
    }

    private void addUser(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        final User user = new User();
        user.setEmail(req.getParameter("email"));
        String walletIdStr = req.getParameter("wallet-id");
        if (!NumberUtils.isDigits(walletIdStr)) {
            resp.getWriter().println("Wallet id should be an integer");
            return;
        }
        Long walletId = Long.parseLong(walletIdStr);
        Wallet wallet = walletDao.findById(walletId);
        if (wallet != null) {
            user.setWallet(wallet);
        } else {
            resp.getWriter().println("There is no wallet with such id");
        }

        userDao.save(user);
        LOG.info("Saved a new User object: {}", user);

        findAllUsers(req, resp);
    }

    private void deleteUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String idStr = req.getParameter("id");
        if (!NumberUtils.isDigits(idStr)) {
            resp.getWriter().println("User id should be an integer");
            return;
        }
        final Long id = Long.parseLong(req.getParameter("id"));
        if (userDao.findById(id) != null) {
            LOG.info("Removing User with id = {}", id);
            userDao.delete(id);
        } else {
            resp.getWriter().println("There is no user with id = " + id);
        }

        findAllUsers(req, resp);
    }

    private void findAllUsers(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final List<User> result = userDao.findAll();
        LOG.info("Found {} objects", result.size());
        for (User user : result) {
            resp.getWriter().write(user.toString() + "\n");
        }
    }
}
