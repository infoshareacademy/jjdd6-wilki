package com.infoshareacademy.jjdd6.wilki;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name = "TOKENS")
@NamedQueries({@NamedQuery(
        name = "Tokens.findAll",
        query = "SELECT t FROM FacebookToken t")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class FacebookToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonIgnore
    private Long id;

    @Column(name = "access_token")
    @JsonProperty("access_token")
    private String accessToken;

    @Column(name = "token_type")
    @JsonProperty("token_type")
    private String tokenType;

    @Column(name = "expires_in")
    @JsonProperty("expires_in")
    private Long expirationSeconds;

    public FacebookToken() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Long getExpirationSeconds() {
        return expirationSeconds;
    }

    public void setExpirationSeconds(Long expirationSeconds) {
        this.expirationSeconds = expirationSeconds;
    }
}
