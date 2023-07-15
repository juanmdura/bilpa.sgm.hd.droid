package com.bilpa.android.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Report implements Serializable {

    private static final long serialVersionUID = 4416073389697406383L;


    @SerializedName("nombre")
    public String name;

    @SerializedName("titulo")
    public String title;

    @SerializedName("secciones")
    public List<ReportSection> sections;

    public Report() {
        sections = new ArrayList<ReportSection>();
    }

    @Override
    public String toString() {
        return "Report{" +
                "name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", sections=" + sections +
                '}';
    }
}
