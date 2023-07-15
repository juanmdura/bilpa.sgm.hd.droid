package com.bilpa.android.services.actions;

import java.util.HashMap;
import java.util.Map;

public class GetVisitaAction extends PreventivosAction<GetVisitaResult> {

    private Long visitaId;

    public GetVisitaAction(Long visitaId) {
        super(GetVisitaResult.class);
        this.visitaId = visitaId;
    }

    @Override
    protected String getOpertaion() {
        return "obtenerVisita";
    }

    @Override
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", String.valueOf(visitaId));
        return params;
    }
}
