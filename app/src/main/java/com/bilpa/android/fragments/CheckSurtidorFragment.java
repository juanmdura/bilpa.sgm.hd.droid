package com.bilpa.android.fragments;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bilpa.android.ActivoActivity;
import com.bilpa.android.BilpaApp;
import com.bilpa.android.R;
import com.bilpa.android.model.Activo;
import com.bilpa.android.model.Chequeo;
import com.bilpa.android.model.ChequeoSurtidor;
import com.bilpa.android.model.Visita;
import com.bilpa.android.services.ApiService;
import com.bilpa.android.services.AsyncCallback;
import com.bilpa.android.services.actions.BaseResult;
import com.bilpa.android.services.actions.GetChequeoSurtidorResult;
import com.bilpa.android.services.actions.SaveChequeoSurtidorAction;
import com.bilpa.android.widgets.OnChequeoListener;
import com.bilpa.android.widgets.RadioCheckboxWidget;
import com.mautibla.utils.ToastUtils;
import com.mautibla.utils.ViewUtils;

public class CheckSurtidorFragment extends SaveFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {


    private Toolbar toolbar;
    private Button vBtnMangueras;
    private Button vBtnProductos;
    private LinearLayout vData;
    private ProgressBar vProgress;
    private RadioCheckboxWidget vCabezalLimpiezaSellado;
    private RadioCheckboxWidget vPruebaDisplay;
    private RadioCheckboxWidget vVisualLimpieza;
    private RadioCheckboxWidget vPresetCara1;
    private RadioCheckboxWidget vPresetCara2;
    private RadioCheckboxWidget vParteElectrica;
    private RadioCheckboxWidget vPlanSellado;
    private RadioCheckboxWidget vOtros;

    private OnChequeoListener mOnChequeoListener;


    public static CheckSurtidorFragment newInstance() {
        CheckSurtidorFragment fragment = new CheckSurtidorFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    public CheckSurtidorFragment() {
        // Required empty public constructor
    }


    public void setOnChequeoListener(OnChequeoListener onChequeoListener) {
        this.mOnChequeoListener = onChequeoListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_check_surtidor, container, false);

        vData = (LinearLayout) v.findViewById(R.id.vData);
        vProgress = (ProgressBar) v.findViewById(R.id.vProgress);
        toolbar = (Toolbar) v.findViewById(R.id.toolbar);


        vCabezalLimpiezaSellado = (RadioCheckboxWidget) v.findViewById(R.id.vCabezalLimpiezaSellado);
        vPruebaDisplay = (RadioCheckboxWidget) v.findViewById(R.id.vPruebaDisplay);
        vVisualLimpieza = (RadioCheckboxWidget) v.findViewById(R.id.vVisualLimpieza);
        vPresetCara1 = (RadioCheckboxWidget) v.findViewById(R.id.vPresetCara1);
        vPresetCara2 = (RadioCheckboxWidget) v.findViewById(R.id.vPresetCara2);
        vParteElectrica = (RadioCheckboxWidget) v.findViewById(R.id.vParteElectrica);
        vPlanSellado = (RadioCheckboxWidget) v.findViewById(R.id.vPlanSellado);
        vOtros = (RadioCheckboxWidget) v.findViewById(R.id.vOtros);

        vBtnMangueras = (Button) v.findViewById(R.id.vBtnMangueras);
        vBtnProductos = (Button) v.findViewById(R.id.vBtnProductos);

        vBtnMangueras.setOnClickListener(this);
        vBtnProductos.setOnClickListener(this);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ActivoActivity activity = (ActivoActivity) getActivity();
        Activo mActivo = activity.mActivo;
        Visita mVisita = activity.mVisita;

        // Hide add btn
        activity.hideAddBtn();

        // Hide panels
        ViewUtils.gone(vData, vProgress);


        // Set secundary title

        toolbar.setTitle(mActivo.descripcion);

        // TODO
        toolbar.setTitle(getString(R.string.activo_tab_chequeo) + " | " + mActivo.descripcion);



        if (mActivo.codigoQR != 0) {
            toolbar.setSubtitle(String.format(getString(R.string.visita_activos_code), String.valueOf(mActivo.codigoQR)));
        }

        // menu para las bombas.
        toolbar.inflateMenu(R.menu.surtidor_check);

        // menu callback
        toolbar.setOnMenuItemClickListener(this);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        load();
    }

