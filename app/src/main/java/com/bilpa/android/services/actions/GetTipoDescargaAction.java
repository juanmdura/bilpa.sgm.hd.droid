package com.bilpa.android.services.actions;

import java.util.HashMap;
import java.util.Map;


public class GetTipoDescargaAction extends PreventivosAction<TipoDescargaResult> {

    public GetTipoDescargaAction() {
        super(TipoDescargaResult.class);
    }

    @Override
    protected String getOpertaion() {
        return "obtenerTiposDeDescarga";
    }

    @Override
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        return params;
    }
}
