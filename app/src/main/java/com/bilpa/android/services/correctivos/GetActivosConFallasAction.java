package com.bilpa.android.services.correctivos;

import java.util.HashMap;
import java.util.Map;


public class GetActivosConFallasAction extends CorretivosAction<GetActivosConFallasResult> {

    private Long idCorrectivo;

    public GetActivosConFallasAction(Long idCorrectivo) {
        super(GetActivosConFallasResult.class);
        this.idCorrectivo = idCorrectivo;
    }

    @Override
    protected String getOpertaion() {
        return "obtenerActivosConFallasReportadas";
    }

    @Override
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("numero", String.valueOf(idCorrectivo));
        return params;
    }
}
