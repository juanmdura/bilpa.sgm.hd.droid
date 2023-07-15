package com.bilpa.android.model;

import java.io.Serializable;

public class DestinoCargo implements Serializable {

    private static final long serialVersionUID = -955809901649641807L;

    public int id;
    public String nombre;

    @Override
    public String toString() {
        return "DestinoCargo{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
