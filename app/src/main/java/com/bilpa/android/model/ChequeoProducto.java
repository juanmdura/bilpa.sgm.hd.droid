package com.bilpa.android.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class ChequeoProducto extends ChequeoBase {

    private static final long serialVersionUID = -7337595585000025408L;


    public Chequeo getMotorUnidadBombeo() {
        return findByName(getLblMotorUnidadBombeo());
    }
    public Chequeo getEliminadorAireGas() {
        return findByName(getLblEliminadorAireGas());
    }
    public Chequeo getCorreasPoleas() {
        return findByName(getLblCorreasPoleas());
    }

    public Chequeo getOtros() {
        return findByName(getLblOtros());
    }

    @NonNull
    public String getLblMotorUnidadBombeo() {
        return "motorUnidadBombeo";
    }
    @NonNull
    public String getLblEliminadorAireGas() {
        return "eliminadorAireGas";
    }
    @NonNull
    public String getLblCorreasPoleas() {
        return "correasPoleas";
    }
    @NonNull
    public String getLblOtros() {
        return "otrosProducto";
    }

    public void setMotorUnidadBombeo(String selectedLabel, boolean checked) {
        setValues(getLblMotorUnidadBombeo(), selectedLabel, checked);
    }
    public void setEliminadorAireGas(String selectedLabel, boolean checked) {
        setValues(getLblEliminadorAireGas(), selectedLabel, checked);
    }
    public void setCorreasPoleas(String selectedLabel, boolean checked) {
        setValues(getLblCorreasPoleas(), selectedLabel, checked);
    }
    public void setOtros(String selectedLabel, boolean checked) {
        setValues(getLblOtros(), selectedLabel, checked);
    }

    @Override
    public String toString() {
        return "ChequeoProducto{} " + super.toString();
    }
}
