package com.bilpa.android.services.actions.pendientes;

import com.bilpa.android.services.actions.BaseResult;
import com.bilpa.android.services.actions.PreventivosAction;

import java.util.HashMap;
import java.util.Map;


public class DescartarPendienteAction extends PreventivosAction<BaseResult> {

    private Long idPendiente;
    private Long idDescartador;
    private String motivoDescarte;

    public DescartarPendienteAction(Long idPendiente, Long idDescartador, String motivoDescarte) {
        super(BaseResult.class);
        this.idPendiente = idPendiente;
        this.idDescartador = idDescartador;
        this.motivoDescarte = motivoDescarte;
    }

    @Override
    protected String getOpertaion() {
        return "descartarPendiente";
    }

    @Override
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        if (idPendiente != null) {
            params.put("idPendiente", String.valueOf(idPendiente));
        }
        if (idDescartador != null) {
            params.put("idDescartador", String.valueOf(idDescartador));
        }
        params.put("motivoDescarte", motivoDescarte);
        return params;
    }
}
