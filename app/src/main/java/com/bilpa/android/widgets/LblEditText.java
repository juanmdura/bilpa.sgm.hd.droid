package com.bilpa.android.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bilpa.android.R;
import com.mautibla.utils.InputUtils;

public class LblEditText extends RelativeLayout {

    private EditText vValue;
    private TextView title;

    public LblEditText(Context context) {
        super(context);
    }

    public LblEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public LblEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public LblEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.lbl_edittext, this, true);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LblEditText, 0, 0);

        title = (TextView) findViewById(R.id.vLbl);
        String lbl = a.getString(R.styleable.LblEditText_lbl);
        title.setText(lbl);

        vValue = (EditText) findViewById(R.id.vValue);

        String hint = a.getString(R.styleable.LblEditText_hint);
        vValue.setHint(hint);

        String value = a.getString(R.styleable.LblEditText_value);
        vValue.setText(value);

        a.recycle();

    }

    public EditText getValue() {
        return vValue;
    }

    public TextView getLabel() {
        return title;
    }

    @Override
    protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
        super.dispatchFreezeSelfOnly(container);
    }

    @Override
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        super.dispatchThawSelfOnly(container);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        return new SavedState(superState, (String) InputUtils.getText(title), InputUtils.getText(vValue));
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        title.setText(savedState.getTitle());
        vValue.setText(savedState.getValue());
    }

    protected static class SavedState extends BaseSavedState {

        private final String title;
        private final String value;

        private SavedState(Parcelable superState, String title, String value) {
            super(superState);
            this.title = title;
            this.value = value;
        }

        private SavedState(Parcel in) {
            super(in);
            title = in.readString();
            value = in.readString();
        }

        public String getTitle() {
            return title;
        }

        public String getValue() {
            return value;
        }

        @Override
        public void writeToParcel(Parcel destination, int flags) {
            super.writeToParcel(destination, flags);
            destination.writeString(title);
            destination.writeString(value);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {

            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }

        };

    }
}
