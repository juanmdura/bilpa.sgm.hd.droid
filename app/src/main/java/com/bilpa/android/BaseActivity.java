package com.bilpa.android;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.bilpa.android.fragments.BaseFragment;
import com.bilpa.android.fragments.DestinoCargoDialogFragment;
import com.bilpa.android.fragments.FallasDialogFragment;
import com.bilpa.android.fragments.ListSelectDialogFragment;
import com.bilpa.android.fragments.OrganizacionesDialogFragment;
import com.bilpa.android.fragments.PicoSelectDialogFragment;
import com.bilpa.android.fragments.ProgressDialogFragment;
import com.bilpa.android.fragments.RepuestosDialogFragment;
import com.bilpa.android.fragments.TareasDialogFragment;
import com.bilpa.android.utils.Logger;

public abstract class BaseActivity extends AppCompatActivity {

    public static final String TAG = "BaseActivity";


    public abstract void onBackPressedImpl();

    @Override
    public void onBackPressed() {
        Fragment f = peekFragment();
        if (BaseFragment.class.isInstance(f)) {
            BaseFragment bf = (BaseFragment) f;
            bf.onBack();
        } else {
            onBackPressedImpl();
        }
    }

    /**
     * Frag transactions
     */

    protected void goToFragment(Fragment newFragment, String tag) {

        String peekTag = peekTag();
        if (peekTag != null && peekTag.equals(tag)) {
            // Do nothing. Fragment is showing
            return;
        }

        if (backToTag(tag) != null) {
            return;
        }

        FragmentManager fm = getFragManager();
        FragmentTransaction t = fm.beginTransaction();

        if (peekTag == null) {
            t.add(R.id.container, newFragment, tag);
        } else {
            t.replace(R.id.container, newFragment, tag);
        }

        t.addToBackStack(tag);
        t.commit();
    }

    public final String peekTag() {
        FragmentManager fm = getFragManager();
        if (fm.getBackStackEntryCount() > 0) {
            String fragmentTag = fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 1).getName();
            if (fragmentTag != null) {
                return fragmentTag;
            }
        }
        return null;
    }

    public final String peekPreviuosTag() {
        FragmentManager fm = getFragManager();
        if (fm.getBackStackEntryCount() > 1) {
            String fragmentTag = fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 2).getName();
            if (fragmentTag != null) {
                return fragmentTag;
            }
        }
        return null;
    }

    public Fragment peekFragment() {
        String tag = peekTag();
        if (tag != null) {
            Fragment f = getFragManager().findFragmentByTag(tag);
            return f;
        }
        return null;
    }

    public Fragment getFragmentByTag(String tag) {
        return getFragManager().findFragmentByTag(tag);
    }

    public Fragment backToTag(String tag) {
        FragmentManager fm = getFragManager();
        Fragment f = fm.findFragmentByTag(tag);
        if (f != null) {
            fm.popBackStack(tag, 0);
            return f;
        } else {
            return null;
        }
    }

    public void backToInit() {
        FragmentManager fm = getFragManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    public Fragment removeToTag(String tag) {
        FragmentManager fm = getFragManager();
        Fragment f = fm.findFragmentByTag(tag);
        if (f != null) {
            fm.popBackStack(tag, 1);
            return f;
        } else {
            return null;
        }
    }

    public FragmentManager getFragManager() {
        return getSupportFragmentManager();
    }

    public final void logStack() {
        FragmentManager fm = getFragManager();
        int count = fm.getBackStackEntryCount();
        if (count > 0) {

            for (int i = count - 1; i >= 0; i--) {
                FragmentManager.BackStackEntry backStackEntryAt = fm.getBackStackEntryAt(i);
                String name = backStackEntryAt.getName();
                Logger.w(TAG, "Stack index=" + i + " " + name);
            }
            Logger.w(TAG, "Stack Counts = " + count);
        } else {
            Logger.w(TAG, "Stack is empty");
        }
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

}
