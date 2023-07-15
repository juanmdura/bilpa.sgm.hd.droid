package com.bilpa.android.services.actions;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class SaveChequeoTanqueAction extends PreventivosAction<BaseResult> {

    private Map<String, String> params;
    private String body;
    private final Gson gson;

    public SaveChequeoTanqueAction() {
        super(BaseResult.class);
        gson = new Gson();
    }

    @Override
    protected String getOpertaion() {
        return "guardarChequeoTanque";
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
        private Long medidaDelAgua;
        private String tipoDeDescarga;

        public Builder idPreventivo(Long idPreventivo) {
            this.idPreventivo = idPreventivo;
            return this;
        }

        public Builder medidaDelAgua(Long medidaDelAgua) {
            this.medidaDelAgua = medidaDelAgua;
            return this;
        }

        public Builder tipoDeDescarga(String tipoDeDescarga) {
            this.tipoDeDescarga = tipoDeDescarga;
            return this;
        }

        public Builder chequeo(String nombreItemChequeo, String valor, boolean pendiente) {
            if (itemsChequeados == null) {
                itemsChequeados = new ArrayList<>();
            }
            itemsChequeados.add(new ChequeoBuilder(nombreItemChequeo, valor, pendiente));
            return this;
        }


        public SaveChequeoTanqueAction create() {
            SaveChequeoTanqueAction s = new SaveChequeoTanqueAction();
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
