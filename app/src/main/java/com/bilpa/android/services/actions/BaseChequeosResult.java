package com.bilpa.android.services.actions;


import com.bilpa.android.model.Chequeo;
import com.bilpa.android.model.ChequeoSurtidor;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public abstract class BaseChequeosResult extends BaseResult {

    public abstract List<Chequeo> getChequeos();

    public final Chequeo getChequeoById(long chequeoId) {
        List<Chequeo> chequeos = getChequeos();
        for (Chequeo chequeo : chequeos) {
            if (chequeo.id.equals(chequeoId)) {
                return chequeo;
            }
        }
        return null;
    }

}
