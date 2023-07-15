package com.bilpa.android.services.actions;

import java.util.HashMap;
import java.util.Map;


public class GetPicosAction extends PreventivosAction<PicosResult> {

    private Long idSurtidor;

    public GetPicosAction(long idSurtidor) {
        super(PicosResult.class);
        this.idSurtidor = idSurtidor;
    }

    @Override
    protected String getOpertaion() {
        return "obtenerPicos";
    }

    @Override
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        if (idSurtidor != null) {
            params.put("idSurtidor", String.valueOf(idSurtidor));
        }
        return params;
    }
}
