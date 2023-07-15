package com.bilpa.android.fragments;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.toolbox.ImageLoader;
import com.bilpa.android.BilpaApp;
import com.bilpa.android.R;
import com.bilpa.android.adapters.CounterAdapter;
import com.bilpa.android.model.Comentario;
import com.bilpa.android.model.DestinoCargo;
import com.bilpa.android.model.Falla;
import com.bilpa.android.model.Pendiente;
import com.bilpa.android.model.Pico;
import com.bilpa.android.model.Repuesto;
import com.bilpa.android.model.Solucion;
import com.bilpa.android.model.Tarea;
import com.bilpa.android.model.correctivos.CActivo;
import com.bilpa.android.model.correctivos.Correctivo;
import com.bilpa.android.services.ApiCorrectivos;
import com.bilpa.android.services.ApiService;
import com.bilpa.android.services.AsyncCallback;
import com.bilpa.android.services.actions.BaseResult;
import com.bilpa.android.services.actions.PicosResult;
import com.bilpa.android.services.actions.activos.GetPicoIdByQrResult;
import com.bilpa.android.services.correctivos.SaveSolucionAction;
import com.bilpa.android.utils.DecimalDigitsInputFilter;
import com.bilpa.android.utils.ImageUtils;
import com.bilpa.android.utils.MaterialAlertDialogUtils;
import com.bilpa.android.utils.MessageUtils;
import com.bilpa.android.utils.SessionStore;
import com.bilpa.android.widgets.LblEditText;
import com.bilpa.android.widgets.LblEditTextHorizontal;
import com.bilpa.android.widgets.LblTextView;
import com.bilpa.android.widgets.RadioWidget;
import com.mautibla.utils.InputUtils;
import com.mautibla.utils.KeyboadUtils;
import com.mautibla.utils.ToastUtils;
import com.mautibla.utils.ViewUtils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

