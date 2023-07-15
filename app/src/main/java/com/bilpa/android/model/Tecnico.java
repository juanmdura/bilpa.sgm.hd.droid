package com.bilpa.android.model;

import java.io.Serializable;

public class Tecnico implements Serializable {

    private static final long serialVersionUID = 2277852441201997908L;

    public int id;
    public String nomnbre;
    public String apellido;
    public String nombreUsuario;
    public String nombreCompleto;

    @Override
    public String toString() {
        return "Tecnico{" +
                "id=" + id +
                ", nomnbre='" + nomnbre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                ", nombreCompleto='" + nombreCompleto + '\'' +
                '}';
    }
}
