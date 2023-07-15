package com.bilpa.android.model;

import java.io.Serializable;

public class TipoRepuesto implements Serializable {

    private static final long serialVersionUID = -4483387183053609629L;

    public long id;
    public String descripcion;
    public boolean inactivo;

    @Override
    public String toString() {
        return "TipoRepuesto{" +
                "id=" + id +
                ", descripcion='" + descripcion + '\'' +
                ", inactivo=" + inactivo +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TipoRepuesto that = (TipoRepuesto) o;

        if (id != that.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
