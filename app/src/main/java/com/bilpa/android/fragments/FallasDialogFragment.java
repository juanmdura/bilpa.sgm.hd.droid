package com.bilpa.android.fragments;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.bilpa.android.BilpaApp;
import com.bilpa.android.R;
import com.bilpa.android.adapters.FallasAdapter;
import com.bilpa.android.adapters.FiltrableAdapter;
import com.bilpa.android.adapters.TipoFallaSpinnerAdapter;
import com.bilpa.android.model.Falla;
import com.bilpa.android.model.TipoFalla;
import com.bilpa.android.services.ApiService;
import com.bilpa.android.services.AsyncCallback;
import com.bilpa.android.services.actions.FallasResult;
import com.bilpa.android.services.actions.TiposFallaResult;
import com.mautibla.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

public class FallasDialogFragment extends SearchDialogFragment implements AdapterView.OnItemClickListener{

    private ListView vList;
    private Spinner vFilter;
    private ProgressBar vListLoading;
    private TextView vEmptyView;
    private FallasAdapter mDataAdapter;

    private OnFallaSelectedListener mOnFallaSelectedListener;

    public interface OnFallaSelectedListener {
        public void onFallaSelected(Falla falla);
    }


    @Override
    protected Toolbar getToolbar() {
        return (Toolbar) getView().findViewById(R.id.vToolbar);
    }

    @Override
    protected String getToolbarTitle() {
        return getString(R.string.fallas_search_title);
    }

    @Override
    protected int getToolbarNavigationIcon() {
        return R.drawable.ic_arrow_back_white_24dp;
    }

    @Override
    protected int getToolbarMenu() {
        return R.menu.fallas;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if (vList != null) {
            FallasAdapter adapter = (FallasAdapter) vList.getAdapter();
            if (adapter != null && adapter.getFilter() != null) {
                Filter filter = adapter.getFilter();
                filter.filter(s);
            }
        }
        return true;
    }

    public void setOnFallaSelectedListener(OnFallaSelectedListener onFallaSelectedListener) {
        this.mOnFallaSelectedListener = onFallaSelectedListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fallas_dialog, container, false);

        vFilter = (Spinner) v.findViewById(R.id.vFilter);
        vList = (ListView) v.findViewById(R.id.vList);
        vListLoading = (ProgressBar) v.findViewById(R.id.vListLoading);
        vEmptyView = (TextView) v.findViewById(R.id.vEmptyView);

        return v;

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        vList.setEmptyView(vEmptyView);

        ViewUtils.gone(vList, vEmptyView);
        ViewUtils.visible(vListLoading);


        if (BilpaApp.getInstance().mTiposFallas == null) {

            ApiService.getTipoFallas(new AsyncCallback<TiposFallaResult>(getActivity()) {
                @Override
                protected void onSuccess(TiposFallaResult result) {
                    BilpaApp.getInstance().mTiposFallas = result;
                    bindTipos(result);
                }
            });
        } else {
            bindTipos(BilpaApp.getInstance().mTiposFallas);
        }


        if (BilpaApp.getInstance().mFallas == null) {
            ApiService.getFallas(new AsyncCallback<FallasResult>(getActivity()) {
                @Override
                protected void onSuccess(FallasResult result) {
                    BilpaApp.getInstance().mFallas= result;
                    bindData(result);
                }
            });
        } else {
            bindData(BilpaApp.getInstance().mFallas);
        }

    }

    private void bindTipos(TiposFallaResult result) {
        if (result.tiposFalla.size() == 0) {
            ViewUtils.gone(vFilter);
        } else {
            ViewUtils.visible(vFilter);
            List<TipoFalla> filterItems= result.tiposFalla;
            TipoFallaSpinnerAdapter adapter = new TipoFallaSpinnerAdapter(getActivity(), filterItems);
            vFilter.setAdapter(adapter);
            vFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    TipoFalla tipoFalla = null;
                    if (position == 0) {
                        tipoFalla = null;
                    } else {
                        tipoFalla = ((TipoFallaSpinnerAdapter)parent.getAdapter()).getItem(position);
                    }
                    if (mDataAdapter != null) {
                        mDataAdapter.selected = tipoFalla;
                        mDataAdapter.notifyDataSetChanged();
                        mDataAdapter.getFilter().filter(getSearchView().getQuery());
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    private void bindData(FallasResult result) {

        ViewUtils.visible(vList, vEmptyView);
        ViewUtils.gone(vListLoading);


        List<Falla> items = new ArrayList<Falla>(result.fallas);

        mDataAdapter = new FallasAdapter(getActivity(), items);
        mDataAdapter.setFilterListener(new FiltrableAdapter.FilterListener() {
            @Override
            public void onFilter() {
                // Se setea el msg por defecto. No se muestra.
                showEmptyMsg(R.string.fallas_search_empty);
            }

            @Override
            public void onNonResults() {
                showEmptyMsg(R.string.fallas_search_empty_filter);
            }
        });
        vList.setAdapter(mDataAdapter);
        vList.setOnItemClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        BilpaApp app = BilpaApp.getInstance();
        app.cancelPendingRequests(ApiService.GET_TIPOS_FALLAS);
        app.cancelPendingRequests(ApiService.GET_FALLAS);
    }

    private void showEmptyMsg(int msg) {
        final String strMsg = getString(msg);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                vEmptyView.setText(strMsg);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mOnFallaSelectedListener != null) {
            Falla item = mDataAdapter.getItem(position);
            mOnFallaSelectedListener.onFallaSelected(item);
        }
        dismiss();
    }



}
