package com.bilpa.android.services.actions;

import java.util.HashMap;
import java.util.Map;


public class GetOrganizacionesAction extends PreventivosAction<OrganizacionesResult> {

    public GetOrganizacionesAction() {
        super(OrganizacionesResult.class);
    }

    @Override
    protected String getOpertaion() {
        return "obtenerOrganizaciones";
    }

    @Override
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        return params;
    }
}
