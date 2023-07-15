package com.bilpa.android.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bilpa.android.BilpaApp;
import com.bilpa.android.CorrectivosActivity;
import com.bilpa.android.CorrectivosDetailActivity;
import com.bilpa.android.R;
import com.bilpa.android.adapters.FiltrableRecycleAdapter;
import com.bilpa.android.model.correctivos.Correctivo;
import com.bilpa.android.services.ApiCorrectivos;
import com.bilpa.android.services.AsyncCallback;
import com.bilpa.android.services.actions.LoginResult;
import com.bilpa.android.services.correctivos.GetCorrectivosResult;
import com.bilpa.android.utils.Consts;
import com.bilpa.android.utils.RecyclerItemClickListener;
import com.bilpa.android.utils.RecyclerItemClickListener.OnItemClickListener;
import com.bilpa.android.utils.SessionStore;
import com.mautibla.utils.ViewUtils;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;


public class CorrectivosFragment extends Fragment implements SearchView.OnQueryTextListener, Toolbar.OnMenuItemClickListener {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());

    GetCorrectivosResult mCorrectivosResult;

    private RecyclerView vListCorrectivos;
    private ProgressBar vProgress;
    private TextView vEmptyView;
    private SearchView mSearchView;
    private MyAdapter mAdapter;

    public static CorrectivosFragment newInstance() {
        CorrectivosFragment fragment = new CorrectivosFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public CorrectivosFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_correctivos, container, false);

        vListCorrectivos = (RecyclerView) v.findViewById(R.id.vListCorrectivos);
        vProgress = (ProgressBar) v.findViewById(R.id.vProgress);
        vEmptyView = (TextView) v.findViewById(R.id.vEmptyView);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((CorrectivosActivity) getActivity()).toolbar.setOnMenuItemClickListener(this);

        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 3);
        vListCorrectivos.setLayoutManager(mLayoutManager);
        vListCorrectivos.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Correctivo correctivo = mCorrectivosResult.correctivos.get(position);
                Intent intent = new Intent(getActivity(), CorrectivosDetailActivity.class);
                intent.putExtra("correctivo", correctivo);
                startActivityForResult(intent, Consts.Request.CORRECTIVOS);
            }
        }));


        load();

    }

    private void load() {

        if (mCorrectivosResult == null) {
            ViewUtils.gone(vListCorrectivos, vEmptyView);
            ViewUtils.visible(vProgress);

            int userId = SessionStore.getSession(getActivity()).id;
            ApiCorrectivos.getCorrectivos(userId, new AsyncCallback<GetCorrectivosResult>(getActivity()) {
                @Override
                protected void onSuccess(GetCorrectivosResult result) {
                    mCorrectivosResult = result;
                    bindCorrectivos(result);
                }
            });

        } else {
            bindCorrectivos(mCorrectivosResult);
        }


    }

    private void bindCorrectivos(GetCorrectivosResult result) {

        ViewUtils.visible(vListCorrectivos);
        ViewUtils.gone(vProgress);

        if (result.correctivos.isEmpty()) {
            ViewUtils.visible(vEmptyView);
        } else {
            ViewUtils.gone(vEmptyView);
        }

        mAdapter = new MyAdapter(getActivity(), result.correctivos);
        mAdapter.setFilterListener(new FiltrableRecycleAdapter.FilterListener() {
            @Override
            public void onFilter() {
                showEmptyMsg(R.string.correctivos_items_empty);
            }
            @Override
            public void onNonResults() {
                showEmptyMsg(R.string.correctivos_items_empty_filter);
            }
        });
        vListCorrectivos.setAdapter(mAdapter);

    }

    private void showEmptyMsg(int msg) {
        final String strMsg = getString(msg);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                vEmptyView.setText(strMsg);
            }
        });
    }


    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home_correctivos, menu);
        super.onCreateOptionsMenu(menu, inflater);

        mSearchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.vMenuSearch));
        mSearchView.setOnQueryTextListener(this);

        MenuItem mUser = menu.findItem(R.id.vMenuUser);
        String[] roles = getResources().getStringArray(R.array.array_roles);
        LoginResult.Session session = SessionStore.getSession(getActivity());
        mUser.setTitle(String.format("%s %s", session.nombre, session.apellido));

    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case R.id.vMenuUpdate:
                mCorrectivosResult = null;
                load();
                break;

            case R.id.vMenuExit:
                SessionStore.clearSession(getActivity());
                getActivity().finish();
                break;
        }

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if (mAdapter != null) {
            mAdapter.getFilter().filter(s);
        }
        return true;
    }

    public class MyAdapter extends FiltrableRecycleAdapter<Correctivo, MyAdapter.ViewHolder> {

        public MyAdapter(Context context, List<Correctivo> myDataset) {
            super(context, myDataset);
        }

        @Override
        protected boolean filterItem(Correctivo item, String filter) {

            if (item.estacion == null) {
                return false;
            }

            if (item.estacion.toLowerCase().contains(filter)) {
                return true;
            }

            return false;
        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_estaciones_list2, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Correctivo item = mData.get(position);

            holder.vCardView.setPreventCornerOverlap(false);

            // Nombre estacion
            holder.vEstacionName.setText(item.estacion);


            // Imagen
            ((CorrectivosActivity) getActivity()).mImageFetcher.loadImage(item.fotoEstacionChica, holder.vPhoto);


            // Localidad
            holder.vLocalidad.setText(String.format(getContext().getString(R.string.correctivos_list_zone), item.localidad));

            // Nro orden
            holder.vOrderNumber.setText(String.format(getString(R.string.correctivo_detail_sello_nro), item.sello, item.id));

            // Inicio
            holder.vDateInit.setText(String.format(getContext().getString(R.string.correctivos_list_init), sdf.format(item.fechaInicio)));

            // Cumplimiento
            if (item.fechaCumplimiento2 != null) {
                ViewUtils.visible(holder.vDateEnd);
                holder.vDateEnd.setText(String.format(getContext().getString(R.string.correctivos_list_end), sdf.format(item.fechaCumplimiento2)));
            } else {
                ViewUtils.gone(holder.vDateEnd);
            }


            // Plazo
            holder.vDatePlazo.setText(String.format(getContext().getString(R.string.correctivos_list_plazo), sdf.format(item.plazo)));

            // Status
            holder.vStatus.setText(item.getStatusLabel(getActivity()));
            holder.vStatus.setBackgroundResource(item.getStatusBg());

        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            private TextView vDatePlazo;
            public ImageView vPhoto;
            public TextView vEstacionName;
            public TextView vLocalidad;
            private TextView vOrderNumber;
            private TextView vDateInit;
            private TextView vDateEnd;
            private TextView vStatus;
            public CardView vCardView;

            public ViewHolder(View v) {
                super(v);
                vCardView = (CardView) v.findViewById(R.id.card_view);
                vEstacionName = (TextView) v.findViewById(R.id.vEstacionName);
                vPhoto = (ImageView) v.findViewById(R.id.vPhoto);
                vLocalidad = (TextView) v.findViewById(R.id.vLocalidad);
                vOrderNumber = (TextView) v.findViewById(R.id.vOrderNumber);
                vDateInit = (TextView) v.findViewById(R.id.vDateInit);
                vDateEnd = (TextView) v.findViewById(R.id.vDateEnd);
                vDatePlazo = (TextView) v.findViewById(R.id.vDatePlazo);
                vStatus = (TextView) v.findViewById(R.id.vStatus);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Consts.Request.CORRECTIVOS && resultCode == Activity.RESULT_OK) {
            mCorrectivosResult = null;
            load();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        BilpaApp app = BilpaApp.getInstance();
        app.cancelPendingRequests(ApiCorrectivos.GET_CORRECTIVOS);
    }
}
