package com.bilpa.android;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.bilpa.android.fragments.SolucionDetailFragment;
import com.bilpa.android.model.Pendiente;
import com.bilpa.android.model.Solucion;
import com.bilpa.android.model.correctivos.CActivo;
import com.bilpa.android.model.correctivos.Correctivo;

public class SolucionDetailActivity extends BaseSupportActivity {

    private static final String TAG_SOLUCION_DETAIL = "TAG_SOLUCION_DETAIL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solucion_detail2);

        Correctivo mCorrectivo = (Correctivo) getIntent().getSerializableExtra("correctivo");
        CActivo mActivo = (CActivo) getIntent().getSerializableExtra("activo");
        Solucion solucion = (Solucion) getIntent().getSerializableExtra("solucion");

        Pendiente pendiente = null;
        if (getIntent().hasExtra("pendiente")) {
            pendiente = (Pendiente) getIntent().getSerializableExtra("pendiente");
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.actionbar_logo);
        toolbar.setTitle(mActivo.display);
        toolbar.setSubtitle(mCorrectivo.estacion);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            SolucionDetailFragment sdf = SolucionDetailFragment.newInstance(mActivo, mCorrectivo, solucion);
            sdf.setPendienteSolved(pendiente);
            ft.add(R.id.vContainer, sdf, TAG_SOLUCION_DETAIL);
            ft.addToBackStack(TAG_SOLUCION_DETAIL);
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
