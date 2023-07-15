package com.bilpa.android.services;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bilpa.android.R;
import com.bilpa.android.services.actions.BaseResult;

/**
 * Created by santilod on 19/12/14.
 */
public abstract class AsyncCallback<T> extends Callback<T> {

    private static final String TAG = "AsyncCallback";

    Context context;

    public AsyncCallback(Context context) {
        this.context = context;
    }

    @Override
    protected void onServiceOperationFail(BaseResult br) {
        Log.e(TAG, br.error);
        String message = br.error;
        onError(message, new MaterialDialog.ButtonCallback() {
            @Override
            public void onPositive(MaterialDialog dialog) {
                onServiceOperationFailOK();
            }
        });
    }

    protected void onServiceOperationFailOK() {
    }

    @Override
    protected void onFail(Throwable caught) {
        Log.e(TAG, caught.getMessage(), caught);
        String message = VolleyErrorHelper.getMessage(caught, context);
        onError(message, new MaterialDialog.ButtonCallback() {
            @Override
            public void onPositive(MaterialDialog dialog) {
                onServiceOperationFailOK();
            }
        });
    }

    public void onError(int msg, MaterialDialog.ButtonCallback callback) {
        String string = context.getString(msg);
        onError(string, callback);
    }

    public void onError(String msg, MaterialDialog.ButtonCallback callback) {
        new MaterialDialog.Builder(context)
                .title(com.bilpa.android.R.string.app_name)
                .iconRes(R.drawable.ic_launcher)
                .content(msg)
                .positiveText("Aceptar")
                .callback(callback)
                .cancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        onServiceOperationFailOK();
                    }
                })
                .show();


    }

}
