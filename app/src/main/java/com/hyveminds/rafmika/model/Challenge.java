package com.hyveminds.rafmika.model;

/**
 * Created by storm on 12/19/2016.
 */

public class Challenge {
    private String token;
    private String serverTime;
    private String expireTime;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getServerTime() {
        return serverTime;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }
}
