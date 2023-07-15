package com.bilpa.android.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class Comentario implements Serializable {

    private static final long serialVersionUID = 8124766872092514927L;

    public String id;

    // "Oct 22, 2016 2:06:02 PM"
    @SerializedName("")
    public Date fecha;

    public String texto;

    @SerializedName("imprimible")
    public boolean visible;

    @SerializedName("activo")
    public boolean activo;

    @Override
    public String toString() {
        return "Comentario{" +
                "texto='" + texto + '\'' +
                ", visible=" + visible +
                '}';
    }
}
