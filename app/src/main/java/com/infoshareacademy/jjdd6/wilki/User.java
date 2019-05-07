package com.infoshareacademy.jjdd6.wilki;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "USER")
@NamedQueries({@NamedQuery(
        name = "User.findAll",
        query = "SELECT u FROM User u"),
        @NamedQuery(name = "User.findUserByEmail",
                query = "SELECT u FROM User u WHERE u.email = :email"),
        @NamedQuery(name = "User.findUserByFbUserId",
                query = "SELECT u FROM User u WHERE u.fbUserId = :fbUserId")})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email")
    @NotNull
    private String email;

    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "fb_user_id")
    @NotNull
    private String fbUserId;

    @OneToOne
    @JoinColumn(name = "wallet_id", unique = true)
    private Wallet wallet;

    @OneToOne
    @JoinColumn(name = "user_token_id", unique = true)
    private FacebookToken userToken;

    public User(String email, Wallet wallet) {
        this.email = email;
        this.wallet = wallet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFbUserId() {
        return fbUserId;
    }

    public void setFbUserId(String fbUserId) {
        this.fbUserId = fbUserId;
    }

    public FacebookToken getUserToken() {
        return userToken;
    }

    public void setUserToken(FacebookToken userToken) {
        this.userToken = userToken;
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", wallet=" + wallet +
                '}';
    }
}
