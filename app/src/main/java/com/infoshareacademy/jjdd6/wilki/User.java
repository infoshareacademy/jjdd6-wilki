package com.infoshareacademy.jjdd6.wilki;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "USER")
@NamedQueries({@NamedQuery(
        name = "User.findAll",
        query = "SELECT u FROM User u"
)})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email")
    @NotNull
    private String email;

    @OneToOne
    @JoinColumn(name = "wallet_id", unique = true)
    private Wallet wallet;

    public User(String email, Wallet wallet) {
        this.email = email;
        this.wallet = wallet;
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
