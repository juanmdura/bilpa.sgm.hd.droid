package com.bilpa.android.services.actions;

import java.util.HashMap;
import java.util.Map;


public class GetDestinoCargosAction extends PreventivosAction<DestinoCargoResult> {

    public GetDestinoCargosAction() {
        super(DestinoCargoResult.class);
    }

    @Override
    protected String getOpertaion() {
        return "obtenerDestinosDelCargo";
    }

    @Override
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        return params;
    }
}
