package com.bilpa.android.services.correctivos;

import java.util.HashMap;
import java.util.Map;


public class GetCorrectivosAction extends CorretivosAction<GetCorrectivosResult> {

    private int idTecnico;

    public GetCorrectivosAction(int idTecnico) {
        super(GetCorrectivosResult.class, "yyyy-MM-dd HH:mm:ss");
        this.idTecnico = idTecnico;
    }

    @Override
    protected String getOpertaion() {
        return "obtenerCorrectivos";
    }

    @Override
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("idTecnico", String.valueOf(idTecnico));
        return params;
    }
}
