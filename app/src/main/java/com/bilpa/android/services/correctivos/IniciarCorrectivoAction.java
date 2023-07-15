package com.bilpa.android.services.correctivos;

import com.bilpa.android.services.actions.BaseResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.Map;


public class IniciarCorrectivoAction extends CorretivosAction<BaseResult> {

    private Map<String, String> params;
    private String body;
    private final Gson gson;

    public IniciarCorrectivoAction() {
        super(BaseResult.class);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("yyyy/MM/dd HH:mm:ss");
        gson = gsonBuilder.create();
    }

    @Override
    protected String getOpertaion() {
        return "iniciarCorrectivo ";
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

        public IniciarCorrectivoAction create() {
            IniciarCorrectivoAction s = new IniciarCorrectivoAction();
            s.createBody(this);
            return s;
        }

    }

}
