package com.bilpa.android.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by peludev on 04/02/15.
 */
public class Estacion implements Serializable {

    private static final long serialVersionUID = -7724122996745236830L;

    public Long id;
    public String numeroSerie;
    public String sello;
    public String zona;
    public String tiempoRespuesta;
    public String nombre;
    public String localidad;
    public String departamento;
    public String direccion;
    public String telefono;
    public String rut;

    @Override
    public String toString() {
        return "Estacion{" +
                "id=" + id +
                ", numeroSerie='" + numeroSerie + '\'' +
                ", sello='" + sello + '\'' +
                ", zona='" + zona + '\'' +
                ", tiempoRespuesta='" + tiempoRespuesta + '\'' +
                ", nombre='" + nombre + '\'' +
                ", localidad='" + localidad + '\'' +
                ", departamento='" + departamento + '\'' +
                ", direccion='" + direccion + '\'' +
                ", telefono='" + telefono + '\'' +
                ", rut='" + rut + '\'' +
                '}';
    }
}
