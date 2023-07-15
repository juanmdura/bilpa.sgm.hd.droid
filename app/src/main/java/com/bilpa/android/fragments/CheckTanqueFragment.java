package com.bilpa.android.fragments;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bilpa.android.ActivoActivity;
import com.bilpa.android.BilpaApp;
import com.bilpa.android.R;
import com.bilpa.android.adapters.SimpleSpinnerAdapter;
import com.bilpa.android.model.Activo;
import com.bilpa.android.model.Chequeo;
import com.bilpa.android.model.ChequeoTanque;
import com.bilpa.android.model.TipoDescarga;
import com.bilpa.android.model.Visita;
import com.bilpa.android.services.ApiService;
import com.bilpa.android.services.AsyncCallback;
import com.bilpa.android.services.actions.BaseResult;
import com.bilpa.android.services.actions.GetChequeoTanqueResult;
import com.bilpa.android.services.actions.SaveChequeoTanqueAction;
import com.bilpa.android.services.actions.TipoDescargaResult;
import com.bilpa.android.widgets.OnChequeoListener;
import com.bilpa.android.widgets.RadioCheckboxWidget;
import com.bilpa.android.widgets.RadioWidget;
import com.mautibla.utils.InputUtils;
import com.mautibla.utils.ToastUtils;
import com.mautibla.utils.ViewUtils;

import java.util.List;

