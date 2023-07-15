package com.bilpa.android.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bilpa.android.R;

public class LblTextView extends RelativeLayout {
    public LblTextView(Context context) {
        super(context);
    }

    public LblTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public LblTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public LblTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.lbl_textview, this, true);

//        if (isInEditMode()) {
//            return;
//        }

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LblTextView, 0, 0);

        String lbl = a.getString(R.styleable.LblTextView_lbl);
        String value = a.getString(R.styleable.LblTextView_value);
        String hint = a.getString(R.styleable.LblTextView_hint);
        int lblMinWidth = a.getDimensionPixelSize(R.styleable.LblTextView_lblMinWith, 100);
        int orientation = a.getInteger(R.styleable.LblTextView_orientation, 0);
        String fontHint = a.getString(R.styleable.LblTextView_fontHint);
        boolean showArrow = a.getBoolean(R.styleable.LblTextView_showArrow, true);

        TextView vLbl = (TextView) findViewById(R.id.vLbl);
        vLbl.setText(lbl);
        vLbl.setMinWidth(lblMinWidth);
        vLbl.setMaxWidth(lblMinWidth);

        TextView vValue = (TextView) findViewById(R.id.vValue);
        vValue.setText(value);
        vValue.setHint(hint);
        vValue.setCompoundDrawablesWithIntrinsicBounds(0, 0, showArrow ? R.drawable.ic_arrow_drop_down_blue_24dp : 0, 0);


        if (fontHint != null && (value == null || value.length() == 0)) {
            setFont(fontHint, Typeface.ITALIC);
        } else {
            setFont("sans-serif-light", Typeface.ITALIC);
        }

        if (showArrow) {

        }




        a.recycle();


    }

    public void setText(String text) {
        if (text == null || text.length() == 0) {
            clearText();
            return;
        }
        TextView vValue = (TextView) findViewById(R.id.vValue);
        vValue.setText(text.trim());
        setFont("sans-serif-condensed", Typeface.BOLD);
    }

    public void setHint(String text) {
        TextView vValue = (TextView) findViewById(R.id.vValue);
        vValue.setHint(text);
        setFont("sans-serif-light", Typeface.ITALIC);
    }

    public void clearText() {
        TextView vValue = (TextView) findViewById(R.id.vValue);
        vValue.setText("");
        setFont("sans-serif-light", Typeface.ITALIC);
    }

    public void setFont(String font, int style) {
        TextView vValue = (TextView) findViewById(R.id.vValue);
        vValue.setTypeface(Typeface.create(font, style));
    }



}
