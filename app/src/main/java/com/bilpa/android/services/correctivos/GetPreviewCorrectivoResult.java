package com.bilpa.android.services.correctivos;


import com.bilpa.android.model.Report;
import com.bilpa.android.services.actions.BaseResult;
import com.google.gson.annotations.SerializedName;

public class GetPreviewCorrectivoResult extends BaseResult {

    private static final long serialVersionUID = 4222054012865915999L;

    @SerializedName("datos")
    public Report report;

    @Override
    public String toString() {
        return "ReportResult{" +
                "report=" + report +
                "} " + super.toString();
    }





}
