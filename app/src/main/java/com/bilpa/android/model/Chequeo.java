package com.bilpa.android.model;

import java.io.Serializable;

public class Chequeo implements Serializable {

    private static final long serialVersionUID = -2905919075179111187L;

    public Long id;
    public String texto;
    public String nombre;
    public String valor;
    public boolean pendiente;
    public int countPendientes;
    public int countCorregidos;

    @Override
    public String toString() {
        return "Chequeo{" +
                "id=" + id +
                ", texto='" + texto + '\'' +
                ", nombre='" + nombre + '\'' +
                ", valor='" + valor + '\'' +
                ", pendiente=" + pendiente +
                ", countPendientes=" + countPendientes +
                ", countCorregidos=" + countCorregidos +
                '}';
    }
}
