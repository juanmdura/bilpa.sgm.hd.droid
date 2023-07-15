package com.bilpa.android.services.actions;

import java.util.HashMap;
import java.util.Map;


public class GetTareasAction extends PreventivosAction<TareasResult> {

    private Long typeId;

    public GetTareasAction() {
        super(TareasResult.class);
        this.typeId = null;
    }

    public GetTareasAction(long typeId) {
        super(TareasResult.class);
        this.typeId = typeId;
    }

    @Override
    protected String getOpertaion() {
        return "obtenerTareas";
    }

    @Override
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        if (typeId != null) {
            params.put("tipo", String.valueOf(typeId));
        }
        return params;
    }
}
