package com.hyveminds.rafmika.response;

import com.hyveminds.rafmika.model.Event;

import java.util.List;

/**
 * Created by storm on 12/20/2016.
 */

public class GetEventResponse extends BaseResponse {
    private List<Event> result;

    public List<Event> getResult() {
        return result;
    }

    public void setResult(List<Event> result) {
        this.result = result;
    }
}
