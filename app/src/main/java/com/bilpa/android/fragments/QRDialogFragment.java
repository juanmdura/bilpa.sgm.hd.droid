package com.bilpa.android.fragments;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.bilpa.android.R;
import com.mautibla.utils.ViewUtils;

import java.util.ArrayList;

import me.dm7.barcodescanner.zbar.BarcodeFormat;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class QRDialogFragment extends ToolbarDialogFragment implements ZBarScannerView.ResultHandler
        ,MessageDialogFragment.MessageDialogListener, View.OnClickListener {

    private ZBarScannerView mScannerView;

    private static final String FLASH_STATE = "FLASH_STATE";
    private static final String AUTO_FOCUS_STATE = "AUTO_FOCUS_STATE";
    private static final String SELECTED_FORMATS = "SELECTED_FORMATS";
    private boolean mFlash;
    private boolean mAutoFocus;
    private Button vBtnNext;
    private Button vBtnRetry;
    private String title;

    public static final QRDialogFragment newInstance() {
        return new QRDialogFragment();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.vBtnRetry:
                ViewUtils.gone(vBtnNext, vBtnRetry);
                mScannerView.startCamera();
                break;

            case R.id.vBtnNext:
                dismiss();
                String result = (String) v.getTag();
                if (result != null) {
                    if (qrListener != null) {
                        qrListener.onQueryCode(result);
                    }
                }
                break;
        }
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public interface QRListener {
        public void onQueryCode(String code);
    }

    private QRListener qrListener;

    public void setQRListener(QRListener qrListener) {
        this.qrListener = qrListener;
    }

    @Override
    protected Toolbar getToolbar() {
        return (Toolbar) getView().findViewById(R.id.vToolbar);
    }

    @Override
    protected String getToolbarTitle() {
        if (title != null) {
            return title;
        } else {
            return getString(R.string.qr_reader_title);
        }
    }

    @Override
    protected int getToolbarNavigationIcon() {
        return R.drawable.ic_arrow_back_white_24dp;
    }

    @Override
    protected int getToolbarMenu() {
        return 0;
    }

    @Override
    protected void onBack() {
        dismiss();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        return false;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {
        View v = inflater.inflate(R.layout.fragment_qr_dialog, container, false);

        vBtnRetry = (Button) v.findViewById(R.id.vBtnRetry);
        vBtnRetry.setOnClickListener(this);

        vBtnNext = (Button) v.findViewById(R.id.vBtnNext);
        vBtnNext.setOnClickListener(this);

        mScannerView = new ZBarScannerView(getActivity());
        if(state != null) {
            mFlash = state.getBoolean(FLASH_STATE, false);
            mAutoFocus = state.getBoolean(AUTO_FOCUS_STATE, true);

        } else {
            mFlash = false;
            mAutoFocus = true;
        }



        setupFormats();


        FrameLayout vContainer = (FrameLayout) v.findViewById(R.id.container);
        vContainer.addView(mScannerView, 0);



        return v;

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ViewUtils.gone(vBtnNext, vBtnRetry);
    }


    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
        mScannerView.setFlash(mFlash);
        mScannerView.setAutoFocus(mAutoFocus);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(FLASH_STATE, mFlash);
        outState.putBoolean(AUTO_FOCUS_STATE, mAutoFocus);
    }

    @Override
    public void handleResult(Result rawResult) {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getActivity().getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {}
        //showMessageDialog("Contents = " + rawResult.getContents() + ", Format = " + rawResult.getBarcodeFormat().getName());
        ViewUtils.visible(vBtnNext, vBtnRetry);
        vBtnNext.setTag(rawResult.getContents());
    }

//    public void showMessageDialog(String message) {
//        DialogFragment fragment = MessageDialogFragment.newInstance("Scan Results", message, this);
//        fragment.show(getActivity().getFragmentManager(), "scan_results");
//    }

    public void closeMessageDialog() {
        closeDialog("scan_results");
    }

    public void closeFormatsDialog() {
        closeDialog("format_selector");
    }

    public void closeDialog(String dialogName) {
        FragmentManager fragmentManager = getActivity().getFragmentManager();
        DialogFragment fragment = (DialogFragment) fragmentManager.findFragmentByTag(dialogName);
        if(fragment != null) {
            fragment.dismiss();
        }
    }



    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // Resume the camera
        mScannerView.startCamera();
        mScannerView.setFlash(mFlash);
        mScannerView.setAutoFocus(mAutoFocus);
    }

    public void setupFormats() {
        if(mScannerView != null) {
            if (mScannerView.getFormats() == null) {
                mScannerView.setFormats(new ArrayList<BarcodeFormat>());
            }
            mScannerView.getFormats().add(BarcodeFormat.QRCODE);
        }
    }



    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
        closeMessageDialog();
        closeFormatsDialog();
    }



}
