package com.bilpa.android;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bilpa.android.fragments.CommentsFragment;
import com.bilpa.android.fragments.CorregidoDetailFragment;
import com.bilpa.android.fragments.CorregidosListFragment;
import com.bilpa.android.fragments.MangueraDetailFragment;
import com.bilpa.android.fragments.ManguerasListFragment;
import com.bilpa.android.fragments.PendienteDetailFragment;
import com.bilpa.android.fragments.PendientesFragment;
import com.bilpa.android.fragments.PicoSelectDialogFragment;
import com.bilpa.android.fragments.QRDialogFragment;
import com.bilpa.android.fragments.SaveFragment;
import com.bilpa.android.model.Activo;
import com.bilpa.android.model.Chequeo;
import com.bilpa.android.model.Corregido;
import com.bilpa.android.model.Estacion;
import com.bilpa.android.model.Pendiente;
import com.bilpa.android.model.Pico;
import com.bilpa.android.model.Visita;
import com.bilpa.android.services.actions.GetChequeoSurtidorPicoResult;
import com.bilpa.android.services.actions.PicosResult;
import com.bilpa.android.widgets.OnAddEditCorregidos;
import com.bilpa.android.widgets.OnAddEditPendientes;
import com.bilpa.android.widgets.OnChequeoListener;
import com.bilpa.android.widgets.ShowHideAddButton;
import com.mautibla.utils.ViewUtils;

import java.util.List;

public class MangueraActivity extends BaseActivity implements View.OnClickListener, ShowHideAddButton, OnAddEditPendientes, OnAddEditCorregidos, OnChequeoListener {

    public static final String TAG = "MangueraActivity";

    public static final String FRAG_LEFT = "fragLeft";
    public static final String FRAG_RIGHT = "fragRight";
    public static final String TAG_CORREGIDOS           = "TAG_CORREGIDOS";
    public static final String TAG_PENDIENTES           = "TAG_PENDIENTES";
    public static final String TAG_COMMENTS             = "TAG_COMMENTS";
    public static final String TAG_CORREGIDO_DETAIL     = "TAG_CORREGIDO_DETAIL";
    public static final String TAG_PENDIENTE_DETAIL     = "TAG_PENDIENTE_DETAIL";

    private Toolbar toolbar;
    private LinearLayout vPanelLeft;
    private LinearLayout vPanelRight;

    public PicosResult mPicosResult;

    private Long mSurtidorId;
    private Long mIdPreventivo;
    private Activo mActivo;
    public Visita mVisita;
    public Estacion mEstacion;
    private ImageButton vBtnAddCorregido;

    public GetChequeoSurtidorPicoResult mChequeoResult;

