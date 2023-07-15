package com.bilpa.android.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ChequeoBase implements Serializable {

    private static final long serialVersionUID = 579847203829032242L;

    public Long id;
    @SerializedName("itemsChequeados")
    public List<Chequeo> items;
    public boolean fueModificado;

    public ChequeoBase() {
        this.items = new ArrayList<>();
    }

    protected Chequeo findByName(String name) {
        if (name != null) {
           for (Chequeo item : items) {
                if (name.equalsIgnoreCase(item.nombre)) {
                    return item;
                }
            }
        }
        return null;
    }

    public Chequeo findById(Long id) {
        if (id != null) {
            for (Chequeo item : items) {
                if (id.equals(item.id)) {
                    return item;
                }
            }
        }
        return null;
    }

    protected void setValues(String nombre, String selectedLabel, boolean checked) {
        Chequeo c = findByName(nombre);
        if (c == null) {
            c = new Chequeo();
            c.nombre = nombre;
            items.add(c);
        }
        c.valor = selectedLabel;
        c.pendiente = checked;
    }
}
