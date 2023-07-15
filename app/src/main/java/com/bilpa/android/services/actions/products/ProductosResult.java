package com.bilpa.android.services.actions.products;


import com.bilpa.android.model.Producto;
import com.bilpa.android.services.actions.BaseResult;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductosResult extends BaseResult {

    private static final long serialVersionUID = -8284172871709385734L;

    @SerializedName("datos")
    public List<Producto> productos;

    @Override
    public String toString() {
        return "ProductosResult{" +
                "productos=" + productos +
                "} " + super.toString();
    }
}
