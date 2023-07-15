package com.bilpa.android.services.actions;

import java.util.HashMap;
import java.util.Map;


public class GetTiposRepuestosAction extends PreventivosAction<TiposRepuestosResult> {

    public GetTiposRepuestosAction() {
        super(TiposRepuestosResult.class);
    }

    @Override
    protected String getOpertaion() {
        return "obtenerTipoRepuestos";
    }

    @Override
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        return params;
    }
}
