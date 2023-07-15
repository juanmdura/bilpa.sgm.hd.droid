package com.bilpa.android.services.correctivos;

import com.bilpa.android.services.actions.pendientes.PendientesResult;

import java.util.HashMap;
import java.util.Map;


public class GetPendientesCorregidoAction extends CorretivosAction<PendientesResult> {

    private long idActivo;

    public GetPendientesCorregidoAction(long idActivo) {
        super(PendientesResult.class, "MMM d',' yyyy h:mm:ss a");
        this.idActivo = idActivo;
    }

    @Override
    protected String getOpertaion() {
        return "obtenerPendientesDeActivo";
    }

    @Override
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("idActivo", String.valueOf(idActivo));
        return params;
    }
}
