package com.bilpa.android.services.actions.products;

import com.bilpa.android.services.actions.PreventivosAction;

import java.util.HashMap;
import java.util.Map;


public class GetChequeoProductoAction extends PreventivosAction<GetChequeoProductoResult> {

    private Long idProducto;
    private Long idPreventivo;

    public GetChequeoProductoAction(Long idProducto, Long idPreventivo) {
        super(GetChequeoProductoResult.class);
        this.idProducto = idProducto;
        this.idPreventivo = idPreventivo;
    }

    @Override
    protected String getOpertaion() {
        return "obtenerChequeoSurtidorProducto";
    }

    @Override
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("idProducto", String.valueOf(idProducto));
        params.put("idPreventivo", String.valueOf(idPreventivo));
        return params;
    }
}
