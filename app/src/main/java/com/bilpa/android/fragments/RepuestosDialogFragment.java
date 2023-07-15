package com.bilpa.android.fragments;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.bilpa.android.BilpaApp;
import com.bilpa.android.R;
import com.bilpa.android.adapters.FiltrableAdapter;
import com.bilpa.android.adapters.RepuestosAdapter;
import com.bilpa.android.adapters.TipoRepuestoSpinnerAdapter;
import com.bilpa.android.model.Repuesto;
import com.bilpa.android.model.TipoRepuesto;
import com.bilpa.android.services.ApiService;
import com.bilpa.android.services.AsyncCallback;
import com.bilpa.android.services.actions.RepuestosResult;
import com.bilpa.android.services.actions.TiposRepuestosResult;
import com.mautibla.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

public class RepuestosDialogFragment extends DialogFragment implements Toolbar.OnMenuItemClickListener, AdapterView.OnItemClickListener {

    private ListView vListRepuestos;
    private Toolbar vToolbar;
    private Spinner vFilter;
    private View vListHeaderSep;
    private ProgressBar vListLoading;
    private TextView vEmptyView;
    private View vListHeader;
    private SearchView mSearchView;
    private RepuestosAdapter mRepuestosAdapter;


    private OnRepuestoSelectedListener mOnRepuestoSelectedListener;

    public interface OnRepuestoSelectedListener {
        public void onRepuestoSelected(Repuesto repuesto);
    }

    public void setOnRepuestoSelectedListener(OnRepuestoSelectedListener onRepuestoSelectedListener) {
        this.mOnRepuestoSelectedListener = onRepuestoSelectedListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_repuestos_dialog, container, false);

        vToolbar = (Toolbar) v.findViewById(R.id.vToolbar);
        vFilter = (Spinner) v.findViewById(R.id.vFilter);
        vListRepuestos = (ListView) v.findViewById(R.id.vListRepuestos);
        vListHeader = (View) v.findViewById(R.id.vListHeader);
        vListHeaderSep = (View) v.findViewById(R.id.vListHeaderSep);
        vListLoading = (ProgressBar) v.findViewById(R.id.vListLoading);
        vEmptyView = (TextView) v.findViewById(R.id.vEmptyView);

        return v;

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        vToolbar.setTitle(getString(R.string.repuestos_search_title));
        vToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        vToolbar.inflateMenu(R.menu.repuestos);
        vToolbar.setOnMenuItemClickListener(this);

        configSearch(vToolbar.getMenu());
        vToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSearchView.isShown()) {
                    mSearchView.clearFocus();
                } else {
                    dismiss();
                }
            }
        });

        vListRepuestos.setEmptyView(vEmptyView);


        ViewUtils.gone(vListRepuestos, vEmptyView, vListHeaderSep, vListHeader);
        ViewUtils.visible(vListLoading);


        if (BilpaApp.getInstance().mTiposRepuestos == null) {
            ApiService.getTiposRepuestos(ApiService.GET_TIPOS_REPUESTOS, new AsyncCallback<TiposRepuestosResult>(getActivity()) {
                @Override
                protected void onSuccess(TiposRepuestosResult result) {
                    BilpaApp.getInstance().mTiposRepuestos = result;
                    bindTiposRepuestos(result);
                }
            });
        } else {
            bindTiposRepuestos(BilpaApp.getInstance().mTiposRepuestos);
        }


        if (BilpaApp.getInstance().mRepuestos == null) {
            ApiService.getRepuestos(ApiService.GET_REPUESTOS, new AsyncCallback<RepuestosResult>(getActivity()) {
                @Override
                protected void onSuccess(RepuestosResult result) {
                    BilpaApp.getInstance().mRepuestos = result;
                    bindRepuestos(result);
                }
            });
        } else {
            bindRepuestos(BilpaApp.getInstance().mRepuestos);
        }

    }

    private void bindTiposRepuestos(TiposRepuestosResult result) {
        if (result.tiposRepuesto.size() == 0) {
            ViewUtils.gone(vFilter);
        } else {
            ViewUtils.visible(vFilter);
            List<TipoRepuesto> filterItems= result.tiposRepuesto;
            TipoRepuestoSpinnerAdapter adapter = new TipoRepuestoSpinnerAdapter(getActivity(), filterItems);
            vFilter.setAdapter(adapter);
            vFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    TipoRepuesto tipoRepuesto = null;
                    if (position == 0) {
                        tipoRepuesto = null;
                    } else {
                        tipoRepuesto = ((TipoRepuestoSpinnerAdapter)parent.getAdapter()).getItem(position);
                    }
                    if (mRepuestosAdapter != null) {
                        mRepuestosAdapter.selectedTipoRepuesto = tipoRepuesto;
                        mRepuestosAdapter.notifyDataSetChanged();
                        mRepuestosAdapter.getFilter().filter(mSearchView.getQuery());
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    private void bindRepuestos(RepuestosResult result) {

        ViewUtils.visible(vListRepuestos, vEmptyView, vListHeaderSep, vListHeader);
        ViewUtils.gone(vListLoading);


        List<Repuesto> items = new ArrayList<>(result.repuestos);

        mRepuestosAdapter = new RepuestosAdapter(getActivity(), items);
        mRepuestosAdapter.setFilterListener(new FiltrableAdapter.FilterListener() {
            @Override
            public void onFilter() {
                showEmptyMsg(R.string.repuestos_search_empty);
            }

            @Override
            public void onNonResults() {
                showEmptyMsg(R.string.repuestos_search_empty_filter);
            }
        });
        vListRepuestos.setAdapter(mRepuestosAdapter);
        vListRepuestos.setOnItemClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        BilpaApp app = BilpaApp.getInstance();
        app.cancelPendingRequests(ApiService.GET_TIPOS_REPUESTOS);
        app.cancelPendingRequests(ApiService.GET_REPUESTOS);
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
    public void onStart() {
        super.onStart();

        // safety check
        if (getDialog() == null) {
            return;
        }

        int dialogWidth = getResources().getDimensionPixelSize(R.dimen.dialogs_default_width);
        int dialogHeight = getResources().getDimensionPixelSize(R.dimen.dialogs_default_height);

        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);

    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {

        switch (menuItem.getItemId()) {

            case R.id.vMenuSearch:

                return true;
            default:
                return false;
        }


    }

    private void configSearch(Menu menu) {
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        mSearchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.vMenuSearch));
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        mSearchView.setIconifiedByDefault(false);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (vListRepuestos != null) {
                    RepuestosAdapter adapter = (RepuestosAdapter) vListRepuestos.getAdapter();
                    if (adapter != null && adapter.getFilter() != null) {
                        Filter filter = adapter.getFilter();
                        filter.filter(s);
                    }
                }
                return true;
            }
        });

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (mOnRepuestoSelectedListener != null) {
            Repuesto item = mRepuestosAdapter.getItem(position);
            mOnRepuestoSelectedListener.onRepuestoSelected(item);
        }
        dismiss();
    }

}
