package com.bilpa.android.fragments;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.bilpa.android.BilpaApp;
import com.bilpa.android.R;
import com.bilpa.android.adapters.ActivosAdapter;
import com.bilpa.android.adapters.FallasAdapter;
import com.bilpa.android.adapters.FiltrableAdapter;
import com.bilpa.android.adapters.TipoFallaSpinnerAdapter;
import com.bilpa.android.model.Activo;
import com.bilpa.android.model.Falla;
import com.bilpa.android.model.TipoFalla;
import com.bilpa.android.services.ApiService;
import com.bilpa.android.services.AsyncCallback;
import com.bilpa.android.services.actions.FallasResult;
import com.bilpa.android.services.actions.TiposFallaResult;
import com.mautibla.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

public class ActivoSelectDialogFragment extends ToolbarDialogFragment implements AdapterView.OnItemClickListener{

    private ListView vList;
    private ProgressBar vListLoading;
    private TextView vEmptyView;
    private ActivosAdapter mDataAdapter;

    private List<Activo> mActivos;

    private OnSelectActivoListener onSelectActivoListener;

    public interface OnSelectActivoListener {
        public void onSelectActivo(Activo activo);
    }

    public void setOnSelectActivoListener(OnSelectActivoListener onSelectActivoListener) {
        this.onSelectActivoListener = onSelectActivoListener;
    }

    @Override
    protected Toolbar getToolbar() {
        return (Toolbar) getView().findViewById(R.id.vToolbar);
    }

    @Override
    protected String getToolbarTitle() {
        return getString(R.string.visita_activos_select_activo);
    }

    @Override
    protected int getToolbarNavigationIcon() {
        return R.drawable.ic_arrow_back_white_24dp;
    }

    @Override
    protected int getToolbarMenu() {
        return 0;
    }

    @Override
    protected void onBack() {
        dismiss();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        return false;
    }


    public static ActivoSelectDialogFragment newInstance(List<Activo> activos) {
        ActivoSelectDialogFragment f = new ActivoSelectDialogFragment();
        f.mActivos = activos;
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_activos_dialog, container, false);
        vList = (ListView) v.findViewById(R.id.vList);
        vListLoading = (ProgressBar) v.findViewById(R.id.vListLoading);
        vEmptyView = (TextView) v.findViewById(R.id.vEmptyView);
        return v;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        vEmptyView.setText("No hay activos disponibles");
        vList.setEmptyView(vEmptyView);
        ViewUtils.gone(vList, vEmptyView);
        ViewUtils.visible(vListLoading);

        bindData(BilpaApp.getInstance().mFallas);

    }

    private void bindData(FallasResult result) {

        ViewUtils.visible(vList, vEmptyView);
        ViewUtils.gone(vListLoading);

        mDataAdapter = new ActivosAdapter(getActivity(), mActivos);
        vList.setAdapter(mDataAdapter);
        vList.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Activo activo = mDataAdapter.getItem(position);
        if (onSelectActivoListener != null) {
            onSelectActivoListener.onSelectActivo(activo);
        }
        dismiss();
    }



}
