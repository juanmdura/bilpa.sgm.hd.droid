package com.bilpa.android.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.bilpa.android.R;

public abstract class ToolbarDialogSupportFragment extends DialogFragment implements Toolbar.OnMenuItemClickListener {

    private Toolbar vToolbar;
   
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int style = DialogFragment.STYLE_NORMAL;
        int theme = R.style.AppTheme_Dialog;
        setStyle(style, theme);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vToolbar = getToolbar();
    }

    protected abstract Toolbar getToolbar();
    protected abstract String getToolbarTitle();
    protected abstract int getToolbarNavigationIcon();
    protected abstract int getToolbarMenu();
    protected abstract void onBack();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        vToolbar.setNavigationIcon(getToolbarNavigationIcon());
        if (getToolbarMenu() != 0) {
            vToolbar.inflateMenu(getToolbarMenu());
        }
        vToolbar.setOnMenuItemClickListener(this);

        TextView vToolbarTitle = (TextView) vToolbar.findViewById(R.id.vToolbarTitle);
        vToolbarTitle.setText(getToolbarTitle());

        vToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBack();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        invalidateSize();
    }

    protected void invalidateSize() {
        // safety check
        if (getDialog() == null) {
            return;
        }

        int dialogWidth = getDialogWidth();
        int dialogHeight = getDialogHeight();

        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
    }

    protected int getDialogHeight() {
        return getResources().getDimensionPixelSize(R.dimen.dialogs_default_height);
    }

    protected int getDialogWidth() {
        return getResources().getDimensionPixelSize(R.dimen.dialogs_default_width);
    }

}
