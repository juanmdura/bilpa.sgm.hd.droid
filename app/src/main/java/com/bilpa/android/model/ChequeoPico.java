package com.bilpa.android.model;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class ChequeoPico extends ChequeoBase {

    private static final long serialVersionUID = 636866982800941664L;

    public Double totalizadorMecanicoInicial;
    public Double totalizadorMecanicoFinal;
    public Double totalizadorElectronicoInicial;
    public Double totalizadorElectronicoFinal;
    public Double calibre1;
    public Double calibre2;
    public Double calibre3;
    public Double calibre4;
    public Double calibre5;
    public Double calibre6;

    public String idPreventivo;
    public Pico pico;
    public Caudal caudal;
    public Precinto precinto;

    @Override
    public String toString() {
        return "ChequeoPico{" +
                "id=" + id +
                ", idPreventivo='" + idPreventivo + '\'' +
                ", totalizadorMecanicoInicial=" + totalizadorMecanicoInicial +
                '}';
    }

    public Chequeo getPicoForroManguera() {
        return findByName(getLblPicoForroManguera());
    }
    public Chequeo getSistemaBloqueo() {
        return findByName(getLblSistemaBloqueo());
    }
    public Chequeo getIdentificacionDelProducto() {
        return findByName(getLblIdentificacionDelProducto());
    }
    public Chequeo getPredeterminacion() {
        return findByName(getLblPredeterminacion());
    }
    public Chequeo getFugas() {
        return findByName(getLblFugas());
    }
    public Chequeo getOtros() {
        return findByName(getLblOtros());
    }

    @NonNull
    public String getLblPicoForroManguera() {
        return "picoForroManguera";
    }
    @NonNull
    public String getLblSistemaBloqueo() {
        return "sistemaBloqueo";
    }
    @NonNull
    public String getLblIdentificacionDelProducto() {
        return "identificacionDelProducto";
    }
    @NonNull
    public String getLblPredeterminacion() {
        return "predeterminacion";
    }
    @NonNull
    public String getLblFugas() {
        return "fugas";
    }
    @NonNull
    public String getLblOtros() {
        return "otrosPico";
    }

    public void setPicoForroManguera(String selectedLabel, boolean checked) {
        setValues(getLblPicoForroManguera(), selectedLabel, checked);
    }
    public void setSistemaBloqueo(String selectedLabel, boolean checked) {
        setValues(getLblSistemaBloqueo(), selectedLabel, checked);
    }
    public void setIdentificacionDelProducto(String selectedLabel, boolean checked) {
        setValues(getLblIdentificacionDelProducto(), selectedLabel, checked);
    }
    public void setPredeterminacion(String selectedLabel, boolean checked) {
        setValues(getLblPredeterminacion(), selectedLabel, checked);
    }
    public void setFugas(String selectedLabel, boolean checked) {
        setValues(getLblFugas(), selectedLabel, checked);
    }
    public void setOtros(String selectedLabel, boolean checked) {
        setValues(getLblOtros(), selectedLabel, checked);
    }

    public Double getCaudalVolumen() {
        if (caudal != null) {
            return caudal.volumen;
        }
        return null;
    }

    public Integer getCaudalTiempo() {
        if (caudal != null) {
            return caudal.tiempo;
        }
        return null;
    }
}