    private void load() {

        ActivoActivity activity = (ActivoActivity) getActivity();
        Activo mActivo = activity.mActivo;
        Visita mVisita = activity.mVisita;

        if (getActivoActivity().mChequeoSurtidorResult != null) {
            bind(getActivoActivity().mChequeoSurtidorResult);
        } else {
            ViewUtils.visible(vProgress);


            ApiService.getChequeoSurtidor(mVisita.id, mActivo.id, new AsyncCallback<GetChequeoSurtidorResult>(getActivity()) {
                @Override
                protected void onSuccess(GetChequeoSurtidorResult result) {
                    getActivoActivity().mChequeoSurtidorResult = result;
                    bind(result);
                }

                @Override
                public void onError(String msg, MaterialDialog.ButtonCallback callback) {
                    ViewUtils.gone(vProgress);
                    super.onError(msg, callback);
                }

                @Override
                protected void onServiceOperationFailOK() {
                    super.onServiceOperationFailOK();
                    getActivity().onBackPressed();
                }
            });
        }
    }

    private void bind(GetChequeoSurtidorResult result) {

        // show data panel
        ViewUtils.gone(vProgress);
        ViewUtils.visible(vData);

        if (result.data.chequeo == null) {
            result.data.chequeo = new ChequeoSurtidor();
        }

        ChequeoSurtidor chequeo = result.data.chequeo;

        bindCheck(vCabezalLimpiezaSellado, chequeo != null ? chequeo.getCabezalLimpiezaSellado() : null);
        bindCheck(vPresetCara1, chequeo != null ? chequeo.getPresetCara1() : null);
        bindCheck(vPresetCara2, chequeo != null ? chequeo.getPresetCara2() : null);
        bindCheck(vPruebaDisplay, chequeo != null ? chequeo.getPruebaDisplayIluminacion() : null);
        bindCheck(vVisualLimpieza, chequeo != null ? chequeo.getVisualYLimpieza() : null);
        bindCheck(vParteElectrica, chequeo != null ? chequeo.getParteElectrica() : null);
        bindCheck(vPlanSellado, chequeo != null ? chequeo.getPlanSellado() : null);
        bindCheck(vOtros, chequeo != null ? chequeo.getOtros() : null);

    }

