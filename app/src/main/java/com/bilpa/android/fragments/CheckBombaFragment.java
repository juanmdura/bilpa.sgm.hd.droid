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

import com.afollestad.materialdialogs.MaterialDialog;
import com.bilpa.android.ActivoActivity;
import com.bilpa.android.BilpaApp;
import com.bilpa.android.R;
import com.bilpa.android.model.Activo;
import com.bilpa.android.model.Chequeo;
import com.bilpa.android.model.ChequeoBomba;
import com.bilpa.android.model.Visita;
import com.bilpa.android.services.ApiService;
import com.bilpa.android.services.AsyncCallback;
import com.bilpa.android.services.actions.BaseResult;
import com.bilpa.android.services.actions.GetChequeoBombaResult;
import com.bilpa.android.services.actions.SaveChequeoBombaAction;
import com.bilpa.android.widgets.OnChequeoListener;
import com.bilpa.android.widgets.RadioCheckboxWidget;
import com.mautibla.utils.ToastUtils;
import com.mautibla.utils.ViewUtils;

public class CheckBombaFragment extends SaveFragment implements
        View.OnClickListener,
        CompoundButton.OnCheckedChangeListener {


    private Toolbar toolbar;
    private LinearLayout vData;
    private ProgressBar vProgress;
    private RadioCheckboxWidget vFallSump;
    private RadioCheckboxWidget vSumpHermetico;
    private RadioCheckboxWidget vDetectorFuga;
    private RadioCheckboxWidget vSifon;
    private RadioCheckboxWidget vOtros;

    private OnChequeoListener mOnChequeoListener;

    public void setOnChequeoListener(OnChequeoListener onChequeoListener) {
        this.mOnChequeoListener = onChequeoListener;
    }

    public static CheckBombaFragment newInstance() {
        CheckBombaFragment fragment = new CheckBombaFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public CheckBombaFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_check_bomba, container, false);

        vData = (LinearLayout) v.findViewById(R.id.vData);
        vProgress = (ProgressBar) v.findViewById(R.id.vProgress);
        toolbar = (Toolbar) v.findViewById(R.id.toolbar);

        vFallSump = (RadioCheckboxWidget) v.findViewById(R.id.vFallSump);
        vSumpHermetico = (RadioCheckboxWidget) v.findViewById(R.id.vSumpHermetico);
        vDetectorFuga = (RadioCheckboxWidget) v.findViewById(R.id.vDetectorFuga);
        vSifon = (RadioCheckboxWidget) v.findViewById(R.id.vSifon);
        vOtros = (RadioCheckboxWidget) v.findViewById(R.id.vOtros);

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

        if (getActivoActivity().mChequeoBombaResult != null) {
            bind(getActivoActivity().mChequeoBombaResult);
        } else {
            ViewUtils.visible(vProgress);


            ApiService.getChequeoBomba(mVisita.id, mActivo.id, new AsyncCallback<GetChequeoBombaResult>(getActivity()) {
                @Override
                protected void onSuccess(GetChequeoBombaResult result) {
                    getActivoActivity().mChequeoBombaResult = result;
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

    private void bind(GetChequeoBombaResult result) {

        // show data panel
        ViewUtils.gone(vProgress);
        ViewUtils.visible(vData);

        ChequeoBomba chequeo = result.data.chequeo;
        if (result.data.chequeo == null) {
            result.data.chequeo = new ChequeoBomba();
        }

        bindCheck(vFallSump, chequeo != null ? chequeo.getFugasSump() : null);
        bindCheck(vSumpHermetico, chequeo != null ? chequeo.getSumpHermetico() : null);
        bindCheck(vDetectorFuga, chequeo != null ? chequeo.getDetectorMecanico() : null);
        bindCheck(vSifon, chequeo != null ? chequeo.getSifon() : null);
        bindCheck(vOtros, chequeo != null ? chequeo.getOtros() : null);

    }

    public void bindCheck(RadioCheckboxWidget chequeoWidget, Chequeo chequeo) {
        chequeoWidget.bindCheckeo(chequeo, this, mCommentsClickListener);
    }

    public ActivoActivity getActivoActivity() {
        return (ActivoActivity) getActivity();
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


    @Override
    protected String getUnsavedExitMsg() {
        return getString(R.string.tanque_chequeo_exit_unsaved_msg);
    }

    @Override
    protected int[] assertMenuItemSavedCheck() {
        return new int[] {
                R.id.vMenuClose
        };
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
                                toolbar.setSubtitle(String.format(getString(R.string.visita_activos_code), String.valueOf(mActivo.codigoQR)));
                            }
                        });

                    }
                });
                break;

            case R.id.vMenuClose:
                getActivity().onBackPressed();
                break;

            case R.id.vMenuSave:
                save();
                break;
        }
    }

    private void save() {

        GetChequeoBombaResult chequeoResult = getActivoActivity().mChequeoBombaResult;
        GetChequeoBombaResult.Data data = chequeoResult.data;
        ChequeoBomba chequeo = chequeoResult.data.chequeo;

        SaveChequeoBombaAction.Builder b = new SaveChequeoBombaAction.Builder()
            .idPreventivo(data.idPreventivo)
            .chequeo(chequeo.getLblFugasSump(), vFallSump.getSelectedLabel(), vFallSump.isChecked())
            .chequeo(chequeo.getLblSumpHermetico(), vSumpHermetico.getSelectedLabel(), vSumpHermetico.isChecked())
            .chequeo(chequeo.getLblDetectorMecanico(), vDetectorFuga.getSelectedLabel(), vDetectorFuga.isChecked())
            .chequeo(chequeo.getLblSifon(), vSifon.getSelectedLabel(), vSifon.isChecked())
            .chequeo(chequeo.getLblOtros(), vOtros.getSelectedLabel(), vOtros.isChecked());

        ApiService.saveChequeoBomba(b, new AsyncCallback<BaseResult>(getActivity()) {
            @Override
            protected void onSuccess(BaseResult result) {
                GetChequeoBombaResult mChequeoBombaResult = getActivoActivity().mChequeoBombaResult;
                ChequeoBomba chequeo = mChequeoBombaResult.data.chequeo;
                chequeo.setFugasSump(vFallSump.getSelectedLabel(), vFallSump.isChecked());
                chequeo.setSumpHermetico(vSumpHermetico.getSelectedLabel(), vSumpHermetico.isChecked());
                chequeo.setDetectorMecanico(vDetectorFuga.getSelectedLabel(), vDetectorFuga.isChecked());
                chequeo.setSifon(vSifon.getSelectedLabel(), vSifon.isChecked());
                chequeo.setOtros(vOtros.getSelectedLabel(), vOtros.isChecked());
                setUnSavedData(false);
                ToastUtils.showToast(getActivity(), R.string.bomba_chequeo_saved_successful);

                if (mOnChequeoListener != null) {
                    mOnChequeoListener.onSaveChequeo();
                }
            }
        });
    }

    @Override
    protected void onActionClick(View view) {
        // not used
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
