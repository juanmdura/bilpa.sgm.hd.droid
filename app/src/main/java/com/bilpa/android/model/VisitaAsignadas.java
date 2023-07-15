package com.bilpa.android.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by peludev on 04/02/15.
 */
public class VisitaAsignadas implements Serializable {


    private static final long serialVersionUID = -7724122996745236830L;

    public Long id;
    public Date fechaProximaVisita;
    public Date fechaUltimaVisita;
    public String estado;

    @SerializedName("diasSinVisita")
    public int diasSinVisitas;

    @SerializedName("tecnicoData")
    public Tecnico tecnico;

    @SerializedName("estacionData")
    public Estacion estacion;

    public VisitaAsignadas() {
    }

    @Override
    public String toString() {
        return "VisitaAsignadas{" +
                "id=" + id +
                ", fechaProximaVisita=" + fechaProximaVisita +
                ", fechaUltimaVisita=" + fechaUltimaVisita +
                ", estado='" + estado + '\'' +
                ", diasSinVisitas=" + diasSinVisitas +
                ", tecnico=" + tecnico +
                ", estacion=" + estacion +
                '}';
    }
}
