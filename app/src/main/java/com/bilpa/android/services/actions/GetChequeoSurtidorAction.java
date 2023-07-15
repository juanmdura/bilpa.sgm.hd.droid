package com.bilpa.android.services.actions;

import java.util.HashMap;
import java.util.Map;


public class GetChequeoSurtidorAction extends PreventivosAction<GetChequeoSurtidorResult> {

    private Long mIdVisita;
    private Long mIdActivo;

    public GetChequeoSurtidorAction(Long idVisita, Long idActivo) {
        super(GetChequeoSurtidorResult.class);
        this.mIdVisita = idVisita;
        this.mIdActivo = idActivo;
    }

    @Override
    protected String getOpertaion() {
        return "obtenerChequeoSurtidor";
    }

    @Override
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("idVisita", String.valueOf(mIdVisita));
        params.put("idActivo", String.valueOf(mIdActivo));
        return params;
    }
}
