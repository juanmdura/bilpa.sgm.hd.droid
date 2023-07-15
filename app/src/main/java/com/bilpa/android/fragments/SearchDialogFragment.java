package com.bilpa.android.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.View;

import com.bilpa.android.R;

public abstract class SearchDialogFragment extends ToolbarDialogFragment implements SearchView.OnQueryTextListener {

    private SearchView mSearchView;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Menu menu = getToolbar().getMenu();

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        mSearchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.vMenuSearch));
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
    }

    protected SearchView getSearchView() {
        return mSearchView;
    }

    @Override
    protected void onBack() {
        if (mSearchView.isShown()) {
            mSearchView.clearFocus();
        } else {
            dismiss();
        }
    }
}
