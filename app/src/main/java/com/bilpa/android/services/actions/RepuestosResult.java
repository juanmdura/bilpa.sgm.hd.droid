package com.bilpa.android.services.actions;


import com.bilpa.android.model.Estacion;
import com.bilpa.android.model.Repuesto;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RepuestosResult extends BaseResult {

    @SerializedName("datos")
    public List<Repuesto> repuestos;

    @Override
    public String toString() {
        return "RepuestosResult{" +
                "repuestos=" + repuestos +
                "} " + super.toString();
    }
}
