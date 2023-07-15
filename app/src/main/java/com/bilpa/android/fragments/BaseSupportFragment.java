package com.bilpa.android.fragments;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.bilpa.android.BaseSupportActivity;
import com.bilpa.android.model.correctivos.CActivo;

import java.util.List;

public abstract class BaseSupportFragment extends Fragment {

    public void onBack() {
        if (BaseSupportActivity.class.isInstance(getActivity())) {
            BaseSupportActivity ba = (BaseSupportActivity) getActivity();
            ba.onBackPressedImpl();
        } else {
            getActivity().onBackPressed();
        }
    }

    public void showLoading() {
        showLoading("Cargando...");
    }

    public void showLoading(String msg) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        ProgressDialogFragment f = ProgressDialogFragment.newInstance(msg);
        f.show(fm, "loading");
    }

    public void hideLoading() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        Fragment loading = fm.findFragmentByTag("loading");
        if (loading != null) {
            ProgressDialogFragment pdf = (ProgressDialogFragment) loading;
            pdf.dismiss();
        }
    }

    public void showOrganizaciones(ListSelectDialogFragment.OnListItemClick onListItemListener) {
        FragmentManager fm = getFragManager();
        OrganizacionesDialogFragment f = new OrganizacionesDialogFragment();
        f.setOnListItemListener(onListItemListener);
        f.show(fm, "organizaciones_dialog");
    }

    public void showRepuestos(RepuestosDialogFragment.OnRepuestoSelectedListener onRepuestoSelectedListener) {
        FragmentManager fragmentManager = getFragManager();
        RepuestosDialogFragment newFragment = new RepuestosDialogFragment();
        newFragment.setOnRepuestoSelectedListener(onRepuestoSelectedListener);
        newFragment.show(fragmentManager, "repuestos_dialog");
    }

    public void showTareas(TareasDialogFragment.OnTareaSelectedListener onTareaSelectedListener) {
        FragmentManager fragmentManager = getFragManager();
        TareasDialogFragment newFragment = new TareasDialogFragment();
        newFragment.setOnTareaSelectedListener(onTareaSelectedListener);
        newFragment.show(fragmentManager, "tareas_search_dialog");
    }

    public void showFallas(FallasDialogFragment.OnFallaSelectedListener onFallaSelectedListener) {
        FragmentManager fragmentManager = getFragManager();
        FallasDialogFragment newFragment = new FallasDialogFragment();
        newFragment.setOnFallaSelectedListener(onFallaSelectedListener);
        newFragment.show(fragmentManager, "fallas_dialog");
    }

    public void showPicos(Long activoId, PicoSelectDialogFragment.OnPicoSelectedListener onPicoSelectedListener) {
        FragmentManager fm = getFragManager();
        PicoSelectDialogFragment f = PicoSelectDialogFragment.newInstance(activoId);
        f.setOnPicoSelectedListener(onPicoSelectedListener);
        f.show(fm, "picos_dialog");
    }

    public void showDestinoCargos(ListSelectDialogFragment.OnListItemClick onListItemListener) {
        FragmentManager fm = getFragManager();
        DestinoCargoDialogFragment f = new DestinoCargoDialogFragment();
        f.setOnListItemListener(onListItemListener);
        f.show(fm, "destinos_dialog");
    }


   public void showQR(QRDialogFragment.QRListener qrListener) {
        FragmentManager fragmentManager = getFragManager();
        QRDialogFragment newFragment = new QRDialogFragment();
        newFragment.setQRListener(qrListener);
        newFragment.show(fragmentManager, "qr");
    }

    public void showQR(final String title, QRDialogFragment.QRListener qrListener) {
        FragmentManager fragmentManager = getFragManager();
        QRDialogFragment newFragment = QRDialogFragment.newInstance();
        newFragment.setTitle(title);
        newFragment.show(fragmentManager, "qr");
    }

    public void showQRSetActivo(List<CActivo> activos, CActivoSelectDialogFragment.OnSelectActivoListener onSelectActivoListener) {
        FragmentManager fm = getFragManager();
        CActivoSelectDialogFragment f = CActivoSelectDialogFragment.newInstance(activos);
        f.setOnSelectActivoListener(onSelectActivoListener);
        f.show(fm, "showActivos");
    }


    public void showSignDialog(String comment, SignDialogFragment2.onSignListener onSignedListener) {
        FragmentManager fragmentManager = getFragManager();
        SignDialogFragment2 newFragment = SignDialogFragment2.newInstance(comment);
        newFragment.setOnSignedListener(onSignedListener);
        newFragment.show(fragmentManager, "sign");
    }

    public FragmentManager getFragManager() {
        return getActivity().getSupportFragmentManager();
    }
}



