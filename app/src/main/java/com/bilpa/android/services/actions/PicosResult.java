package com.bilpa.android.services.actions;


import com.bilpa.android.MangueraActivity;
import com.bilpa.android.model.Corregido;
import com.bilpa.android.model.Pico;
import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PicosResult extends BaseResult {

    private static final long serialVersionUID = -3610524505429046919L;

    @SerializedName("datos")
    public List<Pico> picos;

    @Override
    public String toString() {
        return "PicosResult{" +
                "picos=" + picos +
                "} " + super.toString();
    }

    public List<Pico> getPicos() {
        Collections.sort(picos, new Comparator<Pico>() {
            @Override
            public int compare(Pico lhs, Pico rhs) {
                if (lhs.numeroPico < rhs.numeroPico) {
                    return -1;
                } else if (lhs.numeroPico > rhs.numeroPico) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
        return picos;
    }

    public Pico findPicoById(Long picoId) {
        Long id = Long.valueOf(picoId);
        for (Pico p : picos) {
            if (p.id == id.longValue()) {
                return p;
            }
        }
        return null;
    }
}
