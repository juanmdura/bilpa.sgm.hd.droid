package com.bilpa.android.services.actions;

import java.util.HashMap;
import java.util.Map;


public class GetVisitasAsignadasAction extends PreventivosAction<GetVisitasResult> {

    private int userId;

    public GetVisitasAsignadasAction(int userId) {
        super(GetVisitasResult.class);
        this.userId = userId;
    }

    @Override
    protected String getOpertaion() {
        return "obtenerVisitasAsignadas";
    }

    @Override
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", String.valueOf(userId));
        return params;
    }
}
