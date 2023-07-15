package com.bilpa.android.utils;

import android.content.Context;
import android.content.DialogInterface;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bilpa.android.R;

public class MaterialAlertDialogUtils {

    public static void show(Context context, String msg, String btnPositive, MaterialDialog.ButtonCallback callback) {
        new MaterialDialog.Builder(context)
                .title(com.bilpa.android.R.string.app_name)
                .iconRes(R.drawable.ic_launcher)
                .content(msg)
                .positiveText(btnPositive)
                .callback(callback)
                .show();
    }

    public static void show(Context context, int msg, String btnPositive, MaterialDialog.ButtonCallback callback) {
        show(context, context.getString(msg), btnPositive, callback);
    }


    public static void showAcceptMsg(Context context, String msg) {
        show(context, msg, context.getString(R.string.btn_aceptar), null);
    }

    public static void showAcceptMsg(Context context, int msg) {
        showAcceptMsg(context, context.getString(msg));
    }
}
