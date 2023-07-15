package com.bilpa.android.services.correctivos;


import com.bilpa.android.model.correctivos.Correctivo;
import com.bilpa.android.services.actions.BaseResult;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GetCorrectivosResult extends BaseResult {

    @SerializedName("datos")
    public List<Correctivo> correctivos;

    public GetCorrectivosResult() {
        this.correctivos = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "GetCorrectivosResult{" +
                "correctivos=" + correctivos +
                '}';
    }
}
