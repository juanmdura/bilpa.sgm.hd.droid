package com.bilpa.android.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ChequeoTanque extends ChequeoBase {

    private static final long serialVersionUID = 5741285229737933222L;

    public int tipoDeDescarga;
    public long medidaDelAgua = 0;

    public Chequeo getColorMarcoTapa() {
        return findByName(getLblColorMarcoTapa());
    }

    @NonNull
    public String getLblColorMarcoTapa() {
        return "colorMarcoTapa";
    }


    public Chequeo getTapaLomoTanque() {
        return findByName(getLblTapaLomoTanque());
    }

    @NonNull
    public String getLblTapaLomoTanque() {
        return "tapaLomoTanque";
    }


    public Chequeo getAguaTanque() {
        return findByName(getLblAguaTanque());
    }

    @NonNull
    public String getLblAguaTanque() {
        return "aguaTanque";
    }


    public Chequeo getVentilacion() {
        return findByName(getLblVentilacion());
    }

    @NonNull
    public String getLblVentilacion() {
        return "ventilacion";
    }

    @NonNull
    public String getLblOtros() {
        return "otrosTanque";
    }


    public Chequeo getOtros() {
        return findByName(getLblOtros());
    }

    public void setOtros(String selectedLabel, boolean checked) {
        setValues(getLblOtros(), selectedLabel, checked);
    }

    public void setColorMarcoTapa(String selectedLabel, boolean checked) {
        setValues(getLblColorMarcoTapa(), selectedLabel, checked);
    }

    public void setTapaLomoTanque(String selectedLabel, boolean checked) {
        setValues(getLblTapaLomoTanque(), selectedLabel, checked);
    }

    public void setAguaTanque(String selectedLabel, boolean checked) {
        setValues(getLblAguaTanque(), selectedLabel, checked);
    }

    public void setVentilacion(String selectedLabel, boolean checked) {
        setValues(getLblVentilacion(), selectedLabel, checked);
    }


}
