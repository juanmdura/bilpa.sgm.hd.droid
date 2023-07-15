package com.bilpa.android.services;

import com.bilpa.android.services.actions.BaseResult;

/**
 * Created by santilod on 19/12/14.
 */
public abstract class Callback<T> {

    public Callback() {

    }

    public Callback(String loadingTag) {

    }

    public void fail(Throwable caught) {
        onFail(caught);
    }

    public void success(T result) {

        if (BaseResult.class.isInstance(result)) {
            BaseResult br = (BaseResult) result;
            if (br.status.equals("0")) {
                onSuccess(result);
            } else {
                onServiceOperationFail(br);
            }
        } else {
            onSuccess(result);
        }
    }

    protected abstract void onServiceOperationFail(BaseResult br);

    /**
     * Handle unexpected error
     */
    protected abstract void onFail(Throwable caught);

    /**
     * Service call succes. Implement this method to get results.
     */
    protected abstract void onSuccess(T result);


}
