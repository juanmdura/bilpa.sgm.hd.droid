package com.bilpa.android.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bilpa.android.R;
import com.mautibla.utils.InputUtils;

public class CommentDialogFragment extends DialogFragment {

    private EditText vComment;
    private OnCommentListener onCommentListener;

    private String mComment;
    private String title;
    private String hint;

    public interface OnCommentListener {
        void onComment(String comment);
    }

    public void setOnCommentListener(OnCommentListener onCommentListener) {
        this.onCommentListener = onCommentListener;
    }

    public static CommentDialogFragment newInstance() {
        CommentDialogFragment frag = new CommentDialogFragment();
        frag.mComment = null;
        return frag;
    }

    public static CommentDialogFragment newInstance(String comment) {
        CommentDialogFragment frag = new CommentDialogFragment();
        frag.mComment = comment;
        return frag;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCommentHint(String hint) {
        this.hint = hint;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.comment_dialog_view, null);

        TextView vCommentLbl = (TextView) view.findViewById(R.id.vCommentLbl);
        if (title == null) {
            vCommentLbl.setText(R.string.comments_new_title);
        } else {
            vCommentLbl.setText(title);
        }

        vComment = (EditText) view.findViewById(R.id.vComment);
        if (TextUtils.isEmpty(hint)) {
            vComment.setHint(R.string.visita_sign_lbl_enter_comment);
        } else {
            vComment.setHint(hint);
        }
        if (mComment != null) {
            vComment.setText(mComment);
            vComment.setSelection(vComment.getText().length());
        }

        setCancelable(false);

        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity())
                .customView(view, false)
                .positiveText(R.string.btn_aceptar)
                .negativeText(R.string.btn_cancelar)
                .autoDismiss(false)
                .cancelable(false)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        dialog.dismiss();
                        if (onCommentListener != null) {
                            String comment = InputUtils.getText(vComment);
                            onCommentListener.onComment(comment);
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
}
