package com.bilpa.android.services.actions;


import com.bilpa.android.model.TipoDescarga;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TipoDescargaResult extends BaseResult {

    @SerializedName("datos")
    public List<TipoDescarga> tiposDescarga;

    @Override
    public String toString() {
        return "TipoDescargaResult{" +
                "tiposDescarga=" + tiposDescarga +
                "} " + super.toString();
    }

    public TipoDescarga find(int id) {
        for (TipoDescarga tipoDescarga : tiposDescarga) {
            if (id == tipoDescarga.id) {
                return tipoDescarga;
            }
        }
        return null;
    }
}
