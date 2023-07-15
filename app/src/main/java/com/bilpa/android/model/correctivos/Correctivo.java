package com.bilpa.android.model.correctivos;

import android.content.Context;

import com.bilpa.android.R;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class Correctivo implements Serializable {

    private static final long serialVersionUID = 6128347091523357868L;

    @SerializedName("numero")
    public Long id;
//    public String estado;
    public int idEstado;
    public String tecnico;
    public String prioridad;
    public Date fechaInicio;
    @SerializedName("fechaFin2")
    public Date fechaFin;
    public Date fechaCumplimiento2;
    public String estacion;
    public Long idEstacion;
    public String localidad;
    public String sello;
    public Date plazo;
    public boolean activa;
    public Long numeroDucsa;
    private int statusLabel;
    public String fotoEstacion;
    public String fotoEstacionChica;

    public Date inicioServiceReal;
    public Date finServiceReal;
    public Date inicioServiceUsuario;
    public Date finServiceUsuario;


    @Override
    public String toString() {
        return "\nCorrectivo{" +
                "id=" + id +
                ", estacion='" + estacion + '\'' +
                '}';
    }



    public enum Status {
        PENDING, INPROCESS, FINALIZED
    }

    public String getStatusLabel(Context context) {
        switch (getStatus()) {
            case FINALIZED:
                return "FINALIZADO";
            case INPROCESS:
                return "EN PROCESO";
            case PENDING:
                default:
                return "PENDIENTE";
        }
    }

    public Status getStatus() {
        switch (this.idEstado) {
            case 2: return Status.PENDING;
            case 5: return Status.FINALIZED;
            case 7: return Status.INPROCESS;
            default: return Status.PENDING;
        }
    }

    public void setStatus(Status status) {
        switch (status) {
            case FINALIZED:
                this.idEstado = 5;
                break;
            case INPROCESS:
                this.idEstado = 7;
                break;
            case PENDING:
            default:
                this.idEstado = 2;
                break;
        }
    }

    public int getStatusColor() {
        switch (getStatus()) {
            case PENDING:
                return R.color.correctivo_status_pendiente;
            case INPROCESS:
                return R.color.correctivo_status_inprocess;
            case FINALIZED:
                return R.color.correctivo_status_finished;
        }
        return 0;
    }


    public int getStatusBg() {
        switch (getStatus()) {
            case PENDING:
                return R.drawable.bg_status_correctivo_pendiente;
            case INPROCESS:
                return R.drawable.bg_status_correctivo_inprogress;
            case FINALIZED:
                return R.drawable.bg_status_correctivo_finalized;
        }
        return 0;
    }
}
