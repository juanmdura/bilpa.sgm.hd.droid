package com.bilpa.android.services.correctivos;

import java.util.HashMap;
import java.util.Map;


public class GetPreviewCorrectivoAction extends CorretivosAction<GetPreviewCorrectivoResult> {

    private long idCorrectivo;

    public GetPreviewCorrectivoAction(long idCorrectivo) {
        super(GetPreviewCorrectivoResult.class);
        this.idCorrectivo = idCorrectivo;
    }

    @Override
    protected String getOpertaion() {
        return "obtenerPreviewCorrectivo";
    }

    @Override
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("numero", String.valueOf(idCorrectivo));
        return params;
    }

}
