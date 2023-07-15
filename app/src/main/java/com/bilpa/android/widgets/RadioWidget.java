package com.bilpa.android.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bilpa.android.R;


public class RadioWidget extends LinearLayout {

    private RadioButton radio0;
    private RadioButton radio1;
    private RadioButton radio2;
    private RadioButton radio3;
    private RadioGroup radioGroup;

    public RadioWidget(Context context) {
        super(context);
    }

    public RadioWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RadioWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public RadioWidget(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        LayoutInflater.from(getContext()).inflate(R.layout.radio_widget, this);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RadioWidget, 0, 0);

        String titleText = a.getString(R.styleable.RadioWidget_radioLbl);
        TextView title = (TextView) findViewById(R.id.vLbl);
        title.setText(titleText);

        int lblMinWi = a.getDimensionPixelSize(R.styleable.RadioWidget_radioLblMinWith, 100);
        title.setMinWidth(lblMinWi);

        int maxWidth = a.getDimensionPixelSize(R.styleable.RadioWidget_radioLblMaxWith, -1);
        if (maxWidth > 0) {
            title.setMaxWidth(maxWidth);
        }


        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radio0 = (RadioButton) findViewById(R.id.radioButton0);
        radio1 = (RadioButton) findViewById(R.id.radioButton1);
        radio2 = (RadioButton) findViewById(R.id.radioButton2);
        radio3 = (RadioButton) findViewById(R.id.radioButton3);

        String opt0 = a.getString(R.styleable.RadioWidget_radioOpt0);
        String opt1 = a.getString(R.styleable.RadioWidget_radioOpt1);

        radio0.setText(opt0);
        radio1.setText(opt1);

        int count = a.getInt(R.styleable.RadioWidget_radioCount, 2);
        if (count > 2) {
            String opt2 = a.getString(R.styleable.RadioWidget_radioOpt2);
            radio2.setVisibility(View.VISIBLE);
            radio2.setText(opt2);
        } else {
            radio2.setVisibility(View.GONE);
        }

        if (count > 3) {
            String opt3 = a.getString(R.styleable.RadioWidget_radioOpt3);
            radio3.setVisibility(View.VISIBLE);
            radio3.setText(opt3);
        } else {
            radio3.setVisibility(View.GONE);
        }

        a.recycle();
    }

    public void unSelectAll() {
//        radioGroup.clearCheck();
        radio0.setChecked(false);
        radio1.setChecked(false);
        radio2.setChecked(false);
    }

    public void setOptionByLbl(String label) {

        boolean checked0 = radio0.getText() != null && radio0.getText().equals(label);
        if (checked0) {
            radio0.setChecked(true);
        }

        boolean checked1 = radio1.getText() != null && radio1.getText().equals(label);
        if (checked1) {
            radio1.setChecked(true);
        }

        if (radio2 != null) {
            boolean checked2 = radio2.getText() != null && radio2.getText().equals(label);
            if (checked2) {
                radio2.setChecked(checked2);
            }
        }

        if (radio3 != null) {
            boolean checked3 = radio3.getText() != null && radio3.getText().equals(label);
            if (checked3) {
                radio3.setChecked(checked3);
            }
        }
    }

    public String getSelectedLabel() {
        if (radio0.isChecked()) {
            return (String) radio0.getText();
        } else if (radio1.isChecked()) {
            return (String) radio1.getText();
        } else if (radio2 != null && radio2.isChecked()) {
            return (String) radio2.getText();
        } else if (radio3 != null && radio3.isChecked()) {
            return (String) radio3.getText();
        }
        return "";
    }

    public void setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener listener) {
        radioGroup.setOnCheckedChangeListener(listener);
    }

    public void setChecked(int checked) {
        switch (checked) {
            case 0: radio0.setChecked(true); break;
            case 1: radio1.setChecked(true); break;
            case 2:
                if (radio2 != null) {
                    radio2.setChecked(true);
                }
            case 3:
                if (radio3 != null) {
                    radio3.setChecked(true);
                }
                break;
        }

    }

    public int getChecked() {
        if (radio0.isChecked()) {
            return 0;
        } else if (radio1.isChecked()) {
            return 1;
        } else if (radio2.isChecked()) {
            return 2;
        } else if (radio3.isChecked()) {
            return 3;
        } else {
            return -1;
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        return new SavedState(superState, getChecked());
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        radio0.setChecked(savedState.getChecked() == 0);
        radio1.setChecked(savedState.getChecked() == 1);
        radio2.setChecked(savedState.getChecked() == 2);
        radio3.setChecked(savedState.getChecked() == 3);
    }

    @Override
    protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
        super.dispatchFreezeSelfOnly(container);
    }

    @Override
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        super.dispatchThawSelfOnly(container);
    }

    protected static class SavedState extends BaseSavedState {

        private final int checked;

        private SavedState(Parcelable superState, int checked) {
            super(superState);
            this.checked = checked;
        }

        private SavedState(Parcel in) {
            super(in);
            checked = in.readInt();
        }

        public int getChecked() {
            return checked;
        }

        @Override
        public void writeToParcel(Parcel destination, int flags) {
            super.writeToParcel(destination, flags);
            destination.writeInt(checked);
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Creator<SavedState>() {

            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }

        };

    }

}