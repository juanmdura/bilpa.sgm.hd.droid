package com.mautibla.utils;

import android.os.AsyncTask;

public class TaskUtils {

    public static void cancelTask(AsyncTask<?, ?, ?>... tasks) {
        for (AsyncTask<?, ?, ?> t : tasks) {
            if (t != null) {
                t.cancel(true);
                t = null;
            }
        }
    }

}
