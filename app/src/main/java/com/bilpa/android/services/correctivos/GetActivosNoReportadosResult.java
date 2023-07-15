package com.bilpa.android.services.correctivos;


import com.bilpa.android.model.correctivos.CActivo;
import com.bilpa.android.services.actions.BaseResult;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GetActivosNoReportadosResult extends BaseResult {

    private static final long serialVersionUID = -8611755410534395021L;

    @SerializedName("datos")
    public List<CActivo> data;

    public GetActivosNoReportadosResult() {
        this.data = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "GetActivosNoReportadosResult{" +
                "data=" + data +
                '}';
    }


}
