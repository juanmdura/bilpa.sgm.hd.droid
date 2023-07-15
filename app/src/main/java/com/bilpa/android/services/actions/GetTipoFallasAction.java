package com.bilpa.android.services.actions;

import java.util.HashMap;
import java.util.Map;


public class GetTipoFallasAction extends PreventivosAction<TiposFallaResult> {

    public GetTipoFallasAction() {
        super(TiposFallaResult.class);
    }

    @Override
    protected String getOpertaion() {
        return "obtenerTipoFallasTecnicas";
    }

    @Override
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        return params;
    }
}
