package com.bilpa.android.fragments;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.toolbox.ImageLoader;
import com.bilpa.android.BilpaApp;
import com.bilpa.android.R;
import com.bilpa.android.model.Organizacion;
import com.bilpa.android.model.Pendiente;
import com.bilpa.android.model.correctivos.CActivo;
import com.bilpa.android.model.correctivos.Correctivo;
import com.bilpa.android.services.ApiCorrectivos;
import com.bilpa.android.services.ApiService;
import com.bilpa.android.services.AsyncCallback;
import com.bilpa.android.services.actions.BaseResult;
import com.bilpa.android.services.correctivos.SavePendienteCorrectivoAction;
import com.bilpa.android.utils.ImageUtils;
import com.bilpa.android.utils.MaterialAlertDialogUtils;
import com.bilpa.android.widgets.LblEditTextHorizontal;
import com.bilpa.android.widgets.LblTextView;
import com.mautibla.utils.InputUtils;
import com.mautibla.utils.KeyboadUtils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class PendienteDetailCorregidoFragment extends TakePickPhotoSupportFragment implements
        TextView.OnEditorActionListener {

    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    private AsyncTask<Void, Void, String> mTaskScalePhoto;
    private Toolbar vToolbar;
    private Bitmap mSelectedBitmap;
    private ImageView vPhoto;
    private LblTextView vPlazo;
    private LblEditTextHorizontal vComentario;
    private CheckBox vCommentVisible;
    private Button vBtnCaputrePhoto;
    private Button vBtnPickPhoto;
    private LblTextView vDestinoCargo;

    private Pendiente mPendiente;
    private Correctivo mCorrectivo;
    private CActivo mActivo;




    public static Fragment newInstance(Correctivo correctivo, CActivo activo) {
        PendienteDetailCorregidoFragment fragment = new PendienteDetailCorregidoFragment();
        fragment.mCorrectivo = correctivo;
        fragment.mActivo = activo;
        return fragment;
    }

    public static Fragment newInstance(Correctivo correctivo, CActivo activo, Pendiente pendiente) {
        PendienteDetailCorregidoFragment fragment = new PendienteDetailCorregidoFragment();
        fragment.mCorrectivo = correctivo;
        fragment.mActivo = activo;
        fragment.mPendiente = pendiente;
        return fragment;
    }

    public PendienteDetailCorregidoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pendiente_detail, container, false);

        vToolbar = (Toolbar) v.findViewById(R.id.toolbar);

        LblTextView vChequeo = (LblTextView) v.findViewById(R.id.vChequeo);
        vComentario = (LblEditTextHorizontal) v.findViewById(R.id.vComentario);
        vCommentVisible = (CheckBox) v.findViewById(R.id.vCommentVisible);
        vPlazo = (LblTextView) v.findViewById(R.id.vPlazo);
        vPhoto = (ImageView) v.findViewById(R.id.vPhoto);
        vDestinoCargo = (LblTextView) v.findViewById(R.id.vDetinoCargo);
        vBtnCaputrePhoto = (Button) v.findViewById(R.id.vBtnCaputrePhoto);
        vBtnPickPhoto = (Button) v.findViewById(R.id.vBtnPickPhoto);

        vPlazo.setOnClickListener(this);
        vDestinoCargo.setOnClickListener(this);
        vBtnCaputrePhoto.setOnClickListener(this);
        vBtnPickPhoto.setOnClickListener(this);

        // hide chequeo for correctivos.
        vChequeo.setVisibility(View.GONE);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        bindToolbar();


        /**
         * Se cargan los datos del pendiente si existe
         */
        if (mPendiente != null) {
            bindData();
        }

        vComentario.getEditText().setOnEditorActionListener(this);
        vCommentVisible.setOnCheckedChangeListener(mCheckCommentVisibleListener);
    }

    private void bindData() {
        vComentario.getEditText().setText(mPendiente.comentario);
        vCommentVisible.setChecked(mPendiente.comentarioVisible);

        if (mPendiente.plazo != null) {
            Calendar plazo = Calendar.getInstance();
            plazo.setTime(mPendiente.plazo);
            setTime(vPlazo, plazo);
        } else {
            setTime(vPlazo, null);
        }
        bindPhoto(mPendiente.urlFoto);

        // Bind destinatario
        Organizacion destinatario = new Organizacion();
        destinatario.setNombreOrganizacion(mPendiente.destinatario);
        bindOrganizacion(destinatario);
    }

    private void bindOrganizacion(Organizacion organizacion) {
        vDestinoCargo.setTag(organizacion);
        vDestinoCargo.setText(organizacion.getNombreOrganizacion());
    }

    private void bindPhoto(String urlFoto) {
        ImageLoader imageLoader = BilpaApp.getInstance().getImageLoader();
        if (urlFoto != null) {
            imageLoader.get(urlFoto, ImageLoader.getImageListener(vPhoto, R.drawable.ic_perm_media_white_24dp, R.drawable.ic_perm_media_white_24dp));
        } else {
            vPhoto.setImageResource(R.drawable.ic_perm_media_white_24dp);
        }
    }

    private void bindToolbar() {
        vToolbar.getMenu().clear();
        vToolbar.setTitle(mPendiente == null ? R.string.pendientes_detail_title_new : R.string.pendientes_detail_title_edit);
        vToolbar.setSubtitle(mActivo.display);
        vToolbar.inflateMenu(R.menu.corregido_detail);
        vToolbar.setOnMenuItemClickListener(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private boolean validate() {

        if (InputUtils.isEmpty(vComentario.getEditText())) {
            MaterialAlertDialogUtils.showAcceptMsg(getActivity(), R.string.pendientes_msg_empty_comment);
            return false;
        }

        if (vDestinoCargo.getTag() == null) {
            MaterialAlertDialogUtils.showAcceptMsg(getActivity(), R.string.pendientes_msg_empty_organizacion);
            return false;
        }

        return true;
    }

    private void savePendiente(String base64Image) {

        SavePendienteCorrectivoAction.Builder b = new SavePendienteCorrectivoAction.Builder();
        if (mPendiente != null) {
            b.id = mPendiente.id;
        }



        b.correctivoId = String.valueOf(mCorrectivo.id);
        b.destinatario = ((Organizacion) vDestinoCargo.getTag()).getNombreOrganizacion();
        b.comentario = InputUtils.getText(vComentario.getEditText());
        b.comentarioVisible = vCommentVisible.isChecked();
        b.idActivo = mActivo.id;
        b.fotoBytes = base64Image;
        Calendar plazo = (Calendar) vPlazo.getTag();
        if (plazo != null) {
            b.plazo(plazo.getTime());
        }

        showLoading();

        ApiCorrectivos.savePendiente(b, new AsyncCallback<BaseResult>(getActivity()) {
            @Override
            protected void onSuccess(BaseResult result) {
                setUnSavedData(false);
                hideLoading();
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
            }

            @Override
            public void onError(String msg, MaterialDialog.ButtonCallback callback) {
                hideLoading();
                super.onError(msg, callback);
            }
        });
    }

    private void saveWithPhoto() {

        mTaskScalePhoto = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String imageBase64 = null;
                if (mSelectedBitmap != null) {
                    imageBase64 = ImageUtils.getBase64(ImageUtils.scale(mSelectedBitmap, 600, 600));
                }
                return imageBase64;
            }
            @Override
            protected void onPostExecute(String result) {
                mTaskScalePhoto = null;
                savePendiente(result);
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
        return getString(R.string.pendientes_detail_msg_exit_unsaved);
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
                getActivity().onBackPressed();
                break;

            case R.id.vSave:
                if (validate()) {
                    if (mSelectedBitmap == null) {
                        savePendiente(null);
                    } else {
                        saveWithPhoto();
                    }
                }
                break;
        }
    }

    @Override
    protected void onActionClick(View view) {
        switch (view.getId()) {

            case R.id.vPlazo:
                pickDate(vPlazo);
                break;

            case R.id.vBtnCaputrePhoto:
                dispatchTakePictureIntent(mTakePickPhotoListener);
                break;

            case R.id.vBtnPickPhoto:
                dispatchPickPhotoIntent(mTakePickPhotoListener);
                break;

            case R.id.vDetinoCargo:

                FragmentManager fm = getFragmentManager();
                OrganizacionesDialogFragment f = new OrganizacionesDialogFragment();
                f.setOnListItemListener(new ListSelectDialogFragment.OnListItemClick() {
                    @Override
                    public void onListItemClick(Object o) {
                        setUnSavedData(true);
                        Organizacion organizacion = (Organizacion) o;
                        bindOrganizacion(organizacion);
                    }
                });
                f.show(fm, "organizaciones_dialog");


                break;
        }
    }

    private OnTakePickPhotoListener mTakePickPhotoListener = new OnTakePickPhotoListener() {
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


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        setUnSavedData(true);
        return false;
    }

    private CompoundButton.OnCheckedChangeListener mCheckCommentVisibleListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            setUnSavedData(true);
        }
    };

    private void pickDate(final LblTextView lblTextView) {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        final Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.YEAR, year);
                        cal.set(Calendar.MONTH, monthOfYear);
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        cal.set(Calendar.HOUR_OF_DAY, 0);
                        cal.set(Calendar.MINUTE, 0);
                        cal.set(Calendar.SECOND, 0);
                        setUnSavedData(true);
                        setTime(lblTextView, cal);
                    }
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    private void setTime(LblTextView view, Calendar cal) {
        if (cal == null) {
            view.setTag(null);
            view.setText(null);
            view.setHint(getString(R.string.pendientes_prompt_select_plazo));
        } else {
            view.setTag(cal);
            view.setText(sdf.format(cal.getTime()));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        BilpaApp app = BilpaApp.getInstance();
        app.cancelPendingRequests(ApiService.SAVE_PENDIENTE);

        KeyboadUtils.hideSoftKeyboard(getActivity(), vComentario.getEditText());
    }
}
