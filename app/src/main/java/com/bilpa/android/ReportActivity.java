package com.bilpa.android;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.bilpa.android.fragments.ReportCorrectivoFragment;
import com.bilpa.android.model.correctivos.Correctivo;

import java.util.Date;

public class ReportActivity extends AppCompatActivity {

    public static final String TAG_REPORT = "TAG_REPORT";

    private Correctivo mCorrectivo;
    public Toolbar toolbar;
    private Date mInicio;
    private Date mFin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report2);

        mCorrectivo = (Correctivo) getIntent().getSerializableExtra("correctivo");
        mInicio = (Date) getIntent().getSerializableExtra("inicio");
        mFin = (Date) getIntent().getSerializableExtra("fin");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.actionbar_logo);
        getSupportActionBar().setTitle(mCorrectivo.estacion);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.container, ReportCorrectivoFragment.newInstance(mCorrectivo, mInicio, mFin), TAG_REPORT);
            ft.addToBackStack(TAG_REPORT);
            ft.commit();
        }


    }
    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
