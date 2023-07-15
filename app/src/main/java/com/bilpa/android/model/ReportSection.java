package com.bilpa.android.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ReportSection implements Serializable {

    private static final long serialVersionUID = -2738004269027824158L;

    @SerializedName("titulo")
    public String title;

    @SerializedName("nombre")
    public String name;

    @SerializedName("detalles")
    public List<SectionDetail> detail;

    public ReportSection() {
        detail = new ArrayList<SectionDetail>();
    }

    @Override
    public String toString() {
        return "ReportSection{" +
                "name='" + name + '\'' +
                ", detail=" + detail +
                '}';
    }
}
