package com.bilpa.android.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bilpa.android.R;

public class LblEditTextHorizontal extends RelativeLayout {

    private EditText vValue;

    public LblEditTextHorizontal(Context context) {
        super(context);
    }

    public LblEditTextHorizontal(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public LblEditTextHorizontal(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public LblEditTextHorizontal(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.lbl_edittext_horizontal, this, true);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LblEditText, 0, 0);

        TextView vLbl = (TextView) findViewById(R.id.vLbl);
        vValue = (EditText) findViewById(R.id.vValue);

        String lbl = a.getString(R.styleable.LblEditText_lbl);
        vLbl.setText(lbl);

        int minLines = a.getInteger(R.styleable.LblEditText_minLines, 1);
        vValue.setMinLines(minLines);
        vValue.setMaxLines(minLines);
        if (minLines > 1) {
            vValue.setGravity(Gravity.LEFT | Gravity.TOP);
        } else {
            vValue.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        }

        int lblMinWidth = a.getDimensionPixelSize(R.styleable.LblEditText_lblMinWith, 100);
        vLbl.setMinWidth(lblMinWidth);

        String hint = a.getString(R.styleable.LblEditText_hint);
        vValue.setHint(hint);

        String value = a.getString(R.styleable.LblEditText_value);
        vValue.setText(value);

        a.recycle();
    }

    public EditText getEditText() {
        return vValue;
    }
}
