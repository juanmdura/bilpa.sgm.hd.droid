package com.bilpa.android.services.actions.activos;

import com.bilpa.android.services.actions.BaseResult;
import com.bilpa.android.services.actions.PreventivosAction;

import java.util.HashMap;
import java.util.Map;


public class SetQrToPicoAction extends PreventivosAction<BaseResult> {

    private Long idPico;
    private String codigoQr;

    public SetQrToPicoAction(Long idPico, String codigoQr) {
        super(BaseResult.class);
        this.idPico = idPico;
        this.codigoQr = codigoQr;
    }

    @Override
    protected String getOpertaion() {
        return "asociarQrAPico";
    }

    @Override
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("idPico", String.valueOf(idPico));
        params.put("codigoQr", codigoQr);
        return params;
    }
}
