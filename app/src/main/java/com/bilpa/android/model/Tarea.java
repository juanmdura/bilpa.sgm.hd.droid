package com.bilpa.android.model;

import java.io.Serializable;

public class Tarea implements Serializable {

    private static final long serialVersionUID = 3556100906877806366L;

    public long id;
    public String descripcion;
    public boolean inactiva;
    public TipoTarea tipoTarea;

    @Override
    public String toString() {
        return "Tarea{" +
                "id=" + id +
                ", descripcion='" + descripcion + '\'' +
                ", inactiva=" + inactiva +
                ", tipoTarea=" + tipoTarea +
                '}';
    }
}
