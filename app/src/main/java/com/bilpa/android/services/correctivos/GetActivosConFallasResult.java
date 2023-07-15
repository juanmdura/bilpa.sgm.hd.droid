package com.bilpa.android.services.correctivos;


import com.bilpa.android.model.correctivos.CActivo;
import com.bilpa.android.model.correctivos.FallaActivo;
import com.bilpa.android.services.actions.BaseResult;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GetActivosConFallasResult extends BaseResult {

    @SerializedName("datos")
    public List<FallaActivo> data;

    public GetActivosConFallasResult() {
        this.data = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "GetActivosConFallasResult{" +
                "data=" + data +
                '}';
    }

    public FallaActivo findByActivo(CActivo activo) {
        if (activo != null) {
            for (FallaActivo fallaActivo : data) {
                if (activo.equals(fallaActivo.activo)) {
                    return fallaActivo;
                }
            }
        }
        return null;
    }
}
