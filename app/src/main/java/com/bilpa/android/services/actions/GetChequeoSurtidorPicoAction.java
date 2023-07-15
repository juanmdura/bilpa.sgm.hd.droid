package com.bilpa.android.services.actions;

import java.util.HashMap;
import java.util.Map;


public class GetChequeoSurtidorPicoAction extends PreventivosAction<GetChequeoSurtidorPicoResult> {

    private Long idPico;
    private Long idPreventivo;

    public GetChequeoSurtidorPicoAction(Long idPico, Long idPreventivo) {
        super(GetChequeoSurtidorPicoResult.class);
        this.idPico = idPico;
        this.idPreventivo = idPreventivo;
    }

    @Override
    protected String getOpertaion() {
        return "obtenerChequeoSurtidorPico";
    }

    @Override
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("idPico", String.valueOf(idPico));
        params.put("idPreventivo", String.valueOf(idPreventivo));
        return params;
    }
}
