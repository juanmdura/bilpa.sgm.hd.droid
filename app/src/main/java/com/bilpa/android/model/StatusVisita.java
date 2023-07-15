package com.bilpa.android.model;

public enum StatusVisita {

    PENDIENTE, INICIADA, FINALIZADA;


    public static StatusVisita form(String string) {
        if (string.equalsIgnoreCase("PENDIENTE")) {
            return PENDIENTE;
        } else if (string.equalsIgnoreCase("INICIADA")) {
            return INICIADA;
        } else if (string.equalsIgnoreCase("REALIZADA")) {
            return FINALIZADA;
        }
        return PENDIENTE;
    }

    public static String to(StatusVisita status) {
        switch (status) {
            case PENDIENTE:
                return "PENDIENTE";
            case INICIADA:
                return "INICIADA";
            case FINALIZADA:
                return "REALIZADA";

        }
        return null;
    }
}
