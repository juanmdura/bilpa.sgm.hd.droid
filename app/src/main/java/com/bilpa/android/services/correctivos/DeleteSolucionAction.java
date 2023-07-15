package com.bilpa.android.services.correctivos;

import com.bilpa.android.services.actions.BaseResult;

import java.util.HashMap;
import java.util.Map;


public class DeleteSolucionAction extends CorretivosAction<BaseResult> {

    private Long mIdSolucion;

    public DeleteSolucionAction(Long idSolucion) {
        super(BaseResult.class);
        this.mIdSolucion = idSolucion;
    }

    @Override
    protected String getOpertaion() {
        return "eliminarSolucion";
    }

    @Override
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        if (mIdSolucion != null) {
            params.put("idSolucion", String.valueOf(mIdSolucion));
        }
        return params;
    }
}
