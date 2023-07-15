package com.bilpa.android.services.actions.products;

import com.bilpa.android.services.actions.PreventivosAction;

import java.util.HashMap;
import java.util.Map;


public class GetProductosAction extends PreventivosAction<ProductosResult> {

    private Long idSurtidor;

    public GetProductosAction(Long idSurtidor) {
        super(ProductosResult.class);
        this.idSurtidor = idSurtidor;
    }

    @Override
    protected String getOpertaion() {
        return "obtenerProductos";
    }

    @Override
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("idSurtidor", String.valueOf(idSurtidor));
        return params;
    }
}