    public void bindCheck(RadioCheckboxWidget chequeoWidget, Chequeo chequeo) {
       chequeoWidget.bindCheckeo(chequeo, this, mCommentsClickListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        BilpaApp app = BilpaApp.getInstance();
        app.cancelPendingRequests(ApiService.GET_CHEQUEO);
        app.cancelPendingRequests(ApiService.TAG_SET_QR_ACTIVO);
        app.cancelPendingRequests(ApiService.SAVE_CHEQUEO);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public ActivoActivity getActivoActivity() {
        return (ActivoActivity) getActivity();
    }

    @Override
    protected String getUnsavedExitMsg() {
        return getString(R.string.surtidor_chequeo_exit_unsaved_msg);
    }

    @Override
    protected int[] assertMenuItemSavedCheck() {
        return new int[] {
                R.id.vMenuClose
        };
    }

    @Override
    protected int[] assertViewSavedCheck() {
        return new int[] {
                R.id.vBtnProductos,
                R.id.vBtnMangueras
        };
    }

    @Override
    protected void onActionClick(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.vMenuReparaciones:
                // TODO santilod. Ver si va aca o en otro lado
                ((ActivoActivity) getActivity()).goToCorregidos();
                break;

            case R.id.vMenuPendientes:
                // TODO santilod. Ver si va aca o en otro lado
                ((ActivoActivity) getActivity()).goToPendientes();
                break;

            case R.id.vMenuQR:
                ((ActivoActivity) getActivity()).showQR(new QRDialogFragment.QRListener() {
                    @Override
                    public void onQueryCode(final String code) {
                        final Activo mActivo = getActivoActivity().mActivo;
                        long activoId = mActivo.id;
                        ApiService.setQrToActivo(activoId, code, new AsyncCallback<BaseResult>(getActivity()) {
                            @Override
                            protected void onSuccess(BaseResult result) {
                                ToastUtils.showToast(getActivity(), String.format(getString(R.string.qr_assigned_to_activo), mActivo.descripcion));
                                ((ActivoActivity) getActivoActivity()).mActivo.codigoQR = Integer.valueOf(code);
                                ((ActivoActivity) getActivoActivity()).mUpdated = true;
                            }
                        });

                    }
                });
                break;

            case R.id.vMenuClose:
                getActivity().onBackPressed();
                break;

            case R.id.vMenuSave:
                if (validate()) {
                    save();
                }
                break;
        }
    }

    private boolean validate() {


        return true;
    }

    @Override
    protected void onActionClick(View view) {

        switch (view.getId()) {

            case R.id.vBtnMangueras:

                ((ActivoActivity) getActivity()).goToMangueras();
                break;

            case R.id.vBtnProductos:
                ((ActivoActivity) getActivity()).goToProductos();
                break;
        }

    }

    private void save() {

        GetChequeoSurtidorResult mChequeoSurtidorResult = getActivoActivity().mChequeoSurtidorResult;
        GetChequeoSurtidorResult.Data data = mChequeoSurtidorResult.data;

        SaveChequeoSurtidorAction.Builder b = new SaveChequeoSurtidorAction.Builder()
                .idPreventivo(data.idPreventivo)
                .chequeo(mChequeoSurtidorResult.data.chequeo.getLblCabezalLimpiezaSellado(), vCabezalLimpiezaSellado.getSelectedLabel(), vCabezalLimpiezaSellado.isChecked())
                .chequeo(mChequeoSurtidorResult.data.chequeo.getLblPresetCara1(), vPresetCara1.getSelectedLabel(), vPresetCara1.isChecked())
                .chequeo(mChequeoSurtidorResult.data.chequeo.getLblPresetCara2(), vPresetCara2.getSelectedLabel(), vPresetCara2.isChecked())
                .chequeo(mChequeoSurtidorResult.data.chequeo.getLblPruebaDisplayIluminacion(), vPruebaDisplay.getSelectedLabel(), vPruebaDisplay.isChecked())
                .chequeo(mChequeoSurtidorResult.data.chequeo.getLblVisualYLimpieza(), vVisualLimpieza.getSelectedLabel(), vVisualLimpieza.isChecked())
                .chequeo(mChequeoSurtidorResult.data.chequeo.getLblParteElectrica(), vParteElectrica.getSelectedLabel(), vParteElectrica.isChecked())
                .chequeo(mChequeoSurtidorResult.data.chequeo.getLblPlanSellado(), vPlanSellado.getSelectedLabel(), vPlanSellado.isChecked())
                .chequeo(mChequeoSurtidorResult.data.chequeo.getLblOtros(), vOtros.getSelectedLabel(), vOtros.isChecked());


        ApiService.saveChequeoSurtidor(b, new AsyncCallback<BaseResult>(getActivity()) {
            @Override
            protected void onSuccess(BaseResult result) {

                GetChequeoSurtidorResult mChequeoSurtidorResult = getActivoActivity().mChequeoSurtidorResult;
                ChequeoSurtidor chequeo = mChequeoSurtidorResult.data.chequeo;

                chequeo.setCabezalLimiezaSellado(vCabezalLimpiezaSellado.getSelectedLabel(), vCabezalLimpiezaSellado.isChecked());
                chequeo.setPresetCara1(vPresetCara1.getSelectedLabel(), vPresetCara1.isChecked());
                chequeo.setPresetCara2(vPresetCara2.getSelectedLabel(), vPresetCara2.isChecked());
                chequeo.setPruebaDisplayIluminacion(vPruebaDisplay.getSelectedLabel(), vPruebaDisplay.isChecked());
                chequeo.setVisualYLimpieza(vVisualLimpieza.getSelectedLabel(), vVisualLimpieza.isChecked());
                chequeo.setParteElectrica(vParteElectrica.getSelectedLabel(), vParteElectrica.isChecked());
                chequeo.setPlanSellado(vPlanSellado.getSelectedLabel(), vPlanSellado.isChecked());
                chequeo.setOtros(vOtros.getSelectedLabel(), vOtros.isChecked());

                setUnSavedData(false);
                ToastUtils.showToast(getActivity(), R.string.surtidor_chequeo_saved_successful);


                if (mOnChequeoListener != null) {
                    mOnChequeoListener.onSaveChequeo();
                }

            }
        });
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
            ((ActivoActivity) getActivity()).goToComments(chequeoId, parent.getLabel());
        }
    };

}
