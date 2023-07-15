package com.bilpa.android.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bilpa.android.BilpaApp;
import com.bilpa.android.R;
import com.bilpa.android.adapters.BaseAdapter;
import com.bilpa.android.model.correctivos.CActivo;
import com.bilpa.android.services.actions.FallasResult;
import com.mautibla.utils.ViewUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CActivoSelectDialogFragment extends ToolbarDialogFragment implements AdapterView.OnItemClickListener{

    private ListView vList;
    private ProgressBar vListLoading;
    private TextView vEmptyView;
    private CActiviAdapter mDataAdapter;

    private List<CActivo> mActivos;

    private OnSelectActivoListener onSelectActivoListener;

    public interface OnSelectActivoListener {
        public void onSelectActivo(CActivo activo);
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


    public static CActivoSelectDialogFragment newInstance(List<CActivo> activos) {
        CActivoSelectDialogFragment f = new CActivoSelectDialogFragment();
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

        mDataAdapter = new CActiviAdapter(getActivity(), mActivos);
        vList.setAdapter(mDataAdapter);
        vList.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CActivo activo = mDataAdapter.getItem(position);
        if (onSelectActivoListener != null) {
            onSelectActivoListener.onSelectActivo(activo);
        }
        dismiss();
    }


    private class CActiviAdapter extends BaseAdapter<CActivo> {

        public CActiviAdapter(Context context, List<CActivo> items) {
            super(context, 0, items);
        }

        public Date mToday = Calendar.getInstance().getTime();

        @Override
        protected View newView(int position, ViewGroup parent, CActivo item) {
            View v = mInflater.inflate(R.layout.row_activos_list_2, parent, false);
            ViewHolder holder = new ViewHolder();
            holder.vActivoName = (TextView) v.findViewById(R.id.vActivoName);
            holder.vActivoType = (TextView) v.findViewById(R.id.vActivoType);
            holder.vActivoQR = (TextView) v.findViewById(R.id.vActivoQR);
            holder.vIcoPendiente = (ImageView) v.findViewById(R.id.vIcoPendiente);
            v.setTag(holder);
            return v;
        }

        @Override
        protected void bindView(View v, CActivo item, int position) {
            ViewHolder h = (ViewHolder) v.getTag();

            if (position % 2 == 0) {
                v.setBackgroundResource(R.drawable.row_even_selector);
            } else {
                v.setBackgroundResource(R.drawable.row_odd_selector);
            }

            h.vActivoName.setText(item.display);
            h.vActivoType.setText(item.getTipoLabel());

            if (item.qr != null && item.qr.codigo != null) {
                h.vActivoQR.setText(String.format(getContext().getString(R.string.visita_activos_code), item.qr.codigo));
            } else {
                h.vActivoQR.setText(R.string.visita_activos_code_empty);
            }
        }

        private class ViewHolder {
            public TextView vActivoName;
            public TextView vActivoType;
            public TextView vActivoQR;
            public ImageView vIcoPendiente;
        }
    }



}
