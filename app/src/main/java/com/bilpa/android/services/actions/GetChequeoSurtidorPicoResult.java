package com.bilpa.android.services.actions;


import com.bilpa.android.model.Chequeo;
import com.bilpa.android.model.ChequeoPico;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class GetChequeoSurtidorPicoResult extends BaseChequeosResult {

    private static final long serialVersionUID = -388744166075684804L;

    @SerializedName("datos")
    public Data data;

    @Override
    public String toString() {
        return "GetChequeoTanqueResult{" +
                "data=" + data +
                "} " + super.toString();
    }

    public class Data implements Serializable {

        private static final long serialVersionUID = 1766334683808582168L;

        public long idPreventivo;

        @SerializedName("chequeo")
        public ChequeoPico chequeo;

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
