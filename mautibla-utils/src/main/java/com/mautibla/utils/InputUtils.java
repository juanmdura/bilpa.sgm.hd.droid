package com.mautibla.utils;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class InputUtils {

    /**
     * Devuelve true si el input tiene algun texto
     *
     * @param editText
     * @return
     */
    public static boolean isNotEmpty(EditText editText) {
        if (editText != null) {
            String text = editText.getText().toString();
            if (text != null && text.length() > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Devuelve true si el input tiene algun texto
     *
     * @param editText
     * @return
     */
    public static boolean isEmpty(EditText editText) {
        if (editText != null) {
            String text = getText(editText);
            if (text != null && text.length() > 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Devuelve true si el textview es vacio
     */
    public static boolean isEmpty(TextView textView) {
        if (textView != null) {
            String text = textView.getText().toString();
            if (text != null && text.length() > 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Devuelve true si el String es vacio o null
     */
    public static boolean isEmpty(String text) {
        if (text != null && text.length() > 0) {
            return false;

        }
        return true;
    }

    /**
     * Devuleve el text que contiene en editext.
     */
    public static String getText(TextView textView) {
        if (textView == null) {
            return null;
        }
        String text = textView.getText().toString();
        if (text == null) {
            return null;
        }
        text = text.trim();
        if (text.length() == 0) {
            return null;
        }
        return text;
    }

    public static String getTextWithoutEnters(EditText editText) {
        String text = getText(editText);
        if (text != null) {
            return text.replaceAll("\n", " ").trim();
        }
        return null;
    }

    /**
     * Setea el text en el editext.
     *
     * @param editText
     * @param text
     */
    public static void setText(TextView editText, String text) {
        if (editText == null) {
            return;
        }
        if (text != null) {
            text = text.trim();
            editText.setText(text);
        } else {
            editText.setText("");
        }
    }

    public static void setNum(TextView editText, Integer numText) {
        if (editText == null) {
            return;
        }
        if (numText != null) {
            editText.setText(String.valueOf(numText));
        } else {
            editText.setText("");
        }
    }

    public static void setNum(TextView editText, Long numText) {
        if (editText == null) {
            return;
        }
        if (numText != null) {
            editText.setText(String.valueOf(numText));
        } else {
            editText.setText("");
        }

    }

    public static void setNum(TextView editText, Double numText) {
        if (editText == null) {
            return;
        }
        if (numText != null) {
            editText.setText(String.valueOf(numText));
        } else {
            editText.setText("");
        }
    }


    public static void setNum(TextView editText, Double numText, DecimalFormat df) {
        if (editText == null) {
            return;
        }
        if (numText != null) {
//            String text = String.valueOf(numText);
            editText.setText(df.format(numText));
        } else {
            editText.setText("");
        }
    }


    /**
     * Limpia los datos del editText
     */
    public static void clear(TextView textView) {
        if (textView != null) {
            textView.setText("");
        }
    }

    public static void clear(TextView... textViews) {
        for (TextView textView : textViews) {
            clear(textView);
        }
    }

    public static long getSelectedIndex(Spinner spinner) {
        return spinner.getSelectedItemId();
    }

    public static boolean isNumber(EditText editText) {
        try {
            Double.valueOf(getText(editText));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Double getNumber(EditText editText) {
        try {
            return Double.valueOf(getText(editText));
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isInteger(EditText editText) {
        try {
            Integer.valueOf(getText(editText));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Integer getInt(TextView editText) {
        try {
            return Integer.valueOf(getText(editText));
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isLong(EditText editText) {
        try {
            Long.valueOf(getText(editText));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Long getLong(EditText editText) {
        try {
            return Long.valueOf(getText(editText));
        } catch (Exception e) {
            return null;
        }
    }

    public static Float getFloat(EditText editText) {
        try {
            return Float.valueOf(getText(editText));
        } catch (Exception e) {
            return null;
        }
    }

    public static Double getDoubleOrZero(TextView textView) {
        Double aDouble = getDouble(textView);
        if (aDouble == null) {
            return 0.0;
        } else {
            return aDouble;
        }
    }

    public static Double getDouble(TextView textView) {
        try {
            String text = getText(textView);
            if (text != null) {
                if (text.equals(".")) {
                    return 0.0;
                } else {
                    DecimalFormat decimalFormat = new DecimalFormat("###.###");
                    DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                    symbols.setDecimalSeparator('.');
                    symbols.setGroupingSeparator(',');
                    decimalFormat.setDecimalFormatSymbols(symbols);
                    Number number = decimalFormat.parse(text);
                    return number.doubleValue();
                }
            }
        } catch (Exception e) {
        }
        return null;
    }



    /**
     * Devuelve true si los inputs son iguales
     */
    public static boolean isEquals(EditText editText1, EditText editText2) {
        if (isEmpty(editText1) && isEmpty(editText2)) {
            return true;
        }
        if (editText1 != null) {
            return getText(editText1).equals(getText(editText2));
        }

        return false;
    }


    public static void hideSoftKey(Activity activity, EditText editText) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public static void showSoftKey(Activity activity, EditText editText) {
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public static void setTextDate(TextView textView, Calendar cal, SimpleDateFormat sdf) {
        if (textView == null || cal == null || sdf == null) {
            return;
        }
        InputUtils.setTextDate(textView, cal.getTime(), sdf);
    }

    public static void setTextDate(TextView textView, Date date, SimpleDateFormat sdf) {
        if (textView == null || date == null || sdf == null) {
            return;
        }
        textView.setText(sdf.format(date));
    }

}
