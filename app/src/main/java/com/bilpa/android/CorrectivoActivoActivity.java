package com.bilpa.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.bilpa.android.fragments.PendientesCorrectivosFragment;
import com.bilpa.android.fragments.SolucionesFragment;
import com.bilpa.android.model.correctivos.CActivo;
import com.bilpa.android.model.correctivos.Correctivo;
import com.bilpa.android.utils.Consts;
import com.crashlytics.android.Crashlytics;

public class CorrectivoActivoActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    private Correctivo mCorrectivo;
    private CActivo mActivo;

    public boolean mUpdateCorredigoOnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_correctivo_activo);

        mCorrectivo = (Correctivo) getIntent().getSerializableExtra("correctivo");
        mActivo = (CActivo) getIntent().getSerializableExtra("activo");

        Crashlytics.setString(Consts.CrashKeys.activo, mActivo.display);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.actionbar_logo);
        toolbar.setTitle(mActivo.display);
        toolbar.setSubtitle(mCorrectivo.estacion);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton vFabAdd = (FloatingActionButton) findViewById(R.id.vFabAdd);
        vFabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mViewPager.getCurrentItem() == 0) {
                    Intent intent = new Intent(CorrectivoActivoActivity.this, SolucionDetailActivity.class);
                    intent.putExtra("correctivo", mCorrectivo);
                    intent.putExtra("activo", mActivo);
                    Fragment f = mSectionsPagerAdapter.getActiveFragment(mViewPager, 0);
                    if (f != null) {
                        f.startActivityForResult(intent, Consts.Request.CORRECTIVOS_SOLUCION);
                    }

                } else {
                    // Add Pendiente
                    Intent intent = new Intent(CorrectivoActivoActivity.this, CorrectivoPendienteActivity.class);
                    intent.putExtra("correctivo", mCorrectivo);
                    intent.putExtra("activo", mActivo);
                    Fragment f = mSectionsPagerAdapter.getActiveFragment(mViewPager, 1);
                    if (f != null) {
                        f.startActivityForResult(intent, Consts.Request.CORRECTIVOS_PENDIENTE);
                    }
                }
            }
        });

        if (savedInstanceState != null && savedInstanceState.containsKey("mUpdateCorredigoOnBack")) {
            mUpdateCorredigoOnBack = savedInstanceState.getBoolean("mUpdateCorredigoOnBack", false);
        }


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("mUpdateCorredigoOnBack", mUpdateCorredigoOnBack);
    }

    @Override
    public void onBackPressed() {
        if (mUpdateCorredigoOnBack) {
            setResult(RESULT_OK);
        }
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateSoluciones() {
        Fragment f = mSectionsPagerAdapter.getActiveFragment(mViewPager, 0);
        if (f != null) {
            ((SolucionesFragment) f).updateCorregidos();
        }
    }

    public void updatePendientes() {
        Fragment f = mSectionsPagerAdapter.getActiveFragment(mViewPager, 1);
        if (f != null) {
            ((PendientesCorrectivosFragment) f).updatePendientes();
        }
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return SolucionesFragment.newInstance(mCorrectivo, mActivo);
            } else {
                return PendientesCorrectivosFragment.newInstance(mCorrectivo, mActivo);
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.correctivo_actvio_tab_soluciones);
                case 1:
                    return getString(R.string.correctivo_activo_tab_pendientes);
            }
            return null;
        }

        public Fragment getActiveFragment(ViewPager container, int position) {
            String name = makeFragmentName(container.getId(), getItemId(position));
            return getSupportFragmentManager().findFragmentByTag(name);
        }

        private String makeFragmentName(int viewId, long index) {
            return "android:switcher:" + viewId + ":" + index;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Crashlytics.setString(Consts.CrashKeys.activo, null);
    }
}
