package com.bilpa.android.model;

import java.io.Serializable;

public class TipoFalla implements Serializable {

    private static final long serialVersionUID = 6229538483886356662L;

    public long id;
    public String descripcion;
    public boolean inactivo;

    @Override
    public String toString() {
        return "TipoTarea{" +
                "id=" + id +
                ", descripcion='" + descripcion + '\'' +
                ", inactivo=" + inactivo +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TipoFalla that = (TipoFalla) o;

        if (id != that.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
