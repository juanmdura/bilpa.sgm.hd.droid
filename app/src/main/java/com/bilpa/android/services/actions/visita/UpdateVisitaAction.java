package com.bilpa.android.services.actions.visita;

import com.bilpa.android.model.StatusVisita;
import com.bilpa.android.services.actions.BaseResult;
import com.bilpa.android.services.actions.PreventivosAction;
import com.google.gson.Gson;

import java.util.Map;

public class UpdateVisitaAction extends PreventivosAction<BaseResult> {

    private String body;
    private final Gson gson;

    public UpdateVisitaAction() {
        super(BaseResult.class);
        gson = new Gson();
    }

    @Override
    protected String getOpertaion() {
        return "modificarVisita";
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
        private String fechaInicio;
        private String fechaFin;
        private String firma;
        private String estado;
        private String comentarioFirma;

        public Builder visitaId(Long visitaId) {
            this.idVisita = visitaId;
            return this;
        }

        public Builder fechaInicio(String fechaInicio) {
            this.fechaInicio = fechaInicio;
            return this;
        }

        public Builder fechaFin(String fechaFin) {
            this.fechaFin = fechaFin;
            return this;
        }

        public Builder firma(String firma) {
            this.firma = firma;
            return this;
        }

        public Builder estado(StatusVisita status) {
            this.estado = StatusVisita.to(status);
            return this;
        }

        public Builder comentario(String comentario) {
            this.comentarioFirma = comentario;
            return this;
        }

        public UpdateVisitaAction create() {
            UpdateVisitaAction action = new UpdateVisitaAction();
            action.createBody(this);
            return action;
        }

    }


}
