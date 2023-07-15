package com.bilpa.android.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bilpa.android.R;
import com.bilpa.android.model.Chequeo;
import com.bilpa.android.utils.DimensUtils;


public class RadioCheckboxWidget extends LinearLayout {

    private RadioButton radio0;
    private RadioButton radio1;
    private CheckBox checkbox2;
    private RadioButton radio3;

    private CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener;
    private ImageButton vBtnComments;
    private TextView vTitle;

    public RadioCheckboxWidget(Context context) {
        super(context);
        init(context, null);
    }

    public RadioCheckboxWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RadioCheckboxWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public RadioCheckboxWidget(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        LayoutInflater.from(getContext()).inflate(R.layout.radio_checkbox_widget, this);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RadioWidget, 0, 0);

        String titleText = a.getString(R.styleable.RadioWidget_radioLbl);
        int lblMinWi = a.getDimensionPixelSize(R.styleable.RadioWidget_radioLblMinWith, 100);
        int maxWidth = a.getDimensionPixelSize(R.styleable.RadioWidget_radioLblMaxWith, -1);
        String opt0 = a.getString(R.styleable.RadioWidget_radioOpt0);
        String opt1 = a.getString(R.styleable.RadioWidget_radioOpt1);
        int count = a.getInt(R.styleable.RadioWidget_radioCount, 2);
        String opt2 = a.getString(R.styleable.RadioWidget_radioOpt2);
        String opt3 = a.getString(R.styleable.RadioWidget_radioOpt3);

        vTitle = (TextView) findViewById(R.id.vLbl);
        radio0 = (RadioButton) findViewById(R.id.radioButton0);
        radio1 = (RadioButton) findViewById(R.id.radioButton1);
        checkbox2 = (CheckBox) findViewById(R.id.checkbox2);
        radio3 = (RadioButton) findViewById(R.id.radioButton3);
        vBtnComments = (ImageButton) findViewById(R.id.vBtnComments);

        setUpView(titleText, lblMinWi, maxWidth, count, opt0, opt1, opt2, opt3);

        a.recycle();

        unSelectAll();
        onCheck(radio0, false);
        onCheck(radio1, false);
        onCheck(checkbox2, false);
        onCheck(radio3, false);

