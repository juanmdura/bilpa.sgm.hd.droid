package com.mautibla.utils;

import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator {

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    /**
     * Valida una direccion de email
     *
     * @param email
     * @return true si el email es valido
     */
    public static boolean validate(final String email) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean validate(EditText editText) {
        if (editText == null) {
            return false;
        }
        String text = editText.getText().toString();
        if (text == null || text.length() == 0) {
            return false;
        }
        return validate(text);
    }

}
