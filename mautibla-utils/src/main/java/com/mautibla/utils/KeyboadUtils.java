package com.mautibla.utils;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class KeyboadUtils {

    public static EditText getFocusedView(EditText... editTexts) {
        for (EditText editText : editTexts) {
            if (editText.hasFocus()) {
                return editText;
            }
        }
        return null;
    }

    private static void hideSoftKeyboardByEditText(Activity activity, EditText editText) {
        if (editText == null) {
            return;
        }
        final InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public static void hideSoftKeyboard(Activity activity, EditText... editTexts) {
        EditText focusedView = getFocusedView(editTexts);
        hideSoftKeyboardByEditText(activity, focusedView);
    }

}
