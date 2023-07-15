package com.bilpa.android.services.actions;


import com.bilpa.android.model.TipoRepuesto;
import com.bilpa.android.model.TipoTarea;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TiposTareaResult extends BaseResult {

    @SerializedName("datos")
    public List<TipoTarea> tipoTareas;


    @Override
    public String toString() {
        return "TiposTareaResult{" +
                "tipoTareas=" + tipoTareas +
                "} " + super.toString();
    }
}
