package com.bilpa.android.services.actions;

import java.util.HashMap;
import java.util.Map;


public class GetCorregidosAction extends PreventivosAction<CorregidosResult> {

    private Long idPreventivo;

    public GetCorregidosAction(Long idPreventivo) {
        super(CorregidosResult.class);
        this.idPreventivo = idPreventivo;
    }

    @Override
    protected String getOpertaion() {
        return "obtenerCorregidos";
    }

    @Override
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        if (idPreventivo != null) {
            params.put("idPreventivo", String.valueOf(idPreventivo));
        }
        return params;
    }
}
