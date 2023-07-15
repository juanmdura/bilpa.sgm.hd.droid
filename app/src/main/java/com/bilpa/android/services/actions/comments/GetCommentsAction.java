package com.bilpa.android.services.actions.comments;

import com.bilpa.android.services.actions.PreventivosAction;
import com.bilpa.android.services.actions.pendientes.PendientesResult;

import java.util.HashMap;
import java.util.Map;


public class GetCommentsAction extends PreventivosAction<CommentsResult> {

    private Long chequeoId;

    public GetCommentsAction(Long preventivo) {
        super(CommentsResult.class);
        this.chequeoId = preventivo;
    }

    @Override
    protected String getOpertaion() {
        return "obtenerComentariosChequeo";
    }

    @Override
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        if (chequeoId != null) {
            params.put("idItemChequedo", String.valueOf(chequeoId));
        }
        return params;
    }
}
