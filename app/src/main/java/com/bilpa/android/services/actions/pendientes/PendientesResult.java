package com.bilpa.android.services.actions.pendientes;


import com.bilpa.android.model.Pendiente;
import com.bilpa.android.services.actions.BaseResult;
import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PendientesResult extends BaseResult {

    private static final long serialVersionUID = 1393072572426278678L;

    @SerializedName("datos")
    public List<Pendiente> pendientes;


    @Override
    public String toString() {
        return "PendientesResult{" +
                "pendientes=" + pendientes +
                "} " + super.toString();
    }


    public void sortByPlazo() {
        Collections.sort(pendientes, new Comparator<Pendiente>() {
            @Override
            public int compare(Pendiente lhs, Pendiente rhs) {

                if (lhs.plazo == null && rhs.plazo == null) {
                    return 0;
                } else if (lhs.plazo == null && rhs.plazo != null) {
                    return 1;
                } else if (lhs.plazo != null && rhs.plazo == null) {
                    return -1;
                } else {
                    if (lhs.plazo.before(rhs.plazo)) {
                        return -1;
                    } else if (lhs.plazo.after(rhs.plazo)) {
                        return 1;
                    }
                    return 0;
                }
            }
        });
    }

}
