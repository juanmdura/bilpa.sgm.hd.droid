package com.bilpa.android.services.actions;


import com.bilpa.android.model.Chequeo;
import com.bilpa.android.model.ChequeoBomba;
import com.bilpa.android.model.ChequeoSurtidor;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class GetChequeoSurtidorResult extends BaseChequeosResult {

    private static final long serialVersionUID = -388744166075684804L;

    @SerializedName("datos")
    public Data data;

    @Override
    public String toString() {
        return "GetChequeoTanqueResult{" +
                "data=" + data +
                "} " + super.toString();
    }

    @Override
    public List<Chequeo> getChequeos() {
        return this.data.chequeo.items;
    }

    public class Data implements Serializable {

        private static final long serialVersionUID = 1766334683808582168L;

        public long idPreventivo;

        @SerializedName("chequeo")
        public ChequeoSurtidor chequeo;

        @Override
        public String toString() {
            return "Data{" +
                    "idPreventivo=" + idPreventivo +
                    ", chequeo=" + chequeo +
                    '}';
        }
    }
}
