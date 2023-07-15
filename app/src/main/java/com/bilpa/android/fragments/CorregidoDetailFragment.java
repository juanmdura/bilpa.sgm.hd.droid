package com.bilpa.android.fragments;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.toolbox.ImageLoader;
import com.bilpa.android.ActivoActivity;
import com.bilpa.android.BaseActivity;
import com.bilpa.android.BilpaApp;
import com.bilpa.android.R;
import com.bilpa.android.adapters.CounterAdapter;
import com.bilpa.android.model.Activo;
import com.bilpa.android.model.Chequeo;
import com.bilpa.android.model.ChequeoList;
import com.bilpa.android.model.Comentario;
import com.bilpa.android.model.Corregido;
import com.bilpa.android.model.DestinoCargo;
import com.bilpa.android.model.Falla;
import com.bilpa.android.model.Pendiente;
import com.bilpa.android.model.Pico;
import com.bilpa.android.model.Repuesto;
import com.bilpa.android.model.RepuestoItem;
import com.bilpa.android.model.Tarea;
import com.bilpa.android.services.ApiService;
import com.bilpa.android.services.AsyncCallback;
import com.bilpa.android.services.actions.PicosResult;
import com.bilpa.android.services.actions.SaveCorregidoAction;
import com.bilpa.android.services.actions.SaveCorregidoResult;
import com.bilpa.android.services.actions.activos.GetPicoIdByQrResult;
import com.bilpa.android.utils.ImageUtils;
import com.bilpa.android.utils.MaterialAlertDialogUtils;
import com.bilpa.android.utils.MessageUtils;
import com.bilpa.android.widgets.LblEditTextHorizontal;
import com.bilpa.android.widgets.LblTextView;
import com.bilpa.android.widgets.OnAddEditCorregidos;
import com.bilpa.android.widgets.OnAddEditPendientes;
import com.bilpa.android.widgets.OnChequeoListener;
import com.bilpa.android.widgets.ShowHideAddButton;
import com.mautibla.utils.InputUtils;
import com.mautibla.utils.ToastUtils;
import com.mautibla.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

