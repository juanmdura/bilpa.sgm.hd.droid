package com.bilpa.android.services.actions;

import com.bilpa.android.model.Caudal;
import com.bilpa.android.model.Precinto;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class SaveChequeoSurtidorPicoAction extends PreventivosAction<BaseResult> {

    private Map<String, String> params;
    private String body;
    private final Gson gson;

    public SaveChequeoSurtidorPicoAction() {
        super(BaseResult.class);
        gson = new Gson();
    }

    @Override
    protected String getOpertaion() {
        return "guardarChequeoSurtidorPico";
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

        public Long idPreventivo;
        public Long idPico;
        public Long totalizadorMecanicoInicial;
        public Long totalizadorMecanicoFinal;
        public Double totalizadorElectronicoInicial;
        public Double totalizadorElectronicoFinal;
        public Double calibre1;
        public Double calibre2;
        public Double calibre3;
        public Double calibre4;
        public Double calibre5;
        public Double calibre6;
        private List<ChequeoBuilder> itemsChequeados;

        public Caudal caudal;
        public Precinto precinto;



        public SaveChequeoSurtidorPicoAction create() {
            SaveChequeoSurtidorPicoAction s = new SaveChequeoSurtidorPicoAction();
            s.createBody(this);
            return s;
        }

        public Builder chequeo(String nombreItemChequeo, String valor, boolean pendiente) {
            if (itemsChequeados == null) {
                itemsChequeados = new ArrayList<>();
            }
            itemsChequeados.add(new ChequeoBuilder(nombreItemChequeo, valor, pendiente));
            return this;
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
