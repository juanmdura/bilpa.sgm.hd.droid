package com.bilpa.android.model;

import java.io.Serializable;

public class RepuestoItem implements Serializable {

    private static final long serialVersionUID = -7067015762357412640L;

    public Long id;
    public Long idRepuesto;
    public int cantidad;
    public boolean nuevo;
    public String descripcion;

    @Override
    public String toString() {
        return "RepuestoItem{" +
                "id=" + id +
                ", idRepuesto=" + idRepuesto +
                ", cantidad=" + cantidad +
                ", nuevo=" + nuevo +
                '}';
    }
}
