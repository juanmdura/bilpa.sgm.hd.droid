package com.bilpa.android.model;

import java.io.Serializable;

public class TipoDescarga implements Serializable {

    private static final long serialVersionUID = -408781619196973958L;

    public int id;
    public String nombre;

    @Override
    public String toString() {
        return "TipoDescarga{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TipoDescarga that = (TipoDescarga) o;

        if (id != that.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
