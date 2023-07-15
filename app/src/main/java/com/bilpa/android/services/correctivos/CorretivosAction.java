package com.bilpa.android.services.correctivos;

import com.bilpa.android.BuildConfig;
import com.bilpa.android.services.RestAction;

public abstract class CorretivosAction<T> extends RestAction<T> {

    protected CorretivosAction(Class<T> result) {
        super(result);
    }

    public CorretivosAction(Class<T> result, String datePattern) {
        super(result, datePattern);
    }

    @Override
    protected String getBaseUrl() {
        return BuildConfig.SERVICE_URL + "servicioscorrectivos";
    }
}
