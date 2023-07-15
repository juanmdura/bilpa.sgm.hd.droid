package com.bilpa.android.services.correctivos;

import com.bilpa.android.services.actions.BaseResult;

import java.util.HashMap;
import java.util.Map;


public class CorregirActivoDeFallaReportadaAction extends CorretivosAction<BaseResult> {

    private Long idCorrectivo;
    private Long idActivoCorregido;
    private Long idActivoReportado;

    public CorregirActivoDeFallaReportadaAction(Long idCorrectivo, Long idActivoReportado, Long idActivoCorregido) {
        super(BaseResult.class);

        this.idCorrectivo = idCorrectivo;
        this.idActivoCorregido = idActivoCorregido;
        this.idActivoReportado = idActivoReportado;
    }

    @Override
    protected String getOpertaion() {
        return "corregirActivoDeFallaReportada";
    }

    @Override
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("numero", String.valueOf(idCorrectivo));
        params.put("idActivoReportado", String.valueOf(idActivoReportado));
        params.put("idActivoCorregido", String.valueOf(idActivoCorregido));
        return params;
    }

}
