package com.bilpa.android.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.bilpa.android.R;
import com.bilpa.android.model.Activo;
import com.bilpa.android.model.Repuesto;
import com.bilpa.android.model.TipoRepuesto;
import com.mautibla.utils.bitmaps.ImageFetcher;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RepuestosAdapter extends FiltrableAdapter<Repuesto> implements Filterable {

    public TipoRepuesto selectedTipoRepuesto;

    public RepuestosAdapter(Context context, List<Repuesto> items) {
        super(context, 0, items);
    }

    public Date mToday = Calendar.getInstance().getTime();

    @Override
    protected View newView(int position, ViewGroup parent, Repuesto item) {
        View v = mInflater.inflate(R.layout.row_repuestos_list, parent, false);

        ViewHolder holder = new ViewHolder();
        holder.vRepuestoDesc = (TextView) v.findViewById(R.id.vRepuestoDesc);
        holder.vNroSerie = (TextView) v.findViewById(R.id.vNroSerie);
        v.setTag(holder);

        return v;
    }

    @Override
    protected void bindView(View v, Repuesto item, int position) {
        ViewHolder h = (ViewHolder) v.getTag();
        h.vRepuestoDesc.setText(item.descripcion);
        h.vNroSerie.setText(item.nroSerie);
        if (position % 2 == 0) {
            v.setBackgroundResource(R.drawable.even_list_selector_holo_light);
        } else {
            v.setBackgroundResource(R.drawable.odd_list_selector_holo_light);
        }
    }

    private class ViewHolder {
        public TextView vRepuestoDesc;
        public TextView vNroSerie;
    }

    @Override
    protected boolean filterItem(Repuesto item, String filter) {

        if (item.descripcion == null) {
            return false;
        }

        if (selectedTipoRepuesto != null && !selectedTipoRepuesto.equals(item.tipoRepuesto)) {
            return false;
        }

        if (item.descripcion.toLowerCase().contains(filter)) {
            return true;
        }
        if (item.nroSerie.toLowerCase().contains(filter)) {
            return true;
        }



        return false;
    }
}
