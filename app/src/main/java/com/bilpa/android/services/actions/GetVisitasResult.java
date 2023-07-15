package com.bilpa.android.services.actions;


import com.bilpa.android.model.Estacion;
import com.bilpa.android.model.VisitaAsignadas;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetVisitasResult extends BaseResult {

    @SerializedName("datos")
    public List<VisitaAsignadas> visitaAsignadas;

    @Override
    public String toString() {
        return "GetVisitasResult{" +
                "visitaAsignadas=" + visitaAsignadas +
                "} " + super.toString();
    }
}
