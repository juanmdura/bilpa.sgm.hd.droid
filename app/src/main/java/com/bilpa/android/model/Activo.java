package com.bilpa.android.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Activo implements Serializable {

    public static final int TYPE_SURTIDOR = 1;
    public static final int TYPE_TANQUE = 2;
    public static final int TYPE_BOMBA = 4;
    public static final int TYPE_GENERIC = 6;

    public enum StatusActivo {
        SIN_INICIAR, INICIADO, COMPLETO, NONE
    }

    private static final long serialVersionUID = 6953061730915819876L;

    public long id;
    public String tipoId;
    public String tipo;
    public String descripcion;
    public int codigoQR;
    public String estadoPreventivo;

    public StatusActivo getStatus() {
        if (estadoPreventivo == null) {
            return StatusActivo.NONE;
        } else {
            try {
                return StatusActivo.valueOf(estadoPreventivo);
            } catch (Exception e) {
                return StatusActivo.NONE;
            }
        }
    }

    @Override
    public String toString() {
        return "Activo{" +
                "id=" + id +
                ", tipoId='" + tipoId + '\'' +
                ", tipo='" + tipo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }

    public boolean isSurtidor() {
        return TYPE_SURTIDOR == Integer.valueOf(tipoId);
    }

    public boolean isBomba() {
        return TYPE_BOMBA == Integer.valueOf(tipoId);
    }

    public boolean isTanque() {
        return TYPE_TANQUE == Integer.valueOf(tipoId);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Activo activo = (Activo) o;

        if (id != activo.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
