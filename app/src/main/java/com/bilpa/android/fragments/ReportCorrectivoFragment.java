package com.bilpa.android.fragments;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bilpa.android.BilpaApp;
import com.bilpa.android.R;
import com.bilpa.android.model.Report;
import com.bilpa.android.model.ReportSection;
import com.bilpa.android.model.SectionDetail;
import com.bilpa.android.model.correctivos.Correctivo;
import com.bilpa.android.services.ApiCorrectivos;
import com.bilpa.android.services.ApiService;
import com.bilpa.android.services.AsyncCallback;
import com.bilpa.android.services.actions.BaseResult;
import com.bilpa.android.services.actions.visita.GetCorreosEstacionResult;
import com.bilpa.android.services.correctivos.FinalizarCorrectivoAction;
import com.bilpa.android.services.correctivos.GetPreviewCorrectivoResult;
import com.bilpa.android.services.correctivos.ModificarCorrectivoAction;
import com.mautibla.utils.InputUtils;
import com.mautibla.utils.ToastUtils;
import com.mautibla.utils.ViewUtils;

import java.util.Date;
import java.util.List;

public class ReportCorrectivoFragment extends BaseSupportFragment implements View.OnClickListener {


    private Correctivo mCorrectivo;
    private ProgressBar vProgress;
    private RelativeLayout vData;
    private ImageView vLogo;
    private TextView vReportTitle;
    private LinearLayout vReportSections;
    private Button vBtnNext;


    private String reportFilename;
    private String emailSubject;
    private String signName;
    private Bitmap mSign;
    private String mSignComment;
    private String mSignEncoded;
    private Date mInicio;
    private Date mFin;

    public static ReportCorrectivoFragment newInstance(Correctivo correctivo, Date inicio, Date fin) {
        ReportCorrectivoFragment fragment = new ReportCorrectivoFragment();
        Bundle args = new Bundle();
        fragment.mCorrectivo = correctivo;
        fragment.mInicio = inicio;
        fragment.mFin = fin;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_report, container, false);

        vProgress = (ProgressBar) v.findViewById(R.id.vProgress);
        vData = (RelativeLayout) v.findViewById(R.id.vData);
        ViewUtils.invisible(vProgress, vData);


