package com.bilpa.android.services.actions;

import java.util.HashMap;
import java.util.Map;


public class GetActivoIdByQrAction extends PreventivosAction<GetActivoIdByQrResult> {

    private String codigoQR;
    private Long idEstacion;

    public GetActivoIdByQrAction(String codigoQR, Long idEstacion) {
        super(GetActivoIdByQrResult.class);
        this.codigoQR = codigoQR;
        this.idEstacion = idEstacion;
    }

    @Override
    protected String getOpertaion() {
        return "obtenerIdActivoPorQR";
    }

    @Override
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("codigoQR", codigoQR);
        params.put("idEstacion", String.valueOf(idEstacion));
        return params;
    }
}
