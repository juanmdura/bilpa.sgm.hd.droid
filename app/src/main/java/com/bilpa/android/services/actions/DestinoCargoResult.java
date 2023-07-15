package com.bilpa.android.services.actions;


import com.bilpa.android.model.DestinoCargo;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DestinoCargoResult extends ListResult<DestinoCargo> {

    private static final long serialVersionUID = -3610524505429046919L;

    @SerializedName("datos")
    public List<DestinoCargo> cargos;

    @Override
    public List<DestinoCargo> getItems() {
        return cargos;
    }
}
