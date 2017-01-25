package com.hyveminds.rafmika.model;

/**
 * Created by storm on 12/19/2016.
 */

public class ErrorModel {
    private String message;
    private String code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
