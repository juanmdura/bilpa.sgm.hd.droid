package com.bilpa.android.services.correctivos;

import com.bilpa.android.services.actions.BaseResult;
import com.bilpa.android.services.actions.PreventivosAction;

import java.util.HashMap;
import java.util.Map;


public class DeletePendienteCorregidoAction extends PreventivosAction<BaseResult> {

    private Long idPendiente;

    public DeletePendienteCorregidoAction(Long idPendiente) {
        super(BaseResult.class);
        this.idPendiente = idPendiente;
    }

    @Override
    protected String getOpertaion() {
        return "eliminarPendiente";
    }

    @Override
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        if (idPendiente != null) {
            params.put("idPendiente", String.valueOf(idPendiente));
        }
        return params;
    }
}