public class CheckTanqueFragment extends SaveFragment implements View.OnClickListener, Toolbar.OnMenuItemClickListener,
        CompoundButton.OnCheckedChangeListener, RootFragment,
        TextView.OnEditorActionListener, AdapterView.OnItemSelectedListener {

    private Toolbar toolbar;
    private LinearLayout vData;
    private ProgressBar vProgress;
    private RadioCheckboxWidget vColorMarcaTapa;
    private RadioCheckboxWidget vAguaTanque;
    private RadioCheckboxWidget vVentilacion;
    private RadioCheckboxWidget vTapaLomoTanque;
    private RadioCheckboxWidget vOtros;
    private Spinner vSpinnerTipoDescarga;
    private EditText vMedidaAgua;
    private SimpleSpinnerAdapter<TipoDescarga> adapterTipoDescarga;

    private OnChequeoListener mOnChequeoListener;

    public void setOnChequeoListener(OnChequeoListener onChequeoListener) {
        this.mOnChequeoListener = onChequeoListener;
    }

    public static CheckTanqueFragment newInstance() {
        CheckTanqueFragment fragment = new CheckTanqueFragment();
        return fragment;
    }

    public CheckTanqueFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_check_tanque, container, false);

        vData = (LinearLayout) v.findViewById(R.id.vData);
        vProgress = (ProgressBar) v.findViewById(R.id.vProgress);
        toolbar = (Toolbar) v.findViewById(R.id.toolbar);

        vColorMarcaTapa = (RadioCheckboxWidget) v.findViewById(R.id.vColorMarcaTapa);
        vAguaTanque = (RadioCheckboxWidget) v.findViewById(R.id.vAguaTanque);
        vVentilacion = (RadioCheckboxWidget) v.findViewById(R.id.vVentilacion);
        vTapaLomoTanque = (RadioCheckboxWidget) v.findViewById(R.id.vTapaLomoTanque);
        vOtros = (RadioCheckboxWidget) v.findViewById(R.id.vOtros);

        vSpinnerTipoDescarga = (Spinner) v.findViewById(R.id.vSpinnerTipoDescarga);
        vMedidaAgua = (EditText) v.findViewById(R.id.vMedidaAgua);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ActivoActivity activity = (ActivoActivity) getActivity();
        activity.hideAddBtn();

        ViewUtils.gone(vData, vProgress);

        Activo mActivo = activity.mActivo;
        Visita mVisita = activity.mVisita;


        toolbar.setTitle(mActivo.descripcion);
        if (mActivo.codigoQR != 0) {
            toolbar.setSubtitle(String.format(getString(R.string.visita_activos_code), String.valueOf(mActivo.codigoQR)));
        }

        toolbar.inflateMenu(R.menu.surtidor_check);
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


        if (getActivoActivity().mChequeoTanqueResult != null) {
            GetChequeoTanqueResult result = getActivoActivity().mChequeoTanqueResult;
            bind(result);
        } else {
            ViewUtils.visible(vProgress);
            ApiService.getChequeoTanque(mVisita.id, mActivo.id, new AsyncCallback<GetChequeoTanqueResult>(getActivity()) {
                @Override
                protected void onSuccess(GetChequeoTanqueResult result) {
                    getActivoActivity().mChequeoTanqueResult = result;
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

    private void bind(GetChequeoTanqueResult result) {
        ViewUtils.gone(vProgress);
        ViewUtils.visible(vData);

        if (result.data.chequeo == null) {
            result.data.chequeo = new ChequeoTanque();
        }

        ChequeoTanque chequeoTanque = result.data.chequeo;

        bindCheck(vColorMarcaTapa, chequeoTanque != null ? chequeoTanque.getColorMarcoTapa() : null);
        bindCheck(vAguaTanque, chequeoTanque != null ? chequeoTanque.getAguaTanque() : null);
        bindCheck(vVentilacion, chequeoTanque != null ? chequeoTanque.getVentilacion() : null);
        bindCheck(vTapaLomoTanque, chequeoTanque != null ? chequeoTanque.getTapaLomoTanque() : null);
        bindCheck(vOtros, chequeoTanque != null ? chequeoTanque.getOtros() : null);

        vMedidaAgua.setText(result.data.chequeo.medidaDelAgua + "");
        vMedidaAgua.setOnEditorActionListener(this);

        loadTipoDescagas();

    }

    public void bindCheck(RadioCheckboxWidget chequeoWidget, Chequeo chequeo) {
        chequeoWidget.bindCheckeo(chequeo, this, mCommentsClickListener);
    }

    private void loadTipoDescagas() {

        if (BilpaApp.getInstance().mTipoDescargaResult == null) {
            ApiService.getTiposDescargas(new AsyncCallback<TipoDescargaResult>(getActivity()) {
                @Override
                protected void onSuccess(TipoDescargaResult result) {
                    BilpaApp.getInstance().mTipoDescargaResult = result;
                    bindTipoDescarga(result);
                }

                @Override
                protected void onServiceOperationFailOK() {
                    super.onServiceOperationFailOK();
                    getActivity().onBackPressed();
                }

            });
        } else {
            bindTipoDescarga(BilpaApp.getInstance().mTipoDescargaResult);
        }
    }

    private void bindTipoDescarga(TipoDescargaResult result) {

        /**
         * Creo lista de items
         */
        List<TipoDescarga> items = result.tiposDescarga;
        adapterTipoDescarga = new SimpleSpinnerAdapter<TipoDescarga>(getActivity(), items) {
            @Override
            protected String getDescription(TipoDescarga item) {
                return item.nombre;
            }

            @Override
            protected String getDescriptionDropDown(TipoDescarga item) {
                return item.nombre;
            }
        };
        vSpinnerTipoDescarga.setAdapter(adapterTipoDescarga);

        /**
         * Selecciono el item en caso de que corresponda
         */
        if (getActivoActivity().mChequeoTanqueResult != null) {
            GetChequeoTanqueResult.Data data = getActivoActivity().mChequeoTanqueResult.data;
            if (data != null && data.chequeo != null) {
                ChequeoTanque chequeoTanque = data.chequeo;
                int tipoDeDescargaId = chequeoTanque.tipoDeDescarga;
                TipoDescarga tipoDescarga = result.find(tipoDeDescargaId);
                if (tipoDescarga != null) {
                    int position = adapterTipoDescarga.getPosition(tipoDescarga);
                    vSpinnerTipoDescarga.setSelection(position);
                }
            }
        }

        /**
         * Seteo de forma async el listener caputar los cambios
         */
        vSpinnerTipoDescarga.post(new Runnable() {
            public void run() {
                vSpinnerTipoDescarga.setOnItemSelectedListener(CheckTanqueFragment.this);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        BilpaApp app = BilpaApp.getInstance();
        app.cancelPendingRequests(ApiService.GET_CHEQUEO);
        app.cancelPendingRequests(ApiService.GET_TIPOS_DESCARGAS);
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

        GetChequeoTanqueResult chequeo = getActivoActivity().mChequeoTanqueResult;
        GetChequeoTanqueResult.Data data = chequeo.data;

        final Long medidaDelAgua = !InputUtils.isEmpty(vMedidaAgua) ? InputUtils.getLong(vMedidaAgua) : 0L;

        SaveChequeoTanqueAction.Builder b = new SaveChequeoTanqueAction.Builder()
            .idPreventivo(data.idPreventivo)
            .chequeo(chequeo.data.chequeo.getLblColorMarcoTapa(), vColorMarcaTapa.getSelectedLabel(), vColorMarcaTapa.isChecked())
            .chequeo(chequeo.data.chequeo.getLblAguaTanque(), vAguaTanque.getSelectedLabel(), vAguaTanque.isChecked())
            .chequeo(chequeo.data.chequeo.getLblTapaLomoTanque(), vTapaLomoTanque.getSelectedLabel(), vTapaLomoTanque.isChecked())
            .chequeo(chequeo.data.chequeo.getLblVentilacion(), vVentilacion.getSelectedLabel(), vVentilacion.isChecked())
            .chequeo(chequeo.data.chequeo.getLblOtros(), vOtros.getSelectedLabel(), vOtros.isChecked())
            .medidaDelAgua(medidaDelAgua);

        TipoDescarga td = (TipoDescarga) vSpinnerTipoDescarga.getSelectedItem();
        b.tipoDeDescarga(String.valueOf(td.id));

        ApiService.saveChequeoTanque(b, new AsyncCallback<BaseResult>(getActivity()) {
            @Override
            protected void onSuccess(BaseResult result) {
                GetChequeoTanqueResult tanqueResult = getActivoActivity().mChequeoTanqueResult;
                ChequeoTanque chequeo = tanqueResult.data.chequeo;

                chequeo.setColorMarcoTapa(vColorMarcaTapa.getSelectedLabel(), vColorMarcaTapa.isChecked());
                chequeo.setAguaTanque(vAguaTanque.getSelectedLabel(), vAguaTanque.isChecked());
                chequeo.setTapaLomoTanque(vTapaLomoTanque.getSelectedLabel(), vTapaLomoTanque.isChecked());
                chequeo.setVentilacion(vVentilacion.getSelectedLabel(), vVentilacion.isChecked());
                chequeo.setOtros(vOtros.getSelectedLabel(), vOtros.isChecked());

                chequeo.medidaDelAgua = !InputUtils.isEmpty(vMedidaAgua) ? InputUtils.getLong(vMedidaAgua) : 0L;
                TipoDescarga td = (TipoDescarga) vSpinnerTipoDescarga.getSelectedItem();
                chequeo.tipoDeDescarga = td.id;

                InputUtils.setNum(vMedidaAgua, chequeo.medidaDelAgua);

                setUnSavedData(false);
                ToastUtils.showToast(getActivity(), R.string.tanque_chequeo_saved_successful);

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

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        setUnSavedData(true);
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        setUnSavedData(true);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
