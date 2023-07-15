package com.mautibla.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertDialogUtils {


    public static void show(Context context, int icon, String msg, String btnOk, DialogInterface.OnClickListener positiveListener) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.app_name)
                .setMessage(msg)
                .setPositiveButton(btnOk, positiveListener)
                .setIcon(icon)
                .create()
                .show();
    }

    public static void show(Context context, int icon, int msg, int btnOk, DialogInterface.OnClickListener positiveListener) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.app_name)
                .setMessage(msg)
                .setPositiveButton(btnOk, positiveListener)
                .setIcon(icon)
                .create()
                .show();
    }


}
