package com.bilpa.android.services.actions;

import java.util.HashMap;
import java.util.Map;


public class GetFallasAction extends PreventivosAction<FallasResult> {

    private Long typeId;

    public GetFallasAction() {
        super(FallasResult.class);
        this.typeId = null;
    }

    public GetFallasAction(long typeId) {
        super(FallasResult.class);
        this.typeId = typeId;
    }

    @Override
    protected String getOpertaion() {
        return "obtenerFallasTecnicas";
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
