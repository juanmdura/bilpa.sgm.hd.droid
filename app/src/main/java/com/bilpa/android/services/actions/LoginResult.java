package com.bilpa.android.services.actions;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LoginResult extends BaseResult {

    private static final long serialVersionUID = 4701456303445086903L;

    @SerializedName("datos")
    public Session data;

    @Override
    public String toString() {
        return "GetChequeoTanqueResult{" +
                "data=" + data +
                "} " + super.toString();
    }

    public static class Session implements Serializable {

        private static final long serialVersionUID = -2435358396523141157L;

        public int id;
        public int rol;
        public String nombre;
        public String apellido;

        @Override
        public String toString() {
            return "Session{" +
                    "id='" + id + '\'' +
                    ", rol=" + rol +
                    ", nombre='" + nombre + '\'' +
                    ", apellido='" + apellido + '\'' +
                    '}';
        }
    }
}
