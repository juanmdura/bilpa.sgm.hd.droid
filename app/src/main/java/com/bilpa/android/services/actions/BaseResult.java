package com.bilpa.android.services.actions;

import java.io.Serializable;


public class BaseResult implements Serializable {

    public String status;
    public String error;

    public BaseResult() {
        this.status = "0";
        this.error = null;
    }

    @Override
    public String toString() {
        return "{" +
                "status=" + status +
                ", errors=" + error +
                '}';
    }
}
