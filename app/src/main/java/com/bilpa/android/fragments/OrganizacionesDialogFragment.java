package com.bilpa.android.fragments;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bilpa.android.BilpaApp;
import com.bilpa.android.R;
import com.bilpa.android.adapters.BaseAdapter;
import com.bilpa.android.model.Organizacion;
import com.bilpa.android.services.ApiService;
import com.bilpa.android.services.AsyncCallback;
import com.bilpa.android.services.actions.ListResult;
import com.bilpa.android.services.actions.OrganizacionesResult;

import java.util.ArrayList;
import java.util.List;

public class OrganizacionesDialogFragment extends ListSelectDialogFragment {

    private static final int MIN_ITEMS_DISPLAY = 3;
    private static final int MAX_ITEMS_DISPLAY = 8;

    @Override
    protected void loadDataAsync(AsyncCallback asyncCallback) {
        if (BilpaApp.getInstance().mOrganizacionesResult != null) {
            asyncCallback.success(BilpaApp.getInstance().mOrganizacionesResult);
        } else {
            ApiService.getOrganizaciones(asyncCallback);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        BilpaApp app = BilpaApp.getInstance();
        app.cancelPendingRequests(ApiService.GET_ORGANIZACIONES);
    }

    @Override
    protected String getEmptyMsg() {
        return null;
    }

    @Override
    protected ArrayAdapter createAdapter() {
        return new Adapter(getActivity(), new ArrayList<Organizacion>());
    }

    @Override
    protected void saveDataIfNeeded(ListResult result) {
        if (BilpaApp.getInstance().mOrganizacionesResult == null) {
            BilpaApp.getInstance().mOrganizacionesResult = new OrganizacionesResult();
        }
        BilpaApp.getInstance().mOrganizacionesResult.organizaciones = result.getItems();
    }


    @Override
    protected String getToolbarTitle() {
        return getString(R.string.oraganizaciones_search_cargo_title);
    }

    @Override
    protected void onBack() {
        dismiss();
    }

    private class Adapter extends BaseAdapter<Organizacion> {

        private Adapter(Context context, List<Organizacion> items) {
            super(context, 0, items);
        }

        @Override
        protected View newView(int position, ViewGroup parent, Organizacion item) {
            View v = mInflater.inflate(R.layout.row_item_list1, parent, false);

            ViewHolder holder = new ViewHolder();
            holder.vTitle = (TextView) v.findViewById(R.id.vTitle);

            v.setTag(holder);

            return v;
        }

        @Override
        protected void bindView(View v, Organizacion item, int position) {
            ViewHolder h = (ViewHolder) v.getTag();
            h.vTitle.setText(item.getNombreOrganizacion());
        }


        private class ViewHolder {
            public TextView vTitle;
        }
    }

    protected int getDialogHeight() {

        int abHeight = getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material);

        if (BilpaApp.getInstance().mOrganizacionesResult == null || BilpaApp.getInstance().mOrganizacionesResult.organizaciones.size() == 0) {
            return getContainerMinHeight() + abHeight;
        }

        if (BilpaApp.getInstance().mOrganizacionesResult.organizaciones.size() > getMinItemsDisplay()) {
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
        int count = Math.min(getMaxItemsDisplay(), BilpaApp.getInstance().mOrganizacionesResult.organizaciones.size());
        int height = rowHeight * count;
        return height;
    }

    private int getRowHeight() {
        return getResources().getDimensionPixelSize(R.dimen.row_picos_height);
    }

}
