package com.bilpa.android.fragments;

import android.app.Dialog;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bilpa.android.R;
import com.mautibla.utils.EmailValidator;
import com.mautibla.utils.InputUtils;

import java.util.ArrayList;
import java.util.List;

public class EmailsDialogFragment2 extends DialogFragment implements View.OnClickListener {

    private LinearLayout vEmailsPanel;
    private ImageButton vBtnAdd;
    private String dialogTitle;
    private List<String> mPreloadEmails;

    public interface OnEmaisListener {
        void onEmails(List<String> emails);
    }

    private OnEmaisListener mOnEmailsListener;

    public void setOnEmailsListener(OnEmaisListener onEmailsListener) {
        this.mOnEmailsListener = onEmailsListener;
    }

    public static EmailsDialogFragment2 newInstance(String dialogTitle, List<String> preloadEmails) {
        EmailsDialogFragment2 frag = new EmailsDialogFragment2();
        frag.dialogTitle = dialogTitle;
        frag.mPreloadEmails = preloadEmails;
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();

        /**
         * Vista custom para el dialogo.
         */
        View view = inflater.inflate(R.layout.emails_view, null);


        // Panel que contiene los rows de los emails.
        vEmailsPanel = (LinearLayout) view.findViewById(R.id.vEmailsPanel);

        /**
         * Listener para agregar un nuevo input de email.
         */
        vBtnAdd = (ImageButton) view.findViewById(R.id.vBtnAdd);
        vBtnAdd.setOnClickListener(this);

        /**
         * Se cargan los mails ya asociados a la visita
         */

        if (mPreloadEmails == null || mPreloadEmails.isEmpty()) {
            // Se agrega un campo para ingresar el primero
            addEmptyRow();

        } else {

            // Se agregan los emails ya asociados
            for (int i = 0; i < mPreloadEmails.size(); i++) {
                addRow(mPreloadEmails.get(i));
            }

            // Se agrega un campo para uno nuevo.
            addEmptyRow();
        }


        /**
         * No se puede cancelar el dialogo
         */
        setCancelable(false);


        /**
         * Se crea y se retorna el dialogo
         */
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity())
                .title(dialogTitle)
                .customView(view, false)
                .positiveText(R.string.btn_aceptar)
                .negativeText(R.string.btn_cancelar)
                .autoDismiss(false)
                .cancelable(false)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        if (validate()) {
                            dialog.dismiss();
                            if (mOnEmailsListener != null) {
                                mOnEmailsListener.onEmails(getEmails());
                            }
                        }
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                        dialog.dismiss();
                    }

                    @Override
                    public void onNeutral(MaterialDialog dialog) {
                    }
                });


        MaterialDialog dialog = builder.build();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        return dialog;

    }

    private boolean validate() {

        boolean validate = true;

        int childCount = vEmailsPanel.getChildCount();

        for (int i = 0; i < childCount; i++) {
            View vRow = vEmailsPanel.getChildAt(i);
            EditText vEmail = (EditText) vRow.findViewById(R.id.vEmail);
            if (!EmailValidator.validate(vEmail)) {
                Drawable errorIcon = getResources().getDrawable(R.drawable.ic_error_red_18dp);
                errorIcon.setBounds(new Rect(0, 0, errorIcon.getIntrinsicWidth(), errorIcon.getIntrinsicHeight()));
                vEmail.setError(getString(R.string.visita_emails_invalid_mail), errorIcon);
                validate = false;
            }
        }
        return validate;
    }


    private EditText getEmptyField() {
        int childCount = vEmailsPanel.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View vRow = vEmailsPanel.getChildAt(i);
            EditText vEmail = (EditText) vRow.findViewById(R.id.vEmail);
            if (InputUtils.isEmpty(vEmail)) {
                return vEmail;
            }
        }
        return null;
    }


    private List<String> getEmails() {

        List<String> result = new ArrayList<>();

        int childCount = vEmailsPanel.getChildCount();

        for (int i = 0; i < childCount; i++) {
            View vRow = vEmailsPanel.getChildAt(i);
            EditText vEmail = (EditText) vRow.findViewById(R.id.vEmail);
            String email = InputUtils.getText(vEmail);
            result.add(email);
        }

        return result;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.vBtnAdd:

                EditText emptyField = getEmptyField();
                if (emptyField == null) {
                    addEmptyRow();
                } else {
                    emptyField.requestFocus();
                }
                break;

            case R.id.vBtnDelete:
                View vRow = (View) v.getTag();
                vEmailsPanel.removeView(vRow);
                break;
        }

    }

    private void addEmptyRow() {
        View vRow = getActivity().getLayoutInflater().inflate(R.layout.row_emails, null);
        vEmailsPanel.addView(vRow);
        ImageButton vBtnDelete = (ImageButton) vRow.findViewById(R.id.vBtnDelete);
        vBtnDelete.setTag(vRow);
        vBtnDelete.setOnClickListener(this);
        if (vEmailsPanel.getChildCount() == 1) {
            vBtnDelete.setVisibility(View.INVISIBLE);
        }

    }

    private void addRow(String email) {
        View vRow = getActivity().getLayoutInflater().inflate(R.layout.row_emails, null);
        vEmailsPanel.addView(vRow);
        EditText vEmail = (EditText) vRow.findViewById(R.id.vEmail);
        vEmail.setText(email);
        ImageButton vBtnDelete = (ImageButton) vRow.findViewById(R.id.vBtnDelete);
        vBtnDelete.setOnClickListener(this);
        vBtnDelete.setTag(vRow);
        if (vEmailsPanel.getChildCount() == 1) {
            vBtnDelete.setVisibility(View.INVISIBLE);
        }
    }

}
