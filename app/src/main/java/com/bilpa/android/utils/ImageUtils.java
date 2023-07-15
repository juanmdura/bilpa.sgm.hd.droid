package com.bilpa.android.utils;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Base64;

import com.bilpa.android.services.AsyncCallback;

import java.io.ByteArrayOutputStream;

public class ImageUtils {


    public static String getBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();

        String encodeBytes = Base64.encodeToString(bytes, Base64.NO_WRAP);

        // Prefix is not required by server
        // String prefix = "data:image/jpg;base64,";
        // String base64 = prefix + encodeBytes;

        String base64 = encodeBytes;
        return base64;
    }

    public static Bitmap scale(Bitmap bitmap) {
        int inSampleSize = calculateInSampleSize(bitmap, 1000, 1000);
        int width = bitmap.getWidth() / inSampleSize;
        int height = bitmap.getHeight() / inSampleSize;
        Bitmap createScaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
        return createScaledBitmap;
    }

    public static Bitmap scale(Bitmap bitmap, int w, int h) {
        int inSampleSize = calculateInSampleSize(bitmap, w, h);
        int width = bitmap.getWidth() / inSampleSize;
        int height = bitmap.getHeight() / inSampleSize;
        Bitmap createScaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
        return createScaledBitmap;
    }

    public static int calculateInSampleSize(Bitmap bitmap, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = bitmap.getWidth();
        final int width = bitmap.getHeight();
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger
            // inSampleSize).

            final float totalPixels = width * height;

            // Anything more than 2x the requested pixels we'll sample down
            // further.
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }



}
