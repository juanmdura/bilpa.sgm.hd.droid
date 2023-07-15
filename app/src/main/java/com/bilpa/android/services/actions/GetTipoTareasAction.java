package com.bilpa.android.services.actions;

import java.util.HashMap;
import java.util.Map;


public class GetTipoTareasAction extends PreventivosAction<TiposTareaResult> {

    public GetTipoTareasAction() {
        super(TiposTareaResult.class);
    }

    @Override
    protected String getOpertaion() {
        return "obtenerTipoTareas";
    }

    @Override
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        return params;
    }
}
