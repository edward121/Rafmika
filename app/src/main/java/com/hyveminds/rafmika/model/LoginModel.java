package com.hyveminds.rafmika.model;

/**
 * Created by storm on 12/19/2016.
 */

public class LoginModel {
    private String sessionName;
    private String userId;
    private String version;
    private String vtigerVersion;

    public String getsessionName() {
        return sessionName;
    }

    public void setsessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVtigerVersion() {
        return vtigerVersion;
    }

    public void setVtigerVersion(String vtigerVersion) {
        this.vtigerVersion = vtigerVersion;
    }
}
