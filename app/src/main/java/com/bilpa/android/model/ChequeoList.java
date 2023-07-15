package com.bilpa.android.model;

import com.bilpa.android.services.actions.ListResult;

import java.util.List;

public class ChequeoList extends ListResult<Chequeo> {

    private List<Chequeo> chequeos;

    public ChequeoList(List<Chequeo> chequeos) {
        this.chequeos = chequeos;
    }

    @Override
    public List<Chequeo> getItems() {
        return chequeos;
    }
}
