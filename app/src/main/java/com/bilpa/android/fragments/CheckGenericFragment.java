package com.bilpa.android.fragments;

import android.os.Bundle;
import android.support.v7.internal.view.ContextThemeWrapper;
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
import com.bilpa.android.model.ChequeoGeneric;
import com.bilpa.android.model.Visita;
import com.bilpa.android.services.ApiService;
import com.bilpa.android.services.AsyncCallback;
import com.bilpa.android.services.actions.BaseResult;
import com.bilpa.android.services.actions.GetChequeoBombaResult;
import com.bilpa.android.services.actions.GetChequeoGenericoResult;
import com.bilpa.android.services.actions.SaveChequeoBombaAction;
import com.bilpa.android.services.actions.SaveChequeoGenericoAction;
import com.bilpa.android.widgets.OnChequeoListener;
import com.bilpa.android.widgets.RadioCheckboxWidget;
import com.itextpdf.text.pdf.spatial.units.Linear;
import com.mautibla.utils.ToastUtils;
import com.mautibla.utils.ViewUtils;

import java.util.List;

import static com.bilpa.android.R.id.vFallSump;

public class CheckGenericFragment extends SaveFragment implements
        View.OnClickListener,
        CompoundButton.OnCheckedChangeListener {


    private Toolbar toolbar;
    private LinearLayout vData;
    private ProgressBar vProgress;

    private OnChequeoListener mOnChequeoListener;
    private LinearLayout vChequeosContainer;

    public void setOnChequeoListener(OnChequeoListener onChequeoListener) {
        this.mOnChequeoListener = onChequeoListener;
    }

    public static CheckGenericFragment newInstance() {
        CheckGenericFragment fragment = new CheckGenericFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public CheckGenericFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_check_generic, container, false);

        vData = (LinearLayout) v.findViewById(R.id.vData);
        vProgress = (ProgressBar) v.findViewById(R.id.vProgress);
        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        vChequeosContainer = (LinearLayout) v.findViewById(R.id.vChequeosContainer);

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

        // menu
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

        if (getActivoActivity().mChequeoGenericoResult != null) {
            bind(getActivoActivity().mChequeoGenericoResult);
        } else {
            ViewUtils.visible(vProgress);


            ApiService.getChequeoGenerico(mVisita.id, mActivo.id, new AsyncCallback<GetChequeoGenericoResult>(getActivity()) {
                @Override
                protected void onSuccess(GetChequeoGenericoResult result) {
                    getActivoActivity().mChequeoGenericoResult = result;
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

    private void bind(GetChequeoGenericoResult result) {

        // show data panel
        ViewUtils.gone(vProgress);
        ViewUtils.visible(vData);

        ChequeoGeneric chequeo = result.data.chequeo;
        if (result.data.chequeo == null) {
            result.data.chequeo = new ChequeoGeneric();
        }

        String opt0 = getString(R.string.radio_opt_b);
        String opt1 = getString(R.string.radio_opt_c);
        String opt2 = getString(R.string.radio_opt_p);

        int lblWidth = (int) getContext().getResources().getDimension(R.dimen.activo_check_text_width);

        List<Chequeo> items = chequeo.items;
        for (int i = 0; i < items.size(); i++) {
            Chequeo chequeoItem = items.get(i);
            ContextThemeWrapper contextThemeWrapper;
            if (i % 2 != 0) {
                contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.ActivoChekeoItem_Even);
            } else {
                contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.ActivoChekeoItem_Odd);
            }
            RadioCheckboxWidget widget = new RadioCheckboxWidget(contextThemeWrapper, null, 0);
            widget.setUpView(chequeoItem.texto, lblWidth, 0, 3, opt0, opt1, opt2, null);
            bindCheck(widget, chequeo != null ? chequeoItem : null);
            vChequeosContainer.addView(widget);
        }
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

        GetChequeoGenericoResult chequeoResult = getActivoActivity().mChequeoGenericoResult;
        GetChequeoGenericoResult.Data data = chequeoResult.data;
        ChequeoGeneric chequeo = chequeoResult.data.chequeo;

        SaveChequeoGenericoAction.Builder b = new SaveChequeoGenericoAction.Builder()
            .idPreventivo(data.idPreventivo);


        List<Chequeo> items = chequeo.items;
        for (int i = 0; i < items.size(); i++) {
            Chequeo item = items.get(i);
            RadioCheckboxWidget widget = (RadioCheckboxWidget) vChequeosContainer.getChildAt(i);
            b.chequeo(item.nombre, widget.getSelectedLabel(), widget.isChecked());
        }


        ApiService.saveChequeoGenerico(b, new AsyncCallback<BaseResult>(getActivity()) {
            @Override
            protected void onSuccess(BaseResult result) {
                GetChequeoGenericoResult mChequeoGenericoResult = getActivoActivity().mChequeoGenericoResult;
                ChequeoGeneric chequeo = mChequeoGenericoResult.data.chequeo;
                List<Chequeo> items = chequeo.items;
                for (int i = 0; i < items.size(); i++) {
                    Chequeo item = items.get(i);
                    RadioCheckboxWidget widget = (RadioCheckboxWidget) vChequeosContainer.getChildAt(i);
                    item.valor = widget.getSelectedLabel();
                    item.pendiente = widget.isChecked();
                    setUnSavedData(false);
                    ToastUtils.showToast(getActivity(), R.string.generico_chequeo_saved_successful);
                }

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
