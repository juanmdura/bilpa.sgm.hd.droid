package com.bilpa.android.services.correctivos;

import com.bilpa.android.services.actions.BaseResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;


public class FinalizarCorrectivoAction extends CorretivosAction<BaseResult> {

    private Map<String, String> params;
    private String body;
    private final Gson gson;

    public FinalizarCorrectivoAction() {
        super(BaseResult.class);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
    }

    @Override
    protected String getOpertaion() {
        return "finalizarCorrectivo ";
    }

    @Override
    protected Map<String, String> getParams() {
        return params;
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

        @SerializedName("numero")
        public Long idCorrectivo;

        private List<String> mails;

        public Builder mails(List<String> mails) {
            this.mails = mails;
            return this;
        }

        public FinalizarCorrectivoAction create() {
            FinalizarCorrectivoAction s = new FinalizarCorrectivoAction();
            s.createBody(this);
            return s;
        }

    }

}
