package com.bilpa.android.services.actions.visita;

import com.bilpa.android.services.actions.PreventivosAction;

import java.util.HashMap;
import java.util.Map;


public class GetCorreosEstacionAction extends PreventivosAction<GetCorreosEstacionResult> {

    private Long idEstacion;

    public GetCorreosEstacionAction(long idEstacion) {
        super(GetCorreosEstacionResult.class);
        this.idEstacion = idEstacion;
    }

    @Override
    protected String getOpertaion() {
        return "obtenerCorreosEstacion";
    }

    @Override
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        if (idEstacion != null) {
            params.put("id", String.valueOf(idEstacion));
        }
        return params;
    }
}
