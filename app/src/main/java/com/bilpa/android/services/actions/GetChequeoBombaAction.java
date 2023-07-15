package com.bilpa.android.services.actions;

import java.util.HashMap;
import java.util.Map;


public class GetChequeoBombaAction extends PreventivosAction<GetChequeoBombaResult> {

    private Long mIdVisita;
    private Long mIdActivo;

    public GetChequeoBombaAction(Long idVisita, Long idActivo) {
        super(GetChequeoBombaResult.class);
        this.mIdVisita = idVisita;
        this.mIdActivo = idActivo;
    }

    @Override
    protected String getOpertaion() {
        return "obtenerChequeoBomba";
    }

    @Override
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("idVisita", String.valueOf(mIdVisita));
        params.put("idActivo", String.valueOf(mIdActivo));
        return params;
    }
}
