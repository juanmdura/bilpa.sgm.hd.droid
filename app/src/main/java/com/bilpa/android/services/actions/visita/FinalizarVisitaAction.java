package com.bilpa.android.services.actions.visita;

import com.bilpa.android.services.actions.BaseResult;
import com.bilpa.android.services.actions.PreventivosAction;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

public class FinalizarVisitaAction extends PreventivosAction<BaseResult> {

    private String body;
    private final Gson gson;

    public FinalizarVisitaAction() {
        super(BaseResult.class);
        gson = new Gson();
    }

    @Override
    protected String getOpertaion() {
        return "finalizarVisita";
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

        private Long idVisita;
        private List<String> mails;

        public Builder visitaId(Long visitaId) {
            this.idVisita = visitaId;
            return this;
        }

        public Builder mails(List<String> mails) {
            this.mails = mails;
            return this;
        }

        public FinalizarVisitaAction create() {
            FinalizarVisitaAction action = new FinalizarVisitaAction();
            action.createBody(this);
            return action;
        }

    }


}
