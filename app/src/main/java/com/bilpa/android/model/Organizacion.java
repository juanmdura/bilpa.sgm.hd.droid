package com.bilpa.android.model;

import android.content.Context;

import com.bilpa.android.R;

import java.io.Serializable;

public class Organizacion implements Serializable {

    private static final long serialVersionUID = -8867173587452506909L;

    private String organizacion;

    @Override
    public String toString() {
        return "Organizacion{" +
                "organizacion='" + organizacion + '\'' +
                '}';
    }

    public String getNombreOrganizacion() {
        if (organizacion != null && organizacion.length() > 0) {
            organizacion = organizacion.toLowerCase();
            return Character.toString(organizacion.charAt(0)).toUpperCase()+organizacion.substring(1);
        }
        return "";
    }

    public void setNombreOrganizacion(String nombreOrganizacion) {
        this.organizacion = nombreOrganizacion;
        this.organizacion = getNombreOrganizacion();
    }
}
