package com.bilpa.android.fragments;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bilpa.android.BilpaApp;
import com.bilpa.android.HomeActivity;
import com.bilpa.android.R;
import com.bilpa.android.adapters.EstacionesAdapter3;
import com.bilpa.android.adapters.FiltrableRecycleAdapter;
import com.bilpa.android.model.VisitaAsignadas;
import com.bilpa.android.services.ApiService;
import com.bilpa.android.services.AsyncCallback;
import com.bilpa.android.services.actions.GetVisitasResult;
import com.bilpa.android.services.actions.LoginResult;
import com.bilpa.android.utils.Consts;
import com.bilpa.android.utils.RecyclerItemClickListener;
import com.bilpa.android.utils.SessionStore;
import com.crashlytics.android.Crashlytics;
import com.mautibla.utils.ViewUtils;

import java.util.List;


public class HomeFragment extends ABFragment implements RootFragment, SearchView.OnQueryTextListener, Toolbar.OnMenuItemClickListener {

    public RecyclerView vListEstaciones;
    private SearchView mSearchView;
    private ProgressBar vProgress;
    private TextView vEmptyView;
    private EstacionesAdapter3 adapter;
    private List<VisitaAsignadas> mItems;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public HomeFragment() {
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
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        vListEstaciones = (RecyclerView) v.findViewById(R.id.vListEstaciones);
        vProgress = (ProgressBar) v.findViewById(R.id.vProgress);
        vEmptyView = (TextView) v.findViewById(R.id.vEmptyView);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((HomeActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        Toolbar toolbar = ((HomeActivity) getActivity()).getToolbar();
        toolbar.setOnMenuItemClickListener(this);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        vListEstaciones.setLayoutManager(mLayoutManager);

        Crashlytics.setString(Consts.CrashKeys.seccion, "Preventivos");

        load();
    }

    private void load() {
        ViewUtils.gone(vListEstaciones, vEmptyView);
        ViewUtils.visible(vProgress);

        GetVisitasResult mVisitasAsignadas = ((HomeActivity) getActivity()).mVisitasAsignadas;

        if (mVisitasAsignadas == null) {

            int userId = SessionStore.getSession(getActivity()).id;

            ApiService.getVisitasAsignadas(ApiService.GET_VISITAS_ASIGNADAS, userId, new AsyncCallback<GetVisitasResult>(getActivity()) {
                @Override
                protected void onSuccess(GetVisitasResult result) {
                    ((HomeActivity) getActivity()).mVisitasAsignadas = result;
                    bindData(result);
                }
            });
        } else {
            bindData(mVisitasAsignadas);
        }
    }

    private void bindData(GetVisitasResult result) {
        ViewUtils.visible(vListEstaciones);
        ViewUtils.gone(vProgress);

        mItems = result.visitaAsignadas;

        if (mItems.isEmpty()) {
            ViewUtils.visible(vEmptyView);
        } else {
            ViewUtils.gone(vEmptyView);
        }

        adapter = new EstacionesAdapter3(getActivity(), mItems);
        adapter.setFilterListener(new FiltrableRecycleAdapter.FilterListener() {
            @Override
            public void onFilter() {
                showEmptyMsg(R.string.home_search_empty);
            }
            @Override
            public void onNonResults() {
                showEmptyMsg(R.string.home_search_empty_filter);
            }
        });
        vListEstaciones.setAdapter(adapter);
        vListEstaciones.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                VisitaAsignadas item = adapter.getItem(position);
                ((HomeActivity) getActivity()).goToEstacionDetail(item.id, item.estacion);
            }
        }));

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        BilpaApp.getInstance().cancelPendingRequests(ApiService.GET_VISITAS_ASIGNADAS);
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }


    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
        super.onCreateOptionsMenu(menu, inflater);

        mSearchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.vMenuSearch));
        mSearchView.setOnQueryTextListener(this);

        MenuItem mUser = menu.findItem(R.id.vMenuUser);
        String[] roles = getResources().getStringArray(R.array.array_roles);
        LoginResult.Session session = SessionStore.getSession(getActivity());
        mUser.setTitle(String.format("%s %s", session.nombre, session.apellido));

    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if (adapter != null) {
            adapter.getFilter().filter(s);
        }
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case R.id.vMenuUpdate:
                ((HomeActivity) getActivity()).invalidateVisitasAsignadas();
                load();
                break;

            case R.id.vMenuExit:
                SessionStore.clearSession(getActivity());
                getActivity().finish();
                break;
        }

        return true;
    }
}
