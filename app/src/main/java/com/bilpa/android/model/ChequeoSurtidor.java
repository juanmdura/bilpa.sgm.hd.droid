package com.bilpa.android.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ChequeoSurtidor extends ChequeoBase {

    private static final long serialVersionUID = 579847203829032242L;

    public ChequeoSurtidor() {
        this.items = new ArrayList<>();
    }

    public Chequeo getCabezalLimpiezaSellado() {
        return findByName(getLblCabezalLimpiezaSellado());
    }

    public Chequeo getPruebaDisplayIluminacion() {
        return findByName(getLblPruebaDisplayIluminacion());
    }

    public Chequeo getVisualYLimpieza() {
        return findByName(getLblVisualYLimpieza());
    }

    public Chequeo getPresetCara1() {
        return findByName(getLblPresetCara1());
    }

    public Chequeo getPresetCara2() {
        return findByName(getLblPresetCara2());
    }

    public Chequeo getParteElectrica() {
        return findByName(getLblParteElectrica());
    }

    public Chequeo getPlanSellado() {
        return findByName(getLblPlanSellado());
    }

    public Chequeo getOtros() {
        return findByName(getLblOtros());
    }

    @NonNull
    public String getLblCabezalLimpiezaSellado() {
        return "cabezalLimpiezaSellado";
    }

    @NonNull
    public String getLblPruebaDisplayIluminacion() {
        return "pruebaDisplayIluminacion";
    }

    @NonNull
    public String getLblVisualYLimpieza() {
        return "visualYLimpieza";
    }

    @NonNull
    public String getLblPresetCara1() {
        return "presetCara1";
    }

    @NonNull
    public String getLblPresetCara2() {
        return "presetCara2";
    }

    @NonNull
    public String getLblParteElectrica() {
        return "parteElectrica";
    }

    @NonNull
    public String getLblPlanSellado() {
        return "planSellado";
    }

    @NonNull
    public String getLblOtros() {
        return "otrosSurtidor";
    }

    @Override
    public String toString() {
        return "ChequeoSurtidor{" +
                "id=" + id +
                ", items=" + items +
                ", fueModificado=" + fueModificado +
                '}';
    }

    public void setCabezalLimiezaSellado(String selectedLabel, boolean checked) {
        setValues(getLblCabezalLimpiezaSellado(), selectedLabel, checked);
    }

    public void setPresetCara1(String selectedLabel, boolean checked) {
        setValues(getLblPresetCara1(), selectedLabel, checked);
    }

    public void setPresetCara2(String selectedLabel, boolean checked) {
        setValues(getLblPresetCara2(), selectedLabel, checked);
    }

    public void setPruebaDisplayIluminacion(String selectedLabel, boolean checked) {
        setValues(getLblPruebaDisplayIluminacion(), selectedLabel, checked);
    }

    public void setVisualYLimpieza(String selectedLabel, boolean checked) {
        setValues(getLblVisualYLimpieza(), selectedLabel, checked);
    }

    public void setParteElectrica(String selectedLabel, boolean checked) {
        setValues(getLblParteElectrica(), selectedLabel, checked);
    }

    public void setPlanSellado(String selectedLabel, boolean checked) {
        setValues(getLblPlanSellado(), selectedLabel, checked);
    }

    public void setOtros(String selectedLabel, boolean checked) {
        setValues(getLblOtros(), selectedLabel, checked);
    }


}
