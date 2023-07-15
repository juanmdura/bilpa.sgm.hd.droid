package com.bilpa.android.services.actions;


import com.bilpa.android.model.Estacion;
import com.bilpa.android.model.Visita;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetVisitaResult extends BaseResult {

    @SerializedName("datos")
    public Visita visita;

    @Override
    public String toString() {
        return "GetVisita{" +
                "visita=" + visita +
                "} " + super.toString();
    }
}
