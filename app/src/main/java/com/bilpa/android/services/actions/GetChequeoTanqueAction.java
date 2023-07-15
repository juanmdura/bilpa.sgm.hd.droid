package com.bilpa.android.services.actions;

import java.util.HashMap;
import java.util.Map;


public class GetChequeoTanqueAction extends PreventivosAction<GetChequeoTanqueResult> {

    private Long mIdVisita;
    private Long mIdActivo;

    public GetChequeoTanqueAction(Long idVisita, Long idActivo) {
        super(GetChequeoTanqueResult.class);
        this.mIdVisita = idVisita;
        this.mIdActivo = idActivo;
    }

    @Override
    protected String getOpertaion() {
        return "obtenerChequeoTanque";
    }

    @Override
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("idVisita", String.valueOf(mIdVisita));
        params.put("idActivo", String.valueOf(mIdActivo));
        return params;
    }
}
