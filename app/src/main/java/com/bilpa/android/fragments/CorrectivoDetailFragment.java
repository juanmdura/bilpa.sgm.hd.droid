package com.bilpa.android.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bilpa.android.BilpaApp;
import com.bilpa.android.CorrectivoActivoActivity;
import com.bilpa.android.R;
import com.bilpa.android.ReportActivity;
import com.bilpa.android.adapters.ActivosCorrectivosAdapter;
import com.bilpa.android.adapters.ActivosFallasAdapter;
import com.bilpa.android.model.Pendiente;
import com.bilpa.android.model.correctivos.CActivo;
import com.bilpa.android.model.correctivos.Correctivo;
import com.bilpa.android.model.correctivos.FallaActivo;
import com.bilpa.android.services.ApiCorrectivos;
import com.bilpa.android.services.ApiService;
import com.bilpa.android.services.AsyncCallback;
import com.bilpa.android.services.actions.BaseResult;
import com.bilpa.android.services.actions.GetActivoIdByQrResult;
import com.bilpa.android.services.correctivos.GetActivosConFallasResult;
import com.bilpa.android.services.correctivos.GetActivosNoReportadosResult;
import com.bilpa.android.services.correctivos.IniciarCorrectivoAction;
import com.bilpa.android.services.correctivos.ModificarCorrectivoAction;
import com.bilpa.android.utils.Consts;
import com.bilpa.android.utils.Logger;
import com.bilpa.android.utils.MaterialAlertDialogUtils;
import com.mautibla.utils.ToastUtils;
import com.mautibla.utils.ViewUtils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CorrectivoDetailFragment extends BaseSupportFragment implements View.OnClickListener {

    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault());

    private Correctivo mCorrectivo;

    private TextView vEstacionSelloNro;
    private TextView vDucsaNro;
    private TextView vLocalidad;
    private TextView vDateInit;
    private TextView vDateEnd;
    private TextView vDatePlazo;
    private TextView vStatus;
    private ImageView vSello;
    private LinearLayout vListActivosFallas;
    private LinearLayout vListActivosNoReportados;
    private Button vBtnActivosQR;


    private GetActivosNoReportadosResult mActivosNoReportados;
    private GetActivosConFallasResult mActivosReportados;
    private ActivosFallasAdapter mAdapterFallasReportados;
    private ActivosCorrectivosAdapter mAdapterNoReportados;
    private LinearLayout vFallasContainer;
    private LinearLayout vActivosContainer;
    private TextView vInitLbl;
    private TextView vEndLbl;
    private TextView vInit;
    private TextView vEnd;
    private LinearLayout vInitEndDates;


    public static CorrectivoDetailFragment newInstance(Correctivo correctivo) {
        CorrectivoDetailFragment fragment = new CorrectivoDetailFragment();
        fragment.mCorrectivo = correctivo;
        return fragment;
    }

    public static CorrectivoDetailFragment newInstance(Correctivo correctivo, Pendiente pendiente) {
        CorrectivoDetailFragment fragment = new CorrectivoDetailFragment();
        fragment.mCorrectivo = correctivo;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        int menuId = 0;
        switch (mCorrectivo.getStatus()) {
            case PENDING:
                menuId = R.menu.menu_correctivos_pending;
                break;
            case INPROCESS:
                menuId = R.menu.menu_correctivos_inprocess;
                break;
            case FINALIZED:
                menuId = R.menu.menu_correctivos_finalized;
                break;

        }
        inflater.inflate(menuId, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_correctivo_detail, container, false);

        vEstacionSelloNro = (TextView) v.findViewById(R.id.vEstacionSelloNro);
        vDucsaNro = (TextView) v.findViewById(R.id.vDucsaNro);
        vLocalidad = (TextView) v.findViewById(R.id.vLocalidad);
        vDateInit = (TextView) v.findViewById(R.id.vDateInit);
        vDateEnd = (TextView) v.findViewById(R.id.vDateEnd);
        vDatePlazo = (TextView) v.findViewById(R.id.vDatePlazo);
        vStatus = (TextView) v.findViewById(R.id.vStatus);
        vSello = (ImageView) v.findViewById(R.id.vSello);


        vInitEndDates = (LinearLayout) v.findViewById(R.id.vInitEndDates);
        vInitLbl = (TextView) v.findViewById(R.id.vInitLbl);
        vInit = (TextView) v.findViewById(R.id.vInit);
        vEndLbl = (TextView) v.findViewById(R.id.vEndLbl);
        vEnd = (TextView) v.findViewById(R.id.vEnd);

        vFallasContainer = (LinearLayout) v.findViewById(R.id.vFallasContainer);
        vActivosContainer = (LinearLayout) v.findViewById(R.id.vActivosContainer);


        vListActivosFallas = (LinearLayout) v.findViewById(R.id.vListActivosFallas);
        vListActivosNoReportados = (LinearLayout) v.findViewById(R.id.vListActivosNoReportados);

        vBtnActivosQR = (Button) v.findViewById(R.id.vBtnActivosQR);
        vBtnActivosQR.setOnClickListener(this);

        vInit.setOnClickListener(this);
        vEnd.setOnClickListener(this);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.containsKey("mCorrectivo")) {
            mCorrectivo = (Correctivo) savedInstanceState.getSerializable("mCorrectivo");
        }


        // Nombre del sello y numero de correctivo
        vEstacionSelloNro.setText(String.format(getString(R.string.correctivo_detail_sello_nro), mCorrectivo.sello, String.valueOf(mCorrectivo.id)));

        // Numero ducsa
        if (mCorrectivo.numeroDucsa > 0) {
            ViewUtils.visible(vDucsaNro);
            vDucsaNro.setText(String.format(getString(R.string.correctivo_detail_ducsa_nro), String.valueOf(mCorrectivo.numeroDucsa)));
        } else {
            ViewUtils.gone(vDucsaNro);
        }


        // Localidad
        vLocalidad.setText(String.format(getString(R.string.correctivos_list_zone), mCorrectivo.localidad));

        // Inicio
        vDateInit.setText(String.format(getString(R.string.correctivos_list_init), sdf.format(mCorrectivo.fechaInicio)));

        // Cumplimiento
        if (mCorrectivo.fechaCumplimiento2 != null) {
            vDateEnd.setText(String.format(getString(R.string.correctivos_list_end), sdf.format(mCorrectivo.fechaCumplimiento2)));
            ViewUtils.visible(vDateEnd);
        } else {
            ViewUtils.gone(vDateEnd);
        }


        // Plazo
        vDatePlazo.setText(String.format(getString(R.string.correctivos_list_plazo), sdf.format(mCorrectivo.plazo)));

        // Status
        vStatus.setText(mCorrectivo.getStatusLabel(getActivity()));
        vStatus.setBackgroundResource(mCorrectivo.getStatusBg());

        updateStatus();

        // Icono del sello
        if (Consts.Sello.PETROBRAS.equals(mCorrectivo.sello)) {
            vSello.setImageResource(R.drawable.logo_sello_petrobras);
        } else if (Consts.Sello.ANCAP.equals(mCorrectivo.sello)) {
            vSello.setImageResource(R.drawable.logo_sello_ancap);
        } else if (Consts.Sello.ESSO.equals(mCorrectivo.sello)) {
            vSello.setImageResource(R.drawable.logo_sello_esso);
        } else {
            vSello.setImageResource(R.drawable.logo_sello_ancap);
        }

        bindInitEnds();
        loadActivos();
    }

    private void bindInitEnds() {
        /**
         Set Date inicio y fin del servicio.
         */

        if (mCorrectivo.inicioServiceUsuario != null) {
            Calendar calInit = Calendar.getInstance();
            calInit.setTime(mCorrectivo.inicioServiceUsuario);
            setTime(vInit, calInit);
        } else {
            setTime(vInit, null);
        }

        if (mCorrectivo.finServiceUsuario != null) {
            Calendar calEnd = Calendar.getInstance();
            calEnd.setTime(mCorrectivo.finServiceUsuario);
            setTime(vEnd, calEnd);
        } else {
            setTime(vEnd, null);
        }


        if (mCorrectivo.getStatus() == Correctivo.Status.PENDING) {
            vInitEndDates.setVisibility(View.GONE);
        } else if (mCorrectivo.getStatus() == Correctivo.Status.INPROCESS) {
            vInitEndDates.setVisibility(View.VISIBLE);
            vInit.setEnabled(true);
            vEnd.setEnabled(true);
        } else {
            vInitEndDates.setVisibility(View.VISIBLE);
            vInit.setEnabled(false);
            vEnd.setEnabled(false);
            vInit.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            vEnd.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
    }

    private void invalidateAllActivos() {
        loadActivos();
    }


    private void loadActivos() {

        ViewUtils.gone(vFallasContainer, vActivosContainer);

        /*
            Activos reportados
         */
        vListActivosFallas.removeAllViews();
        mActivosReportados = null;
        ApiCorrectivos.getActivosConFallas(mCorrectivo.id, new AsyncCallback<GetActivosConFallasResult>(getActivity()) {
            @Override
            protected void onSuccess(GetActivosConFallasResult result) {
                mActivosReportados = result;
                bindActivosConFallas(result);
            }
        });

        /*
            Activos no reportados
         */
        vListActivosNoReportados.removeAllViews();
        mActivosNoReportados = null;
        Long idEstacion = mCorrectivo.idEstacion;
        Long idCorrectivo = mCorrectivo.id;
        ApiCorrectivos.getActivosNoReportados(idEstacion, idCorrectivo, new AsyncCallback<GetActivosNoReportadosResult>(getActivity()) {
            @Override
            protected void onSuccess(GetActivosNoReportadosResult result) {
                mActivosNoReportados = result;
                bindActivosNoReportados(result.data);
            }
        });
    }

    private void bindActivosConFallas(GetActivosConFallasResult result) {

        if (result.data.isEmpty()) {
            ViewUtils.gone(vFallasContainer);
        } else {
            mAdapterFallasReportados = new ActivosFallasAdapter(getActivity(), result.data);
            mAdapterFallasReportados.setOnCorregirActivoListener(this);
            final int adapterCount = mAdapterFallasReportados.getCount();
            for (int i = 0; i < adapterCount; i++) {
                View view = mAdapterFallasReportados.getView(i, null, null);

                vListActivosFallas.addView(view);
                FallaActivo fallaActivo = mAdapterFallasReportados.getItem(i);
                view.setTag(fallaActivo);
                view.setOnClickListener(mFallaActivoLisetner);
            }
            ViewUtils.visible(vFallasContainer);
        }

    }


    private void bindActivosNoReportados(List<CActivo> data) {
        if (data.isEmpty()) {
            ViewUtils.gone(vActivosContainer);
        } else {
            mAdapterNoReportados = new ActivosCorrectivosAdapter(getActivity(), data);
            final int adapterCount = mAdapterNoReportados.getCount();
            for (int i = 0; i < adapterCount; i++) {
                View view = mAdapterNoReportados.getView(i, null, null);
                vListActivosNoReportados.addView(view);
                CActivo activo = mAdapterNoReportados.getItem(i);
                view.setTag(activo);
                view.setOnClickListener(mActivoClickLisetener);
            }
            ViewUtils.visible(vActivosContainer);
        }
    }

    private void updateQRCodeActivo(FallaActivo fallaActivo, CActivo newActivo) {
        if (fallaActivo != null) {
            int index = mActivosReportados.data.indexOf(fallaActivo);
            if (index != -1) {
                vListActivosFallas.removeViewAt(index);
                fallaActivo.activo = newActivo;
                View view = mAdapterFallasReportados.getView(index, null, null);
                vListActivosFallas.addView(view, index);
            }
        }

    }


    private void updateQRCodeActivo(CActivo activo) {


        FallaActivo fallaActivo = mActivosReportados.findByActivo(activo);
        if (fallaActivo != null) {
            int index = mActivosReportados.data.indexOf(fallaActivo);
            if (index != -1) {
                vListActivosFallas.removeViewAt(index);
                View view = mAdapterFallasReportados.getView(index, null, null);
                vListActivosFallas.addView(view, index);
            }
        }

        int indexOf = mActivosNoReportados.data.indexOf(activo);
        if (indexOf != -1) {
            vListActivosNoReportados.removeViewAt(indexOf);
            View view = mAdapterNoReportados.getView(indexOf, null, null);
            vListActivosNoReportados.addView(view, indexOf);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("mCorrectivo", mCorrectivo);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        BilpaApp app = BilpaApp.getInstance();
        app.cancelPendingRequests(ApiCorrectivos.GET_ACTIVOS_FALLAS);
        app.cancelPendingRequests(ApiCorrectivos.GET_ACTIVOS_NO_REPORTADOS);
        app.cancelPendingRequests(ApiCorrectivos.INICIAR_CORRECTIVO);
        app.cancelPendingRequests(ApiService.GET_ACTIVO_BY_QR);
        app.cancelPendingRequests(ApiCorrectivos.CORREGIR_ACTIVO_FALLA);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.vBtnActivosQR:

                if (mCorrectivo.getStatus() == Correctivo.Status.PENDING) {
                    MaterialAlertDialogUtils.showAcceptMsg(getActivity(), R.string.correctivo_detail_msg_service_pending_to_read_qr);
                    return;
                } else if (mCorrectivo.getStatus() == Correctivo.Status.FINALIZED) {
                    MaterialAlertDialogUtils.showAcceptMsg(getActivity(), R.string.correctivo_detail_msg_service_finalized_to_read_qr);
                    return;
                }

                super.showQR(new QRDialogFragment.QRListener() {
                    @Override
                    public void onQueryCode(final String code) {

                        showLoading();

                        ApiService.getActivoIdByQr(code, mCorrectivo.idEstacion, new AsyncCallback<GetActivoIdByQrResult>(getActivity()) {
                            @Override
                            protected void onSuccess(GetActivoIdByQrResult result) {
                                hideLoading();

                                Logger.d(EstacionFragment.class.getSimpleName(), "QR Code readed : " + code);

                                if (result.data != -1) {
                                    // Activo encontrado

                                    int activoId = result.data;
                                    CActivo activo = findActivoById(activoId);

                                    if (activo != null) {
                                        goToActivo(activo);
                                    } else {
                                        ToastUtils.showToast(getActivity(), getString(R.string.visita_msg_activo_no_encontrado));
                                    }

                                } else {
                                    // Activo no encontrado. Asociar qr
                                    showListActivos(code);
                                }
                            }
                        });
                    }
                });

                break;

            case R.id.vBtnChangeActivo:

                final FallaActivo activoFalla = (FallaActivo) v.getTag();

                showQR(getString(R.string.qr_reader_title_active_reportado), new QRDialogFragment.QRListener() {
                    @Override
                    public void onQueryCode(final String code) {

                        showLoading();

                        ApiService.getActivoIdByQr(code, mCorrectivo.idEstacion, new AsyncCallback<GetActivoIdByQrResult>(getActivity()) {
                            @Override
                            protected void onSuccess(GetActivoIdByQrResult result) {
                                hideLoading();

                                Logger.d(EstacionFragment.class.getSimpleName(), "QR Code readed : " + code);

                                if (result.data != -1) {
                                    // Activo encontrado

                                    int activoId = result.data;
                                    CActivo activo = findActivoById(activoId);

                                    if (activo != null) {

                                        corregirActivoFalla(activo, activoFalla);

                                    } else {
                                        ToastUtils.showToast(getActivity(), getString(R.string.visita_msg_activo_no_encontrado));
                                    }

                                } else {
                                    // Activo no encontrado.
                                    MaterialAlertDialogUtils.showAcceptMsg(getActivity(), R.string.correctivo_detail_msg_qr_no_asociado);
                                }
                            }


                            @Override
                            public void onError(String msg, MaterialDialog.ButtonCallback callback) {
                                hideLoading();
                                super.onError(msg, callback);
                            }
                        });
                    }
                });

                break;


            case R.id.vInit:
                Calendar calInit = Calendar.getInstance();
                if (mCorrectivo.inicioServiceUsuario != null) {
                    calInit.setTime(mCorrectivo.inicioServiceUsuario);
                } else if (mCorrectivo.inicioServiceReal != null) {
                    calInit.setTime(mCorrectivo.inicioServiceReal);
                }
                pickDate((TextView) v, calInit, new OnPickDateListener() {
                    @Override
                    public void onPickDate(Calendar cal) {
                        ModificarCorrectivoAction.Builder b = new ModificarCorrectivoAction.Builder();
                        b.idCorrectivo = mCorrectivo.id;
                        b.inicio = cal.getTime();
                        ApiCorrectivos.modificarCorrectivo(b, new AsyncCallback<BaseResult>(getActivity()) {
                            @Override
                            protected void onSuccess(BaseResult result) {
                                getActivity().setResult(Activity.RESULT_OK);
                            }
                            @Override
                            public void onError(String msg, MaterialDialog.ButtonCallback callback) {
                                super.onError(msg, callback);
                            }
                        });
                    }
                });
                break;


            case R.id.vEnd:

                Calendar calEnd = Calendar.getInstance();
                if (mCorrectivo.finServiceUsuario != null) {
                    calEnd.setTime(mCorrectivo.finServiceUsuario);
                } else if (mCorrectivo.finServiceReal != null) {
                    calEnd.setTime(mCorrectivo.finServiceReal);
                }
                pickDate((TextView) v, calEnd, new OnPickDateListener() {
                    @Override
                    public void onPickDate(Calendar cal) {
                        ModificarCorrectivoAction.Builder b = new ModificarCorrectivoAction.Builder();
                        b.idCorrectivo = mCorrectivo.id;
                        b.fin = cal.getTime();
                        ApiCorrectivos.modificarCorrectivo(b, new AsyncCallback<BaseResult>(getActivity()) {
                            @Override
                            protected void onSuccess(BaseResult result) {
                                getActivity().setResult(Activity.RESULT_OK);
                            }
                            @Override
                            public void onError(String msg, MaterialDialog.ButtonCallback callback) {
                                super.onError(msg, callback);
                            }
                        });
                    }
                });
                break;
        }

    }

    private void showListActivos(final String code) {

        List<CActivo> activos = getAllActivos();

        super.showQRSetActivo(activos, new CActivoSelectDialogFragment.OnSelectActivoListener() {
            @Override
            public void onSelectActivo(final CActivo activo) {
                CActivo.QR qr = new CActivo.QR();
                qr.codigo = String.valueOf(code);
                activo.qr = qr;
                ApiService.setQrToActivo(activo.id, code, new AsyncCallback<BaseResult>(getActivity()) {
                    @Override
                    protected void onSuccess(BaseResult result) {

                        // Actualizar el activo
                        updateQRCodeActivo(activo);

                        // Navegar al activo
                        goToActivo(activo);

                    }
                });
            }
        });


    }

    private CActivo findActivoById(int activoId) {

        Long id = Long.valueOf(activoId);

        List<FallaActivo> data = mActivosReportados.data;
        for (FallaActivo fallaActivo : data) {
            CActivo activo = fallaActivo.activo;
            if (activo.id.equals(id)) {
                return activo;
            }
        }

        List<CActivo> data1 = mActivosNoReportados.data;
        for (CActivo activo : data1) {
            if (activo.id.equals(id)) {
                return activo;
            }
        }

        return null;
    }

    public List<CActivo> getAllActivos() {

        List<CActivo> allActivos = new ArrayList<>();

        List<FallaActivo> data = mActivosReportados.data;
        for (FallaActivo fallaActivo : data) {
            CActivo activo = fallaActivo.activo;
            allActivos.add(activo);
        }

        List<CActivo> data1 = mActivosNoReportados.data;
        for (CActivo activo : data1) {
            allActivos.add(activo);
        }

        return allActivos;
    }


    private View.OnClickListener mFallaActivoLisetner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FallaActivo fallaActivo = (FallaActivo) v.getTag();
            CActivo activo = fallaActivo.activo;
            goToActivo(activo);
        }
    };

    private View.OnClickListener mActivoClickLisetener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CActivo activo = (CActivo) v.getTag();
            goToActivo(activo);
        }
    };

    private void corregirActivoFalla(CActivo activo, FallaActivo activoFalla) {

        ApiCorrectivos.corregirActivo(mCorrectivo.id, activoFalla.activo.id, activo.id, new AsyncCallback<BaseResult>(getActivity()) {
            @Override
            protected void onSuccess(BaseResult result) {
                invalidateAllActivos();
            }
        });
    }

    private void goToActivo(CActivo activo) {

        switch (mCorrectivo.getStatus()) {

            case PENDING:
                MaterialAlertDialogUtils.showAcceptMsg(getActivity(), R.string.correctivo_detail_msg_activo_access_pending);
                break;

            case INPROCESS:
                Intent intent = new Intent(getActivity(), CorrectivoActivoActivity.class);
                intent.putExtra("correctivo", mCorrectivo);
                intent.putExtra("activo", activo);
                startActivityForResult(intent, Consts.Request.CORRECTIVO_DETAIL_ACTIVOS);
                break;

            case FINALIZED:
                MaterialAlertDialogUtils.showAcceptMsg(getActivity(), R.string.correctivo_detail_msg_activo_access_finalized);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.vInitService:
                if (mCorrectivo.getStatus() == Correctivo.Status.PENDING) {
                    IniciarCorrectivoAction.Builder b = new IniciarCorrectivoAction.Builder();
                    b.idCorrectivo = mCorrectivo.id;
                    // b.fechaInicio = Calendar.getInstance().getTime(); No se le pasa mas la fecha de inicio. FIXME
                    ApiCorrectivos.iniciarCorrectivo(b, new AsyncCallback<BaseResult>(getActivity()) {
                        @Override
                        protected void onSuccess(BaseResult result) {
                            mCorrectivo.setStatus(Correctivo.Status.INPROCESS);
                            updateStatus();
                            bindInitEnds();
                            getActivity().setResult(Activity.RESULT_OK);
                        }
                    });

                } else if (mCorrectivo.getStatus() == Correctivo.Status.INPROCESS) {
                    MaterialAlertDialogUtils.showAcceptMsg(getActivity(), R.string.correctivo_detail_msg_service_is_inprocess);

                } else if (mCorrectivo.getStatus() == Correctivo.Status.FINALIZED) {
                    MaterialAlertDialogUtils.showAcceptMsg(getActivity(), R.string.correctivo_detail_msg_service_is_finished);
                }

                return true;

            case R.id.vFinish:

                if (mCorrectivo.getStatus() == Correctivo.Status.PENDING) {
                    MaterialAlertDialogUtils.showAcceptMsg(getActivity(), R.string.correctivo_detail_msg_service_is_pending);

                } else if (mCorrectivo.getStatus() == Correctivo.Status.FINALIZED) {
                    MaterialAlertDialogUtils.showAcceptMsg(getActivity(), R.string.correctivo_detail_msg_service_is_finished);

                } else if (mCorrectivo.getStatus() == Correctivo.Status.INPROCESS) {

                    if (validateInitEnd()) {

                        Date inicio = ((Calendar) vInit.getTag()).getTime();
                        Date fin = ((Calendar) vEnd.getTag()).getTime();

                        Intent intent = new Intent(getActivity(), ReportActivity.class);
                        intent.putExtra("correctivo", mCorrectivo);
                        intent.putExtra("inicio", inicio);
                        intent.putExtra("fin", fin);
                        startActivityForResult(intent, Consts.Request.CORRECTIVO_DETAIL);
                    }


                }

                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private boolean validateInitEnd() {

        if (vInit.getTag() == null || vEnd.getTag() == null) {
            MaterialAlertDialogUtils.showAcceptMsg(getActivity(), R.string.correctivo_detail_msg_enter_init_end_dates);
            return false;
        }

        return true;
    }

    private void updateStatus() {
        vStatus.setText(mCorrectivo.getStatusLabel(getActivity()));
        vStatus.setBackgroundResource(mCorrectivo.getStatusBg());
        getActivity().supportInvalidateOptionsMenu();

        switch (mCorrectivo.getStatus()) {
            case FINALIZED:
            case PENDING:
                ViewUtils.gone(vBtnActivosQR);
                break;
            case INPROCESS:
                ViewUtils.visible(vBtnActivosQR);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Consts.Request.CORRECTIVO_DETAIL && resultCode == Activity.RESULT_OK) {
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
            return;
        }


        if (requestCode == Consts.Request.CORRECTIVO_DETAIL_ACTIVOS && resultCode == Activity.RESULT_OK) {
            loadActivos();
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public interface OnPickDateListener {
        public void onPickDate(Calendar cal);
    }


    private void pickDate(final TextView tView, Calendar preview, final OnPickDateListener onPickDateListener) {
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

                        final Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.YEAR, year);
                        cal.set(Calendar.MONTH, monthOfYear);
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);


                        Calendar now = Calendar.getInstance();
                        TimePickerDialog tpd = TimePickerDialog.newInstance(
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
                                        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                        cal.set(Calendar.MINUTE, minute);
                                        setTime(tView, cal);
                                        if (onPickDateListener != null) {
                                            onPickDateListener.onPickDate(cal);
                                        }
                                    }
                                },
                                now.get(Calendar.HOUR_OF_DAY),
                                now.get(Calendar.MINUTE),
                                true
                        );
                        tpd.setThemeDark(false);
                        tpd.show(getFragmentManager(), "Timepickerdialog");


                    }
                },
                preview.get(Calendar.YEAR),
                preview.get(Calendar.MONTH),
                preview.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    private void setTime(TextView tView, Calendar cal) {
        if (cal == null) {
            tView.setTag(null);
            if (tView == vInit) {
                vInit.setText(R.string.visita_date_seleccionar_init);
            } else {
                vEnd.setText(R.string.visita_date_seleccionar_end);
            }
        } else {
            tView.setTag(cal);
            tView.setText(sdf.format(cal.getTime()));
        }
    }


}
