package com.bilpa.android.services.actions;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class SaveCorregidoAction extends PreventivosAction<SaveCorregidoResult> {

    private Map<String, String> params;
    private String body;
    private final Gson gson;

    public SaveCorregidoAction() {
        super(SaveCorregidoResult.class);
        gson = new Gson();
    }

    @Override
    protected String getOpertaion() {
        return "guardarCorregido";
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

        private Long idCorregido;
        private Long idPreventivo;
        private Integer idDestinoDelCargo;
        private Long idFalla;
        private Long idTarea;
        private CommentBuiler comentario;
        @SerializedName("repuestosLineaCorregidos")
        private List<RepuestoBuilder> repuestos;
        private String fotoBytes;
        private String foto2Bytes;
        private Long idPico;
        private Long idItemChequeado;
        private Long idPendiente;

        public Builder idCorregido(Long idCorregido) {
            this.idCorregido = idCorregido;
            return this;
        }

        public Builder idPreventivo(Long idPreventivo) {
            this.idPreventivo = idPreventivo;
            return this;
        }

        public Builder idDestinoDelCargo(int idDestinoDelCargo) {
            this.idDestinoDelCargo = idDestinoDelCargo;
            return this;
        }

        public Builder idFalla(Long idFalla) {
            this.idFalla = idFalla;
            return this;
        }

        public Builder idTarea(Long idTarea) {
            this.idTarea = idTarea;
            return this;
        }

        public Builder comentario(String value, boolean visible) {
            this.comentario = new CommentBuiler(null, value, visible);
            return this;
        }

        public Builder repuesto(Long id, Long idRepuesto, boolean nuevo, int cantidad) {
            if (repuestos == null) {
                repuestos = new ArrayList<>();
            }
            repuestos.add(new RepuestoBuilder(idRepuesto, nuevo, cantidad));
            return this;
        }

        public Builder fotoBytes(String fotoBytes) {
            this.fotoBytes = fotoBytes;
            return this;
        }

        public Builder foto2Bytes(String fotoBytes) {
            this.foto2Bytes = fotoBytes;
            return this;
        }

        public Builder idPico(Long idPico) {
            this.idPico = idPico;
            return this;
        }

        public Builder idItemChequeado(Long idChequeo) {
            this.idItemChequeado = idChequeo;
            return this;
        }

        public Builder idPendiente(Long idPendiente) {
            this.idPendiente = idPendiente;
            return this;
        }

        public SaveCorregidoAction create() {
            SaveCorregidoAction s = new SaveCorregidoAction();
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
        private Long id;
        private Long idRepuesto;
        private boolean nuevo;
        private int cantidad;

        private RepuestoBuilder(Long idRepuesto, boolean nuevo, int cantidad) {
            this.id = id;
            this.idRepuesto = idRepuesto;
            this.nuevo = nuevo;
            this.cantidad = cantidad;
        }
    }

}
