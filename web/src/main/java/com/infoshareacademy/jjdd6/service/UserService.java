package com.infoshareacademy.jjdd6.service;

import com.infoshareacademy.jjdd6.dao.FacebookTokenDao;
import com.infoshareacademy.jjdd6.dao.UserDao;
import com.infoshareacademy.jjdd6.dao.WalletDao;
import com.infoshareacademy.jjdd6.wilki.FacebookToken;
import com.infoshareacademy.jjdd6.wilki.FacebookUser;
import com.infoshareacademy.jjdd6.wilki.User;
import com.infoshareacademy.jjdd6.wilki.Wallet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RequestScoped
@Transactional
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Inject
    private UserDao userDao;

    @Inject
    private FacebookTokenDao facebookTokenDao;

    @Inject
    private WalletDao walletDao;

    @Inject
    UserService userService;

    public User loggedUser(HttpServletRequest req) {
        HttpSession session = req.getSession();
        logger.info("Setting 'user' session attribute for sessionId " + session.getId());
        Object userIdObject = session.getAttribute("user");
        logger.info("userIdObject: " + userIdObject);
        Long userId = (Long) userIdObject;
        logger.info("userId: " + userId);
        return userDao.findById(userId);
    }

    public String userProfilePicURL(User user) {
        return "https://graph.facebook.com/v3.3/" + user.getFbUserId() + "/picture";
    }

    public void setupUser(HttpSession session, FacebookToken userToken, FacebookUser facebookUser, List<User> userList) {
        if (userList.isEmpty()) {
            User user = new User();
            user.setEmail(facebookUser.getEmail());
            user.setFbUserId(facebookUser.getId());
            user.setName(facebookUser.getName().split(" ")[0]);
            user.setSurname(facebookUser.getName().split(" ")[1]);
            userToken.setExpireDate(LocalDateTime.now()
                    .plusSeconds(userToken.getExpirationSeconds()));
            user.setUserToken(userToken);
            facebookTokenDao.save(userToken);
            final Wallet wallet = new Wallet();
            user.setWallet(wallet);
            walletDao.save(wallet);
            userDao.save(user);
            setSessionAttributes(session, user);
        } else {
            User user = userList.get(0);
            setSessionAttributes(session, user);
            if (user.getEmail() != null && facebookUser.getEmail() != null && !user.getEmail().equals(facebookUser.getEmail())) {
                user.setEmail(facebookUser.getEmail());
            }
            if (!user.getName().equals(facebookUser.getName())) {
                user.setName(facebookUser.getName());
            }
            if (!user.getUserToken().getAccessToken().equals(userToken.getAccessToken())) {
                user.setUserToken(userToken);
            }
        }
    }

    private void setSessionAttributes(HttpSession session, User user) {
        logger.info("Setting 'user' session attribute for sessionId " + session.getId());
        session.setAttribute("user", user.getId());
        session.setAttribute("tokenExpireDate", user.getUserToken().getExpireDate());
        logger.info("Logged in user id: " + user.getId() + " name: " + user.getName() + " " + user.getSurname() + " token expires on: " + user.getUserToken().getExpireDate());
    }

    public void logoutUser(HttpServletRequest req) {
        String userId = userService.loggedUser(req).getFbUserId();
        String token = userService.loggedUser(req).getUserToken().getAccessToken();
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL("https://graph.facebook.com/" + userId + "/permissions?access_token=" + token);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpURLConnection.setRequestMethod("DELETE");
            logger.info(String.valueOf(httpURLConnection.getResponseCode()));
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            req.getSession().invalidate();
        }
    }

    public User findById(Long id) {
        return userDao.findById(id);
    }

    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    public void saveUser(User user) {
        userDao.save(user);
    }

    public User updateUser(User user) {
        return userDao.update(user);
    }


    public List<User> findByFbUserId(FacebookUser facebookUser) {
        return userDao.findByFbUserId(facebookUser.getId());
    }
}