public class SolucionDetailFragment extends TakePickPhotoSupportFragment implements
        TextView.OnEditorActionListener {

    public static final double CAUDAL_MIN = 9.8;
    public static final double CAUDAL_MAX = 10.20;

    private LblTextView vTarea;
    private LblTextView vFalla;
    private Button vAddRespuesto;
    private LinearLayout vPanelRespuestos;
    private LblTextView vManguera;
    private LblTextView vDetinoCargo;
    private Toolbar vToolbar;

    private Falla mFalla;
    private Tarea mTarea;
    private String mCommnet;
    private DestinoCargo mDestinoCargo;
    private Pico mPico;
    private List<Solucion.RepuestoRow> repuestos;

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

    private LblEditText vTotalizadorElectronicInicial;
    private LblEditText vTotalizadorElectronicFinal;
    private TextView vVolumenRetirado;
    private LblEditText vCalibre1;
    private LblEditText vCalibre2;
    private LblEditText vCalibre3;
    private LblEditText vCalibre4;
    private LblEditText vCalibre5;
    private LblEditText vCalibre6;
    private TextView vCalibreAlto;
    private TextView vCalibreBajo;
    private RadioWidget vPrecintoRemplazado;
    private LblEditText vPrecintoNumeroViejo;
    private LblEditText vPrecintoNumero;
    private RelativeLayout vPrecintoRemplazoPanel;

    private Solucion mSolucion;
    private CActivo mActivo;
    private Correctivo mCorrectivo;
    private Pendiente mPendienteSolved;
    private ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener;
    private LinearLayout vMangueraPanel;
    private LinearLayout vTotalizdoresPanel;
    private final DecimalFormat df;


    public static SolucionDetailFragment newInstance(CActivo activo, Correctivo correctivo, Solucion solucion) {
        SolucionDetailFragment fragment = new SolucionDetailFragment();
        fragment.mActivo = activo;
        fragment.mCorrectivo = correctivo;
        fragment.mSolucion = solucion;
        return fragment;
    }

    public SolucionDetailFragment() {
        // Required empty public constructor

        df = new DecimalFormat("####.###");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        symbols.setGroupingSeparator(',');
        df.setDecimalFormatSymbols(symbols);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_solucion_detail, container, false);

        vToolbar = (Toolbar) v.findViewById(R.id.toolbar);

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


        vMangueraPanel = (LinearLayout) v.findViewById(R.id.vMangueraPanel);
        vTotalizdoresPanel = (LinearLayout) v.findViewById(R.id.vTotalizdoresPanel);
        vTotalizadorElectronicInicial = (LblEditText) v.findViewById(R.id.vTotalizadorElectronicInicial);
        vTotalizadorElectronicFinal = (LblEditText) v.findViewById(R.id.vTotalizadorElectronicFinal);
        vVolumenRetirado = (TextView) v.findViewById(R.id.vVolumenRetirado);
        vCalibre1 = (LblEditText) v.findViewById(R.id.vCalibre1);
        vCalibre2 = (LblEditText) v.findViewById(R.id.vCalibre2);
        vCalibre3 = (LblEditText) v.findViewById(R.id.vCalibre3);
        vCalibreAlto = (TextView) v.findViewById(R.id.vCalibreAlto);
        vCalibreBajo = (TextView) v.findViewById(R.id.vCalibreBajo);
        vPrecintoRemplazoPanel = (RelativeLayout) v.findViewById(R.id.vPrecintoRemplazoPanel);
        vPrecintoRemplazado = (RadioWidget) v.findViewById(R.id.vPrecintoRemplazado);
        vPrecintoNumeroViejo = (LblEditText) v.findViewById(R.id.vPrecintoNumeroViejo);
        vPrecintoNumero = (LblEditText) v.findViewById(R.id.vPrecintoNumero);
        vCalibre4 = (LblEditText) v.findViewById(R.id.vCalibre4);
        vCalibre5 = (LblEditText) v.findViewById(R.id.vCalibre5);
        vCalibre6 = (LblEditText) v.findViewById(R.id.vCalibre6);



        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        bindToolbar();

        if (mActivo != null && mActivo.isSurtidor()) {
            ViewUtils.visible(vManguera, vBtnManguerasQR);
        } else {
            ViewUtils.gone(vManguera, vBtnManguerasQR, vMangueraPanel);
        }

        mGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                setInputType(InputType.TYPE_CLASS_NUMBER, vPrecintoNumero, vPrecintoNumeroViejo);
                setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL, vTotalizadorElectronicInicial, vTotalizadorElectronicFinal);
                setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL, vCalibre1, vCalibre2, vCalibre3);
                setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL, vCalibre4, vCalibre5, vCalibre6);
                DigitsKeyListener decimalKeyListener = DigitsKeyListener.getInstance(false, true);
                vTotalizadorElectronicInicial.getValue().setKeyListener(decimalKeyListener);
                vTotalizadorElectronicFinal.getValue().setKeyListener(decimalKeyListener);
                vCalibre1.getValue().setKeyListener(decimalKeyListener);
                vCalibre2.getValue().setKeyListener(decimalKeyListener);
                vCalibre3.getValue().setKeyListener(decimalKeyListener);
                vCalibre4.getValue().setKeyListener(decimalKeyListener);
                vCalibre5.getValue().setKeyListener(decimalKeyListener);
                vCalibre6.getValue().setKeyListener(decimalKeyListener);


                setOnEditorListener(
                        vTotalizadorElectronicInicial, vTotalizadorElectronicFinal,
                        vCalibre1, vCalibre2, vCalibre3,
                        vPrecintoNumeroViejo, vPrecintoNumero,
                        vCalibre4, vCalibre5, vCalibre6
                );
                setTextChangeListener(mSaveDataWatcher, vPrecintoNumero, vPrecintoNumeroViejo, vCalibre4, vCalibre5, vCalibre6);
                setTextChangeListener(mVolumenWatcher, vTotalizadorElectronicInicial, vTotalizadorElectronicFinal);
                setTextChangeListener(mCalibreWatcher, vCalibre1, vCalibre2, vCalibre3);
                vComentario.getEditText().addTextChangedListener(mSaveDataWatcher);

                getView().getViewTreeObserver().removeOnGlobalLayoutListener(mGlobalLayoutListener);
                mGlobalLayoutListener = null;
            }
        };
        getView().getViewTreeObserver().addOnGlobalLayoutListener(mGlobalLayoutListener);





        /**
         * Se cargan los datos del corregido si existe
         */
        if (mSolucion != null && mSolucion != null) {
            bindData();

        } else {
            vVolumenRetirado.setText("0");
            repuestos = new ArrayList<>();
            vPrecintoRemplazado.setOptionByLbl("No");
            ViewUtils.gone(vPrecintoRemplazoPanel);
            setEnabled(vPrecintoNumero, vPrecintoRemplazado.getChecked() == 0);
        }

        vComentario.getEditText().setOnEditorActionListener(this);
        vCommentVisible.setOnCheckedChangeListener(mCheckCommentVisibleListener);
        vPrecintoRemplazado.setOnCheckedChangeListener(mCheckPrecintoListener);


        vTotalizadorElectronicInicial.getValue().setOnFocusChangeListener(mVolumenRetiradoListener);
        vTotalizadorElectronicFinal.getValue().setOnFocusChangeListener(mVolumenRetiradoListener);
        vCalibre1.getValue().setOnFocusChangeListener(vCalibreLisetener);
        vCalibre2.getValue().setOnFocusChangeListener(vCalibreLisetener);
        vCalibre3.getValue().setOnFocusChangeListener(vCalibreLisetener);

        updateCalibreAltoBajo();
        updateVolumenRetirado();


        // Se setean a los totalizadores filtros para q se permitan ingresar 3 digitos decimales
        InputFilter[] decimal3Filter = {new DecimalDigitsInputFilter(3)};
        vTotalizadorElectronicInicial.getValue().setFilters(decimal3Filter);
        vTotalizadorElectronicFinal.getValue().setFilters(decimal3Filter);

        // Se setean a los calibres filtros para q se permitan ingresar 3 digitos decimales
        vCalibre1.getValue().setFilters(decimal3Filter);
        vCalibre2.getValue().setFilters(decimal3Filter);
        vCalibre3.getValue().setFilters(decimal3Filter);
        vCalibre4.getValue().setFilters(decimal3Filter);
        vCalibre5.getValue().setFilters(decimal3Filter);
        vCalibre6.getValue().setFilters(decimal3Filter);


    }

    private void bindData() {
        bindFalla(mSolucion.falla);
        bindTarea(mSolucion.tarea);
        bindComentario(mSolucion.comentario);
        bindDestinoCargo(mSolucion.destinoDelCargo);
        bindRepuestos();
        String photo1 = mSolucion.urlFoto;
        if (photo1 != null) {
            bindPhoto(vPhoto, photo1);
        }
        String photo2 = mSolucion.urlFoto2;
        if (photo2 != null) {
            bindPhoto(vPhoto2, photo2);
        }

        if (mActivo != null && mActivo.isSurtidor()) {
            bindPico(mSolucion.pico);
            bindTotalizadores(mSolucion);
            bindPrecinto(mSolucion);
            bindCalibre(mSolucion);
            ViewUtils.visible(vMangueraPanel);
        } else {
            ViewUtils.gone(vMangueraPanel);
        }
    }

    private void bindRepuestos() {
        repuestos = mSolucion.repuestosRows;
        for (Solucion.RepuestoRow repuesto : repuestos) {
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
        vToolbar.setTitle(mSolucion == null ? R.string.corregidos_detail_title_new : R.string.corregidos_detail_title_edit);
        vToolbar.inflateMenu(R.menu.corregido_detail);
        vToolbar.setOnMenuItemClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        BilpaApp app = BilpaApp.getInstance();
        app.cancelPendingRequests(ApiService.SAVE_CORREGIDO);
    }

    private void bindComentario(Comentario comment) {
        if (comment != null) {
            vComentario.getEditText().setText(comment.texto);
            vCommentVisible.setChecked(comment.visible);
        }
    }

    private void bindDestinoCargo(DestinoCargo dc) {
        mDestinoCargo = dc;
        vDetinoCargo.setText(dc.nombre);
    }

    private void bindPico(Pico pico) {
        mPico = pico;
        if (pico != null) {
            vManguera.setText(pico.numeroPico + " " + pico.tipoCombusitble);
            ViewUtils.visible(vTotalizdoresPanel);
        } else {
            ViewUtils.gone(vTotalizdoresPanel);
            vManguera.setHint(getString(R.string.corregidos_detail_manguera_hint));
        }
    }

    private void bindTotalizadores(Solucion mSolucion) {
        if (mSolucion != null && mSolucion.contadores != null && !mSolucion.contadores.isEmpty()) {
            InputUtils.setNum(vTotalizadorElectronicInicial.getValue(), mSolucion.contadores.get(0).inicio, df);
            InputUtils.setNum(vTotalizadorElectronicFinal.getValue(), mSolucion.contadores.get(0).fin, df);
        } else {
            InputUtils.clear(vTotalizadorElectronicInicial.getValue(), vTotalizadorElectronicFinal.getValue());
        }
    }

    private void bindPrecinto(Solucion solucion) {
        if (solucion != null &&  solucion.precinto != null) {
            InputUtils.setText(vPrecintoNumeroViejo.getValue(), solucion.precinto.numeroViejo);
            InputUtils.setText(vPrecintoNumero.getValue(), solucion.precinto.numero);

            if ("si".equalsIgnoreCase(solucion.precinto.remplazado)) {
                vPrecintoRemplazado.setOptionByLbl("Si");
                ViewUtils.visible(vPrecintoRemplazoPanel);
            } else {
                vPrecintoRemplazado.setOptionByLbl("No");
                ViewUtils.gone(vPrecintoRemplazoPanel);
            }
            setEnabled(vPrecintoNumero, vPrecintoRemplazado.getChecked() == 0);

        } else {
            InputUtils.clear(
                    vPrecintoNumero.getValue(),
                    vPrecintoNumeroViejo.getValue()
            );

            ViewUtils.gone(vPrecintoRemplazoPanel);
            vPrecintoRemplazado.setOptionByLbl("No");
            setEnabled(vPrecintoNumero, vPrecintoRemplazado.getChecked() == 0);
        }
    }

    private void bindCalibre(Solucion mSolucion) {
        if (mSolucion != null && mSolucion.calibre != null) {
            InputUtils.setNum(vCalibre1.getValue(), mSolucion.calibre.calibre1, df);
            InputUtils.setNum(vCalibre2.getValue(), mSolucion.calibre.calibre2, df);
            InputUtils.setNum(vCalibre3.getValue(), mSolucion.calibre.calibre3, df);
            InputUtils.setNum(vCalibre4.getValue(), mSolucion.calibre.calibre4, df);
            InputUtils.setNum(vCalibre5.getValue(), mSolucion.calibre.calibre5, df);
            InputUtils.setNum(vCalibre6.getValue(), mSolucion.calibre.calibre6, df);
        } else {
            InputUtils.clear(
                    vCalibre1.getValue(),
                    vCalibre2.getValue(),
                    vCalibre3.getValue(),
                    vCalibre4.getValue(),
                    vCalibre5.getValue(),
                    vCalibre6.getValue()
            );
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

    private void addRepuesto(Solucion.RepuestoRow repuestoRow) {

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.row_repuesto, null);

        TextView vValue = (TextView) view.findViewById(R.id.vValue);
        CheckBox vNewCheck = (CheckBox) view.findViewById(R.id.vNewCheck);
        Spinner vSpinnerCount = (Spinner) view.findViewById(R.id.vSpinnerCount);
        ImageView vDelete = (ImageView) view.findViewById(R.id.vDeleteRepuesto);

        // Name
        vValue.setText(repuestoRow.repuesto.descripcion);

        // Is New
        vNewCheck.setChecked(repuestoRow.nuevo);
        vNewCheck.setTag(repuestoRow);
        vNewCheck.setOnCheckedChangeListener(mCheckIsNewRepuestoListener);

        // Count
        List<Integer> counts = new ArrayList<>();

        int[] intArray = getResources().getIntArray(R.array.repuestos_counter_array);
        for (int i : intArray) {
            counts.add(i);
        }
        vSpinnerCount.setTag(repuestoRow);
        CounterAdapter adapter = new CounterAdapter(getActivity(), counts);
        vSpinnerCount.setAdapter(adapter);
        vSpinnerCount.setSelection(adapter.getPosition(repuestoRow.cantidad));
        vSpinnerCount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Solucion.RepuestoRow repuesto = (Solucion.RepuestoRow) parent.getTag();
                CounterAdapter adapter = (CounterAdapter) parent.getAdapter();
                repuesto.cantidad = adapter.getItem(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // Delete
        vDelete.setTag(repuestoRow);
        vDelete.setOnClickListener(this);

        // Add to list
        vPanelRespuestos.addView(view);
    }

    private void deleteRepuesto(View view, Solucion.RepuestoRow r) {
        View parent = (View) view.getParent();
        View row = (View) parent.getParent();
        vPanelRespuestos.removeView(row);
        repuestos.remove(r);
    }

    private boolean validate() {

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

        if (mActivo.isSurtidor()) {
            if (mPico != null) {

                // El pico fue seleccionado entonces debo seleccionar los totalizadores

                if (InputUtils.isEmpty(vTotalizadorElectronicInicial.getValue())) {
                    // Se debe ingresar totailizador inicial
                    MaterialAlertDialogUtils.showAcceptMsg(getActivity(), R.string.correctivo_reparaciones_msg_empty_totalizador_inicial);
                    return false;
                }

                if (InputUtils.isEmpty(vTotalizadorElectronicFinal.getValue())) {
                    // Se debe ingresar totailizador final
                    MaterialAlertDialogUtils.showAcceptMsg(getActivity(), R.string.correctivo_reparaciones_msg_empty_totalizador_final);
                    return false;
                }

                // Se chequea que los calibre si son ingresados cumplan con los valores aceptados
                if (!validateCaibres()) {
                    return false;
                }

            }

        }

        return true;
    }

    private boolean validateCaibres() {

        String msg = "";
        boolean error = false;

        if (!InputUtils.isEmpty(vCalibre1.getValue()) && !isValidCalibre(vCalibre1)) {
            msg = String.format(getString(R.string.solucion_detail_validating_msg_calibre), 1);
            error = true;
        }

        if (!InputUtils.isEmpty(vCalibre2.getValue()) && !isValidCalibre(vCalibre2)) {
            msg = msg + "\n" + String.format(getString(R.string.solucion_detail_validating_msg_calibre), 2);
            error = true;
        }

        if (!InputUtils.isEmpty(vCalibre3.getValue()) && !isValidCalibre(vCalibre3)) {
            msg = msg + "\n" + String.format(getString(R.string.solucion_detail_validating_msg_calibre), 3);
            error = true;
        }

        if (!InputUtils.isEmpty(vCalibre4.getValue()) && !isValidCalibre(vCalibre4)) {
            msg = msg + "\n" + String.format(getString(R.string.solucion_detail_validating_msg_calibre), 4);
            error = true;
        }

        if (!InputUtils.isEmpty(vCalibre5.getValue()) && !isValidCalibre(vCalibre5)) {
            msg = msg + "\n" + String.format(getString(R.string.solucion_detail_validating_msg_calibre), 5);
            error = true;
        }

        if (!InputUtils.isEmpty(vCalibre6.getValue()) && !isValidCalibre(vCalibre6)) {
            msg = msg + "\n" + String.format(getString(R.string.solucion_detail_validating_msg_calibre), 6);
            error = true;
        }

        if (error) {
            MaterialAlertDialogUtils.showAcceptMsg(getActivity(), msg.trim());
            return false;
        }

        return true;
    }

    private boolean isValidCalibre(LblEditText vCalibre) {
        Double number = InputUtils.getNumber(vCalibre.getValue());
        if (number != null) {
            int val = (int) (number * 100);
            int minM = (int) (CAUDAL_MIN * 100);
            int maxM = (int) (CAUDAL_MAX * 100);
            if (isInRange(minM, maxM, val)) {
                return true;
            }
        }
        return false;
    }

    private void saveSolucion(String photo1Bytes, String photo2Bytes) {

        SaveSolucionAction.Builder b = new SaveSolucionAction.Builder();

        // Id de la solucion
        if (mSolucion != null) {
            b.idSolucion = mSolucion.id;
        }

        // Correctivo
        b.idCorrectivo = mCorrectivo.id;

        // Falla
        b.idFalla = mFalla != null ? mFalla.id : null;

        // Tarea
        b.idTarea = mTarea != null ? mTarea.id : null;

        // Tecnico
        b.idTecnico = Long.valueOf(SessionStore.getSession(getActivity()).id);

        // Destino del cargo
        b.idDestinoDelCargo = mDestinoCargo != null ? mDestinoCargo.id : null;

        // Activo
        b.idActivo = mActivo.id;

        // Comentario
        String idComentario = null;
        if (mSolucion != null) {
            idComentario = mSolucion.comentario != null ? mSolucion.comentario.id : null;
        }
        b.comentario(idComentario, InputUtils.getText(vComentario.getEditText()), vCommentVisible.isChecked());

        // Repuestos
        for(Solucion.RepuestoRow r: repuestos) {
            b.repuesto(r.repuesto.id, r.nuevo, r.cantidad);
        }

        // Photos
        if (photo1Bytes != null) {
            b.fotoBytes(photo1Bytes);
        }
        if (photo2Bytes != null) {
            b.foto2Bytes(photo2Bytes);
        }

        // Pendiente (En caso de que sea una reparacion para un pendiente
        if (mPendienteSolved != null) {
            b.idPendiente = mPendienteSolved.id;
        }

        // Pico
        if (mActivo.isSurtidor()) {
            b.idPico = mPico != null ? mPico.id : null;
        }

        if (mPico != null) {

            Long contadorId = null;
            if (mSolucion != null && mSolucion.contadores != null && !mSolucion.contadores.isEmpty()) {
                contadorId = mSolucion.contadores.get(0).id;
            }

            Double totalizadorInicial = InputUtils.getDouble(vTotalizadorElectronicInicial.getValue());
            Double totalizadorFinal = InputUtils.getDouble(vTotalizadorElectronicFinal.getValue());
            b.totalizadores(contadorId, totalizadorInicial, totalizadorFinal);


            // Calibres
            b.calibre = new SaveSolucionAction.Calibres();
            b.calibre.calibre1 = InputUtils.getText(vCalibre1.getValue());
            b.calibre.calibre2 = InputUtils.getText(vCalibre2.getValue());
            b.calibre.calibre3 = InputUtils.getText(vCalibre3.getValue());
            if (vPrecintoRemplazado.getChecked() == 0) {
                b.calibre.calibre4 = InputUtils.getText(vCalibre4.getValue());
                b.calibre.calibre5 = InputUtils.getText(vCalibre5.getValue());
                b.calibre.calibre6 = InputUtils.getText(vCalibre6.getValue());
            }

            // Precinto
            b.precinto = new SaveSolucionAction.Precinto();
            b.precinto.remplazado(vPrecintoRemplazado.getChecked());
            b.precinto.numeroViejo = InputUtils.getText(vPrecintoNumeroViejo.getValue());
            if (vPrecintoRemplazado.getChecked() == 0) {
                b.precinto.numero = InputUtils.getText(vPrecintoNumero.getValue());
            }
        }







        // show loading
        showLoading();


        ApiCorrectivos.saveSolucion(b, new AsyncCallback<BaseResult>(getActivity()) {
            @Override
            protected void onSuccess(BaseResult result) {
                setUnSavedData(false);

                hideLoading();
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
                assertAddedRpuestos();
            }

            @Override
            public void onError(String msg, MaterialDialog.ButtonCallback callback) {
                hideLoading();
                super.onError(msg, callback);
            }
        });

    }

    public void setPendienteSolved(Pendiente pendiente) {
        this.mPendienteSolved = pendiente;
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
                saveSolucion(result.photo1, result.photo2);
            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mTaskScalePhoto.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null, null, null);
        } else {
            mTaskScalePhoto.execute(null, null, null);
        }
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
                if (mSolucion != null) {
                    assertAddedRpuestos();
                }
                getActivity().onBackPressed();
                break;

            case R.id.vSave:
                if (validate()) {
                    if (mSelectedBitmap == null && mSelectedBitmap2 == null) {
                        saveSolucion(null, null);
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

            case R.id.vFalla:
                showFallas(new FallasDialogFragment.OnFallaSelectedListener() {
                    @Override
                    public void onFallaSelected(Falla falla) {
                        setUnSavedData(true);
                        bindFalla(falla);
                    }
                });
                break;
            case R.id.vTarea:
                showTareas(new TareasDialogFragment.OnTareaSelectedListener() {
                    @Override
                    public void onTareaSelected(Tarea tarea) {
                        setUnSavedData(true);
                        bindTarea(tarea);
                    }
                });
                break;

            case R.id.vAddRespuesto:
                showRepuestos(new RepuestosDialogFragment.OnRepuestoSelectedListener() {
                    @Override
                    public void onRepuestoSelected(Repuesto repuesto) {
                        setUnSavedData(true);
                        Solucion.RepuestoRow r = new Solucion.RepuestoRow();
                        r.cantidad = 1;
                        r.repuesto = repuesto;
                        r.nuevo = true;
                        r.id = null;

                        addRepuesto(r);
                        repuestos.add(r);
                    }
                });
                break;


            case R.id.vDetinoCargo:
                showDestinoCargos(new ListSelectDialogFragment.OnListItemClick() {
                    @Override
                    public void onListItemClick(Object o) {
                        setUnSavedData(true);
                        DestinoCargo dc = (DestinoCargo) o;
                        bindDestinoCargo(dc);
                    }
                });
                break;

            case R.id.vManguera:
                showPicos(mActivo.id, new PicoSelectDialogFragment.OnPicoSelectedListener() {
                    @Override
                    public void onPicoSelected(Pico pico) {
                        setUnSavedData(true);
                        bindPico(pico);
                    }
                });
                break;

            case R.id.vDeleteRepuesto:
                setUnSavedData(true);
                Solucion.RepuestoRow repuesto = (Solucion.RepuestoRow) view.getTag();
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

                super.showQR(new QRDialogFragment.QRListener() {
                    @Override
                    public void onQueryCode(String code) {

                        try {
                            int codeInt = Integer.valueOf(code);
                            ToastUtils.showToast(getActivity(), String.format(getString(R.string.mangueras_msg_readed_code), code));
                        } catch (NumberFormatException e) {
                            MessageUtils.showMsg(getActivity(), String.format(getString(R.string.mangueras_msg_qr_must_int), code));
                            return;
                        }


                        SolucionDetailFragment.this.showLoading();

                        Long mVisita = mCorrectivo.idEstacion;

                        ApiService.getPicoIdByQr(code, mVisita, new AsyncCallback<GetPicoIdByQrResult>(getActivity()) {
                            @Override
                            protected void onSuccess(GetPicoIdByQrResult result) {

                                if (result.picoId != -1) {
                                    final Long picoId = result.picoId;

                                    ApiService.getPicos(mActivo.id, new AsyncCallback<PicosResult>(getActivity()) {
                                        @Override
                                        protected void onSuccess(PicosResult result) {

                                            Pico pico = result.findPicoById(picoId);
                                            if (pico != null) {
                                                setUnSavedData(true);
                                                bindPico(pico);
                                            } else {
                                                MessageUtils.showMsg(getActivity(), R.string.mangueras_msg_pico_no_encontrado);
                                            }
                                            hideLoading();
                                        }

                                        @Override
                                        public void onError(String msg, MaterialDialog.ButtonCallback callback) {
                                            super.onError(msg, callback);
                                            hideLoading();
                                        }
                                    });

                                } else {
                                    hideLoading();
                                    MessageUtils.showMsg(getActivity(), R.string.mangueras_msg_pico_no_encontrado);
                                }
                            }

                            @Override
                            public void onError(String msg, MaterialDialog.ButtonCallback callback) {
                                super.onError(msg, callback);
                                hideLoading();
                            }

                        });

                    }
                });
                break;

        }
    }

    RadioGroup.OnCheckedChangeListener mCheckPrecintoListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            setUnSavedData(true);
            int checked = vPrecintoRemplazado.getChecked();
            if (checked == 0) {
                ViewUtils.visible(vPrecintoRemplazoPanel);
            } else {
                ViewUtils.gone(vPrecintoRemplazoPanel);
            }
            setEnabled(vPrecintoNumero, checked == 0);
        }
    };

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

        if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
            View parent = (View) v.getParent().getParent();

            switch (parent.getId()) {

                case R.id.vTotalizadorElectronicInicial:
                    vTotalizadorElectronicFinal.requestFocus();
                    break;

                case R.id.vTotalizadorElectronicFinal:
                    vCalibre1.requestFocus();
                    break;

                case R.id.vCalibre1:
                    validateCalibre(vCalibre1);
                    requestFocus(v, vCalibre2);
                    break;

                case R.id.vCalibre2:
                    validateCalibre(vCalibre2);
                    requestFocus(v, vCalibre3);
                    break;

                case R.id.vCalibre3:
                    validateCalibre(vCalibre3);
                    vPrecintoNumeroViejo.requestFocus();
                    break;

                case R.id.vPrecintoNumeroViejo:
                    if (vPrecintoRemplazado.getChecked() != 0) {
                        KeyboadUtils.hideSoftKeyboard(getActivity(), vPrecintoNumeroViejo.getValue());
                    } else {
                        vPrecintoNumero.requestFocus();
                    }
                    break;

                case R.id.vPrecintoNumero:
                    vCalibre4.requestFocus();
                    break;

                case R.id.vCalibre4:
                    validateCalibre(vCalibre4);
                    vCalibre5.requestFocus();
                    break;

                case R.id.vCalibre5:
                    validateCalibre(vCalibre5);
                    vCalibre6.requestFocus();
                    break;

                case R.id.vCalibre6:
                    validateCalibre(vCalibre6);
                    KeyboadUtils.hideSoftKeyboard(getActivity(), vCalibre6.getValue());
                    v.clearFocus();
                    break;


            }
            return false;
        }

        return false;
    }

    private CompoundButton.OnCheckedChangeListener mCheckIsNewRepuestoListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Solucion.RepuestoRow repuesto = (Solucion.RepuestoRow) buttonView.getTag();
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

    private void setInputType(int typeClassNumber, LblEditText ... lblEditText) {
        for (LblEditText editText : lblEditText) {
            editText.getValue().setInputType(typeClassNumber);
        }
    }

    private void setOnEditorListener(LblEditText ... lblEditText) {
        for (LblEditText editText : lblEditText) {
            editText.getValue().setOnEditorActionListener(this);
        }
    }

    private void setTextChangeListener(TextWatcher listener, LblEditText ... lblEditText) {
        for (LblEditText editText : lblEditText) {
            editText.getValue().addTextChangedListener(listener);
        }
    }

    private void setEnabled(LblEditText lblEditText, boolean enabled) {
        lblEditText.setBackgroundColor(getResources().getColor(enabled ? R.color.bg_field : R.color.bg_field_disabled));
        lblEditText.getValue().setEnabled(enabled);
    }

    private abstract class MyTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void afterTextChanged(Editable s) {
        }
    }

    private TextWatcher mSaveDataWatcher = new MyTextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            setUnSavedData(true);
        }
    };

    private TextWatcher mVolumenWatcher = new MyTextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            updateVolumenRetirado();
            setUnSavedData(true);
        }
    };

    private TextWatcher mCalibreWatcher = new MyTextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            updateCalibreAltoBajo();
            setUnSavedData(true);
        }
    };

    private View.OnFocusChangeListener vCalibreLisetener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            updateCalibreAltoBajo();
        }
    };

    private View.OnFocusChangeListener mVolumenRetiradoListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            updateVolumenRetirado();
        }
    };

    private void updateVolumenRetirado() {
        double init = 0;
        double end = 0;
        if (!InputUtils.isEmpty(vTotalizadorElectronicInicial.getValue())) {
            init =InputUtils.getDoubleOrZero(vTotalizadorElectronicInicial.getValue());
        }
        if (!InputUtils.isEmpty(vTotalizadorElectronicFinal.getValue())) {
            end =InputUtils.getDoubleOrZero(vTotalizadorElectronicFinal.getValue());
        }

        double volumen = end - init;

        InputUtils.setNum(vVolumenRetirado, volumen, df);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public void updateCalibreAltoBajo() {

        double calibre1 = 0;
        double calibre2 = 0;
        double calibre3 = 0;

        if (!InputUtils.isEmpty(vCalibre1.getValue())) {
            calibre1 =InputUtils.getDoubleOrZero(vCalibre1.getValue());
        }
        if (!InputUtils.isEmpty(vCalibre2.getValue())) {
            calibre2 = InputUtils.getDoubleOrZero(vCalibre2.getValue());
        }
        if (!InputUtils.isEmpty(vCalibre3.getValue())) {
            calibre3 = InputUtils.getDoubleOrZero(vCalibre3.getValue());
        }

        double min = Math.min(calibre1, calibre2);
        min = Math.min(min, calibre3);
        InputUtils.setNum(vCalibreBajo, min, df);

        double max = Math.max(calibre1, calibre2);
        max = Math.max(max, calibre3);
        InputUtils.setNum(vCalibreAlto, max, df);

    }

    private void validateCalibre(LblEditText vCalibre) {
        CharSequence newVal = filterCaudal(InputUtils.getText(vCalibre.getValue()));
        if (newVal != null) {
            Drawable errorIcon = getResources().getDrawable(R.drawable.ic_error_red_18dp);
            errorIcon.setBounds(new Rect(0, 0, errorIcon.getIntrinsicWidth(), errorIcon.getIntrinsicHeight()));
            vCalibre.getValue().setError(getContext().getString(R.string.corregidos_calibres_invalid_input), errorIcon);
        } else {
            vCalibre.getValue().setError(null);
        }
    }

    private CharSequence filterCaudal(String newVal) {
        return filterCaudal(CAUDAL_MIN, CAUDAL_MAX, newVal);
    }

    private CharSequence filterCaudal(double min, double max, String newVal) {
        try {
            int val = (int) (Double.valueOf(newVal) * 100);
            int minM = (int) (min * 100);
            int maxM = (int) (max * 100);
            if (isInRange(minM, maxM, val)) {
                return null;
            } else {
                if (val > max) {
                    return "";
                }
            }
        } catch (Exception nfe) {
            Log.w(MangueraDetailFragment.class.getSimpleName(), "Error al filtrar datos del caudal");
        }
        return "";
    }

    private CharSequence filterCaudalMin(double min, double max, String newVal) {
        try {
            double val = Double.valueOf(newVal);
            if (val < min) {
                ToastUtils.showToast(getActivity(), String.format(getString(R.string.mangueras_msg_caudal_between), min, max));
                return "";
            }
        } catch (Exception nfe) {
            Log.w(MangueraDetailFragment.class.getSimpleName(), "Error al filtrar datos del caudal");
        }
        return "";
    }

    private CharSequence filterCaudalMin(String newVal) {
        return filterCaudalMin(CAUDAL_MIN, CAUDAL_MAX, newVal);
    }

    private boolean isInRange(int a, int b, int c) {
        return b > a ? c >= a && c <= b : c >= b && c <= a;
    }

    private void requestFocus(View current, final View next) {
        current.post(new Runnable() {
            @Override
            public void run() {
                next.requestFocus();
            }
        });
    }


}
