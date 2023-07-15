package com.bilpa.android.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

public class DimensUtils {

    public static int convertDpToPixel(Context context, int dp){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return (int) px;
    }
}
