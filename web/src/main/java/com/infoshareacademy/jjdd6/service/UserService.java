package com.infoshareacademy.jjdd6.service;

import com.infoshareacademy.jjdd6.dao.UserDao;
import com.infoshareacademy.jjdd6.wilki.User;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

@RequestScoped
@Transactional
public class UserService {

    @Inject
    UserDao userDao;

    public User loggedUser(HttpServletRequest req) {
        HttpSession session = req.getSession();
        Long userId = (Long) session.getAttribute("user");
        return userDao.findById(userId);


    }

}
