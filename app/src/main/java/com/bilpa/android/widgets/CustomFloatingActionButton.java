package com.bilpa.android.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.bilpa.android.R;

/**
 * Author: by santilod.
 */
public class CustomFloatingActionButton extends FloatingActionButton {
    private int mSize;

    public CustomFloatingActionButton(Context context) {
        this(context, null);
    }

    public CustomFloatingActionButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("PrivateResource")
    public CustomFloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.FloatingActionButton, defStyleAttr,
                R.style.Widget_Design_FloatingActionButton);
        mSize = a.getInt(R.styleable.FloatingActionButton_fabSize, 0);
        a.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            int size = this.getSizeDimension();
            int offsetVertical = (h - size) / 2;
            int offsetHorizontal = (w - size) / 2;
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) getLayoutParams();
            params.leftMargin = params.leftMargin - offsetHorizontal;
            params.rightMargin = params.rightMargin - offsetHorizontal;
            params.topMargin = params.topMargin - offsetVertical;
            params.bottomMargin = params.bottomMargin - offsetVertical;
            setLayoutParams(params);
        }
    }

    @SuppressLint("PrivateResource")
    private int getSizeDimension() {
        switch (mSize) {
            case 1:
                return getResources().getDimensionPixelSize(R.dimen.design_fab_size_mini);
            case 0:
            default:
                return getResources().getDimensionPixelSize(R.dimen.design_fab_size_normal);
        }
    }
}