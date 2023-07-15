package com.bilpa.android.services.actions;


import com.bilpa.android.model.Repuesto;
import com.bilpa.android.model.TipoRepuesto;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TiposRepuestosResult extends BaseResult {

    @SerializedName("datos")
    public List<TipoRepuesto> tiposRepuesto;

    @Override
    public String toString() {
        return "TiposRepuestosResult{" +
                "tiposRepuesto=" + tiposRepuesto +
                "} " + super.toString();
    }
}
