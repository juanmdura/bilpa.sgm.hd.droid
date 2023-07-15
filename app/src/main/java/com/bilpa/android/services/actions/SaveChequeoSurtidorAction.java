package com.bilpa.android.services.actions;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class SaveChequeoSurtidorAction extends PreventivosAction<BaseResult> {

    private Map<String, String> params;
    private String body;
    private final Gson gson;

    public SaveChequeoSurtidorAction() {
        super(BaseResult.class);
        gson = new Gson();
    }

    @Override
    protected String getOpertaion() {
        return "guardarChequeoSurtidor";
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

//        private String cabezalLimpiezaSellado;
//        private String presetCara1;
//        private String presetCara2;
//        private String pruebaDisplayIluminacion;
//        private String visualYLimpieza;
//        private String parteElectrica;
//        public String planSellado;
//
        public Builder idPreventivo(Long idPreventivo) {
            this.idPreventivo = idPreventivo;
            return this;
        }
//
//        public Builder cabezalLimpiezaSellado(String cabezalLimpiezaSellado) {
//            this.cabezalLimpiezaSellado = cabezalLimpiezaSellado;
//            return this;
//        }
//
//        public Builder presetCara1(String presetCara1) {
//            this.presetCara1 = presetCara1;
//            return this;
//        }
//
//        public Builder presetCara2(String presetCara2) {
//            this.presetCara2 = presetCara2;
//            return this;
//        }
//
//        public Builder pruebaDisplayIluminacion(String pruebaDisplayIluminacion) {
//            this.pruebaDisplayIluminacion = pruebaDisplayIluminacion;
//            return this;
//        }
//
//        public Builder visualYLimpieza(String visualYLimpieza) {
//            this.visualYLimpieza = visualYLimpieza;
//            return this;
//        }
//
//        public Builder parteElectrica(String parteElectrica) {
//            this.parteElectrica = parteElectrica;
//            return this;
//        }


        public Builder chequeo(String nombreItemChequeo, String valor, boolean pendiente) {
            if (itemsChequeados == null) {
                itemsChequeados = new ArrayList<>();
            }
            itemsChequeados.add(new ChequeoBuilder(nombreItemChequeo, valor, pendiente));
            return this;
        }


        public SaveChequeoSurtidorAction create() {
            SaveChequeoSurtidorAction s = new SaveChequeoSurtidorAction();
            s.createBody(this);


//            s.params.put("cabezalLimpiezaSellado", cabezalLimpiezaSellado);
//            s.params.put("presetCara1", presetCara1);
//            s.params.put("presetCara2", presetCara2);
//            s.params.put("pruebaDisplayIluminacion", pruebaDisplayIluminacion);
//            s.params.put("visualYLimpieza", visualYLimpieza);
//            s.params.put("parteElectrica", parteElectrica);
//            s.params.put("planSellado", planSellado);
            return s;
        }


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
