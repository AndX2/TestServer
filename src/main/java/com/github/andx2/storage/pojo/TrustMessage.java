package com.github.andx2.storage.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by savos on 30.10.2016.
 */
@Entity
public class TrustMessage {
    @Id
    @GeneratedValue
    private long id;
    private String tokenUser;
    private String eMail;
    private String trustToken;

    public TrustMessage() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTokenUser() {
        return tokenUser;
    }

    public void setTokenUser(String tokenUser) {
        this.tokenUser = tokenUser;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getTrustToken() {
        return trustToken;
    }

    public void setTrustToken(String trustToken) {
        this.trustToken = trustToken;
    }
}
