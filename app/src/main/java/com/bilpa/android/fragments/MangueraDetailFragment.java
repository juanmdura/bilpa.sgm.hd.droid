package com.bilpa.android.fragments;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
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
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bilpa.android.ActivoActivity;
import com.bilpa.android.BilpaApp;
import com.bilpa.android.MangueraActivity;
import com.bilpa.android.R;
import com.bilpa.android.model.Caudal;
import com.bilpa.android.model.Chequeo;
import com.bilpa.android.model.ChequeoPico;
import com.bilpa.android.model.Pico;
import com.bilpa.android.model.Precinto;
import com.bilpa.android.services.ApiService;
import com.bilpa.android.services.AsyncCallback;
import com.bilpa.android.services.actions.BaseResult;
import com.bilpa.android.services.actions.GetChequeoSurtidorPicoResult;
import com.bilpa.android.services.actions.SaveChequeoSurtidorPicoAction;
import com.bilpa.android.utils.Consts;
import com.bilpa.android.utils.DecimalDigitsInputFilter;
import com.bilpa.android.utils.Logger;
import com.bilpa.android.utils.MaterialAlertDialogUtils;
import com.bilpa.android.widgets.LblEditText;
import com.bilpa.android.widgets.OnChequeoListener;
import com.bilpa.android.widgets.RadioCheckboxWidget;
import com.bilpa.android.widgets.RadioWidget;
import com.crashlytics.android.Crashlytics;
import com.mautibla.utils.InputUtils;
import com.mautibla.utils.KeyboadUtils;
import com.mautibla.utils.ToastUtils;
import com.mautibla.utils.ViewUtils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class MangueraDetailFragment extends SaveFragment implements
        TextView.OnEditorActionListener,
        CompoundButton.OnCheckedChangeListener,
        Toolbar.OnMenuItemClickListener {


    public static final double CAUDAL_MIN = 9.8;
    public static final double CAUDAL_MAX = 10.20;
    private final DecimalFormat df;

    private LblEditText vToalizadoMecanidoInicial;
    private LblEditText vToalizadoMecanidoFinal;
    private LblEditText vTotalizadorElectronicInicial;
    private LblEditText vTotalizadorElectronicFinal;
    private TextView vVolumenRetirado;
    private LblEditText vCaudalVolumen;
    private LblEditText vCaudalLitrosMinuto;
    private Toolbar toolbar;
    private Long mIdPreventivo;
    private Pico mPico;
    public Integer mPicoIndex;

    private LinearLayout vData;
    private ProgressBar vProgress;
    private LblEditText vCalibre1;
    private LblEditText vCalibre2;
    private LblEditText vCalibre3;
    private LblEditText vCalibre4;
    private LblEditText vCalibre5;
    private LblEditText vCalibre6;
    private TextView vCalibreAlto;
    private TextView vCalibreBajo;
    private RadioCheckboxWidget vPicoForroManguera;
    private RadioCheckboxWidget vIdentificacionProducto;
    private RadioCheckboxWidget vPredeterminacion;
    private RadioCheckboxWidget vSistemaBloqueo;
    private RadioCheckboxWidget vFugas;
    private RadioCheckboxWidget vOtros;
    private RadioWidget vPrecintoRemplazado;
    private LblEditText vPrecintoNumeroViejo;
    private LblEditText vPrecintoNumero;
    private RelativeLayout vPrecintoRemplazoPanel;
    private TextView vIndex;
    private TextView vToolbarTitle;
    private ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener;
    private Chronometer vCrono;
    private Button vCronoBtn;
    private ScrollView vScroll;

    private OnChequeoListener mOnChequeoListener;

    public void setOnChequeoListener(OnChequeoListener onChequeoListener) {
        this.mOnChequeoListener = onChequeoListener;
    }

    public static MangueraDetailFragment newInstance(int picoIndex, Long mIdPreventivo, Pico pico) {
        MangueraDetailFragment fragment = new MangueraDetailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.mIdPreventivo = mIdPreventivo;
        fragment.mPicoIndex = picoIndex;
        fragment.mPico = pico;
        return fragment;
    }

    public MangueraDetailFragment() {

        df = new DecimalFormat("####.###");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        symbols.setGroupingSeparator(',');
        df.setDecimalFormatSymbols(symbols);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_manguera_detail, container, false);

        vScroll = (ScrollView) v.findViewById(R.id.vScroll);

        vData = (LinearLayout) v.findViewById(R.id.vData);
        vProgress = (ProgressBar) v.findViewById(R.id.vProgress);

        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        vIndex = (TextView) v.findViewById(R.id.vIndex);
        vToolbarTitle = (TextView) v.findViewById(R.id.vToolbarTitle);



        vToalizadoMecanidoInicial = (LblEditText) v.findViewById(R.id.vToalizadoMecanidoInicial);
        vToalizadoMecanidoFinal = (LblEditText) v.findViewById(R.id.vToalizadoMecanidoFinal);
        vTotalizadorElectronicInicial = (LblEditText) v.findViewById(R.id.vTotalizadorElectronicInicial);
        vTotalizadorElectronicFinal = (LblEditText) v.findViewById(R.id.vTotalizadorElectronicFinal);
        vVolumenRetirado = (TextView) v.findViewById(R.id.vVolumenRetirado);


        vCaudalVolumen = (LblEditText) v.findViewById(R.id.vCaudalVolumen);
        vCrono = (Chronometer) v.findViewById(R.id.vCrono);
        vCronoBtn = (Button) v.findViewById(R.id.vCronoBtn);
        vCaudalLitrosMinuto = (LblEditText) v.findViewById(R.id.vCaudalLitrosMinuto);

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


        vPicoForroManguera = (RadioCheckboxWidget) v.findViewById(R.id.vPicoForroManguera);
        vIdentificacionProducto = (RadioCheckboxWidget) v.findViewById(R.id.vIdentificacionProducto);
        vPredeterminacion = (RadioCheckboxWidget) v.findViewById(R.id.vPredeterminacion);
        vSistemaBloqueo = (RadioCheckboxWidget) v.findViewById(R.id.vSistemaBloqueo);
        vFugas = (RadioCheckboxWidget) v.findViewById(R.id.vFugas);
        vOtros= (RadioCheckboxWidget) v.findViewById(R.id.vOtros);

        vCronoBtn.setOnClickListener(this);

        // Non editable fileds.
        setEnabled(vCaudalLitrosMinuto, false);
        vCaudalLitrosMinuto.getValue().setBackgroundResource(0);


        vCrono.setTag(false);
        vCrono.setText("0");

        vCrono.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener(){
            @Override
            public void onChronometerTick(Chronometer arg0) {
            countUp = (SystemClock.elapsedRealtime() - arg0.getBase()) / 1000;
                String asText = countUp + "";
                setCrono(asText);
                updateCaudal();
            }
        });

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mPico == null && savedInstanceState != null) {
            mPico = (Pico) savedInstanceState.getSerializable("mPico");
        }
        if (mIdPreventivo == null && savedInstanceState != null) {
            mIdPreventivo = (Long) savedInstanceState.get("mIdPreventivo");
        }
        if (mPicoIndex == null && savedInstanceState != null) {
            mPicoIndex = (Integer) savedInstanceState.get("mPicoIndex");
        }


        if (mPico != null) {
            Crashlytics.setString(Consts.CrashKeys.pico, mPico.tipoCombusitble);
        } else {
            Crashlytics.setString(Consts.CrashKeys.pico, "null");
        }

        toolbar.setTitle("");
        toolbar.inflateMenu(R.menu.menu_manguera_detail);
        toolbar.setOnMenuItemClickListener(this);

        vIndex.setText(mPicoIndex + "");
        vToolbarTitle.setText(String.format("%s (%d)", mPico.tipoCombusitble, mPico.numeroPico));

        mGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                setInputType(InputType.TYPE_CLASS_NUMBER, vToalizadoMecanidoInicial, vToalizadoMecanidoFinal, vPrecintoNumero, vPrecintoNumeroViejo);
                setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL, vTotalizadorElectronicInicial, vTotalizadorElectronicFinal);
                setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL, vCaudalVolumen, vCaudalLitrosMinuto);
                setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL, vCalibre1, vCalibre2, vCalibre3);
                setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL, vCalibre4, vCalibre5, vCalibre6);


                DigitsKeyListener decimalKeyListener = DigitsKeyListener.getInstance(false, true);
                vTotalizadorElectronicInicial.getValue().setKeyListener(decimalKeyListener);
                vTotalizadorElectronicFinal.getValue().setKeyListener(decimalKeyListener);
                vCaudalVolumen.getValue().setKeyListener(decimalKeyListener);
                vCalibre1.getValue().setKeyListener(decimalKeyListener);
                vCalibre2.getValue().setKeyListener(decimalKeyListener);
                vCalibre3.getValue().setKeyListener(decimalKeyListener);
                vCalibre4.getValue().setKeyListener(decimalKeyListener);
                vCalibre5.getValue().setKeyListener(decimalKeyListener);
                vCalibre6.getValue().setKeyListener(decimalKeyListener);

                getView().getViewTreeObserver().removeOnGlobalLayoutListener(mGlobalLayoutListener);
                mGlobalLayoutListener = null;

            }
        };
        getView().getViewTreeObserver().addOnGlobalLayoutListener(mGlobalLayoutListener);

        GetChequeoSurtidorPicoResult mChequeoResult = ((MangueraActivity) getActivity()).mChequeoResult;
        if (mChequeoResult != null) {
            bind(mChequeoResult);
        } else {
            load();
        }


        // Se setean a los totalizadores filtros para q se permitan ingresar 3 digitos decimales
        InputFilter[] decimal3Filter = {new DecimalDigitsInputFilter(3)};
        vToalizadoMecanidoInicial.getValue().setFilters(decimal3Filter);
        vToalizadoMecanidoFinal.getValue().setFilters(decimal3Filter);
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


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        BilpaApp app = BilpaApp.getInstance();
        app.cancelPendingRequests(ApiService.SET_QR_PICO);
        app.cancelPendingRequests(ApiService.SAVE_CHEQUEO_PICO);
        app.cancelPendingRequests(ApiService.GET_CHEQUEO);

        Crashlytics.setString(Consts.CrashKeys.pico, null);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("mIdPreventivo", mIdPreventivo);
        outState.putInt("mPicoIndex", mPicoIndex);
        outState.putSerializable("mPico", mPico);
    }

    private void setInputType(int typeClassNumber, LblEditText ... lblEditText) {
        for (LblEditText editText : lblEditText) {
            editText.getValue().setInputType(typeClassNumber);
        }
    }

    private void setTextChangeListener(TextWatcher listener, LblEditText ... lblEditText) {
        for (LblEditText editText : lblEditText) {
            editText.getValue().addTextChangedListener(listener);
        }
    }

    private void setOnEditorListener(LblEditText ... lblEditText) {
        for (LblEditText editText : lblEditText) {
            editText.getValue().setOnEditorActionListener(this);
        }
    }

    private void load() {

        ViewUtils.gone(vData);
        ViewUtils.visible(vProgress);

        ApiService.getChequeoSurtidorPico(mPico.id, mIdPreventivo, new AsyncCallback<GetChequeoSurtidorPicoResult>(getActivity()) {
            @Override
            protected void onSuccess(GetChequeoSurtidorPicoResult result) {
                ((MangueraActivity) getActivity()).mChequeoResult = result;
                bind(result);
            }
        });

    }

    private void bind(GetChequeoSurtidorPicoResult result) {

        ViewUtils.visible(vData);
        ViewUtils.gone(vProgress);

        ChequeoPico chequeo = result.data.chequeo;

        bindTotalizadores(chequeo);
        bindCaudal(chequeo);
        bindCalibre(chequeo);
        bindPrecinto(chequeo);

        if (chequeo != null) {

            updateCalibreAltoBajo();
            updateVolumenRetirado();
            updateCaudal();


        } else {
            // Clear all

            InputUtils.clear(
                    vCalibreAlto,
                    vCalibreBajo
            );
            vVolumenRetirado.setText("0");
            vCrono.setText("0");
            ViewUtils.gone(vPrecintoRemplazoPanel);
            vPrecintoRemplazado.setOptionByLbl("No");
            setEnabled(vPrecintoNumero, vPrecintoRemplazado.getChecked() == 0);

        }

        bindCheck(vPicoForroManguera, chequeo != null ? chequeo.getPicoForroManguera() : null);
        bindCheck(vIdentificacionProducto, chequeo != null ? chequeo.getIdentificacionDelProducto() : null);
        bindCheck(vPredeterminacion, chequeo != null ? chequeo.getPredeterminacion() : null);
        bindCheck(vSistemaBloqueo, chequeo != null ? chequeo.getSistemaBloqueo() : null);
        bindCheck(vFugas, chequeo != null ? chequeo.getFugas() : null);
        bindCheck(vOtros, chequeo != null ? chequeo.getOtros() : null);


        vPrecintoRemplazado.setOnCheckedChangeListener(mCheckPrecintoListener);

        setOnEditorListener(
                vToalizadoMecanidoInicial, vToalizadoMecanidoFinal,
                vTotalizadorElectronicInicial, vTotalizadorElectronicFinal,
                vCaudalVolumen,
                vCalibre1, vCalibre2, vCalibre3,
                vPrecintoNumeroViejo, vPrecintoNumero,
                vCalibre4, vCalibre5, vCalibre6
        );

        vTotalizadorElectronicInicial.getValue().setOnFocusChangeListener(mVolumenRetiradoListener);
        vTotalizadorElectronicFinal.getValue().setOnFocusChangeListener(mVolumenRetiradoListener);
        vCaudalVolumen.getValue().setOnFocusChangeListener(mLitrosMinutosListener);
        vCrono.setOnFocusChangeListener(mLitrosMinutosListener);
        vCalibre1.getValue().setOnFocusChangeListener(vCalibreLisetener);
        vCalibre2.getValue().setOnFocusChangeListener(vCalibreLisetener);
        vCalibre3.getValue().setOnFocusChangeListener(vCalibreLisetener);


        addTextChangeListener();

    }

    private void addTextChangeListener() {
        setTextChangeListener(mSaveDataWatcher, vToalizadoMecanidoInicial, vToalizadoMecanidoFinal, vPrecintoNumero, vPrecintoNumeroViejo, vCalibre4, vCalibre5, vCalibre6);
        setTextChangeListener(mVolumenWatcher, vTotalizadorElectronicInicial, vTotalizadorElectronicFinal);
        setTextChangeListener(mCaudalWatcher, vCaudalVolumen);
        setTextChangeListener(mCalibreWatcher, vCalibre1, vCalibre2, vCalibre3);
    }

    private void bindPrecinto(ChequeoPico chequeo) {
        if (chequeo != null &&  chequeo.precinto != null) {
            InputUtils.setText(vPrecintoNumeroViejo.getValue(), chequeo.precinto.numeroViejo);
            InputUtils.setText(vPrecintoNumero.getValue(), chequeo.precinto.numero);

            if ("Si".equalsIgnoreCase(chequeo.precinto.remplazado)) {
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

    private void bindCalibre(ChequeoPico chequeo) {
        if (chequeo != null) {
            InputUtils.setNum(vCalibre1.getValue(), chequeo.calibre1, df);
            InputUtils.setNum(vCalibre2.getValue(), chequeo.calibre2, df);
            InputUtils.setNum(vCalibre3.getValue(), chequeo.calibre3, df);
            InputUtils.setNum(vCalibre4.getValue(), chequeo.calibre4, df);
            InputUtils.setNum(vCalibre5.getValue(), chequeo.calibre5, df);
            InputUtils.setNum(vCalibre6.getValue(), chequeo.calibre6, df);
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

    private void bindCaudal(ChequeoPico chequeo) {
        if (chequeo != null && chequeo.caudal != null) {
            InputUtils.setNum(vCaudalVolumen.getValue(), chequeo.caudal.volumen, df);
            InputUtils.setNum(vCrono, chequeo.caudal.tiempo);
        } else {
            InputUtils.clear(
                    vCaudalVolumen.getValue(),
                    vCrono,
                    vCaudalLitrosMinuto.getValue()
            );
        }
    }

    private void bindTotalizadores(ChequeoPico chequeo) {
        if (chequeo != null) {
            InputUtils.setNum(vToalizadoMecanidoInicial.getValue(), chequeo.totalizadorMecanicoInicial, df);
            InputUtils.setNum(vToalizadoMecanidoFinal.getValue(), chequeo.totalizadorMecanicoFinal, df);
            InputUtils.setNum(vTotalizadorElectronicInicial.getValue(), chequeo.totalizadorElectronicoInicial, df);
            InputUtils.setNum(vTotalizadorElectronicFinal.getValue(), chequeo.totalizadorElectronicoFinal, df);
        } else {
            InputUtils.clear(
                    vToalizadoMecanidoInicial.getValue(),
                    vToalizadoMecanidoFinal.getValue(),
                    vTotalizadorElectronicInicial.getValue(),
                    vTotalizadorElectronicFinal.getValue()
            );
        }
    }

    public void bindCheck(RadioCheckboxWidget chequeoWidget, Chequeo chequeo) {
        chequeoWidget.bindCheckeo(chequeo, this, mCommentsClickListener);
    }

    private void updateCaudal() {

        double volumen = 0;
        double tiempo = 0;
        if (!InputUtils.isEmpty(vCaudalVolumen.getValue())) {
            volumen =InputUtils.getDoubleOrZero(vCaudalVolumen.getValue());
        }

        String crono = getCrono();
        if (crono != null) {
            tiempo = Double.valueOf(crono);
        }

        double litros = 0;
        if (tiempo != 0) {
            litros = (volumen / tiempo ) * 60;
        }

        InputUtils.setNum(vCaudalLitrosMinuto.getValue(), round(litros, 3), df);

    }

    Object o = new Object();
    private String getCrono() {
        synchronized (o) {
            return InputUtils.getText(vCrono);
        }
    }

    private void setCrono(String val) {
        synchronized (o) {
            InputUtils.setText(vCrono, val);
        }
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

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



    @Override
    public void onDetach() {
        super.onDetach();
    }


    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        LblEditText parent = (LblEditText) v.getParent().getParent();

        if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {

            switch (parent.getId()) {

                case R.id.vToalizadoMecanidoInicial:
                    vToalizadoMecanidoFinal.requestFocus();
                    break;

                case R.id.vToalizadoMecanidoFinal:
                    vTotalizadorElectronicInicial.requestFocus();
                    break;

                case R.id.vTotalizadorElectronicInicial:
                    vTotalizadorElectronicFinal.requestFocus();
                    break;

                case R.id.vTotalizadorElectronicFinal:
                    vCaudalVolumen.requestFocus();
                    break;

                case R.id.vCaudalVolumen:
                    KeyboadUtils.hideSoftKeyboard(getActivity(), vCaudalVolumen.getValue());
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
                    requestFocus(v, vPicoForroManguera);
                    break;


            }
            return false;
        }

        return false;
    }

    long countUp;

    private void startCrono() {
        vCrono.setBase(SystemClock.elapsedRealtime());
        vCrono.start();
    }



    private void requestFocus(View current, final View next) {
        current.post(new Runnable() {
            @Override
            public void run() {
                next.requestFocus();
            }
        });
    }

    private void validateCalibre(LblEditText vCalibre) {
        CharSequence newVal = filterCaudal(InputUtils.getText(vCalibre.getValue()));
        if (newVal != null) {
            Drawable errorIcon = getResources().getDrawable(R.drawable.ic_error_red_18dp);
            errorIcon.setBounds(new Rect(0, 0, errorIcon.getIntrinsicWidth(), errorIcon.getIntrinsicHeight()));
            vCalibre.getValue().setError("Debes ingresar un valor entre 9.80 y 10.20", errorIcon);
        } else {
            vCalibre.getValue().setError(null);
        }
    }

    @Override
    protected String getUnsavedExitMsg() {
        String msg = getString(R.string.mangueras_chequeo_exit_unsaved_msg);
        return String.format(msg, mPico.tipoCombusitble, mPico.numeroPico);
    }

    @Override
    protected int[] assertMenuItemSavedCheck() {
        return new int[0];
    }

    @Override
    protected int[] assertViewSavedCheck() {
        return new int[0];
    }

    @Override
    protected void onActionClick(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.vMenuReparaciones:
                // TODO santilod. Ver si va aca o en otro lado
                ((MangueraActivity) getActivity()).goToCorregidos();
                break;

            case R.id.vMenuPendientes:
                // TODO santilod. Ver si va aca o en otro lado
                ((MangueraActivity) getActivity()).goToPendientes();
                break;

            case R.id.vMenuQR:
                ((MangueraActivity) getActivity()).showQR(new QRDialogFragment.QRListener() {
                    @Override
                    public void onQueryCode(final String code) {
                        ApiService.setQrToPico(mPico.id, code, new AsyncCallback<BaseResult>(getActivity()) {
                            @Override
                            protected void onSuccess(BaseResult result) {
                                mPico.codigoQR = code;
                                ToastUtils.showToast(getActivity(), String.format(getString(R.string.mangueras_qr_assigned), code));
                                ((MangueraActivity) getActivity()).updatePico(mPico);
                            }
                        });
                    }
                });
                break;

            case R.id.vMenuSave:
                if (validate()) {
                    save();
                }
                break;

        }
    }

    private boolean validate() {

        // Se chequea que los calibre si son ingresados cumplan con los valores aceptados
        if (!validateCaibres()) {
            return false;
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

    @Override
    protected void onActionClick(View view) {
        switch (view.getId()) {

            case R.id.vCronoBtn:

                Boolean active = (Boolean) vCrono.getTag();
                if (!active) {
                    vCronoBtn.setText(R.string.btn_stop);
                    startCrono();
                } else {
                    vCrono.stop();
                    vCronoBtn.setText(R.string.btn_init);
                }
                vCrono.setTag(!active);
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        setUnSavedData(true);
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

    private void setEnabled(LblEditText lblEditText, boolean enabled) {
        lblEditText.setBackgroundColor(getResources().getColor(enabled ? R.color.bg_field : R.color.bg_field_disabled));
        lblEditText.getValue().setEnabled(enabled);
    }

    private void save() {

        SaveChequeoSurtidorPicoAction.Builder b = new SaveChequeoSurtidorPicoAction.Builder();

        b.idPico = mPico.id;
        b.idPreventivo = mIdPreventivo;
        b.totalizadorMecanicoInicial = InputUtils.getLong(vToalizadoMecanidoInicial.getValue());
        b.totalizadorMecanicoFinal = InputUtils.getLong(vToalizadoMecanidoFinal.getValue());
        b.totalizadorElectronicoInicial = InputUtils.getDouble(vTotalizadorElectronicInicial.getValue());
        b.totalizadorElectronicoFinal = InputUtils.getDouble(vTotalizadorElectronicFinal.getValue());
        b.caudal = new Caudal();
        b.caudal.volumen = InputUtils.getDouble(vCaudalVolumen.getValue());
        b.caudal.tiempo = InputUtils.getInt(vCrono);
        b.caudal.litrosPorMinuto = InputUtils.getDouble(vCaudalLitrosMinuto.getValue());
        b.calibre1 = InputUtils.getDouble(vCalibre1.getValue());
        b.calibre2 = InputUtils.getDouble(vCalibre2.getValue());
        b.calibre3 = InputUtils.getDouble(vCalibre3.getValue());
        b.precinto = new Precinto();
        b.precinto.remplazado = getPrecintoRemplazoString();
        b.precinto.numeroViejo = InputUtils.getText(vPrecintoNumeroViejo.getValue());
        if (vPrecintoRemplazado.getChecked() == 0) {
            b.precinto.numero = InputUtils.getText(vPrecintoNumero.getValue());
            b.calibre4 = InputUtils.getDouble(vCalibre4.getValue());
            b.calibre5 = InputUtils.getDouble(vCalibre5.getValue());
            b.calibre6 = InputUtils.getDouble(vCalibre6.getValue());
        }

        GetChequeoSurtidorPicoResult mChequeoResult = ((MangueraActivity) getActivity()).mChequeoResult;
        b.chequeo(mChequeoResult.data.chequeo.getLblPicoForroManguera(), vPicoForroManguera.getSelectedLabel(), vPicoForroManguera.isChecked());
        b.chequeo(mChequeoResult.data.chequeo.getLblSistemaBloqueo(), vSistemaBloqueo.getSelectedLabel(), vSistemaBloqueo.isChecked());
        b.chequeo(mChequeoResult.data.chequeo.getLblIdentificacionDelProducto(), vIdentificacionProducto.getSelectedLabel(), vIdentificacionProducto.isChecked());
        b.chequeo(mChequeoResult.data.chequeo.getLblPredeterminacion(), vPredeterminacion.getSelectedLabel(), vPredeterminacion.isChecked());
        b.chequeo(mChequeoResult.data.chequeo.getLblFugas(), vFugas.getSelectedLabel(), vFugas.isChecked());
        b.chequeo(mChequeoResult.data.chequeo.getLblOtros(), vOtros.getSelectedLabel(), vOtros.isChecked());

        ApiService.saveChequeoSurtidorPico(b, new AsyncCallback<BaseResult>(getActivity()) {
            @Override
            protected void onSuccess(BaseResult result) {
                setUnSavedData(false);
                ToastUtils.showToast(getActivity(), R.string.mangueras_chequeo_saved_successful);

                if (mOnChequeoListener != null) {
                    mOnChequeoListener.onSaveChequeo();
                }

            }
        });

    }

    private String getPrecintoRemplazoString() {
        switch (vPrecintoRemplazado.getChecked()) {
            case 0: return "SI";
            case 1: return "NO";
            case 2: return "N_A";
        }
        return null;
    }


    private TextWatcher mSaveDataWatcher = new MyTextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            super.onTextChanged(s, start, before, count);
        }
    };


    private TextWatcher mVolumenWatcher = new MyTextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            updateVolumenRetirado();
            super.onTextChanged(s, start, before, count);
        }
    };

    private TextWatcher mCaudalWatcher = new MyTextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            updateCaudal();
            super.onTextChanged(s, start, before, count);
        }
    };

    private TextWatcher mCalibreWatcher = new MyTextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            updateCalibreAltoBajo();
            super.onTextChanged(s, start, before, count);
        }
    };

    private View.OnFocusChangeListener mVolumenRetiradoListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            updateVolumenRetirado();
        }
    };

    private View.OnFocusChangeListener mLitrosMinutosListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            updateCaudal();
        }
    };

    private View.OnFocusChangeListener vCalibreLisetener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            updateCalibreAltoBajo();
        }
    };



    private abstract class MyTextWatcher implements TextWatcher {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Logger.d("mSaveDataWatcher onTextChanged + CharSequence", String.valueOf(s));
            Logger.d("mSaveDataWatcher onTextChanged + start", String.valueOf(start));
            Logger.d("mSaveDataWatcher onTextChanged + before", String.valueOf(before));
            Logger.d("mSaveDataWatcher onTextChanged + count", String.valueOf(count));
            if (count > 0 || before > 0) {
                setUnSavedData(true);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
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

    private CharSequence filterCaudal(String newVal) {
        return filterCaudal(CAUDAL_MIN, CAUDAL_MAX, newVal);
    }


    private boolean isInRange(int a, int b, int c) {
        return b > a ? c >= a && c <= b : c >= b && c <= a;
    }

    private View.OnClickListener mCommentsClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RadioCheckboxWidget parent = (RadioCheckboxWidget) v.getParent().getParent().getParent();
            Long chequeoId = (Long) v.getTag();
            ((MangueraActivity) getActivity()).goToComments(chequeoId, parent.getLabel());
        }
    };

}
