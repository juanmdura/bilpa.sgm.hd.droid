package com.bilpa.android.services.actions;


import com.bilpa.android.model.Chequeo;
import com.bilpa.android.model.ChequeoTanque;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class GetChequeoTanqueResult extends BaseChequeosResult {

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

        private static final long serialVersionUID = 4327581057521646907L;

        public long idPreventivo;

        @SerializedName("chequeo")
        public ChequeoTanque chequeo;


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
