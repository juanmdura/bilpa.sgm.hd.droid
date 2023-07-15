package com.bilpa.android.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bilpa.android.BilpaApp;
import com.bilpa.android.R;
import com.bilpa.android.adapters.CorregidosAdapter;
import com.bilpa.android.model.Corregido;
import com.bilpa.android.services.ApiService;
import com.bilpa.android.services.AsyncCallback;
import com.bilpa.android.services.actions.BaseResult;
import com.bilpa.android.services.actions.CorregidosResult;
import com.bilpa.android.widgets.OnAddEditCorregidos;
import com.bilpa.android.widgets.OnAddEditPendientes;
import com.bilpa.android.widgets.ShowHideAddButton;
import com.mautibla.utils.ViewUtils;

import java.util.List;

public class CorregidosListFragment extends Fragment implements
        RootFragment, View.OnClickListener, AdapterView.OnItemClickListener {

    private ListView vListCorregidos;
    private ProgressBar vProgress;
    private TextView vEmptyView;
    private CorregidosAdapter adapter;

    public CorregidosResult mCorregidosResult;
    private Toolbar vSecondayToolbar;

    public static CorregidosListFragment newInstance() {
        CorregidosListFragment fragment = new CorregidosListFragment();
        return fragment;
    }

    public CorregidosListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_corregidos_list, container, false);

        vSecondayToolbar = (Toolbar) v.findViewById(R.id.vSecondayToolbar);
        vListCorregidos = (ListView) v.findViewById(R.id.vListCorregidos);
        vProgress = (ProgressBar) v.findViewById(R.id.vProgress);
        vEmptyView = (TextView) v.findViewById(R.id.vEmptyView);

        vListCorregidos.setEmptyView(vEmptyView);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Toolbar secundario
        vSecondayToolbar.getMenu().clear();
        vSecondayToolbar.setTitle(R.string.corregidos_list_title);

        if (savedInstanceState != null && savedInstanceState.containsKey("mCorregidosResult")) {
            mCorregidosResult = (CorregidosResult) savedInstanceState.get("mCorregidosResult");
        }

        ((ShowHideAddButton) getActivity()).showAddBtn();

        ViewUtils.gone(vEmptyView, vProgress, vListCorregidos);


        if (mCorregidosResult == null) {

            ViewUtils.visible(vProgress);
            Long idPreventivo = ((OnAddEditPendientes) getActivity()).getIdPreventivo();

            ApiService.getCorregidos(idPreventivo, new AsyncCallback<CorregidosResult>(getActivity()) { // TODO santilod .. cambiar servicio para obtener reparaciones por preventivo/activo
                @Override
                protected void onSuccess(CorregidosResult result) {
                    mCorregidosResult = result;
                    bindCorregidos(result);
                }

                @Override
                protected void onServiceOperationFailOK() {
                    super.onServiceOperationFailOK();
                    getActivity().onBackPressed();
                }
            });
        } else {
            bindCorregidos(mCorregidosResult);
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("mCorregidosResult", mCorregidosResult);
    }


    private void bindCorregidos(CorregidosResult result) {
        ViewUtils.visible(vListCorregidos, vEmptyView);
        ViewUtils.gone(vProgress);
        List<Corregido> corregidos = result.corregidos;
        adapter = new CorregidosAdapter(getActivity(), corregidos);
        adapter.setOnDeleteListener(this);
        vListCorregidos.setAdapter(adapter);
        vListCorregidos.setOnItemClickListener(this);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        BilpaApp app = BilpaApp.getInstance();
        app.cancelPendingRequests(ApiService.GET_CORREGIDOS);
        app.cancelPendingRequests(ApiService.DELETE_CORREGIDO);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.vDelete:
                final Corregido corregido = (Corregido) v.getTag();
                new MaterialDialog.Builder(getActivity())
                        .title(com.bilpa.android.R.string.app_name)
                        .iconRes(R.drawable.ic_launcher)
                        .content(String.format(getString(R.string.corregidos_list_delete_alert), corregido.falla.descripcion))
                        .positiveText(R.string.btn_aceptar)
                        .negativeText(R.string.btn_cancelar)
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                doDeleteCorregido(corregido);
                            }
                        })
                        .show();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CorregidosAdapter adapter = (CorregidosAdapter) parent.getAdapter();
        Corregido corregido = adapter.getItem(position);
        ((OnAddEditCorregidos) getActivity()).goToEditCorregido(corregido);
    }



    private void doDeleteCorregido(final Corregido corregido) {
        ApiService.deleteCorredigo(corregido.id, new AsyncCallback<BaseResult>(getActivity()) {
            @Override
            protected void onSuccess(BaseResult result) {
                mCorregidosResult.corregidos.remove(corregido);
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void invalidateCorregidos() {
        mCorregidosResult = null;
    }

}
