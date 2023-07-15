package com.bilpa.android.fragments;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bilpa.android.R;
import com.bilpa.android.services.AsyncCallback;
import com.bilpa.android.utils.ImageUtils;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.mautibla.utils.InputUtils;

public class SignDialogFragment2 extends DialogFragment {

    private EditText vComment;
    private AsyncTask<Bitmap, Void, String> mTaskScalePhoto;
    private String mComment;

    public interface onSignListener {
        public void onSign(Bitmap bitmap, String signEncoded, String comment);
    }

    private onSignListener onSignedListener;

    public void setOnSignedListener(onSignListener onSignedListener) {
        this.onSignedListener = onSignedListener;
    }

    private SignaturePad vSignaturePad;

    public static SignDialogFragment2 newInstance(String comment) {
        SignDialogFragment2 frag = new SignDialogFragment2();
        frag.mComment = comment;
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
        View view = inflater.inflate(R.layout.sign_view, null);

        vComment = (EditText) view.findViewById(R.id.vComment);
        if (mComment != null) {
            vComment.setText(mComment);
        }

        vSignaturePad = (SignaturePad) view.findViewById(R.id.signature_pad);
        vSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onSigned() {
                //Event triggered when the pad is signed
                Log.i("pad", "onSigned");
            }

            @Override
            public void onClear() {
                //Event triggered when the pad is cleared
                Log.i("pad", "onClear");
            }
        });


        setCancelable(false);

        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity())
                //.title(getString(R.string.sign_enter_sign))
                .customView(view, false)
                .positiveText(R.string.btn_aceptar)
                .negativeText(R.string.btn_cancelar)
                .neutralText(R.string.btn_limpiar)
                .autoDismiss(false)
                .cancelable(false)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        dialog.dismiss();

                        if (onSignedListener != null) {


                            Bitmap sign = vSignaturePad.getSignatureBitmap();


                            encodeBitmap(sign, new AsyncCallback<String>(getActivity()) {
                                @Override
                                protected void onSuccess(String result) {
                                    Bitmap signTrasparent = vSignaturePad.getSignatureBitmap();
                                    String comment = InputUtils.getText(vComment);
                                    onSignedListener.onSign(signTrasparent, result, comment);
                                }
                            });


                        }
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                        dialog.dismiss();
                    }

                    @Override
                    public void onNeutral(MaterialDialog dialog) {
                        //     super.onNeutral(dialog);
                        vSignaturePad.clear();
                    }
                });


        MaterialDialog dialog = builder.build();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        return dialog;

    }

    private void encodeBitmap(Bitmap sign, final AsyncCallback<String> callback) {

        mTaskScalePhoto = new AsyncTask<Bitmap, Void, String>() {
            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap mSign = params[0];
                String imageBase64 = null;
                if (mSign != null) {
                    imageBase64 = ImageUtils.getBase64(ImageUtils.scale(mSign, 600, 600));
                }
                return imageBase64;
            }
            @Override
            protected void onPostExecute(String result) {
                mTaskScalePhoto = null;
                callback.success(result);
            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mTaskScalePhoto.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, sign, null, null);
        } else {
            mTaskScalePhoto.execute(sign, null, null);
        }
    }
}
