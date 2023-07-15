package com.bilpa.android.services.actions.comments;

import com.bilpa.android.services.actions.BaseResult;
import com.bilpa.android.services.actions.PreventivosAction;

import java.util.Map;


public class SaveCheckCommentAction extends PreventivosAction<BaseResult> {

    private String body;

    public SaveCheckCommentAction() {
        super(BaseResult.class);
    }

    @Override
    protected String getOpertaion() {
        return "agregarComentarioChequeo";
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

        public boolean imprimible;
        public Long idUsuario;
        public String texto;
        public Long idItemChequeado;

        public SaveCheckCommentAction create() {
            SaveCheckCommentAction s = new SaveCheckCommentAction();
            s.createBody(this);
            return s;
        }
    }
}
