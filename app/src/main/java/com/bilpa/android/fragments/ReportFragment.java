package com.bilpa.android.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import com.bilpa.android.BilpaApp;
import com.bilpa.android.HomeActivity;
import com.bilpa.android.R;
import com.bilpa.android.model.Report;
import com.bilpa.android.model.ReportSection;
import com.bilpa.android.model.SectionDetail;
import com.bilpa.android.services.ApiService;
import com.bilpa.android.services.AsyncCallback;
import com.bilpa.android.services.actions.BaseResult;
import com.bilpa.android.services.actions.visita.GetCorreosEstacionResult;
import com.bilpa.android.services.actions.visita.ReportResult;
import com.bilpa.android.services.actions.visita.UpdateVisitaAction;
import com.bilpa.android.utils.MaterialAlertDialogUtils;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mautibla.utils.InputUtils;
import com.mautibla.utils.ToastUtils;
import com.mautibla.utils.ViewUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReportFragment extends BaseFragment implements View.OnClickListener {

    private Long idEstacion;
    private Long idVisita;
    private String mInitDate;
    private String mEndDate;
    private String visitaName;
    private String planificacion;
    private Toolbar toolbar;
    private ProgressBar vProgress;
    private RelativeLayout vData;
    private ImageView vLogo;
    private TextView vReportTitle;
    private LinearLayout vReportSections;
    private Button vBtnNext;

    private Font fReportTitle, fSectionTitle, fSectionRow;

    private String reportFilename;
    private String emailSubject;
    private String signName;
    private ReportResult reportResult;
    private Bitmap mSign;
    private String mSignComment;
    private String mSignEncoded;



    public static ReportFragment newInstance(Long idEstacion, Long idVisita, String mInitDate, String mEndDate, String visitaName, String planificacion) {
        ReportFragment fragment = new ReportFragment();
        fragment.idEstacion = idEstacion;
        fragment.idVisita = idVisita;
        fragment.mInitDate = mInitDate;
        fragment.mEndDate = mEndDate;
        fragment.visitaName = visitaName;
        fragment.planificacion = planificacion;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_report, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_report, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        ((HomeActivity) getActivity()).setSupportActionBar(toolbar);
        ((HomeActivity) getActivity()).getSupportActionBar().setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
//        toolbar.setLogoDescription("");
//        TextView vToolbarTitle = (TextView) findViewById(R.id.vToolbarTitle);
//        TextView vToolbarDesc = (TextView) findViewById(R.id.vToolbarDesc);
//        vToolbarTitle.setText(R.string.report_ab_title);
//        vToolbarDesc.setText(visitaName);


        vProgress = (ProgressBar) getView().findViewById(R.id.vProgress);
        vData = (RelativeLayout) getView().findViewById(R.id.vData);
        ViewUtils.gone(vProgress, vData);


        vLogo = (ImageView) getView().findViewById(R.id.vLogo);
        vReportTitle = (TextView) getView().findViewById(R.id.vReportTitle);
        vReportSections = (LinearLayout) getView().findViewById(R.id.vReportSections);
        vReportSections.removeAllViews();


        vBtnNext = (Button) getView().findViewById(R.id.vBtnNext);
        vBtnNext.setOnClickListener(this);

        // Create report file name

        try {
            BaseFont baseFont = BaseFont.createFont("assets/DroidSerif-Regular.ttf", "ISO-8859-1", BaseFont.EMBEDDED);
            BaseFont baseBold = BaseFont.createFont("assets/DroidSerif-Bold.ttf", "ISO-8859-1", BaseFont.EMBEDDED);

            fReportTitle    = new Font(baseBold, 14);
            fSectionTitle   = new Font(baseBold, 10);
            fSectionRow     = new Font(baseFont, 10);

        } catch (Exception e) {
            e.printStackTrace();

            fReportTitle    = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
            fSectionTitle   = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
            fSectionRow     = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
        }




        if (savedInstanceState == null) {
            load();
        } else {
            reportResult = (ReportResult) savedInstanceState.getSerializable("reportResult");
            bind(reportResult);
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("reportResult", reportResult);
    }


    private void load() {

        ViewUtils.visible(vProgress);


        ApiService.getReport(idVisita, new AsyncCallback<ReportResult>(getActivity()) {
            @Override
            protected void onSuccess(ReportResult result) {
                reportResult = result;
                bind(result);
            }
        });

    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        BilpaApp app = BilpaApp.getInstance();
        app.cancelPendingRequests(ApiService.GET_REPORT);
        app.cancelPendingRequests(ApiService.GET_EMAILS_ESTACION);
        app.cancelPendingRequests(ApiService.FINISH_VISITA);
        hideLoading();
    }

    private void bind(ReportResult result) {

        String reportTitle = result.report.name;

        reportFilename = String.format("%s - %s - %s.pdf", visitaName, reportTitle, planificacion);
        signName = String.format("%s - %s - %s.png", visitaName, reportTitle, planificacion);
        emailSubject = String.format("%s | %s | %s", visitaName, reportTitle, planificacion);

        ViewUtils.visible(vData);
        ViewUtils.gone(vProgress);


        vReportTitle.setText(String.format("%s | %s | %s", visitaName, reportResult.report.name, planificacion));
        vReportSections.removeAllViews();

        LayoutInflater inflater = getActivity().getLayoutInflater();

        List<ReportSection> sections = result.report.sections;
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
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.vBtnNext:

                ((HomeActivity) getActivity()).showSignDialog(mSignComment, new SignDialogFragment2.onSignListener() {
                    @Override
                    public void onSign(Bitmap bitmap, String signEncoded, String comment) {
                        mSign = bitmap;
                        mSignEncoded = signEncoded;
                        mSignComment = comment;

                        if (mSign != null) {
                            // Actualizar visita
                            doUpdateVisita();
                        } else {
                            ToastUtils.showToast(getActivity(), "Debes ingresar la firma.");
                        }
                    }
                });

                break;
        }
    }

    private void doUpdateVisita() {

        UpdateVisitaAction.Builder b = new UpdateVisitaAction.Builder();
        b.visitaId(idVisita);
        b.fechaInicio(mInitDate);
        b.fechaFin(mEndDate);
        if (mSign != null) {
            b.firma(mSignEncoded);
            b.comentario(mSignComment);
        }

        showLoading();

        ApiService.updateVisita(b, new AsyncCallback<BaseResult>(getActivity()) {
            @Override
            protected void onSuccess(BaseResult result) {
                ApiService.getEmailsEstacion(idEstacion, new AsyncCallback<GetCorreosEstacionResult>(getActivity()) {
                    @Override
                    protected void onSuccess(GetCorreosEstacionResult result) {
                        hideLoading();
                        showEmailsDialog(result.getEmails());
                    }

                    @Override
                    protected void onFail(Throwable caught) {
                        hideLoading();
                        super.onFail(caught);
                    }
                });
            }

            @Override
            protected void onFail(Throwable caught) {
                hideLoading();
                super.onFail(caught);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.vShowReport:
                pdfCreate();
                showPdf();
                break;

            case R.id.vEmail:
                pdfCreate();
                signCreate();
                sendEmail();
                break;
        }

        return true;
    }

    public void sendEmail() {

        Intent i = new Intent(Intent.ACTION_SEND_MULTIPLE);

        String subject = emailSubject;
        i.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);

        String textMsg = String.format(getString(R.string.report_email_body), visitaName);
        i.putExtra(android.content.Intent.EXTRA_TEXT, textMsg);

        ArrayList<Uri> uris = new ArrayList<Uri>();

        File file = new File(getReportFilePath());
        Uri uri = Uri.fromFile(file);
        uris.add(uri);

        // Uncomment for attach sign in mail
//        if (sign != null) {
//            File file2 = new File(getSignFilePath());
//            Uri uri2 = Uri.fromFile(file2);
//            uris.add(uri2);
//        }


        i.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);

        i.setType("text/pdf");

        startActivity(i);
    }


    private String getReportFilePath() {
        return getDirectory().getPath() + "/" + reportFilename;
    }

    private String getSignFilePath() {
        return getDirectory().getPath() + "/" + signName;
    }

    public void showPdf() {

        try {

            String path = getReportFilePath();
            File file = new File(path);
            Intent i = new Intent( Intent.ACTION_VIEW );
            i.setDataAndType(Uri.fromFile(file),"application/pdf");
            startActivity(i);

        } catch (ActivityNotFoundException e) {
            MaterialAlertDialogUtils.showAcceptMsg(getActivity(), R.string.report_show_not_view_app);
        }



    }

    public void pdfCreate() {


        String path = getReportFilePath();

        try {
            Document document = new Document(PageSize.A4, 60, 60, 60, 60);
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();
            addContent(document);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void signCreate() {
        if (mSign == null) {
            return;
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(getSignFilePath());
            mSign.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addContent(Document document) throws Exception {

        document.newPage();


        /**
         * Add logo
         */
        ImageView vLogo = (ImageView) getView().findViewById(R.id.vLogo);

        BitmapDrawable bd = (BitmapDrawable) vLogo.getDrawable();

        Bitmap bitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.logo_reporte_2);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100 , stream);
        Image logo = Image.getInstance(stream.toByteArray());


        Report report = reportResult.report;

        /**
         * Add title
         */

        PdfPTable header = new PdfPTable(5);
        PdfPCell cellLogo = new PdfPCell(logo, true);
        cellLogo.setBorder(Rectangle.NO_BORDER);
        header.addCell(cellLogo);

        Phrase phrase = new Phrase();
        phrase.add(new Chunk(visitaName + "\n", fReportTitle));
        phrase.add(new Chunk(reportResult.report.name + "\n", fSectionTitle));
        phrase.add(new Chunk(planificacion + "\n", fSectionRow));

        PdfPCell cellTitle = new PdfPCell(phrase);


        cellTitle.setBorder(Rectangle.NO_BORDER);
        cellTitle.setColspan(4);
        cellTitle.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitle.setVerticalAlignment(Element.ALIGN_MIDDLE);

        header.addCell(cellTitle);
        header.setSpacingBefore(15.0f);
        header.setSpacingAfter(15.0f);
        header.setWidthPercentage(100);

        header.completeRow();

        document.add(header);


        /**
         * Add sections
         */

        for (ReportSection section : report.sections) {
            if (section == null || section.detail.isEmpty()) {
                continue;
            }

            if (section.title != null) {
                document.add(new Paragraph(section.title, fSectionTitle));
            }
            List<SectionDetail> detail = section.detail;

            PdfPTable sTable = new PdfPTable(2);
            sTable.setSpacingBefore(15.0f);
            sTable.setSpacingAfter(15.0f);
            sTable.setWidthPercentage(100);

            boolean first = true;
            for (SectionDetail sectionDetail : detail) {
                if ("rowVacia".equals(sectionDetail.format)) {
                    document.add(sTable);

                    sTable = new PdfPTable(2);
                    sTable.setSpacingBefore(15.0f);
                    sTable.setSpacingAfter(15.0f);
                    sTable.setWidthPercentage(100);
                    first = true;

                } else {
                    addPdfRowSection(sTable, sectionDetail.lbl, sectionDetail.val, sectionDetail.format, first || sectionDetail.isBold());
                    first = false;
                }
            }
            document.add(sTable);
        }

        /**
         * Add sign
         */

        if (mSign != null) {

            PdfPTable t = new PdfPTable(1);
            t.setSpacingBefore(25.0f);
            t.setWidthPercentage(80);

            // Commnet title
            PdfPCell cell = new PdfPCell(new Phrase(getString(R.string.report_sign_lbl_comment), fSectionTitle));
            cell.setBorder(Rectangle.NO_BORDER);
            t.addCell(cell);
            t.completeRow();

            // Comment
            String comment = mSignComment != null ? mSignComment : getString(R.string.report_sign_comment_empty);
            PdfPCell cell2 = new PdfPCell(new Phrase(comment, fSectionRow));
            cell2.setBorder(Rectangle.NO_BORDER);
            t.addCell(cell2);
            t.completeRow();

            // Empty row
            PdfPCell cellEmpty = new PdfPCell(new Phrase("\n", fSectionRow));
            cellEmpty.setBorder(Rectangle.NO_BORDER);
            t.addCell(cellEmpty);
            t.completeRow();

            // Sign title
            PdfPCell cell3 = new PdfPCell(new Phrase(getString(R.string.report_sign_lbl_sign), fSectionTitle));
            cell3.setBorder(Rectangle.NO_BORDER);
            t.addCell(cell3);
            t.completeRow();

            // Sign
            Bitmap bitmapSign = Bitmap.createScaledBitmap(mSign, 300, 150, false);
            ByteArrayOutputStream streamSign = new ByteArrayOutputStream();
            bitmapSign.compress(Bitmap.CompressFormat.PNG, 100 , streamSign);
            Image iSign = Image.getInstance(streamSign.toByteArray());
            iSign.setAlignment(Element.ALIGN_CENTER);
            iSign.setBackgroundColor(BaseColor.LIGHT_GRAY);

            PdfPCell cell4 = new PdfPCell();
            cell4.setPaddingLeft(60);
            cell4.setPaddingRight(60);
            cell4.addElement(iSign);
            cell4.setBorder(Rectangle.NO_BORDER);
            t.addCell(cell4);
            t.completeRow();

            document.add(t);

        }




    }

    private void addPdfRowSection(PdfPTable pSection, String lbl, String val, String format, boolean first) {


        if ("rowCompleta".equals(format)) {

            pSection.addCell(new Phrase(lbl, getRowFont(first)));
            pSection.addCell(new Phrase(val, getRowFont(first)));
            pSection.completeRow();

        } else if ("rowSimple".equals(format)) {
            PdfPCell cell = new PdfPCell(new Phrase(lbl, getRowFont(first)));
            cell.setColspan(2);
            pSection.addCell(cell);
            pSection.completeRow();

        } else if ("rowValor".equals(format)) {
            PdfPCell cell = new PdfPCell(new Phrase(""));
            pSection.addCell(cell);
            pSection.addCell(new Phrase(val, getRowFont(first)));
            pSection.completeRow();

        } else if ("rowVacia".equals(format)) {
            PdfPCell cell = new PdfPCell(new Phrase(""));
            cell.setColspan(2);
            pSection.addCell(cell);
            pSection.completeRow();
        }

    }

    private Font getRowFont(boolean first) {
        if (first) {
            return fSectionTitle;
        } else {
            return fSectionRow;
        }
    }


    private File getDirectory() {
        File filesDir = getActivity().getExternalFilesDir(null);
        return filesDir;
    }

    public void showSignDialog(String comment, SignDialogFragment2.onSignListener onSignedListener) {
        FragmentManager fragmentManager = getFragManager();
        SignDialogFragment2 newFragment = SignDialogFragment2.newInstance(comment);
        newFragment.setOnSignedListener(onSignedListener);
        newFragment.show(fragmentManager, "sign");
    }

    public void showEmailsDialog(List<String> emails) {
        FragmentManager fragmentManager = getFragManager();
        EmailsDialogFragment newFragment = EmailsDialogFragment.newInstance(idVisita, emails, new EmailsDialogFragment.OnFinishVistaListener() {
            @Override
            public void onFinishVisita() {

                Fragment f = getFragManager().findFragmentByTag(HomeActivity.TAG_ESTACION_DETAIL);
                if (f != null && EstacionFragment.class.isInstance(f)) {
                    EstacionFragment ef = (EstacionFragment) f;
                    ef.setUnSavedData(false);
                }
                ToastUtils.showToast(getActivity(), R.string.visita_msg_finished);
                HomeActivity activity = (HomeActivity) getActivity();
                activity.invalidateVisitasAsignadas();
                activity.backToTag(HomeActivity.TAG_HOME);

            }
        });
        newFragment.show(fragmentManager, "emails");
    }

    public void showLoading() {
        showLoading("Cargando...");
    }

    public void showLoading(String msg) {
        FragmentManager fm = getFragManager();
        ProgressDialogFragment f = ProgressDialogFragment.newInstance(msg);
        f.show(fm, "loading");
    }

    public void hideLoading() {
        FragmentManager fm = getFragManager();
        Fragment loading = fm.findFragmentByTag("loading");
        if (loading != null) {
            ProgressDialogFragment pdf = (ProgressDialogFragment) loading;
            pdf.dismiss();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public FragmentManager getFragManager() {
        return getFragmentManager();
    }



}
