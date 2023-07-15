package com.bilpa.android.model;

import java.io.Serializable;

public class Precinto implements Serializable {

    private static final long serialVersionUID = 1638176869342743380L;

    public Long id;
    public String remplazado;
    public String numero;
    public String numeroViejo;


    @Override
    public String toString() {
        return "Precinto{" +
                "id=" + id +
                ", remplazado='" + remplazado + '\'' +
                ", numero='" + numero + '\'' +
                ", numeroViejo='" + numeroViejo + '\'' +
                '}';
    }
}
