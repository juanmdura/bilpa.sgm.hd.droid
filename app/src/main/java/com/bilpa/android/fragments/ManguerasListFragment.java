package com.bilpa.android.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bilpa.android.BilpaApp;
import com.bilpa.android.MangueraActivity;
import com.bilpa.android.R;
import com.bilpa.android.adapters.ManguerasAdapter;
import com.bilpa.android.model.Pico;
import com.bilpa.android.services.ApiService;
import com.bilpa.android.services.AsyncCallback;
import com.bilpa.android.services.actions.BaseResult;
import com.bilpa.android.services.actions.PicosResult;
import com.bilpa.android.services.actions.activos.GetPicoIdByQrResult;
import com.bilpa.android.utils.MessageUtils;
import com.mautibla.utils.ToastUtils;
import com.mautibla.utils.ViewUtils;

public class ManguerasListFragment extends Fragment implements AdapterView.OnItemClickListener, Toolbar.OnMenuItemClickListener {


    private ListView vListMangueras;
    private ProgressBar vProgress;
    private TextView vEmptyView;
    private Long mSurtidorId;
    private LinearLayout vData;
    private Toolbar toolbar;
    private ManguerasAdapter mManguerasAdapter;

    public static ManguerasListFragment newInstance(Long surtidorId) {
        ManguerasListFragment fragment = new ManguerasListFragment();
        fragment.mSurtidorId = surtidorId;
        return fragment;
    }

    public ManguerasListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mangueras_list, container, false);


        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        vData = (LinearLayout) v.findViewById(R.id.vData);
        vProgress = (ProgressBar) v.findViewById(R.id.vProgress);
        vEmptyView = (TextView) v.findViewById(R.id.vEmptyView);
        vListMangueras = (ListView) v.findViewById(R.id.vListMangueras);

        vListMangueras.setEmptyView(vEmptyView);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        toolbar.setTitle("");
        toolbar.inflateMenu(R.menu.menu_manguera_list);
        toolbar.setOnMenuItemClickListener(this);


        load();

    }

    private void load() {

        ViewUtils.visible(vProgress);
        ViewUtils.gone(vData, vEmptyView);

        PicosResult mPicosResult = ((MangueraActivity) getActivity()).mPicosResult;
        if (mPicosResult == null ) {
            ApiService.getPicos(mSurtidorId, new AsyncCallback<PicosResult>(getActivity()) {
                @Override
                protected void onSuccess(PicosResult result) {
                    ((MangueraActivity) getActivity()).mPicosResult = result;
                    bind(result);
                }
            });
        } else {
            bind(mPicosResult);
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        BilpaApp app = BilpaApp.getInstance();
        app.cancelPendingRequests(ApiService.GET_PICOS);
        app.cancelPendingRequests(ApiService.SET_QR_PICO);
        app.cancelPendingRequests(ApiService.GET_PICO_BY_QR);

    }



    @Override
    public void onDetach() {
        super.onDetach();
    }


    private void bind(PicosResult result) {

        ViewUtils.visible(vData);
        ViewUtils.gone(vProgress);
        mManguerasAdapter = new ManguerasAdapter(getActivity(), result.getPicos());
        vListMangueras.setAdapter(mManguerasAdapter);
        vListMangueras.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ManguerasAdapter adapter = (ManguerasAdapter) parent.getAdapter();
        Pico pico = adapter.getItem(position);
        ((MangueraActivity) getActivity()).goToMangueraDetail(position+1, pico);
        vListMangueras.setItemChecked(position, true);
    }

    public void setActivated(int position) {
        vListMangueras.setItemChecked(position, true);
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {

        switch (menuItem.getItemId()) {

            case R.id.vMenuReadQR:
                ((MangueraActivity) getActivity()).showQR(new QRDialogFragment.QRListener() {
                    @Override
                    public void onQueryCode(final String code) {

                        try {
                            int codeInt = Integer.valueOf(code);
                            ToastUtils.showToast(getActivity(), String.format(getString(R.string.mangueras_msg_readed_code), code));
                        } catch (NumberFormatException e) {
                            MessageUtils.showMsg(getActivity(), String.format(getString(R.string.mangueras_msg_qr_must_int), code));
                            return;
                        }


                        ((MangueraActivity) getActivity()).showLoading();

                        Long mVisita = ((MangueraActivity) getActivity()).mEstacion.id;

                        ApiService.getPicoIdByQr(code, mVisita, new AsyncCallback<GetPicoIdByQrResult>(getActivity()) {
                            @Override
                            protected void onSuccess(GetPicoIdByQrResult result) {
                                ((MangueraActivity) getActivity()).hideLoading();

                                if (result.picoId != -1) {
                                    // Pico encontrado
                                    Pico pico = findPicoById(result.picoId);
                                    if (pico != null) {
                                        int picoIndex = findPicoIndex(pico);
                                        vListMangueras.setItemChecked(picoIndex-1, true);
                                        ((MangueraActivity) getActivity()).goToMangueraDetail(picoIndex, pico);
                                    } else {
                                        MessageUtils.showMsg(getActivity(), R.string.mangueras_msg_pico_no_encontrado);
                                    }

                                } else {
                                    showListPicos(code);
                                }
                            }

                            @Override
                            public void onError(String msg, MaterialDialog.ButtonCallback callback) {
                                super.onError(msg, callback);
                                ((MangueraActivity) getActivity()).hideLoading();
                            }

                        });
                    }
                });
                break;
        }

        return true;


    }

    private Pico findPicoById(Long picoId) {
        Long id = Long.valueOf(picoId);
        PicosResult mPicosResult = ((MangueraActivity) getActivity()).mPicosResult;
        for (Pico p : mPicosResult.picos) {
            if (p.id == id.longValue()) {
                return p;
            }
        }
        return null;
    }

    private int findPicoIndex(Pico pico) {
        PicosResult mPicosResult = ((MangueraActivity) getActivity()).mPicosResult;
        return mPicosResult.getPicos().indexOf(pico) + 1;
    }

    private void showListPicos(final String qrCode) {
        PicosResult mPicosResult = ((MangueraActivity) getActivity()).mPicosResult;

        ((MangueraActivity) getActivity()).showPicos(new PicoSelectDialogFragment.OnPicoSelectedListener() {
            @Override
            public void onPicoSelected(final Pico pico) {
                ApiService.setQrToPico(pico.id, qrCode, new AsyncCallback<BaseResult>(getActivity()) {
                    @Override
                    protected void onSuccess(BaseResult result) {
                        int picoIndex = findPicoIndex(pico);
                        pico.codigoQR = qrCode;
                        updatePico(pico);
                        vListMangueras.setItemChecked(picoIndex-1, true);
                        ((MangueraActivity) getActivity()).goToMangueraDetail(picoIndex, pico);
                    }
                });
            }
        });
    }

    public void updatePico(Pico newPico) {
        PicosResult picosResult = ((MangueraActivity) getActivity()).mPicosResult;
        if (picosResult != null) {
            if (picosResult.getPicos().contains(newPico)) {
                int index = picosResult.getPicos().indexOf(newPico);
                Pico pico = picosResult.getPicos().get(index);
                pico.codigoQR = newPico.codigoQR;
                mManguerasAdapter.notifyDataSetChanged();
            }
        }
    }
}
