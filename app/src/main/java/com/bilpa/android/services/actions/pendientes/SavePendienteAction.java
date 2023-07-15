package com.bilpa.android.services.actions.pendientes;

import com.bilpa.android.services.actions.BaseResult;
import com.bilpa.android.services.actions.PreventivosAction;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;


public class SavePendienteAction extends PreventivosAction<BaseResult> {

    private String body;

    public SavePendienteAction() {
        super(BaseResult.class);
    }

    @Override
    protected String getOpertaion() {
        return "guardarPendiente";
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
        private String plazo;
        public Long idPreventivo;
        public Long idItemChequeado;
        public String destinatario;
        public String comentario;
        public boolean comentarioVisible;
        public String fotoBytes;

        public SavePendienteAction create() {
            SavePendienteAction s = new SavePendienteAction();
            s.createBody(this);
            return s;
        }

        public Builder plazo(Date date) {
            String plazo = sdfSave.format(date);
            this.plazo = plazo;
            return this;
        }
    }

    private static SimpleDateFormat sdfSave = new SimpleDateFormat("MMM d',' yyyy hh:mm:ss a", Locale.ENGLISH);

}
