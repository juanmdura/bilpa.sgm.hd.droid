package com.bilpa.android.services.actions.visita;


import com.bilpa.android.model.Report;
import com.bilpa.android.services.actions.BaseResult;
import com.google.gson.annotations.SerializedName;

public class ReportResult extends BaseResult {

    private static final long serialVersionUID = -7426656839557283491L;

    @SerializedName("datos")
    public Report report;

    @Override
    public String toString() {
        return "ReportResult{" +
                "report=" + report +
                "} " + super.toString();
    }


}
