package com.bilpa.android.utils;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bilpa.android.R;

public class MessageUtils {

    public static void showMsg(Context context, int msg) {
        showMsg(context, msg, null);
    }

    public static void showMsg(Context context, String msg) {
        showMsg(context, msg, null);
    }

    public static void showMsg(Context context, int msg, MaterialDialog.ButtonCallback callback) {
        new MaterialDialog.Builder(context)
                .title(com.bilpa.android.R.string.app_name)
                .iconRes(R.drawable.ic_launcher)
                .content(msg)
                .positiveText(R.string.btn_aceptar)
                .callback(callback)
                .show();
    }

    public static void showMsg(Context context, String msg, MaterialDialog.ButtonCallback callback) {
        new MaterialDialog.Builder(context)
                .title(com.bilpa.android.R.string.app_name)
                .iconRes(R.drawable.ic_launcher)
                .content(msg)
                .positiveText(R.string.btn_aceptar)
                .callback(callback)
                .show();
    }

    public static void showAlert(Context context, int msg, MaterialDialog.ButtonCallback callback) {
        new MaterialDialog.Builder(context)
                .title(com.bilpa.android.R.string.app_name)
                .iconRes(R.drawable.ic_launcher)
                .content(msg)
                .positiveText(R.string.btn_aceptar)
                .negativeText(R.string.btn_cancelar)
                .callback(callback)
                .show();
    }


}
