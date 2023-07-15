package com.bilpa.android.services.correctivos;


import com.bilpa.android.model.Solucion;
import com.bilpa.android.services.actions.BaseResult;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GetSolucionesResult extends BaseResult {

    private static final long serialVersionUID = 1970346197573305498L;

    @SerializedName("datos")
    public List<Solucion> data;

    public GetSolucionesResult() {
        this.data = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "GetSolucionesResult{" +
                "data=" + data +
                '}';
    }
}
