package com.bilpa.android.services.actions;

import com.bilpa.android.BuildConfig;
import com.bilpa.android.services.RestAction;

public abstract class PreventivosAction<T> extends RestAction<T> {

    protected PreventivosAction(Class<T> result) {
        super(result);
    }

    public PreventivosAction(Class<T> result, String datePattern) {
        super(result, datePattern);
    }

    @Override
    protected String getBaseUrl() {
        return BuildConfig.SERVICE_URL + "servicios";
    }
}
