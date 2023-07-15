package com.bilpa.android.model;

import java.io.Serializable;

public class Repuesto implements Serializable {

    private static final long serialVersionUID = 5168905414034218395L;

    public Long id;
    public String descripcion;
    public String nroSerie;
    public boolean inactivo;
    public int precio;
    public TipoRepuesto tipoRepuesto;

    @Override
    public String toString() {
        return "\nRepuesto{" +
                "id=" + id +
                ", descripcion='" + descripcion + '\'' +
                ", nroSerie='" + nroSerie + '\'' +
                ", inactivo=" + inactivo +
                ", precio=" + precio +
                ", tipoRepuesto=" + tipoRepuesto +
                '}';
    }
}