        vLogo = (ImageView) v.findViewById(R.id.vLogo);
        vReportTitle = (TextView) v.findViewById(R.id.vReportTitle);
        vReportSections = (LinearLayout) v.findViewById(R.id.vReportSections);
        vReportSections.removeAllViews();
        vBtnNext = (Button) v.findViewById(R.id.vBtnNext);
        vBtnNext.setOnClickListener(this);



        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.containsKey("correctivo")) {
            mCorrectivo = (Correctivo) savedInstanceState.getSerializable("correctivo");
        }

        ViewUtils.visible(vProgress);
        ViewUtils.invisible(vData);

        ApiCorrectivos.getPreview(mCorrectivo.id, new AsyncCallback<GetPreviewCorrectivoResult>(getActivity()) {
            @Override
            protected void onSuccess(GetPreviewCorrectivoResult result) {
                bind(result.report);
            }
        });

    }

    private void bind(Report report) {

        String reportTitle = report.name;

        String visitaName = mCorrectivo.estacion;

        reportFilename = String.format("%s - %s.pdf", visitaName, reportTitle);
        signName = String.format("%s - %s.png", visitaName, reportTitle);
        emailSubject = String.format("%s | %s", visitaName, reportTitle);

        ViewUtils.visible(vData);
        ViewUtils.gone(vProgress);


        vReportTitle.setText(String.format("%s | %s", visitaName, report.name));
        vReportSections.removeAllViews();

        LayoutInflater inflater = getActivity().getLayoutInflater();

        List<ReportSection> sections = report.sections;
        for (ReportSection section : sections) {
            if (section == null) {
                continue;
            }

            View vSection = inflater.inflate(R.layout.report_section, null);
            TextView vSectionTitle = (TextView) vSection.findViewById(R.id.vSectionTitle);
            TableLayout vSectionTable = (TableLayout) vSection.findViewById(R.id.vSectionTable);
            vReportSections.addView(vSection);


            if (section.title == null) {
                ViewUtils.gone(vSectionTitle);
            } else {
                ViewUtils.visible(vSectionTitle);
                vSectionTitle.setText(section.title);
            }

            List<SectionDetail> detail = section.detail;
            boolean first = true;

            for (SectionDetail sectionDetail : detail) {
                if ("rowVacia".equals(sectionDetail.format)) {

                    vSection = inflater.inflate(R.layout.report_section, null);
                    vSectionTable = (TableLayout) vSection.findViewById(R.id.vSectionTable);
                    vSectionTitle = (TextView) vSection.findViewById(R.id.vSectionTitle);
                    ViewUtils.gone(vSectionTitle);
                    vReportSections.addView(vSection);
                    first = true;

                } else {
                    addRowSection(inflater, vSectionTable, sectionDetail, first || sectionDetail.isBold());
                    first = false;
                }

            }

        }

        ViewUtils.gone(vProgress);
        ViewUtils.visible(vData);


    }

    private void addRowSection(LayoutInflater inflater, TableLayout vSectionTable, SectionDetail sectionDetail, boolean first) {

        String lbl = sectionDetail.lbl;
        String val = sectionDetail.val;
        String format = sectionDetail.format;


        TableRow vRow = (TableRow) inflater.inflate(R.layout.report_section_row, null);
        TextView vLbl = (TextView) vRow.findViewById(R.id.vSectionRowLbl);
        TextView vVal = (TextView) vRow.findViewById(R.id.vSectionRowVal);

        if (first) {
            vLbl.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));
            vVal.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));
            vLbl.setTextColor(getResources().getColor(R.color.black));
            vVal.setTextColor(getResources().getColor(R.color.black));
        } else {
            vLbl.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
            vVal.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        }


        if (sectionDetail.color != null && sectionDetail.color.equalsIgnoreCase("blue")) {
            vLbl.setTextColor(getResources().getColor(R.color.report_blue));
            vVal.setTextColor(getResources().getColor(R.color.report_blue));
        }


        InputUtils.clear(vLbl, vVal);

        if ("rowCompleta".equals(format)) {

            vLbl.setText(lbl);
            vVal.setText(val);
            ViewUtils.visible(vLbl, vVal);

        } else if ("rowSimple".equals(format)) {

            ViewUtils.gone(vVal);
            vRow.removeView(vVal);

            ViewUtils.visible(vLbl);
            vLbl.setText(lbl);
            TableRow.LayoutParams params = (TableRow.LayoutParams) vLbl.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.weight = 1;
            vLbl.setLayoutParams(params);

        } else if ("rowValor".equals(format)) {
            ViewUtils.visible(vLbl, vVal);
            vLbl.setText("");
            vVal.setText(val);
        }

        vSectionTable.addView(vRow);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("correctivo", mCorrectivo);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        BilpaApp app = BilpaApp.getInstance();
        app.cancelPendingRequests(ApiCorrectivos.GET_PREVIEW_CORRECTIVOS);
        app.cancelPendingRequests(ApiService.GET_EMAILS_ESTACION);
        app.cancelPendingRequests(ApiCorrectivos.MODIFICAR_CORRECTIVO);
        app.cancelPendingRequests(ApiCorrectivos.FINALIZAR_CORRECTIVO);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.vBtnNext:

                showSignDialog(mSignComment, new SignDialogFragment2.onSignListener() {
                    @Override
                    public void onSign(Bitmap bitmap, String signEncoded, String comment) {
                        mSign = bitmap;
                        mSignEncoded = signEncoded;
                        mSignComment = comment;
                        if (mSign != null) {
                            // Actualizar correctivo

                            ModificarCorrectivoAction.Builder b = new ModificarCorrectivoAction.Builder();
                            b.idCorrectivo = mCorrectivo.id;
                            b.comentario = mSignComment;
                            b.firma = mSignEncoded;
//                            b.inicio = mInicio;
//                            b.fin = mFin;
                            showLoading();
                            ApiCorrectivos.modificarCorrectivo(b, new AsyncCallback<BaseResult>(getActivity()) {
                                @Override
                                protected void onSuccess(BaseResult result) {
                                    ApiService.getEmailsEstacion(mCorrectivo.idEstacion, new AsyncCallback<GetCorreosEstacionResult>(getActivity()) {
                                        @Override
                                        protected void onSuccess(GetCorreosEstacionResult result) {
                                            hideLoading();
                                            showEmails(result.getEmails());
                                        }
                                        @Override
                                        public void onError(String msg, MaterialDialog.ButtonCallback callback) {
                                            hideLoading();
                                            super.onError(msg, callback);
                                        }
                                    });
                                }
                                @Override
                                public void onError(String msg, MaterialDialog.ButtonCallback callback) {
                                    hideLoading();
                                    super.onError(msg, callback);
                                }
                            });

                        } else {
                            ToastUtils.showToast(getActivity(), "Debes ingresar la firma.");
                        }
                    }
                });

                break;
        }
    }

    public void showEmails(List<String> emails) {
        String title = "Desea cerrar el servicio?";
        EmailsDialogFragment2 newFragment = EmailsDialogFragment2.newInstance(title, emails);
        newFragment.setOnEmailsListener(new EmailsDialogFragment2.OnEmaisListener() {
            @Override
            public void onEmails(List<String> emails) {

                FinalizarCorrectivoAction.Builder b = new FinalizarCorrectivoAction.Builder();
                b.idCorrectivo = mCorrectivo.id;
                b.mails(emails);
                showLoading();
                ApiCorrectivos.finalizarCorrectivo(b, new AsyncCallback<BaseResult>(getActivity()) {
                    @Override
                    protected void onSuccess(BaseResult result) {
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
        });
        newFragment.show(getFragManager(), "emails");
    }


}
