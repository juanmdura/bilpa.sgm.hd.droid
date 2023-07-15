package com.bilpa.android.services.actions.comments;

import com.bilpa.android.services.actions.BaseResult;
import com.bilpa.android.services.actions.PreventivosAction;

import java.util.Map;


public class UpdateCheckCommentAction extends PreventivosAction<BaseResult> {

    private String body;

    public UpdateCheckCommentAction() {
        super(BaseResult.class);
    }

    @Override
    protected String getOpertaion() {
        return "modificarComentarioChequeo";
    }

    @Override
    protected Map<String, String> getParams() {
        return null;
    }

    @Override
    protected String getBodyPost() {
        return body;
    }

    private void createBody(Builder b) {
        String json = gson.toJson(b);
        body = json;
    }

    public static class Builder {

        public Long id;
        public boolean imprimible;
        public Long idUsuario;
        public String texto;
        public Long idItemChequeado;

        public UpdateCheckCommentAction create() {
            UpdateCheckCommentAction s = new UpdateCheckCommentAction();
            s.createBody(this);
            return s;
        }
    }
}
