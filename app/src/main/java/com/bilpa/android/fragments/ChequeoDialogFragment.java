package com.bilpa.android.fragments;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bilpa.android.R;
import com.bilpa.android.adapters.BaseAdapter;
import com.bilpa.android.model.Chequeo;
import com.bilpa.android.model.ChequeoList;
import com.bilpa.android.services.AsyncCallback;
import com.bilpa.android.services.actions.ListResult;

import java.util.ArrayList;
import java.util.List;

public class ChequeoDialogFragment extends ListSelectDialogFragment {

    private ChequeoList chequeos;

    public static ChequeoDialogFragment newInstance(ChequeoList chequeos) {
        ChequeoDialogFragment f = new ChequeoDialogFragment();
        f.chequeos = chequeos;
        return f;
    }

    @Override
    protected void loadDataAsync(AsyncCallback asyncCallback) {
        asyncCallback.success(chequeos);
    }

    @Override
    protected String getEmptyMsg() {
        return null;
    }

    @Override
    protected ArrayAdapter createAdapter() {
        return new Adapter(getActivity(), new ArrayList<Chequeo>());
    }

    @Override
    protected void saveDataIfNeeded(ListResult result) {
    }

    @Override
    protected String getToolbarTitle() {
        return getString(R.string.chequeos_select_title);
    }

    @Override
    protected void onBack() {
        dismiss();
    }

    @Override
    protected int getContainerMinHeight() {
        return ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    @Override
    protected int getContainerMaxHeight() {
        return ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    private class Adapter extends BaseAdapter<Chequeo> {

        private Adapter(Context context, List<Chequeo> items) {
            super(context, 0, items);
        }

        @Override
        protected View newView(int position, ViewGroup parent, Chequeo item) {
            View v = mInflater.inflate(R.layout.row_item_list1, parent, false);
            ViewHolder holder = new ViewHolder();
            holder.vTitle = (TextView) v.findViewById(R.id.vTitle);
            v.setTag(holder);
            return v;
        }

        @Override
        protected void bindView(View v, Chequeo item, int position) {
            ViewHolder h = (ViewHolder) v.getTag();
            h.vTitle.setText(item.texto);
        }

        private class ViewHolder {
            public TextView vTitle;
        }
    }
}
