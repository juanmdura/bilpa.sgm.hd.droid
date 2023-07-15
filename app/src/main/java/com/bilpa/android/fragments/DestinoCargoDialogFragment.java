package com.bilpa.android.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bilpa.android.BilpaApp;
import com.bilpa.android.R;
import com.bilpa.android.adapters.BaseAdapter;
import com.bilpa.android.adapters.PicosAdapter;
import com.bilpa.android.model.DestinoCargo;
import com.bilpa.android.model.Pico;
import com.bilpa.android.services.ApiService;
import com.bilpa.android.services.AsyncCallback;
import com.bilpa.android.services.actions.DestinoCargoResult;
import com.bilpa.android.services.actions.ListResult;
import com.bilpa.android.services.actions.PicosResult;
import com.mautibla.utils.ViewUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DestinoCargoDialogFragment extends ListSelectDialogFragment {

    private static final int MIN_ITEMS_DISPLAY = 3;
    private static final int MAX_ITEMS_DISPLAY = 8;



    @Override
    protected void loadDataAsync(AsyncCallback asyncCallback) {
        if (BilpaApp.getInstance().mDestinoCargoResult != null) {
            asyncCallback.success(BilpaApp.getInstance().mDestinoCargoResult);
        } else {
            ApiService.getDestinoCargo(asyncCallback);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        BilpaApp app = BilpaApp.getInstance();
        app.cancelPendingRequests(ApiService.GET_DESTINO_CARGOS);
    }

    @Override
    protected String getEmptyMsg() {
        return null;
    }

    @Override
    protected ArrayAdapter createAdapter() {
        return new Adapter(getActivity(), new ArrayList<DestinoCargo>());
    }

    @Override
    protected void saveDataIfNeeded(ListResult result) {
        if (BilpaApp.getInstance().mDestinoCargoResult == null) {
            BilpaApp.getInstance().mDestinoCargoResult = new DestinoCargoResult();
        }
        BilpaApp.getInstance().mDestinoCargoResult.cargos = result.getItems();
    }


    @Override
    protected String getToolbarTitle() {
        return getString(R.string.destino_search_cargo_title);
    }

    @Override
    protected void onBack() {
        dismiss();
    }

    private class Adapter extends BaseAdapter<DestinoCargo> {

        private Adapter(Context context, List<DestinoCargo> items) {
            super(context, 0, items);
        }

        @Override
        protected View newView(int position, ViewGroup parent, DestinoCargo item) {
            View v = mInflater.inflate(R.layout.row_item_list1, parent, false);

            ViewHolder holder = new ViewHolder();
            holder.vTitle = (TextView) v.findViewById(R.id.vTitle);

            v.setTag(holder);

            return v;
        }

        @Override
        protected void bindView(View v, DestinoCargo item, int position) {
            ViewHolder h = (ViewHolder) v.getTag();
            h.vTitle.setText(item.nombre);
        }


        private class ViewHolder {
            public TextView vTitle;
        }
    }

    protected int getDialogHeight() {

        int abHeight = getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material);

        if (BilpaApp.getInstance().mDestinoCargoResult == null || BilpaApp.getInstance().mDestinoCargoResult.cargos.size() == 0) {
            return getContainerMinHeight() + abHeight;
        }

        if (BilpaApp.getInstance().mDestinoCargoResult.cargos.size() > getMinItemsDisplay()) {
            return getContainerMaxHeight() + abHeight;
        }

        return ViewGroup.LayoutParams.WRAP_CONTENT;
    }


    @Override
    protected int getContainerMinHeight() {
        int rowHeight = getRowHeight();
        int height = rowHeight * getMinItemsDisplay();
        return height;
    }

    private static int getMinItemsDisplay() {
        return MIN_ITEMS_DISPLAY;
    }

    private int getMaxItemsDisplay() {
        return MAX_ITEMS_DISPLAY;
    }

    @Override
    protected int getContainerMaxHeight() {
        int rowHeight = getRowHeight();
        int count = Math.min(getMaxItemsDisplay(), BilpaApp.getInstance().mDestinoCargoResult.cargos.size());
        int height = rowHeight * count;
        return height;
    }

    private int getRowHeight() {
        return getResources().getDimensionPixelSize(R.dimen.row_picos_height);
    }

}
