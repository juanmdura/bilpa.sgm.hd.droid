package com.bilpa.android.services.actions;


import com.bilpa.android.model.TipoFalla;
import com.bilpa.android.model.TipoTarea;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TiposFallaResult extends BaseResult {

    @SerializedName("datos")
    public List<TipoFalla> tiposFalla;


    @Override
    public String toString() {
        return "TiposFallaResult{" +
                "tiposFalla=" + tiposFalla +
                "} " + super.toString();
    }
}
