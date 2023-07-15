package com.bilpa.android.services.actions;


import com.bilpa.android.model.Corregido;
import com.bilpa.android.model.Visita;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CorregidosResult extends BaseResult {

    private static final long serialVersionUID = -3610524505429046919L;

    @SerializedName("datos")
    public List<Corregido> corregidos;

}
