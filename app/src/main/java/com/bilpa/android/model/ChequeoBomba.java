package com.bilpa.android.model;

import android.support.annotation.NonNull;

public class ChequeoBomba extends ChequeoBase {

    private static final long serialVersionUID = 579847203829032242L;

    public Chequeo getSifon() {
        return findByName(getLblSifon());
    }
    public Chequeo getFugasSump() {
        return findByName(getLblFugasSump());
    }
    public Chequeo getSumpHermetico() {
        return findByName(getLblSumpHermetico());
    }
    public Chequeo getDetectorMecanico() {
        return findByName(getLblDetectorMecanico());
    }
    public Chequeo getOtros() {
        return findByName(getLblOtros());
    }

    @NonNull
    public String getLblSifon() {
        return "sifon";
    }
    @NonNull
    public String getLblFugasSump() {
        return "fugasSump";
    }
    @NonNull
    public String getLblSumpHermetico() {
        return "sumpHermetico";
    }
    @NonNull
    public String getLblDetectorMecanico() {
        return "detectorMecanicoFuga";
    }
    @NonNull
    public String getLblOtros() {
        return "otrosBomba";
    }

    public void setSifon(String selectedLabel, boolean checked) {
        setValues(getLblSifon(), selectedLabel, checked);
    }
    public void setFugasSump(String selectedLabel, boolean checked) {
        setValues(getLblFugasSump(), selectedLabel, checked);
    }
    public void setSumpHermetico(String selectedLabel, boolean checked) {
        setValues(getLblSumpHermetico(), selectedLabel, checked);
    }
    public void setDetectorMecanico(String selectedLabel, boolean checked) {
        setValues(getLblDetectorMecanico(), selectedLabel, checked);
    }
    public void setOtros(String selectedLabel, boolean checked) {
        setValues(getLblOtros(), selectedLabel, checked);
    }
}