public class CorregidoDetailFragment extends TakePickPhotoFragment implements
        TextView.OnEditorActionListener {

    private static final String CHEQUEOS_DIALOG = "CHEQUEOS_PICK";

    private LblTextView vChequeo;
    private LblTextView vTarea;
    private LblTextView vFalla;
    private Button vAddRespuesto;
    private LinearLayout vPanelRespuestos;
    private LblTextView vManguera;
    private LblTextView vDetinoCargo;
    private Toolbar vToolbar;

    Corregido mCorregido;

    private Falla mFalla;
    private Tarea mTarea;
    private String mCommnet;
    private DestinoCargo mDestinoCargo;
    private Pico mPico;
    private List<RepuestoItem> repuestos;

    private LblEditTextHorizontal vComentario;
    private CheckBox vCommentVisible;
    private Button vBtnCaputrePhoto;
    private Button vBtnPickPhoto;
    private Bitmap mSelectedBitmap;
    private Bitmap mSelectedBitmap2;
    private ImageView vPhoto;

    private AsyncTask<Void, Void, PhotoStr> mTaskScalePhoto;
    private Button vBtnCaputrePhoto2;
    private Button vBtnPickPhoto2;
    private ImageView vPhoto2;
    private Button vBtnManguerasQR;
    private ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener;
    private Pendiente pendiente;


    public static CorregidoDetailFragment newInstance() {
        CorregidoDetailFragment fragment = new CorregidoDetailFragment();
        return fragment;
    }

    public static Fragment newInstance(Corregido corregido) {
        CorregidoDetailFragment fragment = new CorregidoDetailFragment();
        fragment.mCorregido = corregido;
        return fragment;
    }

    /**
     * Create a CorregidoDetailFragment for repair option for pendientes
     */
    public static CorregidoDetailFragment newInstance(Pendiente pendiente) {
        CorregidoDetailFragment fragment = new CorregidoDetailFragment();
        fragment.pendiente = pendiente;
        return fragment;
    }

    public CorregidoDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_corregido_detail, container, false);

        vToolbar = (Toolbar) v.findViewById(R.id.toolbar);

        vChequeo = (LblTextView) v.findViewById(R.id.vChequeo);
        vFalla = (LblTextView) v.findViewById(R.id.vFalla);
        vTarea = (LblTextView) v.findViewById(R.id.vTarea);
        vAddRespuesto = (Button) v.findViewById(R.id.vAddRespuesto);
        vDetinoCargo = (LblTextView) v.findViewById(R.id.vDetinoCargo);
        vManguera = (LblTextView) v.findViewById(R.id.vManguera);
        vBtnManguerasQR = (Button) v.findViewById(R.id.vBtnManguerasQR);
        vComentario = (LblEditTextHorizontal) v.findViewById(R.id.vComentario);
        vCommentVisible = (CheckBox) v.findViewById(R.id.vCommentVisible);

        vBtnCaputrePhoto = (Button) v.findViewById(R.id.vBtnCaputrePhoto);
        vBtnPickPhoto = (Button) v.findViewById(R.id.vBtnPickPhoto);
        vPhoto = (ImageView) v.findViewById(R.id.vPhoto);

        vBtnCaputrePhoto2 = (Button) v.findViewById(R.id.vBtnCaputrePhoto2);
        vBtnPickPhoto2 = (Button) v.findViewById(R.id.vBtnPickPhoto2);
        vPhoto2 = (ImageView) v.findViewById(R.id.vPhoto2);

        vChequeo.setOnClickListener(this);
        vFalla.setOnClickListener(this);
        vTarea.setOnClickListener(this);
        vAddRespuesto.setOnClickListener(this);
        vDetinoCargo.setOnClickListener(this);
        vManguera.setOnClickListener(this);

        vBtnCaputrePhoto.setOnClickListener(this);
        vBtnPickPhoto.setOnClickListener(this);
        vBtnCaputrePhoto2.setOnClickListener(this);
        vBtnPickPhoto2.setOnClickListener(this);
        vBtnManguerasQR.setOnClickListener(this);

        vPanelRespuestos = (LinearLayout) v.findViewById(R.id.vPanelRespuestos);
        vPanelRespuestos.removeAllViews();

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((ShowHideAddButton) getActivity()).hideAddBtn();

        bindToolbar();

        Activo activo = null;
        if (ActivoActivity.class.isInstance(getActivity())) {
            activo = ((ActivoActivity) getActivity()).mActivo;
        }

        if (activo != null && activo.isSurtidor()) {
            ViewUtils.visible(vManguera, vBtnManguerasQR);
        } else {
            ViewUtils.gone(vManguera, vBtnManguerasQR);
        }

        // If edit corregido, then bind current chequeo if non null
        if (mCorregido != null) {
            Chequeo chequeo = ((OnChequeoListener) getActivity()).getChequeoById(mCorregido.idItemChequeado);
            if (chequeo != null) {
                bindChequeo(chequeo);
            }
        }

        /**
         * Se cargan los datos del corregido si existe
         */
        if (mCorregido != null) {
            bindFalla(mCorregido.falla);
            bindTarea(mCorregido.tarea);
            bindComentario(mCorregido.comentario);
            bindDestinoCargo(mCorregido.destinoDelCargo);
            bindRepuestos();
            bindPhoto(vPhoto, mCorregido.urlFoto);
            bindPhoto(vPhoto2, mCorregido.urlFoto2);

            if (activo != null && activo.isSurtidor()) {
                bindPico(mCorregido.pico);
            }

        } else {
            repuestos = new ArrayList<>();
        }

        vComentario.getEditText().setOnEditorActionListener(this);
        vCommentVisible.setOnCheckedChangeListener(mCheckCommentVisibleListener);


        mGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                vComentario.getEditText().addTextChangedListener(mSaveDataWatcher);
                getView().getViewTreeObserver().removeOnGlobalLayoutListener(mGlobalLayoutListener);
                mGlobalLayoutListener = null;
            }
        };
        getView().getViewTreeObserver().addOnGlobalLayoutListener(mGlobalLayoutListener);

    }

    private void bindRepuestos() {
        repuestos = mCorregido.repuestos;
        for (RepuestoItem repuesto : repuestos) {
            addRepuesto(repuesto);
        }
    }

    private void bindPhoto(ImageView vPhoto, String urlFoto) {
        ImageLoader imageLoader = BilpaApp.getInstance().getImageLoader();
        if (urlFoto != null) {
            imageLoader.get(urlFoto, ImageLoader.getImageListener(vPhoto, R.drawable.ic_perm_media_white_24dp, R.drawable.ic_perm_media_white_24dp));
        } else {
            vPhoto.setImageResource(R.drawable.ic_perm_media_white_24dp);
        }
    }

    private void bindToolbar() {
        vToolbar.getMenu().clear();
        vToolbar.setTitle(mCorregido == null ? R.string.corregidos_detail_title_new : R.string.corregidos_detail_title_edit);
        vToolbar.inflateMenu(R.menu.corregido_detail);
        vToolbar.setOnMenuItemClickListener(this);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        BilpaApp app = BilpaApp.getInstance();
        app.cancelPendingRequests(ApiService.SAVE_CORREGIDO);
    }

    private void bindChequeo(Chequeo chequeo) {
        vChequeo.setTag(chequeo);
        vChequeo.setText(chequeo.texto);
    }

    private void bindComentario(Comentario comment) {
        vComentario.getEditText().setText(comment.texto);
        vCommentVisible.setChecked(comment.visible);
    }

    private void bindDestinoCargo(DestinoCargo dc) {
        mDestinoCargo = dc;
        vDetinoCargo.setText(dc.nombre);
    }

    private void bindPico(Pico pico) {
        mPico = pico;
        if (pico != null) {
            vManguera.setText(pico.numeroPico + " " + pico.tipoCombusitble);
        } else {
            vManguera.setHint(getString(R.string.corregidos_detail_manguera_hint));
        }
    }

    private void bindFalla(Falla falla) {
        mFalla = falla;
        vFalla.setText(falla.descripcion);
    }

    private void bindTarea(Tarea tarea) {
        mTarea = tarea;
        vTarea.setText(tarea.descripcion);
    }

    private void addRepuesto(RepuestoItem repuesto) {

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.row_repuesto, null);

        TextView vValue = (TextView) view.findViewById(R.id.vValue);
        CheckBox vNewCheck = (CheckBox) view.findViewById(R.id.vNewCheck);
        Spinner vSpinnerCount = (Spinner) view.findViewById(R.id.vSpinnerCount);
        ImageView vDelete = (ImageView) view.findViewById(R.id.vDeleteRepuesto);

        // Name
        vValue.setText(repuesto.descripcion);

        // Is New
        vNewCheck.setChecked(repuesto.nuevo);
        vNewCheck.setTag(repuesto);
        vNewCheck.setOnCheckedChangeListener(mCheckIsNewRepuestoListener);

        // Count
        List<Integer> counts = new ArrayList<>();

        int[] intArray = getResources().getIntArray(R.array.repuestos_counter_array);
        for (int i : intArray) {
            counts.add(i);
        }
        vSpinnerCount.setTag(repuesto);
        CounterAdapter adapter = new CounterAdapter(getActivity(), counts);
        vSpinnerCount.setAdapter(adapter);
        vSpinnerCount.setSelection(adapter.getPosition(repuesto.cantidad));
        vSpinnerCount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                RepuestoItem repuesto = (RepuestoItem) parent.getTag();
                CounterAdapter adapter = (CounterAdapter) parent.getAdapter();
                repuesto.cantidad = adapter.getItem(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // Delete
        vDelete.setTag(repuesto);
        vDelete.setOnClickListener(this);

        // Add to list
        vPanelRespuestos.addView(view);
    }

    private void deleteRepuesto(View view, RepuestoItem r) {
        View parent = (View) view.getParent();
        View row = (View) parent.getParent();
        vPanelRespuestos.removeView(row);
        repuestos.remove(r);
    }

    private boolean validate() {

        if (vChequeo.getTag() == null) {
            MaterialAlertDialogUtils.showAcceptMsg(getActivity(), R.string.pendientes_chequeo_msg_empty_comment);
            return false;
        }

        Long idPreventivo = ((OnAddEditCorregidos) getActivity()).getIdPreventivo();
        if (idPreventivo == null) {
            MaterialAlertDialogUtils.showAcceptMsg(getActivity(), R.string.corregidos_msg_preventivo_not_found);
            return false;
        }

        if (mFalla == null) {
            MaterialAlertDialogUtils.showAcceptMsg(getActivity(), R.string.corregidos_msg_falla_select);
            return false;
        }

        if (mTarea == null) {
            MaterialAlertDialogUtils.showAcceptMsg(getActivity(), R.string.corregidos_msg_tarea_select);
            return false;
        }

        if (mDestinoCargo == null) {
            MaterialAlertDialogUtils.showAcceptMsg(getActivity(), R.string.corregidos_msg_destinocargo_select);
            return false;
        }

        return true;
    }

    private void saveCorredigo(String photo1Bytes, String photo2Bytes) {

        setUnSavedData(false);

        SaveCorregidoAction.Builder b = new SaveCorregidoAction.Builder();
        if (mCorregido != null) {
            b.idCorregido(mCorregido.id);
        }

        Long idPreventivo = ((OnAddEditCorregidos) getActivity()).getIdPreventivo();
        b.idPreventivo(idPreventivo);
        b.idFalla(mFalla != null ? mFalla.id : null);
        b.idTarea(mTarea != null ? mTarea.id : null);
        b.comentario(InputUtils.getText(vComentario.getEditText()), vCommentVisible.isChecked());
        b.idItemChequeado(((Chequeo) vChequeo.getTag()).id);
        b.idDestinoDelCargo(mDestinoCargo != null ? mDestinoCargo.id : null);

        // En caso que se trate de una reparacion de un pendiente, se envia el id del pendiente.
        if (pendiente != null) {
            b.idPendiente(pendiente.id);
        }

        if (ActivoActivity.class.isInstance(getActivity())) {
            Activo activo = ((ActivoActivity) getActivity()).mActivo;
            if (activo.isSurtidor()) {
                b.idPico(mPico != null ? mPico.id : null);
            }
        }

        for(RepuestoItem r: repuestos) {
            b.repuesto(r.id, r.idRepuesto, r.nuevo, r.cantidad);
        }

        if (photo1Bytes != null) {
            b.fotoBytes(photo1Bytes);
        }
        if (photo2Bytes != null) {
            b.foto2Bytes(photo2Bytes);
        }

        ((BaseActivity) getActivity()).showLoading();

        ApiService.saveCorredigo(b, new AsyncCallback<SaveCorregidoResult>(getActivity()) {
            @Override
            protected void onSuccess(SaveCorregidoResult result) {
                ((BaseActivity) getActivity()).hideLoading();
                ((OnAddEditCorregidos) getActivity()).invalidateCorregidos();

                // Si fue una reparacion de un pendiente, entonces se invalida la lista de pendientes
                if (pendiente != null) {
                    ((OnAddEditPendientes) getActivity()).invalidatePendientes();
                }

                getActivity().onBackPressed();
                assertAddedRpuestos();
            }

            @Override
            public void onError(String msg, MaterialDialog.ButtonCallback callback) {
                ((BaseActivity) getActivity()).hideLoading();
                super.onError(msg, callback);
            }
        });

    }

    private class PhotoStr {
        private String photo1;
        private String photo2;
    }

    private void saveWithPhoto() {

        mTaskScalePhoto = new AsyncTask<Void, Void, PhotoStr>() {
            @Override
            protected PhotoStr doInBackground(Void... params) {
                PhotoStr result = new PhotoStr();

                String imageBase64 = null;
                if (mSelectedBitmap != null) {
                    result.photo1 = ImageUtils.getBase64(ImageUtils.scale(mSelectedBitmap, 600, 600));
                }
                if (mSelectedBitmap2 != null) {
                    result.photo2 = ImageUtils.getBase64(ImageUtils.scale(mSelectedBitmap2, 600, 600));
                }
                return result;
            }
            @Override
            protected void onPostExecute(PhotoStr result) {
                mTaskScalePhoto = null;
                saveCorredigo(result.photo1, result.photo2);
            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mTaskScalePhoto.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null, null, null);
        } else {
            mTaskScalePhoto.execute(null, null, null);
        }
    }



    public ActivoActivity getActivoActivity() {
        return (ActivoActivity) getActivity();
    }


    @Override
    protected String getUnsavedExitMsg() {
        return getString(R.string.corregidos_msg_exit_unsaved);
    }

    @Override
    protected int[] assertMenuItemSavedCheck() {
        return new int[] { R.id.vCancel };
    }

    @Override
    protected int[] assertViewSavedCheck() {
        return null;
    }

    @Override
    protected void onActionClick(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.vCancel:
                if (mCorregido != null) {
                    assertAddedRpuestos();
                }
                getActivity().onBackPressed();
                break;

            case R.id.vSave:
                if (validate()) {
                    if (mSelectedBitmap == null && mSelectedBitmap2 == null) {
                        saveCorredigo(null, null);
                    } else {
                        saveWithPhoto();
                    }
                }
                break;
        }
    }

    private void assertAddedRpuestos() {
        if (repuestos == null || repuestos.isEmpty()) {
            ToastUtils.showToast(getActivity(), R.string.corregidos_msg_empty_repuestos);
        }
    }

    @Override
    protected void onActionClick(View view) {
        switch (view.getId()) {

            case R.id.vChequeo:
                List<Chequeo> chequeos = ((OnChequeoListener) getActivity()).getChequeos();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                ChequeoDialogFragment f= ChequeoDialogFragment.newInstance(new ChequeoList(chequeos));
                f.setOnListItemListener(new ListSelectDialogFragment.OnListItemClick() {
                    @Override
                    public void onListItemClick(Object o) {
                        Chequeo chequeo = (Chequeo) o;
                        bindChequeo(chequeo);
                    }
                });
                f.show(fm, CHEQUEOS_DIALOG);
                break;

            case R.id.vFalla:
                ((BaseActivity) getActivity()).showFallas(new FallasDialogFragment.OnFallaSelectedListener() {
                    @Override
                    public void onFallaSelected(Falla falla) {
                        setUnSavedData(true);
                        bindFalla(falla);
                    }
                });
                break;
            case R.id.vTarea:
                ((BaseActivity) getActivity()).showTareas(new TareasDialogFragment.OnTareaSelectedListener() {
                    @Override
                    public void onTareaSelected(Tarea tarea) {
                        setUnSavedData(true);
                        bindTarea(tarea);
                    }
                });
                break;

            case R.id.vAddRespuesto:
                ((BaseActivity) getActivity()).showRepuestos(new RepuestosDialogFragment.OnRepuestoSelectedListener() {
                    @Override
                    public void onRepuestoSelected(Repuesto repuesto) {
                        setUnSavedData(true);

                        RepuestoItem r = new RepuestoItem();
                        r.cantidad = 1;
                        r.idRepuesto = repuesto.id;
                        r.nuevo = true;
                        r.id = null;
                        r.descripcion = repuesto.descripcion;

                        addRepuesto(r);
                        repuestos.add(r);
                    }
                });
                break;


            case R.id.vDetinoCargo:
                ((BaseActivity) getActivity()).showDestinoCargos(new ListSelectDialogFragment.OnListItemClick() {
                    @Override
                    public void onListItemClick(Object o) {
                        setUnSavedData(true);
                        DestinoCargo dc = (DestinoCargo) o;
                        bindDestinoCargo(dc);
                    }
                });
                break;

            case R.id.vManguera:
                Activo activo = ((ActivoActivity) getActivity()).mActivo;
                ((BaseActivity) getActivity()).showPicos(activo.id, new PicoSelectDialogFragment.OnPicoSelectedListener() {
                    @Override
                    public void onPicoSelected(Pico pico) {
                        setUnSavedData(true);
                        bindPico(pico);
                    }
                });
                break;

            case R.id.vDeleteRepuesto:
                setUnSavedData(true);
                RepuestoItem repuesto = (RepuestoItem) view.getTag();
                deleteRepuesto(view, repuesto);
                break;

            case R.id.vBtnCaputrePhoto:
                dispatchTakePictureIntent(mTakePickPhoto1Listener);
                break;

            case R.id.vBtnPickPhoto:
                dispatchPickPhotoIntent(mTakePickPhoto1Listener);
                break;

            case R.id.vBtnCaputrePhoto2:
                dispatchTakePictureIntent(mTakePickPhoto2Listener);
                break;

            case R.id.vBtnPickPhoto2:
                dispatchPickPhotoIntent(mTakePickPhoto2Listener);
                break;

            case R.id.vBtnManguerasQR:

                ((ActivoActivity) getActivity()).showQR(new QRDialogFragment.QRListener() {
                    @Override
                    public void onQueryCode(String code) {

                        try {
                            int codeInt = Integer.valueOf(code);
                            ToastUtils.showToast(getActivity(), String.format(getString(R.string.mangueras_msg_readed_code), code));
                        } catch (NumberFormatException e) {
                            MessageUtils.showMsg(getActivity(), String.format(getString(R.string.mangueras_msg_qr_must_int), code));
                            return;
                        }


                        ((ActivoActivity) getActivity()).showLoading();

                        Long mVisita = ((ActivoActivity) getActivity()).mEstacion.id;

                        ApiService.getPicoIdByQr(code, mVisita, new AsyncCallback<GetPicoIdByQrResult>(getActivity()) {
                            @Override
                            protected void onSuccess(GetPicoIdByQrResult result) {

                                if (result.picoId != -1) {
                                    final Long picoId = result.picoId;
                                    Activo activo = ((ActivoActivity) getActivity()).mActivo;
                                    ApiService.getPicos(activo.id, new AsyncCallback<PicosResult>(getActivity()) {
                                        @Override
                                        protected void onSuccess(PicosResult result) {

                                            Pico pico = result.findPicoById(picoId);
                                            if (pico != null) {
                                                setUnSavedData(true);
                                                bindPico(pico);
                                            } else {
                                                MessageUtils.showMsg(getActivity(), R.string.mangueras_msg_pico_no_encontrado);
                                            }

                                            ((ActivoActivity) getActivity()).hideLoading();

                                        }

                                        @Override
                                        public void onError(String msg, MaterialDialog.ButtonCallback callback) {
                                            super.onError(msg, callback);
                                            ((ActivoActivity) getActivity()).hideLoading();
                                        }
                                    });



                                } else {
                                    ((ActivoActivity) getActivity()).hideLoading();
                                    MessageUtils.showMsg(getActivity(), R.string.mangueras_msg_pico_no_encontrado);
                                }
                            }

                            @Override
                            public void onError(String msg, MaterialDialog.ButtonCallback callback) {
                                super.onError(msg, callback);
                                ((ActivoActivity) getActivity()).hideLoading();
                            }

                        });

                    }
                });

                break;

        }
    }

    private OnTakePickPhotoListener mTakePickPhoto1Listener = new OnTakePickPhotoListener() {
        @Override
        public void onCapturePhoto(Bitmap bitmap) {
            setUnSavedData(true);
            mSelectedBitmap = bitmap;
            vPhoto.setImageBitmap(bitmap);
        }
        @Override
        public void onPickPhoto(Bitmap bitmap) {
            setUnSavedData(true);
            mSelectedBitmap = bitmap;
            vPhoto.setImageBitmap(bitmap);
        }
    };

    private OnTakePickPhotoListener mTakePickPhoto2Listener = new OnTakePickPhotoListener() {
        @Override
        public void onCapturePhoto(Bitmap bitmap) {
            setUnSavedData(true);
            mSelectedBitmap2 = bitmap;
            vPhoto2.setImageBitmap(bitmap);
        }

        @Override
        public void onPickPhoto(Bitmap bitmap) {
            setUnSavedData(true);
            mSelectedBitmap2 = bitmap;
            vPhoto2.setImageBitmap(bitmap);
        }
    };

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        setUnSavedData(true);
        return false;
    }

    private CompoundButton.OnCheckedChangeListener mCheckIsNewRepuestoListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            RepuestoItem repuesto = (RepuestoItem) buttonView.getTag();
            repuesto.nuevo = isChecked;
            setUnSavedData(true);
        }
    };

    private CompoundButton.OnCheckedChangeListener mCheckCommentVisibleListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            setUnSavedData(true);
        }
    };

    private TextWatcher mSaveDataWatcher = new MyTextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            setUnSavedData(true);
        }
    };

    private abstract class MyTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void afterTextChanged(Editable s) {
        }
    }
}
