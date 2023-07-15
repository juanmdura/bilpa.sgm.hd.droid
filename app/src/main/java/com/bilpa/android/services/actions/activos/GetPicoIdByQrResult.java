package com.bilpa.android.services.actions.activos;


import com.bilpa.android.services.actions.BaseResult;
import com.google.gson.annotations.SerializedName;

public class GetPicoIdByQrResult extends BaseResult {

    private static final long serialVersionUID = 2559783638869501526L;

    @SerializedName("datos")
    public Long picoId;

    public GetPicoIdByQrResult() {
        this.picoId = -1L;
    }

    @Override
    public String toString() {
        return "GetPicoIdByQrResult{" +
                "picoId=" + picoId +
                "} " + super.toString();
    }
}
