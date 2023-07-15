package com.bilpa.android.services.actions.products;


import com.bilpa.android.model.Chequeo;
import com.bilpa.android.model.ChequeoProducto;
import com.bilpa.android.services.actions.BaseChequeosResult;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class GetChequeoProductoResult extends BaseChequeosResult {

    private static final long serialVersionUID = -4099617461593701505L;

    @SerializedName("datos")
    public Data data;

    @Override
    public String toString() {
        return "GetChequeoTanqueResult{" +
                "data=" + data +
                "} " + super.toString();
    }

    public class Data implements Serializable {

        private static final long serialVersionUID = -6057382163381923948L;

        public long idPreventivo;

        @SerializedName("chequeo")
        public ChequeoProducto chequeo;

        @Override
        public String toString() {
            return "Data{" +
                    "idPreventivo=" + idPreventivo +
                    ", chequeo=" + chequeo +
                    '}';
        }


    }

    @Override
    public List<Chequeo> getChequeos() {
        return this.data.chequeo.items;
    }
}
