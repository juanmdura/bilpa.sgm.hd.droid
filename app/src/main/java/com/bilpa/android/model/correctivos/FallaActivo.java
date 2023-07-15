package com.bilpa.android.model.correctivos;

import java.io.Serializable;

public class FallaActivo implements Serializable {

    private static final long serialVersionUID = -8303770404339786326L;

    public CActivo activo;
    public CReparacion reparacion;

    @Override
    public String toString() {
        return "FallaActivo{" +
                "activo=" + activo +
                ", reparacion=" + reparacion +
                '}';
    }


}
