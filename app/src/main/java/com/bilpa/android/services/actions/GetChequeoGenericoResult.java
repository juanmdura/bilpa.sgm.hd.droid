package com.bilpa.android.services.actions;


import com.bilpa.android.model.Chequeo;
import com.bilpa.android.model.ChequeoGeneric;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class GetChequeoGenericoResult extends BaseChequeosResult {

    private static final long serialVersionUID = 7882106234882289114L;

    @SerializedName("datos")
    public Data data;

    @Override
    public String toString() {
        return "GetChequeoTanqueResult{" +
                "data=" + data +
                "} " + super.toString();
    }

    public class Data implements Serializable {

        private static final long serialVersionUID = -1294882959209862555L;

        public long idPreventivo;

        @SerializedName("chequeo")
        public ChequeoGeneric chequeo;

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
