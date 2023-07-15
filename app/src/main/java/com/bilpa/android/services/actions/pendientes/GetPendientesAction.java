package com.bilpa.android.services.actions.pendientes;

import com.bilpa.android.services.actions.PreventivosAction;

import java.util.HashMap;
import java.util.Map;


public class GetPendientesAction extends PreventivosAction<PendientesResult> {

    private Long idPreventivo;

    public GetPendientesAction(Long preventivo) {
        super(PendientesResult.class);
        this.idPreventivo = preventivo;
    }

    @Override
    protected String getOpertaion() {
        return "obtenerPendientes";
    }

    @Override
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        if (idPreventivo != null) {
            params.put("idPreventivo", String.valueOf(idPreventivo));
        }
        return params;
    }

//    TODO santilod que hacemos con este servicio. Queda el nuevo o el viejo.
//    @Override
//    protected String getOpertaion() {
//        return "obtenerPendientesDeActivo";
//    }
//
//    @Override
//    protected Map<String, String> getParams() {
//        Map<String, String> params = new HashMap<String, String>();
//        if (idPreventivo != null) {
//            params.put("idActivo", String.valueOf(idPreventivo));
//        }
//        return params;
//    }

}
