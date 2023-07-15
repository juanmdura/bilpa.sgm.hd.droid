package com.bilpa.android.services.correctivos;

import java.util.HashMap;
import java.util.Map;


public class GetSolucionesAction extends CorretivosAction<GetSolucionesResult> {

    private long idCorrectivo;
    private long idActivo;

    public GetSolucionesAction(long idCorrectivo, long idActivo) {
        super(GetSolucionesResult.class);
        this.idCorrectivo = idCorrectivo;
        this.idActivo = idActivo;
    }

    @Override
    protected String getOpertaion() {
        return "obtenerSoluciones";
    }

    @Override
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("numero", String.valueOf(idCorrectivo));
        params.put("idActivo", String.valueOf(idActivo));
        return params;
    }

}
