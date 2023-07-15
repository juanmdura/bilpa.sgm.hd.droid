package com.bilpa.android.services.correctivos;

import java.util.HashMap;
import java.util.Map;


public class GetActivosNoReportadosAction extends CorretivosAction<GetActivosNoReportadosResult> {

    private Long idEstacion;
    private Long idCorrectivo;

    public GetActivosNoReportadosAction(Long idEstacion, Long idCorrectivo) {
        super(GetActivosNoReportadosResult.class);
        this.idEstacion = idEstacion;
        this.idCorrectivo = idCorrectivo;
    }

    @Override
    protected String getOpertaion() {
        return "obtenerActivosNoReportados";
    }

    @Override
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("idEstacion", String.valueOf(idEstacion));
        params.put("numero", String.valueOf(idCorrectivo));
        return params;
    }
}
