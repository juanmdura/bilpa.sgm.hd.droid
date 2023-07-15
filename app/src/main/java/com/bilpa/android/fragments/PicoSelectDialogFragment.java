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
import android.widget.TextView;

import com.bilpa.android.BilpaApp;
import com.bilpa.android.R;
import com.bilpa.android.adapters.ActivosAdapter;
import com.bilpa.android.adapters.PicosAdapter;
import com.bilpa.android.model.Activo;
import com.bilpa.android.model.Pico;
import com.bilpa.android.model.Repuesto;
import com.bilpa.android.services.ApiService;
import com.bilpa.android.services.AsyncCallback;
import com.bilpa.android.services.actions.FallasResult;
import com.bilpa.android.services.actions.PicosResult;
import com.mautibla.utils.ViewUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PicoSelectDialogFragment extends ToolbarDialogFragment implements AdapterView.OnItemClickListener{

    private ListView vList;
    private ProgressBar vListLoading;
    private TextView vEmptyView;
    private PicosAdapter mDataAdapter;

    private PicosResult mPicosResult;
    private long mIdSurtidor;

    public OnPicoSelectedListener onPicoSelectedListener;

    public interface OnPicoSelectedListener {
        public void onPicoSelected(Pico pico);
    }

    public void setOnPicoSelectedListener(OnPicoSelectedListener onPicoSelectedListener) {
        this.onPicoSelectedListener = onPicoSelectedListener;
    }

    @Override
    protected Toolbar getToolbar() {
        return (Toolbar) getView().findViewById(R.id.vToolbar);
    }

    @Override
    protected String getToolbarTitle() {
        return getString(R.string.corregidos_detail_manguera_hint);
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


    public static PicoSelectDialogFragment newInstance(long idSurtidor) {
        PicoSelectDialogFragment f = new PicoSelectDialogFragment();
        f.mIdSurtidor = idSurtidor;
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_picos_dialog, container, false);
        vList = (ListView) v.findViewById(R.id.vList);
        vListLoading = (ProgressBar) v.findViewById(R.id.vListLoading);
        vEmptyView = (TextView) v.findViewById(R.id.vEmptyView);
        return v;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        vEmptyView.setText(R.string.corregidos_detail_mangueras_empty);
        vList.setEmptyView(vEmptyView);
        ViewUtils.gone(vList, vEmptyView);
        ViewUtils.visible(vListLoading);


        ViewUtils.visible(vListLoading);
        ApiService.getPicos(mIdSurtidor, new AsyncCallback<PicosResult>(getActivity()) {
            @Override
            protected void onSuccess(PicosResult result) {
                mPicosResult = result;
                Collections.sort(result.picos, new Comparator<Pico>() {
                    @Override
                    public int compare(Pico lhs, Pico rhs) {
                        if (lhs.numeroPico < rhs.numeroPico) {
                            return -1;
                        } else if (lhs.numeroPico > rhs.numeroPico) {
                            return 1;
                        } else {
                            return 0;
                        }
                    }
                });

                bindData(result);
            }
        });

    }

    private void bindData(PicosResult result) {

        ViewUtils.visible(vList, vEmptyView);
        ViewUtils.gone(vListLoading);

        mDataAdapter = new PicosAdapter(getActivity(), result.picos);
        vList.setAdapter(mDataAdapter);
        vList.setOnItemClickListener(this);

        invalidateSize();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Pico pico = mDataAdapter.getItem(position);
        if (onPicoSelectedListener != null) {
            onPicoSelectedListener.onPicoSelected(pico);
        }
        dismiss();
    }

    protected int getDialogHeight() {
        return getResources().getDimensionPixelSize(R.dimen.dialogs_medium_height);
    }

    protected int getDialogWidth() {
        return getResources().getDimensionPixelSize(R.dimen.dialogs_medium_width);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        BilpaApp app = BilpaApp.getInstance();
        app.cancelPendingRequests(ApiService.GET_PICOS);
    }


}
