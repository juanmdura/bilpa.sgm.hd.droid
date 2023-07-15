package com.bilpa.android.fragments;

import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public abstract class ABFragment extends Fragment implements View.OnClickListener {

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            View actionView = item.getActionView();
            if (actionView != null) {
                actionView.setTag(item);
                actionView.setOnClickListener(this);
            }
        }
    }

    @Override
    public void onClick(View view) {
        Object tag = view.getTag();
        if (tag != null) {
            if (MenuItem.class.isInstance(tag)) {
                MenuItem menuItem = (MenuItem) tag;
                onOptionsItemSelected(menuItem);
            }
        }
    }

    public boolean showABBack() {
        return false;
    }

}
