package com.bilpa.android;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.bilpa.android.fragments.PendienteDetailCorregidoFragment;
import com.bilpa.android.model.Pendiente;
import com.bilpa.android.model.correctivos.CActivo;
import com.bilpa.android.model.correctivos.Correctivo;

public class CorrectivoPendienteActivity extends BaseSupportActivity {

    private static final String TAG_PENDIENTE_DETAIL = "TAG_PENDIENTE_DETAIL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_correctivo_pendiente_detail);

        Correctivo mCorrectivo = (Correctivo) getIntent().getSerializableExtra("correctivo");
        CActivo mActivo = (CActivo) getIntent().getSerializableExtra("activo");
        Pendiente pendiente = (Pendiente) getIntent().getSerializableExtra("pendiente");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.actionbar_logo);
        toolbar.setTitle(mActivo.display);
        toolbar.setSubtitle(mCorrectivo.estacion);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.vContainer, PendienteDetailCorregidoFragment.newInstance(mCorrectivo, mActivo, pendiente), TAG_PENDIENTE_DETAIL);
            ft.addToBackStack(TAG_PENDIENTE_DETAIL);
            ft.commit();
        }
    }

    @Override
    public void onBackPressedImpl() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
