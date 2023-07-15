package com.bilpa.android.model;

import java.io.Serializable;

public class Pico implements Serializable {

    private static final long serialVersionUID = -3480453725941943693L;

    public long id;
    public long numeroPico;
    public long numeroEnLaEstacion;
    public String tipoCombusitble;
    public Long idSurtidor;
    public String codigoQR;

    @Override
    public String toString() {
        return "Pico{" +
                "id=" + id +
                ", numeroPico=" + numeroPico +
                ", numeroEnLaEstacion=" + numeroEnLaEstacion +
                ", tipoCombusitble='" + tipoCombusitble + '\'' +
                ", idSurtidor='" + idSurtidor + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pico pico = (Pico) o;

        if (id != pico.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
