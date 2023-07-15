package com.bilpa.android.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.bilpa.android.BilpaApp;
import com.bilpa.android.HomeActivity;
import com.bilpa.android.R;
import com.bilpa.android.adapters.ActivosAdapter;
import com.bilpa.android.model.Activo;
import com.bilpa.android.model.Estacion;
import com.bilpa.android.model.StatusVisita;
import com.bilpa.android.model.Visita;
import com.bilpa.android.services.ApiService;
import com.bilpa.android.services.AsyncCallback;
import com.bilpa.android.services.actions.BaseResult;
import com.bilpa.android.services.actions.GetActivoIdByQrResult;
import com.bilpa.android.services.actions.GetVisitaResult;
import com.bilpa.android.services.actions.visita.UpdateVisitaAction;
import com.bilpa.android.utils.Consts;
import com.bilpa.android.utils.Logger;
import com.bilpa.android.utils.MessageUtils;
import com.crashlytics.android.Crashlytics;
import com.mautibla.utils.ToastUtils;
import com.mautibla.utils.ViewUtils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class EstacionFragment extends SaveFragment implements AdapterView.OnItemClickListener, View.OnClickListener {


    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault());
    private SimpleDateFormat sdfSave = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());


    private ListView vListActivos;
    private Long mVisitaId;
    private TextView vEstacionName;
    private TextView vSchedule;

    private Visita visita;
    private Estacion mEstacion;

    private ImageView vSignPreview;
    private TextView vInit;
    private TextView vEnd;
    private Toolbar toolbarEstacion;
    private LinearLayout vFragContent;
    private ProgressBar vProgress;
    private Toolbar vToolbarActivos;

    private Bitmap mSign;
    private String mSignComment;
    private ActivosAdapter mActivosAdapter;


    public static EstacionFragment newInstance(Long visitaId, Estacion estacion) {
        EstacionFragment fragment = new EstacionFragment();
        Bundle args = new Bundle();
        fragment.mVisitaId = visitaId;
        fragment.mEstacion = estacion;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_estaciones, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_estacion, container, false);

        vFragContent = (LinearLayout) v.findViewById(R.id.vFragContent);
        vProgress = (ProgressBar) v.findViewById(R.id.vProgress);

        toolbarEstacion = (Toolbar) v.findViewById(R.id.toolbarEstacion);
        vToolbarActivos = (Toolbar) v.findViewById(R.id.vToolbarActivos);

        vListActivos = (ListView) v.findViewById(R.id.vListActivos);

        vEstacionName = (TextView) v.findViewById(R.id.vEstacionName);
        vSchedule = (TextView) v.findViewById(R.id.vSchedule);


        vInit = (TextView) v.findViewById(R.id.vInit);
        vEnd = (TextView) v.findViewById(R.id.vEnd);

        vInit.setOnClickListener(this);
        vEnd.setOnClickListener(this);

        vSignPreview = (ImageView) v.findViewById(R.id.vSignPreview);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (visita == null) {
            if (savedInstanceState != null && savedInstanceState.containsKey("visita")) {
                visita = (Visita) savedInstanceState.getSerializable("visita");
            }
        }

        if (mVisitaId == null) {
            if (savedInstanceState != null && savedInstanceState.containsKey("mVisitaId")) {
                mVisitaId = savedInstanceState.getLong("mVisitaId");
            }
        }

        if (mEstacion == null) {
            if (savedInstanceState != null && savedInstanceState.containsKey("mEstacion")) {
                mEstacion = (Estacion) savedInstanceState.getSerializable("mEstacion");
            }
        }


        Crashlytics.setString(Consts.CrashKeys.estacionNombre, mEstacion.nombre);


        ((HomeActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Toolbar toolbar = ((HomeActivity) getActivity()).getToolbar();
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });


        ViewUtils.gone(vFragContent, vProgress);
        ViewUtils.visible(vProgress);

        if (visita == null) {
            load();
        } else {
            bindVisita();
            setUnSavedData(false);
        }

        setUnSavedData(false);

    }

    private void load() {
        setTime(vInit, null);
        setTime(vEnd, null);

        ApiService.getVisitas(ApiService.GET_VISITA, mVisitaId, new AsyncCallback<GetVisitaResult>(getActivity()) {

            @Override
            protected void onSuccess(GetVisitaResult result) {
                visita = result.visita;
                bindVisita();
            }

            @Override
            public void onError(String msg, MaterialDialog.ButtonCallback callback) {
                getActivity().onBackPressed();
                super.onError(msg, callback);
            }

        });
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("visita", visita);
        outState.putLong("mVisitaId", mVisitaId);
        outState.putSerializable("mEstacion", mEstacion);
    }

    private void bindVisita() {

        StatusVisita status = StatusVisita.form(visita.estado);

        // Toolbar Visita
        bindToolbarVisita(status);

        // Toolbar activos
        bindToolbarActivos(status);

        // bind data
        ViewUtils.visible(vFragContent);
        ViewUtils.gone(vProgress);
        vEstacionName.setText(visita.nombreEstacion);
        vSchedule.setText(String.format(getString(R.string.visita_estacion_planificada), visita.fechaProximaVisita));

        // bind inicio
        if (visita.fechaInicio != null) {
            Calendar inicio = Calendar.getInstance();
            inicio.setTime(visita.fechaInicio);
            setTime(vInit, inicio);
        } else {
            setTime(vInit, null);
        }

        // bind fin
        if (visita.fechaFin != null) {
            Calendar fin = Calendar.getInstance();
            fin.setTime(visita.fechaFin);
            setTime(vEnd, fin);
        } else {
            setTime(vEnd, null);
        }

        // bind sign
        ImageLoader mImageLoader = BilpaApp.getInstance().getImageLoader();
        if (visita.signUrl != null) {

            mImageLoader.get(visita.signUrl, new ImageLoader.ImageListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    vSignPreview.setImageResource(R.drawable.ic_perm_media_white_24dp);
                }
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    if (response.getBitmap() != null) {
                        mSign = response.getBitmap();
                        vSignPreview.setImageBitmap(response.getBitmap());
                    } else {
                        vSignPreview.setImageResource(R.drawable.ic_perm_media_white_24dp);
                    }
                }
            });
        }

        mSignComment = visita.signComment;


        // bind activos
        List<Activo> activos = visita.activos;
        mActivosAdapter = new ActivosAdapter(getActivity(), activos);
        vListActivos.setAdapter(mActivosAdapter);
        vListActivos.setOnItemClickListener(this);
    }

    private void bindToolbarVisita(StatusVisita status) {
        int menu = status == StatusVisita.INICIADA ? R.menu.menu_estaciones_edit_iniciada : R.menu.menu_estaciones_edit_pendiente;
        toolbarEstacion.getMenu().clear();
        toolbarEstacion.inflateMenu(menu);
        toolbarEstacion.setOnMenuItemClickListener(this);
    }

    private void bindToolbarActivos(StatusVisita status) {
        vToolbarActivos.getMenu().clear();
        if (status == StatusVisita.INICIADA) {
            vToolbarActivos.inflateMenu(R.menu.menu_estaciones_activos);
            vToolbarActivos.setOnMenuItemClickListener(this);
        }
    }

    private void doInitVisita() {

        final Calendar now = Calendar.getInstance();
        String init = sdfSave.format(now.getTime());

        UpdateVisitaAction.Builder b = new UpdateVisitaAction.Builder();
        b.visitaId(mVisitaId);
        b.fechaInicio(init);
        b.estado(StatusVisita.INICIADA);

        ApiService.updateVisita(b, new AsyncCallback<BaseResult>(getActivity()) {
            @Override
            protected void onSuccess(BaseResult result) {
                visita.estado = StatusVisita.to(StatusVisita.INICIADA);
                bindToolbarVisita(StatusVisita.INICIADA);
                bindToolbarActivos(StatusVisita.INICIADA);
                setTime(vInit, now);
                setUnSavedData(false);
                ((HomeActivity) getActivity()).invalidateVisitasAsignadas();
            }
        });

    }

    private void doSaveVisita() {

        Calendar calInit = (Calendar) vInit.getTag();
        Calendar calEnd = (Calendar) vEnd.getTag();

        if (calEnd != null && calEnd.before(calInit)) {
            showMsg(R.string.visita_msg_fin_must_after_init);
            return;
        }

        UpdateVisitaAction.Builder b = new UpdateVisitaAction.Builder();
        b.visitaId(mVisitaId);
        b.fechaInicio(sdfSave.format(calInit.getTime()));
        if (calEnd != null) {
            b.fechaFin(sdfSave.format(calEnd.getTime()));
        }

        ApiService.updateVisita(b, new AsyncCallback<BaseResult>(getActivity()) {
            @Override
            protected void onSuccess(BaseResult result) {
                setUnSavedData(false);
                ToastUtils.showToast(getActivity(), R.string.visita_msg_saved);
                ((HomeActivity) getActivity()).invalidateVisitasAsignadas();
            }
        });


    }

    private void doFinishVisita() {

        Calendar end = (Calendar) vEnd.getTag();
        if (end == null) {
            showMsg(R.string.visita_msg_finalizar_select_end_data);
            return;
        }

        Calendar init = (Calendar) vInit.getTag();
        if (end.before(init)) {
            showMsg(R.string.visita_msg_fin_must_after_init);
            return;
        }

        goToReport();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        BilpaApp app = BilpaApp.getInstance();
        app.cancelPendingRequests(ApiService.GET_VISITA);
        app.cancelPendingRequests(ApiService.SAVE_VISITA);
        app.cancelPendingRequests(ApiService.GET_ACTIVO_BY_QR);
        app.cancelPendingRequests(ApiService.TAG_SET_QR_ACTIVO);

        Crashlytics.setString(Consts.CrashKeys.estacionNombre, null);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (StatusVisita.form(visita.estado) != StatusVisita.INICIADA) {
            showMsg(R.string.visita_msg_must_init);
            return;
        }

        if (StatusVisita.form(visita.estado) == StatusVisita.FINALIZADA) {
            showMsg(R.string.visita_msg_status_finish);
            return;
        }

        ActivosAdapter adapter = (ActivosAdapter) parent.getAdapter();
        Activo activo = adapter.getItem(position);
        ((HomeActivity) getActivity()).goToActivo(EstacionFragment.this, visita, activo, mEstacion);
    }

    @Override
    protected String getUnsavedExitMsg() {
        return getString(R.string.visita_exit_unsaved_msg);
    }

    @Override
    protected int[] assertMenuItemSavedCheck() {
        return null;
    }

    @Override
    protected int[] assertViewSavedCheck() {
        return null;
    }

    @Override
    protected void onActionClick(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.vReport:
                goToReport();
                break;

            case R.id.vInit:
                doInitVisita();
                break;

            case R.id.vSave:
                doSaveVisita();
                break;

            case R.id.vFinish:
                doFinishVisita();
                break;

            case R.id.vMenuQR:

                ((HomeActivity) getActivity()).showQR(new QRDialogFragment.QRListener() {
                    @Override
                    public void onQueryCode(final String code) {

                        int codeInt = 0;

                        try {
                            codeInt = Integer.valueOf(code);
                            ToastUtils.showToast(getActivity(), String.format(getString(R.string.visita_msg_readed_code), code));
                        } catch (NumberFormatException e) {
                            MessageUtils.showMsg(getActivity(), String.format(getString(R.string.visita_msg_qr_must_int), code));
                            return;
                        }


                        ((HomeActivity) getActivity()).showLoading();

                        ApiService.getActivoIdByQr(code, mEstacion.id, new AsyncCallback<GetActivoIdByQrResult>(getActivity()) {
                            @Override
                            protected void onSuccess(GetActivoIdByQrResult result) {
                                ((HomeActivity) getActivity()).hideLoading();

                                Logger.d(EstacionFragment.class.getSimpleName(), "QR Code readed : " + code);

                                if (result.data != -1) {
                                    // Activo encontrado

                                    int activoId = result.data;
                                    Activo activo = findActivoById(activoId);

                                    if (activo != null) {
                                        ((HomeActivity) getActivity()).goToActivo(EstacionFragment.this, visita, activo, mEstacion);
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
        }

    }

    private void goToReport() {

        /**
         * Bug https://www.fabric.io/mautibla/android/apps/com.bilpa.android/issues/56324c5bf5d3a7f76b2c0603/sessions/56a90ad900ad00010b6d971fc800004f
         *
         * In line
         *
         *              String initDate = sdfSave.format(init.getTime());
         */


        Crashlytics.log(Log.DEBUG, "EstacionFragment.goToReport", "La estacion es de nombre " + mEstacion.nombre);


        Calendar init = ((Calendar) vInit.getTag());
        Calendar end = (Calendar) vEnd.getTag();

        Long estacionId = mEstacion.id;
        long visitaId = visita.id;
        String initDate = sdfSave.format(init.getTime());
        String endDate = sdfSave.format(end.getTime());
        String visitaName = visita.nombreEstacion;
        String planificacion = visita.fechaProximaVisita;

        ((HomeActivity) getActivity()).goToReport(estacionId, visitaId, initDate, endDate, visitaName, planificacion);
    }

    @Override
    protected void onActionClick(View v) {
        if (StatusVisita.form(visita.estado) != StatusVisita.INICIADA) {
            showMsg(R.string.visita_msg_must_init);
            return;
        }

        if (StatusVisita.form(visita.estado) == StatusVisita.FINALIZADA) {
            showMsg(R.string.visita_msg_status_finish);
            return;
        }

        switch (v.getId()) {

            case R.id.vInit:
                pickDate((TextView) v);
                break;
            case R.id.vEnd:
                pickDate((TextView) v);
                break;
        }
    }

    private void pickDate(final TextView tView) {
        Calendar now = Calendar.getInstance();
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
                                        setUnSavedData(true);
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
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
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

    private Activo findActivoById(int activoId) {

        Long id = Long.valueOf(activoId);

        List<Activo> activos = visita.activos;
        for (Activo a : activos) {
            if (a.id == id.longValue()) {
                return a;
            }
        }
        return null;
    }


    private void showListActivos(final String qrCode) {
        ((HomeActivity) getActivity()).showQRSetActivo(visita.activos, new ActivoSelectDialogFragment.OnSelectActivoListener() {
            @Override
            public void onSelectActivo(final Activo activo) {
                activo.codigoQR = Integer.valueOf(qrCode);
                mActivosAdapter.notifyDataSetChanged();

                ApiService.setQrToActivo(activo.id, qrCode, new AsyncCallback<BaseResult>(getActivity()) {
                    @Override
                    protected void onSuccess(BaseResult result) {
                        ((HomeActivity) getActivity()).goToActivo(EstacionFragment.this, visita, activo, mEstacion);
                    }
                });
            }
        });
    }

    private void showMsg(int msg, MaterialDialog.ButtonCallback callback) {
        new MaterialDialog.Builder(getActivity())
                .title(com.bilpa.android.R.string.app_name)
                .iconRes(R.drawable.ic_launcher)
                .content(msg)
                .positiveText(R.string.btn_aceptar)
                .callback(callback)
                .show();
    }

    private void showAlert(int msg, MaterialDialog.ButtonCallback callback) {
        new MaterialDialog.Builder(getActivity())
                .title(com.bilpa.android.R.string.app_name)
                .iconRes(R.drawable.ic_launcher)
                .content(msg)
                .positiveText(R.string.btn_aceptar)
                .negativeText(R.string.btn_cancelar)
                .callback(callback)
                .show();
    }

    private void showMsg(int msg) {
        showMsg(msg, null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == HomeActivity.REQUEST_CODE_SHOW_ACTIVO && resultCode == Activity.RESULT_OK) {
            Activo activo = (Activo) data.getSerializableExtra("activo");
            if (activo != null) {
                int index = visita.activos.indexOf(activo);
                visita.activos.remove(index);
                visita.activos.add(index, activo);
                mActivosAdapter.notifyDataSetChanged();
            }


            boolean reloadVisita = data.getBooleanExtra("reloadVisita", true);
            if (reloadVisita) {

                ApiService.getVisitas(ApiService.GET_VISITA, mVisitaId, new AsyncCallback<GetVisitaResult>(getActivity()) {
                    @Override
                    protected void onSuccess(GetVisitaResult result) {
                        visita.activos = result.visita.activos;

                        // bind activos
                        List<Activo> activos = visita.activos;
                        mActivosAdapter = new ActivosAdapter(getActivity(), activos);
                        vListActivos.setAdapter(mActivosAdapter);
                        vListActivos.setOnItemClickListener(EstacionFragment.this);

                    }

                    @Override
                    public void onError(String msg, MaterialDialog.ButtonCallback callback) {
                        getActivity().onBackPressed();
                        super.onError(msg, callback);
                    }
                });



            }

        }
    }
}