    public boolean mResultReloadVisita = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manguera);


        mVisita = (Visita) getIntent().getSerializableExtra("mVisita");
        mActivo = (Activo) getIntent().getSerializableExtra("mActivo");
        mEstacion = (Estacion) getIntent().getSerializableExtra("mEstacion");


        mSurtidorId = (Long) getIntent().getSerializableExtra("mSurtidorId");
        mIdPreventivo = (Long) getIntent().getSerializableExtra("mIdPreventivo");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setLogoDescription("");
        TextView vToolbarTitle = (TextView) findViewById(R.id.vToolbarTitle);
        TextView vToolbarDesc = (TextView) findViewById(R.id.vToolbarDesc);
        vToolbarTitle.setText(mActivo.descripcion);
        vToolbarDesc.setText("Mangueras");


        vPanelLeft = (LinearLayout) findViewById(R.id.vPanelLeft);
        vPanelRight = (LinearLayout) findViewById(R.id.vPanelRight);
        vBtnAddCorregido = (ImageButton) findViewById(R.id.vBtnAdd);
        vBtnAddCorregido.setOnClickListener(this);

        if (savedInstanceState == null) {
            goToChild(ManguerasListFragment.newInstance(mSurtidorId), R.id.vPanelLeft, FRAG_LEFT);

        } else {
            mPicosResult = (PicosResult) savedInstanceState.getSerializable("mPicosResult");
        }

        if (savedInstanceState != null) {
            mResultReloadVisita = savedInstanceState.getBoolean("mResultReloadVisita", false);
        } else {
            mResultReloadVisita = false;
        }

        hideAddBtn();


    }

    public Toolbar getToolbar() {
        return toolbar;
    }


    public void onBackPressedImpl() {
        Fragment rFrag = peekFragment();
        if (rFrag != null) {
            if (rFrag != null && SaveFragment.class.isInstance(rFrag)) {
                final SaveFragment sf = (SaveFragment) rFrag;
                if (sf.isUnSavedData()) {
                    sf.showLeftMsg(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            sf.setUnSavedData(false);
                            onBackPressedImpl2();
                        }
                    });
                    return;
                }
            }
        }
        onBackPressedImpl2();
    }

    public void onBackPressedImpl2() {

        if (mResultReloadVisita) {
            Intent intent = new Intent();
            intent.putExtra("reloadVisita", true);
            setResult(RESULT_OK, intent);
        }

        if (TAG_CORREGIDOS.equals(peekTag())) {
            hideAddBtn();
            invalidateChequeos();
            getFragManager().popBackStack();
            return;

        } else if (TAG_CORREGIDO_DETAIL.equals(peekTag())) {
            showAddBtn();
            getFragManager().popBackStack();
            return;

        } else if (TAG_COMMENTS.equals(peekTag())) {
            hideAddBtn();
            getFragManager().popBackStack();
            return;

        } else if (TAG_PENDIENTES.equals(peekTag())) {
            PendientesFragment pf = (PendientesFragment) peekFragment();
            invalidateChequeos();
            hideAddBtn();
            getFragManager().popBackStack();
            return;

        } else if (TAG_PENDIENTE_DETAIL.equals(peekTag())) {
            showAddBtn();
            getFragManager().popBackStack();
            return;
        }

        finish();
    }

    private void invalidateChequeos() {
        mChequeoResult = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            // TODO santilod. Falta ver que hacer con el btn de pendientes

            case R.id.vBtnAdd:

                Fragment current = peekFragment();
                if (current != null) {

                    if (PendientesFragment.class.isInstance(current)) {
                        goToAddPendiente();
                        return;
                    }

                    if (CorregidosListFragment.class.isInstance(current)) {
                        goToAddCorregido();
                        return;
                    }

                    if (CommentsFragment.class.isInstance(current)) {
                        CommentsFragment cf = (CommentsFragment) current;
                        cf.showAddComment();
                        return;
                    }
                }
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("mPicosResult", mPicosResult);
        outState.putBoolean("mResultReloadVisita", mResultReloadVisita);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        return true;
    }

    private void goToChild(Fragment f, int container, String tag) {
        FragmentManager fm = getFragManager();
        FragmentTransaction t = fm.beginTransaction();
        t.replace(container, f, tag);
        t.addToBackStack(tag);
        t.commit();
    }

    public void showQR(QRDialogFragment.QRListener qrListener) {
        FragmentManager fragmentManager = getFragManager();
        QRDialogFragment newFragment = new QRDialogFragment();
        newFragment.setQRListener(qrListener);
        newFragment.show(fragmentManager, "qr");
    }

    public void goToMangueraDetail(final int picoIndex, final Pico pico, MaterialDialog.ButtonCallback callbackOnUnsaved) {
        Fragment rFrag = findFragmentByTag(FRAG_RIGHT);
        if (rFrag != null) {
            if (rFrag != null && SaveFragment.class.isInstance(rFrag)) {
                final SaveFragment sf = (SaveFragment) rFrag;
                if (sf.isUnSavedData()) {
                    sf.showLeftMsg(callbackOnUnsaved);
                    return;
                }
            }
        }
        goToMangueraDetailImpl(picoIndex, pico);
    }

    public void goToMangueraDetail(final int picoIndex, final Pico pico) {

        Fragment rFrag = findFragmentByTag(FRAG_RIGHT);
        if (rFrag != null) {
            if (rFrag != null && SaveFragment.class.isInstance(rFrag)) {
                final SaveFragment sf = (SaveFragment) rFrag;
                if (sf.isUnSavedData()) {
                    sf.showLeftMsg(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            sf.setUnSavedData(false);
                            goToMangueraDetailImpl(picoIndex, pico);
                        }

                        @Override
                        public void onNegative(MaterialDialog dialog) {

                            Fragment lFrag = findFragmentByTag(FRAG_LEFT);
                            if (lFrag == null) {
                                return;
                            }

                            Fragment rFrag = findFragmentByTag(FRAG_RIGHT);
                            if (rFrag == null) {
                                return;
                            }

                            ManguerasListFragment listFragment = (ManguerasListFragment) lFrag;
                            MangueraDetailFragment detailFragment = (MangueraDetailFragment) rFrag;

                            listFragment.setActivated(detailFragment.mPicoIndex-1);

                        }
                    });
                    return;
                }
            }
        }
        goToMangueraDetailImpl(picoIndex, pico);
    }

    private void goToMangueraDetailImpl(int picoIndex, Pico pico) {

        mChequeoResult = null;
        hideAddBtn();
        backToTag(FRAG_LEFT);


        MangueraDetailFragment f = MangueraDetailFragment.newInstance(picoIndex, mIdPreventivo, pico);
        f.setOnChequeoListener(this);
        goToChild(f, R.id.vPanelRight, FRAG_RIGHT);
    }

    private Fragment findFragmentByTag(String tag) {
        FragmentManager fm = getFragManager();
        Fragment f = fm.findFragmentByTag(tag);
        return f;
    }

    public void showPicos(PicoSelectDialogFragment.OnPicoSelectedListener onPicoSelectedListener) {
        FragmentManager fm = getFragManager();
        PicoSelectDialogFragment f = PicoSelectDialogFragment.newInstance(mActivo.id);
        f.setOnPicoSelectedListener(onPicoSelectedListener);
        f.show(fm, "picos_dialog");
    }

    public void updatePico(Pico pico) {

        Fragment lFrag = findFragmentByTag(FRAG_LEFT);
        if (lFrag != null) {
            if (ManguerasListFragment.class.isInstance(lFrag)) {
                ManguerasListFragment listFragment = (ManguerasListFragment) lFrag;
                listFragment.updatePico(pico);
            }
        }

    }

    @Override
    public long getIdActivo() {
        return mActivo.id;
    }

    public Long getIdPreventivo() {
        return mIdPreventivo;
    }

    public void goToComments(Long chequeoId, String nombreChequeo) {
        showAddBtn();
        goToChild(CommentsFragment.newInstance(chequeoId, nombreChequeo), R.id.vPanelRight, TAG_COMMENTS);
    }

    public void goToPendientes() {
        showAddBtn();
        goToChild(PendientesFragment.newInstance(), R.id.vPanelRight, TAG_PENDIENTES);
    }

    @Override
    public void goToCorregidos() {
        showAddBtn();
        goToChild(CorregidosListFragment.newInstance(), R.id.vPanelRight, TAG_CORREGIDOS);
    }

    @Override
    public void goToAddCorregido() {
        hideAddBtn();
        goToChild(CorregidoDetailFragment.newInstance(), R.id.vPanelRight, TAG_CORREGIDO_DETAIL);
    }

    @Override
    public void goToEditCorregido(Corregido corregido) {
        hideAddBtn();
        goToChild(CorregidoDetailFragment.newInstance(corregido), R.id.vPanelRight, TAG_CORREGIDO_DETAIL);
    }

    @Override
    public void goToRepairPendinete(Pendiente pendiente) {
        hideAddBtn();
        goToFragment(CorregidoDetailFragment.newInstance(pendiente), TAG_CORREGIDO_DETAIL);
    }

    @Override
    public void goToAddPendiente() {
        hideAddBtn();
        goToChild(PendienteDetailFragment.newInstance(), R.id.vPanelRight, TAG_PENDIENTE_DETAIL);
    }

    @Override
    public void goToEditPendiente(Pendiente pendiente) {
        hideAddBtn();
        goToChild(PendienteDetailFragment.newInstance(pendiente), R.id.vPanelRight, TAG_PENDIENTE_DETAIL);
    }

    @Override
    public void invalidatePendientes() {
        Fragment f = getFragManager().findFragmentByTag(TAG_PENDIENTES);
        if (PendientesFragment.class.isInstance(f)) {
            PendientesFragment pf = (PendientesFragment) f;
            pf.invalidatePendientes();
        }
    }

    @Override
    public void invalidateCorregidos() {
        Fragment f = getFragManager().findFragmentByTag(TAG_CORREGIDOS);
        if (CorregidosListFragment.class.isInstance(f)) {
            CorregidosListFragment cf = (CorregidosListFragment) f;
            cf.invalidateCorregidos();
        }
    }

    @Override
    public void showAddBtn() {
        ViewUtils.visible(vBtnAddCorregido);
    }

    @Override
    public void hideAddBtn() {
        ViewUtils.gone(vBtnAddCorregido);
    }

    @Override
    public void onSaveChequeo() {
        mResultReloadVisita = true;
    }

    @Override
    public List<Chequeo> getChequeos() {
        return mChequeoResult.getChequeos();
    }

    @Override
    public Chequeo getChequeoById(long chequeoId) {
        return mChequeoResult.getChequeoById(chequeoId);
    }
}
