package com.bilpa.android.model;

import java.io.Serializable;

public class Producto implements Serializable {

    private static final long serialVersionUID = 2591031390065792432L;

    public Long id;
    public String codigo;
    public String nombre;

    @Override
    public String toString() {
        return "Producto{" +
                "id=" + id +
                ", codigo='" + codigo + '\'' +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
