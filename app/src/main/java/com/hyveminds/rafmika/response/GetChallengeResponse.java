package com.hyveminds.rafmika.response;

import com.hyveminds.rafmika.model.Challenge;

/**
 * Created by storm on 12/19/2016.
 */

public class GetChallengeResponse extends BaseResponse {
    private Challenge result;

    public Challenge getResult() {
        return result;
    }

    public void setResult(Challenge result) {
        this.result = result;
    }
}
