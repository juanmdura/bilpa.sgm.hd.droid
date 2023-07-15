package com.bilpa.android.fragments;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bilpa.android.BaseActivity;
import com.bilpa.android.R;

public abstract class SaveSupportFragment extends BaseSupportFragment implements View.OnClickListener, Toolbar.OnMenuItemClickListener {


    private boolean unSavedData = false;

    public boolean isUnSavedData() {
        return unSavedData;
    }

    public void setUnSavedData(boolean unSavedData) {
        this.unSavedData = unSavedData;
    }

    public void showMsgUnSaved(final MaterialDialog.ButtonCallback callback) {
        showLeftMsg(new MaterialDialog.ButtonCallback() {
            @Override
            public void onPositive(MaterialDialog dialog) {
                setUnSavedData(false);
                callback.onPositive(dialog);
            }
        });
    }

    public void showMsgUnSaved(final View view) {
        showLeftMsg(new MaterialDialog.ButtonCallback() {
            @Override
            public void onPositive(MaterialDialog dialog) {
                setUnSavedData(false);
                onActionClick(view);
            }
        });
    }

    public void showMsgUnSaved(final MenuItem item) {
        showLeftMsg(new MaterialDialog.ButtonCallback() {
            @Override
            public void onPositive(MaterialDialog dialog) {
                setUnSavedData(false);
                onActionClick(item);
            }
        });
    }

    public void showLeftMsg(MaterialDialog.ButtonCallback callback) {
        new MaterialDialog.Builder(getActivity())
                .title(R.string.app_name)
                .iconRes(R.drawable.ic_launcher)
                .content(getUnsavedExitMsg())
                .positiveText(R.string.btn_aceptar)
                .negativeText(R.string.btn_cancelar)
                .callback(callback)
                .show();
    }

        @Override
    public final void onClick(View v) {

        int[] ints = assertViewSavedCheck();
        boolean hasItemsNeedSave = ints != null && ints.length > 0;

        if (!unSavedData || !hasItemsNeedSave) {
            onActionClick(v);
            return;
        }

        for (int i : ints) {
            if (i == v.getId()) {
                showMsgUnSaved(v);
                return;
            }
        }

        onActionClick(v);

    }

    @Override
    public final boolean onMenuItemClick(MenuItem menuItem) {

        int[] ints = assertMenuItemSavedCheck();
        boolean hasItemsNeedSave = ints != null && ints.length > 0;

        if (!unSavedData || !hasItemsNeedSave) {
            onActionClick(menuItem);
            return true;
        }

        for (int i : ints) {
            if (i == menuItem.getItemId()) {
                showMsgUnSaved(menuItem);
                return true;
            }
        }

        onActionClick(menuItem);

        return true;
    }

    @Override
    public void onBack() {
        if (isUnSavedData()) {
            showLeftMsg(new MaterialDialog.ButtonCallback() {
                @Override
                public void onPositive(MaterialDialog dialog) {
                    setUnSavedData(false);
                    if (BaseActivity.class.isInstance(getActivity())) {
                        BaseActivity ba = (BaseActivity) getActivity();
                        ba.onBackPressedImpl();
                    } else {
                        getActivity().onBackPressed();
                    }
                }
            });
        } else {
            super.onBack();
        }
    }

    protected abstract String getUnsavedExitMsg();
    protected abstract int[] assertMenuItemSavedCheck();
    protected abstract int[] assertViewSavedCheck();
    protected abstract void onActionClick(MenuItem item);
    protected abstract void onActionClick(View view);
}
