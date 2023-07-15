package com.bilpa.android.services.actions.corregidos;

import com.bilpa.android.services.actions.BaseResult;
import com.bilpa.android.services.actions.PreventivosAction;

import java.util.HashMap;
import java.util.Map;


public class DeleteCorregidoAction extends PreventivosAction<BaseResult> {

    private Long idCorregido;

    public DeleteCorregidoAction(Long idCorregido) {
        super(BaseResult.class);
        this.idCorregido = idCorregido;
    }

    @Override
    protected String getOpertaion() {
        return "eliminarCorregido";
    }

    @Override
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        if (idCorregido != null) {
            params.put("idCorregido", String.valueOf(idCorregido));
        }
        return params;
    }
}
