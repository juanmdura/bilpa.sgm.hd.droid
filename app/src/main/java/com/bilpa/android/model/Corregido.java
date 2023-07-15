package com.bilpa.android.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Corregido implements Serializable {

    private static final long serialVersionUID = 5783703122694467819L;

    public long id;
    public long idPreventivo;

    public Long idItemChequeado;
    public String textoItemChequado;
    public Falla falla;
    public Tarea tarea;
    public DestinoCargo destinoDelCargo;
    public Comentario comentario;
    public String urlFoto;
    public String urlFoto2;
    public Pico pico;

    @SerializedName("listaDeRepuestos")
    public List<RepuestoItem> repuestos;

    public Corregido() {
        this.repuestos = new ArrayList();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Corregido corregido = (Corregido) o;

        if (id != corregido.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}

