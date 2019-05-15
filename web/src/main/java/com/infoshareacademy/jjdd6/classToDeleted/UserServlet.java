//package com.infoshareacademy.jjdd6.servlet;
//
//import com.infoshareacademy.jjdd6.dao.UserDao;
//import com.infoshareacademy.jjdd6.dao.WalletDao;
//import com.infoshareacademy.jjdd6.validation.Validators;
//import com.infoshareacademy.jjdd6.wilki.User;
//import com.infoshareacademy.jjdd6.wilki.Wallet;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import javax.inject.Inject;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.transaction.Transactional;
//import javax.websocket.Session;
//import java.io.IOException;
//import java.math.BigDecimal;
//import java.util.List;
//
//@WebServlet(urlPatterns = "/user")
//@Transactional
//public class UserServlet extends HttpServlet {
//
//    private static Logger logger = LoggerFactory.getLogger(UserServlet.class);
//
//    @Inject
//    private UserDao userDao;
//
//    @Inject
//    private WalletDao walletDao;
//
//    @Inject
//    private Validators validators;
//
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
//            throws IOException {
//
//        final String action = getAction(req, resp);
//        if (action == null) {
//            return;
//        }
//
//        if (action.equals("findAll")) {
//            findAllUsers(req, resp);
//        } else if (action.equals("add")) {
//            addUser(req, resp);
//        } else if (action.equals("delete")) {
//            deleteUser(req, resp);
//        } else if (action.equals("update")) {
//            updateUser(req, resp);
//        } else {
//            resp.getWriter().write("Unknown action.");
//        }
//    }
//
//    private String getAction(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        final String action = req.getParameter("action");
//        logger.info("Requested action: {}", action);
//        if (action == null || action.isEmpty()) {
//            resp.getWriter().write("Empty action parameter.");
//        }
//        return action;
//    }
//
//    private void updateUser(HttpServletRequest req, HttpServletResponse resp)
//            throws IOException {
//
//        String idStr = req.getParameter("id");
//
//        if (validators.isNotIntegerOrIsSmallerThanZero(idStr)) {
//            resp.getWriter().println("User id should be an integer greater than 0");
//            logger.info("Incorrect user id = {}", idStr);
//            return;
//        }
//
//        if (validators.isIdNotPresent(idStr)) {
//            resp.getWriter().println("No User found");
//            logger.info("No User found for id = {}, nothing to be updated", idStr);
//            return;
//        }
//
//        final Long id = Long.parseLong(idStr);
//        logger.info("Updating user with id = {}", id);
//
//        final User existingUser = userDao.findById(id);
//
//        String email = req.getParameter("email");
//        if (validators.isEmailIncorrect(email)) {
//            logger.info("Incorrect email = {} " + email);
//            return;
//        }
//        existingUser.setEmail(email);
//
//        String walletIdStr = req.getParameter("wallet_id");
//
//        if (validators.isNotIntegerOrIsSmallerThanZero(walletIdStr)) {
//            resp.getWriter().println("Wallet id should be an integer greater than 0");
//            logger.info("Incorrect wallet id = {}", idStr);
//            return;
//        }
//
//        if (!validators.isWalletNotPresent(walletIdStr)) {
//            resp.getWriter().println("No wallet found");
//            logger.info("No wallet found for id = {}, nothing to be updated", idStr);
//            return;
//        }
//
//        Long walletId = Long.parseLong(walletIdStr);
//        Wallet wallet = walletDao.findById(walletId);
//        existingUser.setWallet(wallet);
//
//        userDao.update(existingUser);
//        logger.info("User updated: {}", existingUser);
//
//        findAllUsers(req, resp);
//    }
//
//    private void addUser(HttpServletRequest req, HttpServletResponse resp)
//            throws IOException {
//
//        final Wallet wallet = new Wallet();
//        wallet.setBaseCash(BigDecimal.valueOf(0.00));
//        walletDao.save(wallet);
//        logger.info("Saved a new wallet object: {}", wallet);
//
//        final User user = new User();
//        String email = req.getParameter("email");
//
//        if (validators.isEmailIncorrect(email)) {
//            logger.info("Incorrect email = {} " + email);
//            return;
//        }
//
//        if (validators.isEmailPresent(email)) {
//            resp.getWriter().println("Email = {} already exist" + email);
//            logger.info("Email = {} already exist" + email);
//            return;
//        }
//        user.setEmail(email);
//        user.setWallet(wallet);
//
//        userDao.save(user);
//        logger.info("Saved a new User object: {}", user);
//
//        findAllUsers(req, resp);
//    }
//
//    private void deleteUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//
//        String idStr = req.getParameter("id");
//
//        if (validators.isNotIntegerOrIsSmallerThanZero(idStr)) {
//            resp.getWriter().println("User id should be an integer greater than 0");
//            logger.info("Incorrect user id = {}", idStr);
//            return;
//        }
//
//        if (validators.isIdNotPresent(idStr)) {
//            resp.getWriter().println("No User found");
//            logger.info("No User found for id = {}, nothing to be updated", idStr);
//            return;
//        }
//
//        final Long id = Long.parseLong(req.getParameter("id"));
//        userDao.delete(id);
//        logger.info("Removing User with id = {}", id);
//
//        findAllUsers(req, resp);
//    }
//
//    private void findAllUsers(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        final List<User> result = userDao.findAll();
//        logger.info("Found {} objects", result.size());
//        for (User user : result) {
//            resp.getWriter().write(user.toString() + "\n");
//        }
//    }
//}
