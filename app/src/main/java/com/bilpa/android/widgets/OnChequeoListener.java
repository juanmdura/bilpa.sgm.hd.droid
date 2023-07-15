package com.bilpa.android.widgets;

import com.bilpa.android.model.Chequeo;

import java.util.List;

/**
 * Author: by santilod.
 */
public interface OnChequeoListener {

    public void onSaveChequeo();

    public List<Chequeo> getChequeos();

    public Chequeo getChequeoById(long chequeoId);

}
