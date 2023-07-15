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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bilpa.android.fragments.CommentsFragment;
import com.bilpa.android.fragments.CorregidoDetailFragment;
import com.bilpa.android.fragments.CorregidosListFragment;
import com.bilpa.android.fragments.PendienteDetailFragment;
import com.bilpa.android.fragments.PendientesFragment;
import com.bilpa.android.fragments.ProductDetailFragment;
import com.bilpa.android.fragments.ProductosListFragment;
import com.bilpa.android.fragments.QRDialogFragment;
import com.bilpa.android.fragments.SaveFragment;
import com.bilpa.android.model.Activo;
import com.bilpa.android.model.Chequeo;
import com.bilpa.android.model.Corregido;
import com.bilpa.android.model.Pendiente;
import com.bilpa.android.model.Producto;
import com.bilpa.android.services.actions.products.GetChequeoProductoResult;
import com.bilpa.android.services.actions.products.ProductosResult;
import com.bilpa.android.widgets.OnAddEditCorregidos;
import com.bilpa.android.widgets.OnAddEditPendientes;
import com.bilpa.android.widgets.OnChequeoListener;
import com.bilpa.android.widgets.ShowHideAddButton;
import com.mautibla.utils.ViewUtils;

import java.util.List;

public class ProductosActivity extends BaseActivity implements View.OnClickListener, ShowHideAddButton, OnAddEditPendientes, OnAddEditCorregidos, OnChequeoListener {

    public static final String TAG = "ProductosActivity";

    public static final String FRAG_LEFT = "fragLeft";
    public static final String FRAG_RIGHT = "fragRight";
    public static final String TAG_CORREGIDOS           = "TAG_CORREGIDOS";
    public static final String TAG_PENDIENTES           = "TAG_PENDIENTES";
    public static final String TAG_COMMENTS             = "TAG_COMMENTS";
    public static final String TAG_CORREGIDO_DETAIL     = "TAG_CORREGIDO_DETAIL";
    public static final String TAG_PENDIENTE_DETAIL     = "TAG_PENDIENTE_DETAIL";


    private Toolbar toolbar;
    private LinearLayout vPanelLeft;
    private RelativeLayout vPanelRight;

    public ProductosResult mProductosResult;

    private Long mSurtidorId;
    private Long mIdPreventivo;
    private Activo mActivo;
    private ImageButton vBtnAddCorregido;

    public GetChequeoProductoResult mChequeoResult;

    public boolean mResultReloadVisita = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);


        mActivo = (Activo) getIntent().getSerializableExtra("mActivo");
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
        vToolbarDesc.setText("Productos");


        vPanelLeft = (LinearLayout) findViewById(R.id.vPanelLeft);
        vPanelRight = (RelativeLayout) findViewById(R.id.vPanelRight);
        vBtnAddCorregido = (ImageButton) findViewById(R.id.vBtnAdd);
        vBtnAddCorregido.setOnClickListener(this);

        if (savedInstanceState == null) {
            goToChild(ProductosListFragment.newInstance(mSurtidorId), R.id.vPanelLeft, FRAG_LEFT);

        } else {
            mProductosResult = (ProductosResult) savedInstanceState.getSerializable("mProductosResult");
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
            CorregidosListFragment cf = (CorregidosListFragment) peekFragment();
            hideAddBtn();
            invalidateChequeos();
            getFragManager().popBackStack();
            return;

        } else if (TAG_CORREGIDO_DETAIL.equals(peekTag())) {
            showAddBtn();
            getFragManager().popBackStack();
            return;

        } else if (TAG_PENDIENTES.equals(peekTag())) {
            PendientesFragment pf = (PendientesFragment) peekFragment();
            hideAddBtn();
            invalidateChequeos();
            getFragManager().popBackStack();
            return;

        } else if (TAG_COMMENTS.equals(peekTag())) {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.vBtnAdd:

                Fragment current = peekFragment();
                if (current != null) {

                    if (PendientesFragment.class.isInstance(current)) {
                        PendientesFragment pf = (PendientesFragment) current;
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
                        return;
                    }
                }
                break;

        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("mProductosResult", mProductosResult);
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
        t.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_out, R.anim.fade_in);
        t.replace(container, f, tag);
        t.addToBackStack(tag);
        t.commit();
    }

    public void showQR() {
        FragmentManager fragmentManager = getFragManager();
        QRDialogFragment newFragment = new QRDialogFragment();
        newFragment.show(fragmentManager, "qr");
    }

    public void goToProductoDetail(final Producto producto, final int index) {

        Fragment rFrag = findFragmentByTag(FRAG_RIGHT);
        if (rFrag != null) {
            if (rFrag != null && SaveFragment.class.isInstance(rFrag)) {
                final SaveFragment sf = (SaveFragment) rFrag;
                if (sf.isUnSavedData()) {
                    sf.showLeftMsg(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            sf.setUnSavedData(false);
                            goToProductoDetailImpl(producto, index);
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

                            ProductosListFragment listFragment = (ProductosListFragment) lFrag;
                            ProductDetailFragment detailFragment = (ProductDetailFragment) rFrag;

                            listFragment.setActivated(detailFragment.mIndex);
                        }
                    });
                    return;
                }
            }
        }
        goToProductoDetailImpl(producto, index);
    }

    private void goToProductoDetailImpl(Producto producto, int index) {
        mChequeoResult = null;
        hideAddBtn();
        backToTag(FRAG_LEFT);

        ProductDetailFragment f = ProductDetailFragment.newInstance(producto, index);
        f.setOnChequeoListener(this);
        goToChild(f, R.id.vPanelRight, FRAG_RIGHT);
    }

    private Fragment findFragmentByTag(String tag) {
        FragmentManager fm = getFragManager();
        Fragment f = fm.findFragmentByTag(tag);
        return f;
    }


    @Override
    public long getIdActivo() {
        return mActivo.id;
    }

    public Long getIdPreventivo() {
        return mIdPreventivo;
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
    public void invalidateCorregidos() {
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

    private void invalidateChequeos() {
        mChequeoResult = null;
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
