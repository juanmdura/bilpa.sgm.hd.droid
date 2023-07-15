package com.bilpa.android.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SectionDetail implements Serializable {

    private static final long serialVersionUID = -3414274178669693830L;

    @SerializedName("label")
    public String lbl;

    @SerializedName("valor")
    public String val;


    @SerializedName("formato")
    public String format;
//    "formato": "rowCompleta"
//    "formato": "rowSimple"
//    "formato": "rowVacia"
//    "formato": "rowValor"

    public String color;


    @Override
    public String toString() {
        return "SectionDetail{" +
                "lbl='" + lbl + '\'' +
                ", val='" + val + '\'' +
                '}';
    }

    public boolean isBold() {
        if (lbl != null && val != null) {
            if ("Manguera".equalsIgnoreCase(lbl) && "Litros despachados".equalsIgnoreCase(val)) {
                return true;
            }
        }

        if (lbl != null) {
            if ("Valores por manguera".equalsIgnoreCase(lbl)) {
                return true;
            }

            if ("Elementos que necesitaron corrección".equalsIgnoreCase(lbl)) {
                return true;
            }

            if ("Elementos que quedaron pendientes".equalsIgnoreCase(lbl)) {
                return true;
            }

        }

        return false;
    }

}
