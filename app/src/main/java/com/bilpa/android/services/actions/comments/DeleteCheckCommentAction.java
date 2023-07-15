package com.bilpa.android.services.actions.comments;

import com.bilpa.android.services.actions.BaseResult;
import com.bilpa.android.services.actions.PreventivosAction;

import java.util.Map;


public class DeleteCheckCommentAction extends PreventivosAction<BaseResult> {

    private String body;

    public DeleteCheckCommentAction() {
        super(BaseResult.class);
    }

    @Override
    protected String getOpertaion() {
        return "eliminarComentarioChequeo";
    }

    @Override
    protected Map<String, String> getParams() {
        return null;
    }

    @Override
    protected String getBodyPost() {
        return body;
    }

    private void createBody(DeleteCheckCommentAction.Builder b) {
        String json = gson.toJson(b);
        body = json;
    }

    public static class Builder {

        public Long id;

        public DeleteCheckCommentAction create() {
            DeleteCheckCommentAction s = new DeleteCheckCommentAction();
            s.createBody(this);
            return s;
        }
    }
}
