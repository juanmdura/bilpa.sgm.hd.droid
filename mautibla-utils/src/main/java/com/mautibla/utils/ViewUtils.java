package com.mautibla.utils;

import android.view.View;
import android.widget.TextView;

public class ViewUtils {

    public static void visible(View... views) {
        for (View v : views) {
            v.setVisibility(View.VISIBLE);
        }
    }

    public static void invisible(View... views) {
        for (View v : views) {
            v.setVisibility(View.INVISIBLE);
        }
    }

    public static void gone(View... views) {
        for (View v : views) {
            v.setVisibility(View.GONE);
        }
    }

    public static boolean isVisible(View view) {
        return view.getVisibility() == View.VISIBLE;
    }

    public static boolean isInvisible(View view) {
        return view.getVisibility() == View.INVISIBLE;
    }

    public static boolean isGone(View view) {
        return view.getVisibility() == View.GONE;
    }

    /**
     * Capitalize text of TextView
     */
    public static void capitalize(TextView textView) {
        String s = (String) textView.getText();
        final StringBuilder result = new StringBuilder(s.length());
        String[] words = s.split("\\s");
        for (int i = 0, l = words.length; i < l; ++i) {
            if (i > 0) result.append(" ");
            result.append(Character.toUpperCase(words[i].charAt(0)))
                    .append(words[i].substring(1).toLowerCase());

        }
        textView.setText(result);
    }
}
