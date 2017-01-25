package com.hyveminds.rafmika.response;

import com.hyveminds.rafmika.model.LoginModel;

/**
 * Created by storm on 12/19/2016.
 */

public class LoginResponse extends BaseResponse {
    private LoginModel result;

    public LoginModel getResult() {
        return result;
    }

    public void setResult(LoginModel result) {
        this.result = result;
    }
}
