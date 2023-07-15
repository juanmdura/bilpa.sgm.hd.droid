package com.bilpa.android.model;

import java.io.Serializable;

public class Falla implements Serializable {

    private static final long serialVersionUID = 2506700563467347335L;

    public long id;
    public long tipo;
    public long subTipo;
    public String descripcion;
    public boolean inactiva;

    @Override
    public String toString() {
        return "Falla{" +
                "id=" + id +
                ", tipo=" + tipo +
                ", subTipo=" + subTipo +
                ", descripcion='" + descripcion + '\'' +
                ", inactiva=" + inactiva +
                '}';
    }
}
