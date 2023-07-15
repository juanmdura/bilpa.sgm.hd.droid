package com.bilpa.android.services.actions;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class SaveChequeoBombaAction extends PreventivosAction<BaseResult> {

    private Map<String, String> params;
    private String body;
    private final Gson gson;

    public SaveChequeoBombaAction() {
        super(BaseResult.class);
        gson = new Gson();
    }

    @Override
    protected String getOpertaion() {
        return "guardarChequeoBomba";
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

        private Long idPreventivo;

        private List<ChequeoBuilder> itemsChequeados;


        public Builder idPreventivo(Long idPreventivo) {
            this.idPreventivo = idPreventivo;
            return this;
        }

        public Builder chequeo(String nombreItemChequeo, String valor, boolean pendiente) {
            if (itemsChequeados == null) {
                itemsChequeados = new ArrayList<>();
            }
            itemsChequeados.add(new ChequeoBuilder(nombreItemChequeo, valor, pendiente));
            return this;
        }


        public SaveChequeoBombaAction create() {
            SaveChequeoBombaAction s = new SaveChequeoBombaAction();
            s.createBody(this);
            return s;
        }

        private static class ChequeoBuilder {
            private String nombreItemChequeo;
            private String valor;
            private boolean pendiente;

            private ChequeoBuilder(String nombreItemChequeo, String valor, boolean pendiente) {
                this.nombreItemChequeo = nombreItemChequeo;
                this.valor = valor;
                this.pendiente = pendiente;
            }
        }


    }

}
