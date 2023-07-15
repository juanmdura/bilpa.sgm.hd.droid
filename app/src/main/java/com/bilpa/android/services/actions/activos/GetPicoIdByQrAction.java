package com.bilpa.android.services.actions.activos;

import com.bilpa.android.services.actions.PreventivosAction;

import java.util.HashMap;
import java.util.Map;


public class GetPicoIdByQrAction extends PreventivosAction<GetPicoIdByQrResult> {

    private String codigoQR;
    private Long idEstacion;

    public GetPicoIdByQrAction(String codigoQR, Long idEstacion) {
        super(GetPicoIdByQrResult.class);
        this.codigoQR = codigoQR;
        this.idEstacion = idEstacion;
    }

    @Override
    protected String getOpertaion() {
        return "obtenerIdPicoPorQR";
    }

    @Override
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("codigoQR", codigoQR);
        params.put("idEstacion", String.valueOf(idEstacion));
        return params;
    }
}
