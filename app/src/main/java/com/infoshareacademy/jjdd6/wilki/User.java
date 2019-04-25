package com.infoshareacademy.jjdd6.wilki;

import javax.persistence.*;

@Entity
@Table(name = "USER")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email")
    private String email;

    @OneToOne
    @JoinColumn(name = "wallet_id", unique = true)
    private Wallet wallet;
}
