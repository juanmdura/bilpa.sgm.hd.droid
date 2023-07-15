package com.bilpa.android.services.correctivos;

import com.bilpa.android.services.actions.BaseResult;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class SaveSolucionAction extends CorretivosAction<BaseResult> {

    private Map<String, String> params;
    private String body;
    private final Gson gson;

    public SaveSolucionAction() {
        super(BaseResult.class);
        gson = new Gson();
    }

    @Override
    protected String getOpertaion() {
        return "guardarSolucion";
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

        @SerializedName("id")
        public Long idSolucion;
        @SerializedName("numero")
        public Long idCorrectivo;
        public Long idFalla;
        public Long idTarea;
        public Long idTecnico;
        public Integer idDestinoDelCargo;
        public Long idActivo;
        private CommentBuiler comentario;
        @SerializedName("repuestosLineaCorregidos")
        private List<RepuestoBuilder> repuestos;
        public Long idPico;
        private String fotoBytes;
        private String foto2Bytes;
        public Long idPendiente;
        @SerializedName("contador")
        public Totalizadores totalizadores;
        public Calibres calibre;
        public Precinto precinto;



//        private Long idCorregido;
//        private Long idPreventivo;


//        private Long idItemChequeado;

//        public Builder idCorregido(Long idCorregido) {
//            this.idCorregido = idCorregido;
//            return this;
//        }
//
//        public Builder idPreventivo(Long idPreventivo) {
//            this.idPreventivo = idPreventivo;
//            return this;
//        }

//        public Builder idDestinoDelCargo(int idDestinoDelCargo) {
//            this.idDestinoDelCargo = idDestinoDelCargo;
//            return this;
//        }
//
//        public Builder idFalla(Long idFalla) {
//            this.idFalla = idFalla;
//            return this;
//        }
//
//        public Builder idTarea(Long idTarea) {
//            this.idTarea = idTarea;
//            return this;
//        }

        public Builder totalizadores(Long id, Double inicio, Double fin) {
            this.totalizadores = new Totalizadores();
            this.totalizadores.id = id;
            this.totalizadores.inicio = inicio;
            this.totalizadores.fin = fin;
            return this;
        }
//
        public Builder comentario(String idComentario, String value, boolean visible) {
            this.comentario = new CommentBuiler(idComentario, value, visible);
            return this;
        }

        public Builder repuesto(Long idRepuesto, boolean nuevo, int cantidad) {
            if (repuestos == null) {
                repuestos = new ArrayList<>();
            }
            repuestos.add(new RepuestoBuilder(idRepuesto, nuevo, cantidad));
            return this;
        }

        public Builder fotoBytes(String bytes) {
            this.fotoBytes = bytes;
            return this;
        }

        public Builder foto2Bytes(String bytes) {
            this.foto2Bytes = bytes;
            return this;
        }

        public SaveSolucionAction create() {
            SaveSolucionAction s = new SaveSolucionAction();
            s.createBody(this);
            return s;
        }


    }

    private static class CommentBuiler {

        private String id;
        private String texto;
        private boolean visible;

        private CommentBuiler(String id, String texto, boolean visible) {
            this.id = id;
            this.texto = texto;
            this.visible = visible;
        }
    }

    private static class RepuestoBuilder {
        private Long idRepuesto;
        private boolean nuevo;
        private int cantidad;

        private RepuestoBuilder(Long idRepuesto, boolean nuevo, int cantidad) {
            this.idRepuesto = idRepuesto;
            this.nuevo = nuevo;
            this.cantidad = cantidad;
        }
    }


    private static class Totalizadores {
        public Long id;
        public Double inicio;
        public Double fin;
    }


    public static class Calibres {
        public String calibre1;
        public String calibre2;
        public String calibre3;
        public String calibre4;
        public String calibre5;
        public String calibre6;
    }

    public static class Precinto {

        public String numero;
        public String numeroViejo;
        private String remplazado;

        public Precinto remplazado(int value) {
            this.remplazado = getPrecintoRemplazoString(value);
            return this;
        }

        private String getPrecintoRemplazoString(int integer) {
            switch (integer) {
                case 0: return "SI";
                case 1: return "NO";
                case 2: return "N_A";
            }
            return null;
        }
    }





}
