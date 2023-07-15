package com.bilpa.android.services.actions.comments;

import com.bilpa.android.model.Comentario;
import com.bilpa.android.services.actions.BaseResult;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CommentsResult extends BaseResult {

    private static final long serialVersionUID = 998846461867335051L;

    @SerializedName("datos")
    public List<Comentario> comentarios;

    public CommentsResult() {
        this.comentarios = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "CommentsResult{" +
                "comentarios=" + comentarios +
                '}';
    }
}