        enabledCheckListener();
    }

    public void setUpView(String titleText, int lblMinWi, int maxWidth, int count, String opt0, String opt1, String opt2, String opt3) {
        setTitle(titleText);
        setTitleMinWidthPixels(lblMinWi);
        if (maxWidth > 0) {
            vTitle.setMaxWidth(maxWidth);
        }

        setLblOpt0(opt0);
        setLblOpt1(opt1);
        if (count > 2) {
            checkbox2.setVisibility(View.VISIBLE);
            setLblOpt2(opt2);
        } else {
            checkbox2.setVisibility(View.GONE);
        }
        if (count > 3) {
            radio3.setVisibility(View.VISIBLE);
            setLblOpt3(opt3);
        } else {
            radio3.setVisibility(View.GONE);
        }
    }

    public void setTitleMinWidthDp(int dp) {
        int px = DimensUtils.convertDpToPixel(getContext(), dp);
        setTitleMinWidthPixels(px);
    }

    private void setTitleMinWidthPixels(int lblMinWi) {
        vTitle.setMinWidth(lblMinWi);
    }

    public void setTitle(String titleText) {
        vTitle.setText(titleText);
    }

    public void setLblOpt3(String opt3) {
        radio3.setText(opt3);
    }

    public void setLblOpt2(String opt2) {
        checkbox2.setText(opt2);
    }

    public void setLblOpt1(String opt1) {
        radio1.setText(opt1);
    }

    public void setLblOpt0(String opt0) {
        radio0.setText(opt0);
    }

    public String getLabel() {
        return (String) vTitle.getText();
    }

    public ImageButton getBtnComments() {
        return vBtnComments;
    }

    public void unSelectAll() {
        radio0.setChecked(false);
        radio1.setChecked(false);
        checkbox2.setChecked(false);
        radio3.setChecked(false);
    }

    public void setOptionByLbl(String label) {

        if (label == null) {
            radio0.setChecked(false);
            radio1.setChecked(false);
            radio3.setChecked(false);
            return;
        }

        boolean checked0 = radio0.getText() != null && radio0.getText().equals(label);
        if (checked0) {
            radio0.setChecked(true);
        }

        boolean checked1 = radio1.getText() != null && radio1.getText().equals(label);
        if (checked1) {
            radio1.setChecked(true);
        }

        boolean checked3 = radio3.getText() != null && radio3.getText().equals(label);
        if (checked3) {
            radio3.setChecked(true);
        }

    }

    public void setRadiosEnabled(boolean enabled) {
        if (radio0 != null) {
            radio0.setEnabled(enabled);
        }
        if (radio1 != null) {
            radio1.setEnabled(enabled);
        }
        if (radio3 != null) {
            radio3.setEnabled(enabled);
        }
    }

    public void setOptionCheck(boolean checked) {
        if (checkbox2 != null) {
            checkbox2.setChecked(checked);
        }
    }
    public void setOptionEnabled(boolean enabled) {
        if (checkbox2 != null) {
            checkbox2.setEnabled(enabled);
        }
    }


    public String getSelectedLabel() {
        if (radio0.isChecked()) {
            return (String) radio0.getText();
        } else if (radio1.isChecked()) {
            return (String) radio1.getText();
        } else if (radio3 != null && radio3.isChecked()) {
            return (String) radio3.getText();
        }
        return null;
    }

    public int getSelectedInt() {
        if (radio0.isChecked()) {
            return 0;
        } else if (radio1.isChecked()) {
            return 1;
        } else if (radio3.isChecked()) {
            return 3;
        } else {
            return -1;
        }
    }

    public boolean isChecked() {
         if (checkbox2 != null ) {
             return checkbox2.isChecked();
         }
        return false;
    }

    public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener listener) {
        this.mOnCheckedChangeListener = listener;
    }

    CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            onCheck(buttonView, isChecked);

            if (mChequeo != null) {
                if (buttonView != checkbox2) {
                    mChequeo.valor = getSelectedLabel();
                } else {
                    mChequeo.pendiente = isChecked;
                }
            }
        }
    };

    private void onCheck(CompoundButton buttonView, boolean isChecked) {
        disabledCheckLisetner();

        if (buttonView != checkbox2) {
            radio0.setChecked(radio0 != null && radio0 == buttonView && isChecked);
            radio1.setChecked(radio1 != null && radio1 == buttonView && isChecked);
            radio3.setChecked(radio3 != null && radio3 == buttonView && isChecked);
        } else {
            buttonView.setChecked(isChecked);
        }

        enabledCheckListener();

        if (mOnCheckedChangeListener != null) {
            mOnCheckedChangeListener.onCheckedChanged(buttonView, isChecked);
        }
    }

    private void enabledCheckListener() {
        setOnCheckChangeListener(radio0, listener);
        setOnCheckChangeListener(radio1, listener);
        setOnCheckChangeListener(checkbox2, listener);
        setOnCheckChangeListener(radio3, listener);
    }

    private void disabledCheckLisetner() {
        setOnCheckChangeListener(radio0, null);
        setOnCheckChangeListener(radio1, null);
        setOnCheckChangeListener(checkbox2, null);
        setOnCheckChangeListener(radio3, null);
    }

    private void setOnCheckChangeListener(CompoundButton button, CompoundButton.OnCheckedChangeListener listener) {
        button.setOnCheckedChangeListener(listener);
    }

    public void setChecked(int checked) {
        switch (checked) {
            case 0: radio0.setChecked(true); break;
            case 1: radio1.setChecked(true); break;
            case 2:
                if (checkbox2 != null) {
                    checkbox2.setChecked(true);
                }
                break;
            case 3:
                if (radio3 != null) {
                    radio3.setChecked(true);
                }
                break;
        }

    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        return new SavedState(superState, getSelectedInt(), isChecked());
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        radio0.setChecked(savedState.getSelected() == 0);
        radio1.setChecked(savedState.getSelected() == 1);
        checkbox2.setChecked(savedState.getChecked());
        radio3.setChecked(savedState.getSelected() == 3);

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

        private final int selected;
        private final boolean checked;

        private SavedState(Parcelable superState, int selected, boolean checked) {
            super(superState);
            this.selected = selected;
            this.checked = checked;
        }

        private SavedState(Parcel in) {
            super(in);
            selected = in.readInt();
            checked = in.readInt() == 1;
        }

        public int getSelected() {
            return selected;
        }

        public boolean getChecked() {
            return checked;
        }

        @Override
        public void writeToParcel(Parcel destination, int flags) {
            super.writeToParcel(destination, flags);
            destination.writeInt(selected);
            destination.writeInt(checked ? 1 : 0);
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

    Chequeo mChequeo;

    public void setChequeo(Chequeo chequeo) {
        this.mChequeo = chequeo;
    }

    public void bindCheckeo(Chequeo chequeo, CompoundButton.OnCheckedChangeListener checkListener,
                            View.OnClickListener mCommentsClickListener) {

        setChequeo(chequeo);

        if (chequeo == null) {
            unSelectAll();
        } else {

            setOptionCheck(chequeo.pendiente);

            if (chequeo.valor != null && (chequeo.valor.equalsIgnoreCase("R") || chequeo.countCorregidos > 0)) {
                setOptionByLbl("R");
            } else {
                setOptionByLbl(chequeo.valor);
            }

            String selectedLabel = getSelectedLabel();
            if (selectedLabel != null && (selectedLabel.equalsIgnoreCase("R") && chequeo.countCorregidos > 0)) {
                setRadiosEnabled(false);
            } else {
                setRadiosEnabled(true);
            }

            getBtnComments().setTag(chequeo.id);
            getBtnComments().setOnClickListener(mCommentsClickListener);
        }

        setOnCheckedChangeListener(checkListener);
    }

}