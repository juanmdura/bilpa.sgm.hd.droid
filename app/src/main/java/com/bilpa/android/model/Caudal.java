package com.bilpa.android.model;

import java.io.Serializable;

public class Caudal implements Serializable {

    private static final long serialVersionUID = -1219326642932233490L;

    public Long id;
    public Integer tiempo;
    public Double litrosPorMinuto;
    public Double volumen;

    @Override
    public String toString() {
        return "Caudal{" +
                "id=" + id +
                ", tiempo=" + tiempo +
                ", litrosPorMinuto=" + litrosPorMinuto +
                ", volumen=" + volumen +
                '}';
    }
}
