package com.bilpa.android.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bilpa.android.R;
import com.bilpa.android.model.Estacion;
import com.bilpa.android.model.VisitaAsignadas;

import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EstacionesAdapter extends FiltrableAdapter<VisitaAsignadas> {

    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());

    public EstacionesAdapter(Context context, List<VisitaAsignadas> items) {
        super(context, 0, items);
    }

    @Override
    protected boolean filterItem(VisitaAsignadas item, String filter) {

        if (item.estacion == null) {
            return false;
        }

        if (item.estacion.nombre == null) {
            return false;
        }

        if (item.estacion.nombre.toLowerCase().contains(filter)) {
            return true;
        }

        return false;
    }

    public Date mToday = Calendar.getInstance().getTime();

    @Override
    protected View newView(int position, ViewGroup parent, VisitaAsignadas item) {
        View v = mInflater.inflate(R.layout.row_estaciones_list, parent, false);


        ViewHolder holder = new ViewHolder();
        holder.vEstacionName = (TextView) v.findViewById(R.id.vEstacionName);
        holder.vZone = (TextView) v.findViewById(R.id.vZone);
        holder.vDateLastVisit = (TextView) v.findViewById(R.id.vDateLastVisit);
        holder.vDateNextVisit = (TextView) v.findViewById(R.id.vDateNextVisit);
        holder.vDaysWithoutVisit = (TextView) v.findViewById(R.id.vDaysWithoutVisit);
        holder.vStatus = (TextView) v.findViewById(R.id.vStatus);

        v.setTag(holder);

        return v;
    }

    @Override
    protected void bindView(View v, VisitaAsignadas item, int position) {
        ViewHolder h = (ViewHolder) v.getTag();
        h.vEstacionName.setText(item.estacion.nombre);
        h.vZone.setText(item.estacion.zona);

        if (item.fechaProximaVisita != null) {
            h.vDateNextVisit.setText(sdf.format(item.fechaProximaVisita));
        } else {
            h.vDateNextVisit.setText("-");
        }
        if (item.fechaUltimaVisita != null) {
            h.vDateLastVisit.setText(sdf.format(item.fechaUltimaVisita));
        } else {
            h.vDateLastVisit.setText("-");
        }

        h.vDaysWithoutVisit.setText(String.valueOf(item.diasSinVisitas));
        if (item.diasSinVisitas >= 120) {

            int red = getContext().getResources().getColor(R.color.b_actionBarTextColor);

            h.vEstacionName.setTextColor(red);
            h.vZone.setTextColor(red);
            h.vDateLastVisit.setTextColor(red);
            h.vDateNextVisit.setTextColor(red);
            h.vDaysWithoutVisit.setTextColor(red);
            h.vStatus.setTextColor(red);
            h.vDaysWithoutVisit.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_alert, 0);
        } else {

            int priColor = getContext().getResources().getColor(R.color.b_textColorPrimary);
            int secColor = getContext().getResources().getColor(R.color.b_textColorSecondary);

            h.vEstacionName.setTextColor(priColor);
            h.vZone.setTextColor(secColor);
            h.vDateLastVisit.setTextColor(secColor);
            h.vDateNextVisit.setTextColor(secColor);
            h.vDaysWithoutVisit.setTextColor(secColor);
            h.vStatus.setTextColor(secColor);
            h.vDaysWithoutVisit.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_alert_invisible, 0);
        }

        if (position % 2 == 0) {
            v.setBackgroundResource(R.drawable.even_list_selector_holo_light);
        } else {
            v.setBackgroundResource(R.drawable.odd_list_selector_holo_light);
        }

        h.vStatus.setText(item.estado.toUpperCase());
    }

    private class ViewHolder {
        public TextView vEstacionName;
        public TextView vZone;
        public TextView vDateLastVisit;
        public TextView vDateNextVisit;
        public TextView vDaysWithoutVisit;
        public TextView vStatus;
    }
}
