package com.bilpa.android.services.actions.visita;

import com.bilpa.android.services.actions.PreventivosAction;

import java.util.HashMap;
import java.util.Map;


public class GetReportAction extends PreventivosAction<ReportResult> {

    private Long idVisita;

    public GetReportAction(long idVisita) {
        super(ReportResult.class);
        this.idVisita = idVisita;
    }

    @Override
    protected String getOpertaion() {
        return "reporteVisitaPreventivaEstaciones";
    }

    @Override
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        if (idVisita != null) {
            params.put("idVisita", String.valueOf(idVisita));
        }
        return params;
    }
}
