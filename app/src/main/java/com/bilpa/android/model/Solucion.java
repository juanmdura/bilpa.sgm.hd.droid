package com.bilpa.android.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Solucion implements Serializable {

    private static final long serialVersionUID = -8536231676830021870L;



    public long id;
    public Tarea tarea;
    @SerializedName("fallaTecnica")
    public Falla falla;
    public DestinoCargo destinoDelCargo;
    public Comentario comentario;

    public String urlFoto;
    public String urlFoto2;

    @SerializedName("repuestos")
    public List<RepuestoRow> repuestosRows;

    public Pico pico;
    public List<Totalizadores> contadores;
    public Calibre calibre;
    public Precinto precinto;

    public Solucion() {
        this.repuestosRows = new ArrayList();
        this.contadores = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Solucion solucion = (Solucion) o;
        return id == solucion.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    public static class RepuestoRow implements Serializable {

        private static final long serialVersionUID = 695616722232524171L;

        public Long id;
        public Repuesto repuesto;
        public int cantidad;
        public boolean nuevo;

        @Override
        public String toString() {
            return "RepuestoRow{" +
                    "id=" + id +
                    ", repuesto=" + repuesto +
                    ", cantidad=" + cantidad +
                    ", nuevo=" + nuevo +
                    '}';
        }
    }

    public static class Totalizadores implements Serializable {

        private static final long serialVersionUID = 5694516180428284326L;

        public Long id;
        public Double inicio;
        public Double fin;

        @Override
        public String toString() {
            return "Totalizadores{" +
                    "id=" + id +
                    ", inicio=" + inicio +
                    ", fin=" + fin +
                    '}';
        }
    }

    public static class Calibre implements Serializable {
        private static final long serialVersionUID = -8003864043138950379L;
        public Long id;
        public Double calibre1;
        public Double calibre2;
        public Double calibre3;
        public Double calibre4;
        public Double calibre5;
        public Double calibre6;

    }

    public static class Precinto implements Serializable {
        private static final long serialVersionUID = 5855220071962636518L;
        public Long id;
        public String remplazado;
        public String numeroViejo;
        public String numero;
    }
}

