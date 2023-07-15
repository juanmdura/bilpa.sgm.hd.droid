package com.bilpa.android.fragments;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.bilpa.android.adapters.TareasAdapter;
import com.bilpa.android.adapters.TipoTareaSpinnerAdapter;
import com.bilpa.android.model.Tarea;
import com.bilpa.android.model.TipoTarea;
import com.bilpa.android.services.ApiService;
import com.bilpa.android.services.AsyncCallback;
import com.bilpa.android.services.actions.TareasResult;
import com.bilpa.android.services.actions.TiposTareaResult;
import com.mautibla.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

public class TareasDialogFragment extends DialogFragment implements Toolbar.OnMenuItemClickListener, AdapterView.OnItemClickListener {

    private ListView vList;
    private Toolbar vToolbar;
    private Spinner vFilter;
    private ProgressBar vListLoading;
    private TextView vEmptyView;
    private SearchView mSearchView;
    private TareasAdapter mDataAdapter;

    private OnTareaSelectedListener mOnTareaSelectedListener;

    public interface OnTareaSelectedListener {
        public void onTareaSelected(Tarea tarea);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        int style = DialogFragment.STYLE_NORMAL;
//        int theme = R.style.BilpaTheme_Dialog;
//        setStyle(style, theme);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        BilpaApp app = BilpaApp.getInstance();
        app.cancelPendingRequests(ApiService.GET_TIPOS_TAREAS);
        app.cancelPendingRequests(ApiService.GET_TAREAS);
    }


    public void setOnTareaSelectedListener(OnTareaSelectedListener onTareaSelectedListener) {
        this.mOnTareaSelectedListener = onTareaSelectedListener;
    }


    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tareas_dialog, container, false);

        vToolbar = (Toolbar) v.findViewById(R.id.vToolbar);
        vFilter = (Spinner) v.findViewById(R.id.vFilter);
        vList = (ListView) v.findViewById(R.id.vList);
        vListLoading = (ProgressBar) v.findViewById(R.id.vListLoading);
        vEmptyView = (TextView) v.findViewById(R.id.vEmptyView);

        return v;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        vToolbar.setTitle(getString(R.string.tareas_search_title));
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

        vList.setEmptyView(vEmptyView);


        ViewUtils.gone(vList, vEmptyView);
        ViewUtils.visible(vListLoading);


        if (BilpaApp.getInstance().mTiposTareas == null) {


            ApiService.getTipoTareas(ApiService.GET_TIPOS_TAREAS, new AsyncCallback<TiposTareaResult>(getActivity()) {
                @Override
                protected void onSuccess(TiposTareaResult result) {
                    BilpaApp.getInstance().mTiposTareas = result;
                    bindTipos(result);
                }
            });
        } else {
            bindTipos(BilpaApp.getInstance().mTiposTareas);
        }


        if (BilpaApp.getInstance().mTareas == null) {
            ApiService.getTareas(ApiService.GET_TAREAS, new AsyncCallback<TareasResult>(getActivity()) {
                @Override
                protected void onSuccess(TareasResult result) {
                    BilpaApp.getInstance().mTareas = result;
                    bindData(result);
                }
            });
        } else {
            bindData(BilpaApp.getInstance().mTareas);
        }

    }

    private void bindTipos(TiposTareaResult result) {
        if (result.tipoTareas.size() == 0) {
            ViewUtils.gone(vFilter);
        } else {
            ViewUtils.visible(vFilter);
            List<TipoTarea> filterItems= result.tipoTareas;
            TipoTareaSpinnerAdapter adapter = new TipoTareaSpinnerAdapter(getActivity(), filterItems);
            vFilter.setAdapter(adapter);
            vFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    TipoTarea tipoTarea = null;
                    if (position == 0) {
                        tipoTarea = null;
                    } else {
                        tipoTarea = ((TipoTareaSpinnerAdapter)parent.getAdapter()).getItem(position);
                    }
                    if (mDataAdapter != null) {
                        mDataAdapter.selected = tipoTarea;
                        mDataAdapter.notifyDataSetChanged();
                        mDataAdapter.getFilter().filter(mSearchView.getQuery());
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    private void bindData(TareasResult result) {

        ViewUtils.visible(vList, vEmptyView);
        ViewUtils.gone(vListLoading);


        List<Tarea> items = new ArrayList<Tarea>(result.tareas);

        mDataAdapter = new TareasAdapter(getActivity(), items);
        mDataAdapter.setFilterListener(new FiltrableAdapter.FilterListener() {
            @Override
            public void onFilter() {
                // Se setea el msg por defecto. No se muestra.
                showEmptyMsg(R.string.tareas_search_empty);
            }

            @Override
            public void onNonResults() {
                showEmptyMsg(R.string.tareas_search_empty_filter);
            }
        });
        vList.setAdapter(mDataAdapter);
        vList.setOnItemClickListener(this);
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

        // ... other stuff you want to do in your onStart() method
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
                //((RepuestosAdapter) vList.getAdapter()).getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (vList != null) {
                    TareasAdapter adapter = (TareasAdapter) vList.getAdapter();
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
        if (mOnTareaSelectedListener != null) {
            Tarea item = mDataAdapter.getItem(position);
            mOnTareaSelectedListener.onTareaSelected(item);
        }
        dismiss();
    }


}
