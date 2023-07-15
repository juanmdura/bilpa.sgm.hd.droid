package com.bilpa.android.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bilpa.android.BilpaApp;
import com.bilpa.android.CorrectivoActivoActivity;
import com.bilpa.android.R;
import com.bilpa.android.SolucionDetailActivity;
import com.bilpa.android.adapters.SolucionesAdapter;
import com.bilpa.android.model.Solucion;
import com.bilpa.android.model.correctivos.CActivo;
import com.bilpa.android.model.correctivos.Correctivo;
import com.bilpa.android.services.ApiCorrectivos;
import com.bilpa.android.services.ApiService;
import com.bilpa.android.services.AsyncCallback;
import com.bilpa.android.services.actions.BaseResult;
import com.bilpa.android.services.correctivos.GetSolucionesResult;
import com.bilpa.android.utils.Consts;
import com.mautibla.utils.ViewUtils;

import java.util.List;

public class SolucionesFragment extends Fragment implements
        RootFragment, View.OnClickListener, AdapterView.OnItemClickListener {

    private ListView vList;
    private ProgressBar vProgress;
    private TextView vEmptyView;
    private SolucionesAdapter adapter;

    public Correctivo mCorrectivo;
    public CActivo mActivo;

    public GetSolucionesResult mSolucionesResult;

    public static SolucionesFragment newInstance(Correctivo correctivo, CActivo activo) {
        SolucionesFragment fragment = new SolucionesFragment();
        fragment.mCorrectivo = correctivo;
        fragment.mActivo = activo;
        return fragment;
    }

    public SolucionesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_soluciones_list, container, false);

        vList = (ListView) v.findViewById(R.id.vList);
        vProgress = (ProgressBar) v.findViewById(R.id.vProgress);
        vEmptyView = (TextView) v.findViewById(R.id.vEmptyView);

        vList.setEmptyView(vEmptyView);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.containsKey("mSolucionesResult")) {
            mSolucionesResult = (GetSolucionesResult) savedInstanceState.get("mSolucionesResult");
            mCorrectivo = (Correctivo) savedInstanceState.getSerializable("mCorrectivo");
            mActivo = (CActivo) savedInstanceState.getSerializable("mActivo");
        }

        load();
    }

    private void load() {
        ViewUtils.gone(vEmptyView, vProgress);


        if (mSolucionesResult == null) {

            ViewUtils.visible(vProgress);

            ApiCorrectivos.getSoluciones(mCorrectivo.id, mActivo.id, new AsyncCallback<GetSolucionesResult>(getActivity()) {
                @Override
                protected void onSuccess(GetSolucionesResult result) {
                    mSolucionesResult = result;
                    bindSoluciones(result);
                }

                @Override
                protected void onServiceOperationFailOK() {
                    super.onServiceOperationFailOK();
                    getActivity().onBackPressed();
                }
            });


        } else {
            bindSoluciones(mSolucionesResult);
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("mSolucionesResult", mSolucionesResult);
        outState.putSerializable("mCorrectivo", mCorrectivo);
        outState.putSerializable("mActivo", mActivo);
    }


    private void bindSoluciones(GetSolucionesResult result) {
        ViewUtils.visible(vList, vEmptyView);
        ViewUtils.gone(vProgress);
        List<Solucion> data = result.data;
        adapter = new SolucionesAdapter(getActivity(), data);
        adapter.setOnDeleteListener(this);
        vList.setAdapter(adapter);
        vList.setOnItemClickListener(this);
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
                final Solucion data = (Solucion) v.getTag();
                new MaterialDialog.Builder(getActivity())
                        .title(R.string.app_name)
                        .iconRes(R.drawable.ic_launcher)
                        .content(String.format(getString(R.string.corregidos_list_delete_alert), data.falla.descripcion))
                        .positiveText(R.string.btn_aceptar)
                        .negativeText(R.string.btn_cancelar)
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                ApiCorrectivos.deleteSolucion(data.id, new AsyncCallback<BaseResult>(getActivity()) {
                                    @Override
                                    protected void onSuccess(BaseResult result) {
                                        updateCorregidos();
                                        if (mActivo.tienePendientes) {
                                            ((CorrectivoActivoActivity) getActivity()).updatePendientes();
                                        }
                                    }
                                });
                            }
                        })
                        .show();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SolucionesAdapter adapter = (SolucionesAdapter) parent.getAdapter();
        Solucion solucion = adapter.getItem(position);
        Intent intent = new Intent(getActivity(), SolucionDetailActivity.class);
        intent.putExtra("correctivo", mCorrectivo);
        intent.putExtra("activo", mActivo);
        intent.putExtra("solucion", solucion);
        startActivityForResult(intent, Consts.Request.CORRECTIVOS_SOLUCION);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Se creo o se edito una solucion. Se recarga el listado de soluciones.
        if (requestCode == Consts.Request.CORRECTIVOS_SOLUCION && resultCode == Activity.RESULT_OK) {
            updateCorregidos();
            ((CorrectivoActivoActivity) getActivity()).mUpdateCorredigoOnBack = true;
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    public void invalidateSoluciones() {
        mSolucionesResult = null;
    }

    public void updateCorregidos() {
        ((CorrectivoActivoActivity) getActivity()).mUpdateCorredigoOnBack = true;
        invalidateSoluciones();
        load();
    }
}
