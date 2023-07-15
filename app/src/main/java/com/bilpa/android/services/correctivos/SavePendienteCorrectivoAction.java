package com.bilpa.android.services.correctivos;

import com.bilpa.android.services.actions.BaseResult;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;


public class SavePendienteCorrectivoAction extends CorretivosAction<BaseResult> {

    private String body;

    public SavePendienteCorrectivoAction() {
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
        @SerializedName("numero")
        public String correctivoId;
        public String destinatario;
        public String comentario;
        public boolean comentarioVisible;
        public Long idActivo;
        public String fotoBytes;

        public SavePendienteCorrectivoAction create() {
            SavePendienteCorrectivoAction s = new SavePendienteCorrectivoAction();
            s.createBody(this);
            return s;
        }

        public Builder plazo(Date date) {
            String plazo = sdfSave.format(date);
            this.plazo = plazo;
            return this;
        }
    }

    private static SimpleDateFormat sdfSave = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss", new Locale("ES"));




}
