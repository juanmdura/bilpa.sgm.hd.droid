package com.bilpa.android.services.actions;


import com.bilpa.android.model.Organizacion;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrganizacionesResult extends ListResult<Organizacion> {

    private static final long serialVersionUID = -3610524505429046919L;

    @SerializedName("datos")
    public List<Organizacion> organizaciones;

    public OrganizacionesResult() {
    }

    @Override
    public List<Organizacion> getItems() {
        return organizaciones;
    }
}
