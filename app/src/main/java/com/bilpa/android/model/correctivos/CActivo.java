package com.bilpa.android.model.correctivos;

import java.io.Serializable;

public class CActivo implements Serializable {

    public static final int TYPE_SURTIDOR = 1;
    public static final int TYPE_TANQUE = 2;
    public static final int TYPE_BOMBA = 4;

    public Long id;
    public int tipo;
    public String estado;
    public String display;
    public boolean tieneReparaciones;
    public boolean tienePendientes;
    public QR qr;

    @Override
    public String toString() {
        return "CActivo{" +
                "id=" + id +
                ", tipo=" + tipo +
                ", estado='" + estado + '\'' +
                ", display='" + display + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CActivo cActivo = (CActivo) o;

        return !(id != null ? !id.equals(cActivo.id) : cActivo.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public boolean isSurtidor() {
        return TYPE_SURTIDOR == Integer.valueOf(tipo);
    }

    public boolean isBomba() {
        return TYPE_BOMBA == Integer.valueOf(tipo);
    }

    public boolean isTanque() {
        return TYPE_TANQUE == Integer.valueOf(tipo);
    }

    public static class QR implements Serializable {

        private static final long serialVersionUID = 8803624850450912961L;

        public Long id;
        public String codigo;
    }

    public String getTipoLabel() {
        switch (tipo) {
            case TYPE_BOMBA:
                return "BOMBA";
            case TYPE_SURTIDOR:
                return "SURTIDOR";
            case TYPE_TANQUE:
                return "TANQUE";

        }
        return null;
    }

}
