package com.bilpa.android.services.actions;


import com.bilpa.android.model.Repuesto;
import com.bilpa.android.model.Tarea;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TareasResult extends BaseResult {

    @SerializedName("datos")
    public List<Tarea> tareas;

    @Override
    public String toString() {
        return "TareasResult{" +
                "tareas=" + tareas +
                "} " + super.toString();
    }
}
