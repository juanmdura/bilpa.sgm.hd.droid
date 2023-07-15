package com.bilpa.android.fragments;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.bilpa.android.BilpaApp;
import com.bilpa.android.ProductosActivity;
import com.bilpa.android.R;
import com.bilpa.android.model.Chequeo;
import com.bilpa.android.model.ChequeoProducto;
import com.bilpa.android.model.Producto;
import com.bilpa.android.services.ApiService;
import com.bilpa.android.services.AsyncCallback;
import com.bilpa.android.services.actions.BaseResult;
import com.bilpa.android.services.actions.pendientes.SaveChequeoProductoAction;
import com.bilpa.android.services.actions.products.GetChequeoProductoResult;
import com.bilpa.android.widgets.OnChequeoListener;
import com.bilpa.android.widgets.RadioCheckboxWidget;
import com.mautibla.utils.ToastUtils;
import com.mautibla.utils.ViewUtils;

public class ProductDetailFragment extends SaveFragment implements CompoundButton.OnCheckedChangeListener {


    private Producto mProducto;
    private Toolbar vToolbar;
    private LinearLayout vData;
    private ProgressBar vProgress;
    private RadioCheckboxWidget vMonitorBombeo;
    private RadioCheckboxWidget vEliminadorAireGases;
    private RadioCheckboxWidget vCorreasPoleas;
    private RadioCheckboxWidget vOtros;
    public int mIndex;

    private OnChequeoListener mOnChequeoListener;

    public void setOnChequeoListener(OnChequeoListener onChequeoListener) {
        this.mOnChequeoListener = onChequeoListener;
    }

    public static ProductDetailFragment newInstance(Producto producto, int index) {
        ProductDetailFragment fragment = new ProductDetailFragment();
        fragment.mProducto = producto;
        fragment.mIndex = index;
        return fragment;
    }

    public ProductDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_producto_detail, container, false);

        vToolbar = (Toolbar) v.findViewById(R.id.toolbar);
        vData = (LinearLayout) v.findViewById(R.id.vData);
        vProgress = (ProgressBar) v.findViewById(R.id.vProgress);

        vMonitorBombeo = (RadioCheckboxWidget) v.findViewById(R.id.vMonitorBombeo);
        vEliminadorAireGases = (RadioCheckboxWidget) v.findViewById(R.id.vEliminadorAireGases);
        vCorreasPoleas = (RadioCheckboxWidget) v.findViewById(R.id.vCorreasPoleas);
        vOtros = (RadioCheckboxWidget) v.findViewById(R.id.vOtros);

        // Hide panels
        ViewUtils.gone(vData, vProgress);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        vToolbar.getMenu().clear();
        vToolbar.setTitle(mProducto.nombre);
        vToolbar.inflateMenu(R.menu.producto_detail);
        vToolbar.setOnMenuItemClickListener(this);

    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (((ProductosActivity) getActivity()).mChequeoResult == null)  {
            load();
        } else {
            bind(((ProductosActivity) getActivity()).mChequeoResult);
        }

    }

    private void load() {
        ViewUtils.visible(vProgress);

        Long idPreventivo = ((ProductosActivity) getActivity()).getIdPreventivo();
        ApiService.getChequeoProducto(mProducto.id, idPreventivo, new AsyncCallback<GetChequeoProductoResult>(getActivity()) {
            @Override
            protected void onSuccess(GetChequeoProductoResult result) {
                ((ProductosActivity) getActivity()).mChequeoResult = result;
                bind(result);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        BilpaApp app = BilpaApp.getInstance();
        app.cancelPendingRequests(ApiService.GET_CHEQUEO);
        app.cancelPendingRequests(ApiService.SAVE_CHEQUEO);
    }

    private void bind(GetChequeoProductoResult result) {
        // show data panel
        ViewUtils.gone(vProgress);
        ViewUtils.visible(vData);

        if (result.data.chequeo == null) {
            result.data.chequeo = new ChequeoProducto();
        }

        ChequeoProducto chequeo = result.data.chequeo;
        bindCheck(vMonitorBombeo, chequeo.getMotorUnidadBombeo());
        bindCheck(vEliminadorAireGases, chequeo.getEliminadorAireGas());
        bindCheck(vCorreasPoleas, chequeo.getCorreasPoleas());
        bindCheck(vOtros, chequeo.getOtros());
    }

    public void bindCheck(RadioCheckboxWidget chequeoWidget, Chequeo chequeo) {
        chequeoWidget.bindCheckeo(chequeo, this, mCommentsClickListener);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    protected String getUnsavedExitMsg() {
        return getString(R.string.productos_chequeo_exit_unsaved_msg);
    }

    @Override
    protected int[] assertMenuItemSavedCheck() {
        return new int[] { R.id.vCancel };
    }

    @Override
    protected int[] assertViewSavedCheck() {
        return null;
    }

    @Override
    protected void onActionClick(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.vMenuReparaciones:
                // TODO santilod. Ver si va aca o en otro lado
                ((ProductosActivity) getActivity()).goToCorregidos();
                break;

            case R.id.vMenuPendientes:
                // TODO santilod. Ver si va aca o en otro lado
                ((ProductosActivity) getActivity()).goToPendientes();
                break;

            case R.id.vCancel:
                getActivity().onBackPressed();
                break;

            case R.id.vSave:
                saveProducto();
                break;
        }
    }

    private void saveProducto() {

        ChequeoProducto cProducto = ((ProductosActivity) getActivity()).mChequeoResult.data.chequeo;

        SaveChequeoProductoAction.Builder b = new SaveChequeoProductoAction.Builder();
        b.idPreventivo = ((ProductosActivity) getActivity()).getIdPreventivo();
        b.idProducto = mProducto.id;
        b.chequeo(cProducto.getLblMotorUnidadBombeo(), vMonitorBombeo.getSelectedLabel(), vMonitorBombeo.isChecked());
        b.chequeo(cProducto.getLblEliminadorAireGas(), vEliminadorAireGases.getSelectedLabel(), vEliminadorAireGases.isChecked());
        b.chequeo(cProducto.getLblCorreasPoleas(), vCorreasPoleas.getSelectedLabel(), vCorreasPoleas.isChecked());
        b.chequeo(cProducto.getLblOtros(), vOtros.getSelectedLabel(), vOtros.isChecked());

        ApiService.saveChequeoProducto(b, new AsyncCallback<BaseResult>(getActivity()) {
            @Override
            protected void onSuccess(BaseResult result) {
                setUnSavedData(false);
                ToastUtils.showToast(getActivity(), R.string.productos_chequeo_saved_successful);
                if (mOnChequeoListener != null) {
                    mOnChequeoListener.onSaveChequeo();
                }
            }
        });

    }

    @Override
    protected void onActionClick(View view) {
        switch (view.getId()) {
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        setUnSavedData(true);
    }

    private View.OnClickListener mCommentsClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RadioCheckboxWidget parent = (RadioCheckboxWidget) v.getParent().getParent().getParent();
            Long chequeoId = (Long) v.getTag();
            ((ProductosActivity) getActivity()).goToComments(chequeoId, parent.getLabel());
        }
    };

}
