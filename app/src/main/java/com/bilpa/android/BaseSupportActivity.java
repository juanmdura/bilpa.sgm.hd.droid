package com.bilpa.android;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.bilpa.android.fragments.BaseSupportFragment;

public abstract class BaseSupportActivity extends AppCompatActivity {

    public static final String TAG = "BaseActivity";


    public abstract void onBackPressedImpl();

    @Override
    public void onBackPressed() {
        Fragment f = peekFragment();
        if (BaseSupportFragment.class.isInstance(f)) {
            BaseSupportFragment bf = (BaseSupportFragment) f;
            bf.onBack();
        } else {
            onBackPressedImpl();
        }
    }

    public Fragment peekFragment() {
        String tag = peekTag();
        if (tag != null) {
            Fragment f = getFragManager().findFragmentByTag(tag);
            return f;
        }
        return null;
    }

    public final String peekTag() {

        FragmentManager fm = getFragManager();
        if (fm.getBackStackEntryCount() > 0) {
            String fragmentTag = fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 1).getName();
            if (fragmentTag != null) {
                return fragmentTag;
            }
        }

        return null;
    }

    public final String peekPreviuosTag() {
        FragmentManager fm = getFragManager();
        if (fm.getBackStackEntryCount() > 1) {
            String fragmentTag = fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 2).getName();
            if (fragmentTag != null) {
                return fragmentTag;
            }
        }
        return null;
    }

    public FragmentManager getFragManager() {
        return getSupportFragmentManager();
    }

}
