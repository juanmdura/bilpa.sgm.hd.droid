package com.bilpa.android.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Visita implements Serializable {


    private static final long serialVersionUID = -7687087992978330729L;

    public long id;

    public Date fechaInicio;
    public Date fechaFin;
    public String fechaProximaVisita;
    public String nombreEstacion;
    @SerializedName("url")
    public String signUrl;
    @SerializedName("comentarioFirma")
    public String signComment;
    @SerializedName("listaActivos")
    public List<Activo> activos;
    public String estado;

    public Visita() {
        this.activos = new ArrayList<Activo>();
    }

    @Override
    public String toString() {
        return "Visita{" +
                "id=" + id +
                ", nombreEstacion='" + nombreEstacion + '\'' +
                ", activos=" + activos +
                '}';
    }
}
