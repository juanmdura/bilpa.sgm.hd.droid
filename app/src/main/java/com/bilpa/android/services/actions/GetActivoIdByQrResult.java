package com.bilpa.android.services.actions;


import com.google.gson.annotations.SerializedName;

public class GetActivoIdByQrResult extends BaseResult {

    private static final long serialVersionUID = -7957093470831955729L;

    @SerializedName("datos")
    public int data;

    public GetActivoIdByQrResult() {
        this.data = -1;
    }

    @Override
    public String toString() {
        return "GetActivoIdByQrResult{" +
                "data=" + data +
                "} " + super.toString();
    }
}
