package com.bilpa.android.model;

import java.io.Serializable;
import java.util.Date;

public class Pendiente implements Serializable {

    public enum State {
        INICIADO, REPARADO
    }

    private static final long serialVersionUID = 5295225233131495861L;

    public Long id;
    public String comentario;
    public boolean comentarioVisible;
    public Date plazo;
    public String urlFoto;
    public String destinatario;
    private String estado;
    public Date fechaReparado;
    public Long idItemChequeado;
    public String textoItemChequado;

    @Override
    public String toString() {
        return "Pendiente{" +
                "id=" + id +
                ", comentario='" + comentario + '\'' +
                ", comentarioVisible=" + comentarioVisible +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pendiente pendiente = (Pendiente) o;

        if (id != null ? !id.equals(pendiente.id) : pendiente.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }



    public State getStatus() {
        if (State.INICIADO.toString().equalsIgnoreCase(estado)) {
            return State.INICIADO;
        } else if (State.REPARADO.toString().equalsIgnoreCase(estado)) {
            return State.REPARADO;
        }
        return State.INICIADO;
    }
}

