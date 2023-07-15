package com.bilpa.android.model.correctivos;

import java.io.Serializable;

public class CReparacion implements Serializable {

    private static final long serialVersionUID = -7761536131713226990L;

    public int id;
    public int tipo;
    public FallaReportada fallaReportada;
    public String comentario;

    public static class FallaReportada implements Serializable {

        private static final long serialVersionUID = 3906333990889831334L;

        public int id;
        public int tipo;
        public String descripcion;

        @Override
        public String toString() {
            return "descripcion='" + descripcion + '\'';
        }
    }

    @Override
    public String toString() {
        return "CReparacion{" +
                "id=" + id +
                ", comentario='" + comentario + '\'' +
                ", fallaReportada=" + fallaReportada +
                '}';
    }
}
