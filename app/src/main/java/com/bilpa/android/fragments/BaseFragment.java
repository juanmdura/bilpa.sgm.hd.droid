package com.bilpa.android.fragments;


import android.support.v4.app.Fragment;

import com.bilpa.android.BaseActivity;

public abstract class BaseFragment extends Fragment {

    public void onBack() {
        if (BaseActivity.class.isInstance(getActivity())) {
            BaseActivity ba = (BaseActivity) getActivity();
            ba.onBackPressedImpl();
        } else {
            getActivity().onBackPressed();
        }
    }

}



