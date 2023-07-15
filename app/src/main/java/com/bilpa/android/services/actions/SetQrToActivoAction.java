package com.bilpa.android.services.actions;

import java.util.HashMap;
import java.util.Map;


public class SetQrToActivoAction extends PreventivosAction<BaseResult> {

    private Long idActivo;
    private String codigoQr;

    public SetQrToActivoAction(Long idActivo, String codigoQr) {
        super(BaseResult.class);
        this.idActivo = idActivo;
        this.codigoQr = codigoQr;
    }

    @Override
    protected String getOpertaion() {
        return "asociarQrAActivo";
    }

    @Override
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("idActivo", String.valueOf(idActivo));
        params.put("codigoQr", codigoQr);
        return params;
    }
}
