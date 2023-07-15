package com.bilpa.android.services.actions;


import com.bilpa.android.model.Falla;
import com.bilpa.android.model.Tarea;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FallasResult extends BaseResult {

    @SerializedName("datos")
    public List<Falla> fallas;

    @Override
    public String toString() {
        return "FallasResult{" +
                "fallas=" + fallas +
                "} " + super.toString();
    }
}
