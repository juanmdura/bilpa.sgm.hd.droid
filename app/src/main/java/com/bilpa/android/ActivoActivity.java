package com.bilpa.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bilpa.android.fragments.CheckBombaFragment;
import com.bilpa.android.fragments.CheckGenericFragment;
import com.bilpa.android.fragments.CheckSurtidorFragment;
import com.bilpa.android.fragments.CheckTanqueFragment;
import com.bilpa.android.fragments.CommentsFragment;
import com.bilpa.android.fragments.CorregidoDetailFragment;
import com.bilpa.android.fragments.CorregidosListFragment;
import com.bilpa.android.fragments.PendienteDetailFragment;
import com.bilpa.android.fragments.PendientesFragment;
import com.bilpa.android.fragments.QRDialogFragment;
import com.bilpa.android.fragments.SaveFragment;
import com.bilpa.android.model.Activo;
import com.bilpa.android.model.Chequeo;
import com.bilpa.android.model.Corregido;
import com.bilpa.android.model.Estacion;
import com.bilpa.android.model.Pendiente;
import com.bilpa.android.model.Visita;
import com.bilpa.android.services.actions.BaseChequeosResult;
import com.bilpa.android.services.actions.GetChequeoBombaResult;
import com.bilpa.android.services.actions.GetChequeoGenericoResult;
import com.bilpa.android.services.actions.GetChequeoSurtidorResult;
import com.bilpa.android.services.actions.GetChequeoTanqueResult;
import com.bilpa.android.utils.Consts;
import com.bilpa.android.widgets.OnAddEditCorregidos;
import com.bilpa.android.widgets.OnAddEditPendientes;
import com.bilpa.android.widgets.OnChequeoListener;
import com.bilpa.android.widgets.ShowHideAddButton;
import com.crashlytics.android.Crashlytics;
import com.mautibla.utils.ViewUtils;

import java.util.List;

