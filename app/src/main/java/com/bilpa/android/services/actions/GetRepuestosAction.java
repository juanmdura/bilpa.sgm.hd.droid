package com.bilpa.android.services.actions;

import java.util.HashMap;
import java.util.Map;


public class GetRepuestosAction extends PreventivosAction<RepuestosResult> {

    public GetRepuestosAction() {
        super(RepuestosResult.class);
    }

    @Override
    protected String getOpertaion() {
        return "obtenerRepuestos";
    }

    @Override
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        return params;
    }
}
