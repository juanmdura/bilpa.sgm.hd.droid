package com.bilpa.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.bilpa.android.fragments.ActivoSelectDialogFragment;
import com.bilpa.android.fragments.EstacionFragment;
import com.bilpa.android.fragments.HomeFragment;
import com.bilpa.android.fragments.QRDialogFragment;
import com.bilpa.android.fragments.ReportFragment;
import com.bilpa.android.fragments.SignDialogFragment2;
import com.bilpa.android.model.Activo;
import com.bilpa.android.model.Estacion;
import com.bilpa.android.model.Visita;
import com.bilpa.android.services.actions.GetVisitasResult;

import java.util.List;

public class HomeActivity extends BaseActivity {

    public static final String TAG = "HomeActivity";

    public static final String TAG_ESTACION_DETAIL = "TAG_ESTACION_DETAIL";
    public static final String TAG_HOME = "TAG_HOME";
    public static final String TAG_REPORT = "TAG_REPORT";

    public static final int REQUEST_CODE_SHOW_ACTIVO = 100;

    private Toolbar toolbar;

    public GetVisitasResult mVisitasAsignadas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.actionbar_logo);
        getSupportActionBar().setTitle(R.string.preventivos_ab_title);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, CorrectivosActivity.class));
                finish();
            }
        });

        if (savedInstanceState == null) {
            goToHome();
        } else {
            if (mVisitasAsignadas == null) {
                if (savedInstanceState != null && savedInstanceState.containsKey("visitasAsignadas")) {
                    mVisitasAsignadas = (GetVisitasResult) savedInstanceState.getSerializable("visitasAsignadas");
                }
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("visitasAsignadas", mVisitasAsignadas);
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void goToHome() {
        goToFragment(HomeFragment.newInstance(), TAG_HOME);
    }

    public void goToEstacionDetail(Long visitaId, Estacion estacion) {


        AppBarLayout vAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        vAppBarLayout.setExpanded(false);

        goToFragment(EstacionFragment.newInstance(visitaId, estacion), TAG_ESTACION_DETAIL);
    }

    public void goToActivo(Fragment f, Visita visita, Activo activo, Estacion estacion) {
        Intent i = new Intent(this, ActivoActivity.class);
        i.putExtra("visita", visita);
        i.putExtra("activo", activo);
        i.putExtra("mEstacion", estacion);
        f.startActivityForResult(i, REQUEST_CODE_SHOW_ACTIVO);
    }

    public void goToReport(Long estacionId, long visitaId, String initDate, String endDate, String visitaName, String planificacion) {
        goToFragment(ReportFragment.newInstance(estacionId, visitaId, initDate, endDate, visitaName, planificacion), TAG_REPORT);
    }

    public void onBackPressedImpl() {
        if (peekTag().equals(TAG_HOME)) {
            finish();
        } else {
            getFragManager().popBackStack();
        }
    }

    public void showQR(QRDialogFragment.QRListener qrListener) {
        FragmentManager fragmentManager = getFragManager();
        QRDialogFragment newFragment = new QRDialogFragment();
        newFragment.setQRListener(qrListener);
        newFragment.show(fragmentManager, "qr");
    }

    public void showQRSetActivo(List<Activo> activos, ActivoSelectDialogFragment    .OnSelectActivoListener onSelectActivoListener) {
        FragmentManager fm = getFragManager();
        ActivoSelectDialogFragment f = ActivoSelectDialogFragment.newInstance(activos);
        f.setOnSelectActivoListener(onSelectActivoListener);
        f.show(fm, "showActivos");
    }

    public void showSignDialog(String comment, SignDialogFragment2.onSignListener onSignedListener) {
        FragmentManager fragmentManager = getFragManager();
        SignDialogFragment2 newFragment = SignDialogFragment2.newInstance(comment);
        newFragment.setOnSignedListener(onSignedListener);
        newFragment.show(fragmentManager, "sign");
    }

    public void invalidateVisitasAsignadas() {
        mVisitasAsignadas = null;
    }
}
