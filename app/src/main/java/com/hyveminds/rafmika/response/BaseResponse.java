package com.hyveminds.rafmika.response;

import com.hyveminds.rafmika.model.ErrorModel;

/**
 * Created by storm on 12/19/2016.
 */

public class BaseResponse {
    protected boolean success;
    protected ErrorModel error;


    public boolean getSuccess()
    {
        return this.success;
    }

    public boolean isSuccessful()
    {
        return this.success == true;
    }

    public void setStatus(boolean paramString)
    {
        this.success = paramString;
    }

    public ErrorModel getError() {
        return error;
    }

    public void setError(ErrorModel error) {
        this.error = error;
    }
}