public class ActivoActivity extends BaseActivity implements View.OnClickListener,
        ShowHideAddButton, OnAddEditPendientes, OnAddEditCorregidos,
        OnChequeoListener {

    public static final String TAG = "HomeActivity";

    public static final String TAG_CHEQUEO              = "TAG_CHEQUEO";
    public static final String TAG_CORREGIDOS           = "TAG_CORREGIDOS";
    public static final String TAG_PENDIENTES           = "TAG_PENDIENTES";
    public static final String TAG_COMMENTS             = "TAG_COMMENTS";
    public static final String TAG_CORREGIDO_DETAIL     = "TAG_CORREGIDO_DETAIL";
    public static final String TAG_PENDIENTE_DETAIL     = "TAG_PENDIENTE_DETAIL";

    public static final int REQUEST_CODE_SHOW_MANGUERAS = 101;
    public static final int REQUEST_CODE_SHOW_PRODUCTOS = 102;


    private Toolbar toolbar;
    private TextView vToolbarTitle;
    private ImageButton vBtnAddCorregido;

    public Visita mVisita;
    public Activo mActivo;
    public Estacion mEstacion;

    // TODO .. Armar una abstract class que sea parde de todos los chequeos, y mantener una sola refenrencia.
    public GetChequeoSurtidorResult mChequeoSurtidorResult;
    public GetChequeoTanqueResult mChequeoTanqueResult;
    public GetChequeoBombaResult mChequeoBombaResult;
    public GetChequeoGenericoResult mChequeoGenericoResult;

    public boolean mUpdated = false;

    public boolean mResultReloadVisita = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activo);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setLogoDescription("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        vToolbarTitle = (TextView) findViewById(R.id.vToolbarTitle);

        vBtnAddCorregido = (ImageButton) findViewById(R.id.vBtnAdd);
        vBtnAddCorregido.setOnClickListener(this);

        if (savedInstanceState != null) {
            mActivo = (Activo) savedInstanceState.getSerializable("mActivo");
            mVisita = (Visita) savedInstanceState.getSerializable("mVisita");
            mEstacion = (Estacion) savedInstanceState.getSerializable("mEstacion");

            mChequeoSurtidorResult = (GetChequeoSurtidorResult) savedInstanceState.getSerializable("mChequeoSurtidorResult");
            mChequeoTanqueResult = (GetChequeoTanqueResult) savedInstanceState.getSerializable("mChequeoTanqueResult");
            mChequeoBombaResult = (GetChequeoBombaResult) savedInstanceState.getSerializable("mChequeoBombaResult");

        }

        if (savedInstanceState == null) {
            mVisita = (Visita) getIntent().getSerializableExtra("visita");
            mActivo = (Activo) getIntent().getSerializableExtra("activo");
            mEstacion = (Estacion) getIntent().getSerializableExtra("mEstacion");

            hideAddBtn();
            goToInit();
        }

        if (mActivo != null) {
            Crashlytics.setString(Consts.CrashKeys.activo, mActivo.descripcion);
        } else {
            Crashlytics.setString(Consts.CrashKeys.activo, "null");
        }



        if (savedInstanceState != null) {
            mResultReloadVisita = savedInstanceState.getBoolean("mResultReloadVisita", false);
        } else {
            mResultReloadVisita = false;
        }


        vToolbarTitle.setText(mActivo.descripcion);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("mActivo", mActivo);
        outState.putSerializable("mVisita", mVisita);
        outState.putSerializable("mEstacion", mEstacion);
        outState.putSerializable("mChequeoSurtidorResult", mChequeoSurtidorResult);
        outState.putSerializable("mChequeoTanqueResult", mChequeoTanqueResult);
        outState.putSerializable("mChequeoBombaResult", mChequeoBombaResult);
        outState.putSerializable("mChequeoGenericoResult", mChequeoGenericoResult);
        outState.putBoolean("mResultReloadVisita", mResultReloadVisita);
    }

    public Toolbar getToolbar() {
        return toolbar;
    }


   public void onBackPressedImpl() {

        if (mUpdated || mResultReloadVisita) {
            Intent intent = new Intent();
            if (mUpdated) {
                intent.putExtra("activo", mActivo);
            }
            if (mResultReloadVisita) {
                intent.putExtra("reloadVisita", true);
            }
            setResult(RESULT_OK, intent);
        }

        if (TAG_CHEQUEO.equals(peekTag())) {
            finish();


        } else if (TAG_COMMENTS.equals(peekTag())) {
            hideAddBtn();
            getFragManager().popBackStack();

        } else if (TAG_CORREGIDOS.equals(peekTag())) {
            hideAddBtn();
            invalidateChequeos();
            getFragManager().popBackStack();

        } else if (TAG_PENDIENTES.equals(peekTag())) {
            hideAddBtn();
            invalidateChequeos();
            getFragManager().popBackStack();

        } else if (TAG_PENDIENTE_DETAIL.equals(peekTag())) {
            showAddBtn();
            getFragManager().popBackStack();

        } else {
            getFragManager().popBackStack();
        }

    }

    @Override
    public void onClick(final View v) {

        Fragment f = peekFragment();
        if (f != null && SaveFragment.class.isInstance(f)) {
            final SaveFragment sf = (SaveFragment) f;
            if (sf.isUnSavedData()) {
                sf.showLeftMsg(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        onClickImpl(v);
                    }
                });
                return;
            }
        }
        onClickImpl(v);
    }

    private void onClickImpl(View v) {
        switch (v.getId()) {

            case R.id.vBtnAdd:

                Fragment current = peekFragment();
                if (current != null) {

                    if (PendientesFragment.class.isInstance(current)) {
                        goToAddPendiente();
                        return;
                    }

                    if (CorregidosListFragment.class.isInstance(current)) {
                        CorregidosListFragment cf = (CorregidosListFragment) current;
                        goToAddCorregido();
                        return;
                    }

                    if (CommentsFragment.class.isInstance(current)) {
                        CommentsFragment cf = (CommentsFragment) current;
                        cf.showAddComment();
                    }
                }
                break;
        }
    }

    private void goToInit() {
        Integer type = Integer.valueOf(mActivo.tipoId);
        switch (type) {
            case Activo.TYPE_SURTIDOR:
                goToChequeoSurtidor();
                break;
            case Activo.TYPE_TANQUE:
                goToChequeoTanque();
                break;
            case Activo.TYPE_BOMBA:
                goToChequeoBomba();
                break;
            default:
                // En caso de que el tipo se diferente Surtidor, tanque o manguera,
                // entonces se intenta cargar un activo generico.
                goToChequeoGenerico();
                break;

        }

    }

    public void hideAddBtn() {
        ViewUtils.gone(vBtnAddCorregido);
    }

    public void showAddBtn() {
        ViewUtils.visible(vBtnAddCorregido);
    }

    public void goToChequeoSurtidor() {
        CheckSurtidorFragment f = CheckSurtidorFragment.newInstance();
        f.setOnChequeoListener(this);
        goToFragment(f, TAG_CHEQUEO);
    }

    public void goToChequeoTanque() {
        CheckTanqueFragment f = CheckTanqueFragment.newInstance();
        f.setOnChequeoListener(this);
        goToFragment(f, TAG_CHEQUEO);
    }

    public void goToChequeoBomba() {
        CheckBombaFragment f = CheckBombaFragment.newInstance();
        f.setOnChequeoListener(this);
        goToFragment(f, TAG_CHEQUEO);
    }

    private void goToChequeoGenerico() {
        CheckGenericFragment f = CheckGenericFragment.newInstance();
        f.setOnChequeoListener(this);
        goToFragment(f, TAG_CHEQUEO);
    }

    @Override
    public void goToCorregidos() {
        showAddBtn();
        goToFragment(CorregidosListFragment.newInstance(), TAG_CORREGIDOS);
    }

    public void goToPendientes() {
        showAddBtn();
        goToFragment(PendientesFragment.newInstance(), TAG_PENDIENTES);
    }

    public void goToComments(Long chequeoId, String nombreChequeo) {
        showAddBtn();
        goToFragment(CommentsFragment.newInstance(chequeoId, nombreChequeo), TAG_COMMENTS);
    }

    @Override
    public void goToAddCorregido() {
        hideAddBtn();
        goToFragment(CorregidoDetailFragment.newInstance(), TAG_CORREGIDO_DETAIL);
    }

    @Override
    public void goToEditCorregido(Corregido corregido) {
        hideAddBtn();
        goToFragment(CorregidoDetailFragment.newInstance(corregido), TAG_CORREGIDO_DETAIL);
    }

    @Override
    public void goToRepairPendinete(Pendiente pendiente) {
        hideAddBtn();
        goToFragment(CorregidoDetailFragment.newInstance(pendiente), TAG_CORREGIDO_DETAIL);
    }

    @Override
    public void goToAddPendiente() {
        hideAddBtn();
        goToFragment(PendienteDetailFragment.newInstance(), TAG_PENDIENTE_DETAIL);
    }

    public void goToEditPendiente(Pendiente pendiente) {
        hideAddBtn();
        goToFragment(PendienteDetailFragment.newInstance(pendiente), TAG_PENDIENTE_DETAIL);
    }

    public void goToProductos() {
        Intent i = new Intent(this, ProductosActivity.class);
        i.putExtra("mActivo", mActivo);
        i.putExtra("mSurtidorId", mActivo.id);
        i.putExtra("mIdPreventivo", getIdPreventivo());
        startActivityForResult(i, REQUEST_CODE_SHOW_PRODUCTOS);
    }

    public void goToMangueras() {
        Intent i = new Intent(this, MangueraActivity.class);
        i.putExtra("mActivo", mActivo);
        i.putExtra("mSurtidorId", mActivo.id);
        i.putExtra("mIdPreventivo", getIdPreventivo());
        i.putExtra("mVisita", mVisita);
        i.putExtra("mEstacion", mEstacion);
        startActivityForResult(i, REQUEST_CODE_SHOW_MANGUERAS);
    }

    public void showQR(QRDialogFragment.QRListener qrListener) {
        FragmentManager fragmentManager = getFragManager();
        QRDialogFragment newFragment = new QRDialogFragment();
        newFragment.setQRListener(qrListener);
        newFragment.show(fragmentManager, "qr");
    }


    @Override
    public void invalidateCorregidos() {
        Fragment f = getFragManager().findFragmentByTag(TAG_CORREGIDOS);
        if (CorregidosListFragment.class.isInstance(f)) {
            CorregidosListFragment cf = (CorregidosListFragment) f;
            cf.invalidateCorregidos();
        }
    }

    public void invalidatePendientes() {
        Fragment f = getFragManager().findFragmentByTag(TAG_PENDIENTES);
        if (PendientesFragment.class.isInstance(f)) {
            PendientesFragment pf = (PendientesFragment) f;
            pf.invalidatePendientes();
        }
    }

    public void invalidateChequeos() {
        mChequeoSurtidorResult = null;
        mChequeoTanqueResult= null;
        mChequeoBombaResult = null;
        mChequeoGenericoResult = null;
    }

    @Override
    public long getIdActivo() {
        return mActivo.id;
    }

    public Long getIdPreventivo() {
        Integer type = Integer.valueOf(mActivo.tipoId);
        switch (type) {

            case Activo.TYPE_SURTIDOR:
                return mChequeoSurtidorResult != null ? mChequeoSurtidorResult.data.idPreventivo : null;

            case Activo.TYPE_TANQUE:
                return mChequeoTanqueResult != null ? mChequeoTanqueResult.data.idPreventivo : null;

            case Activo.TYPE_BOMBA:
                return mChequeoBombaResult != null ? mChequeoBombaResult.data.idPreventivo : null;

            default:
                return mChequeoGenericoResult != null ? mChequeoGenericoResult.data.idPreventivo : null;
        }
    }

    @Override
    public void onSaveChequeo() {
        mResultReloadVisita = true;
    }

    @Override
    public List<Chequeo> getChequeos() {
        return getChequeoResult().getChequeos();
    }

    @Override
    public Chequeo getChequeoById(long chequeoId) {
        return getChequeoResult().getChequeoById(chequeoId);
    }

    private BaseChequeosResult getChequeoResult() {
        Integer type = Integer.valueOf(mActivo.tipoId);
        switch (type) {
            case Activo.TYPE_SURTIDOR:
                return mChequeoSurtidorResult;
            case Activo.TYPE_TANQUE:
                return mChequeoTanqueResult;
            case Activo.TYPE_BOMBA:
                return mChequeoBombaResult;
            case Activo.TYPE_GENERIC:
                return mChequeoGenericoResult;
            default:
                return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (REQUEST_CODE_SHOW_MANGUERAS == requestCode && RESULT_OK == resultCode) {
            mResultReloadVisita = data.getBooleanExtra("reloadVisita", false);
            return;
        }

        if (REQUEST_CODE_SHOW_PRODUCTOS == requestCode && RESULT_OK == resultCode) {
            mResultReloadVisita = data.getBooleanExtra("reloadVisita", false);
            return;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Crashlytics.setString(Consts.CrashKeys.activo, null);
    }
}
