package com.bilpa.android.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bilpa.android.R;
import com.bilpa.android.model.VisitaAsignadas;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EstacionesAdapter2 extends RecyclerView.Adapter<EstacionesAdapter2.ViewHolder> {

    private final Context mContext;
    private final List<VisitaAsignadas> mItems;

    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());

    public EstacionesAdapter2(Context context, List<VisitaAsignadas> items) {
        mContext = context;
        mItems = items;
    }

//    @Override
//    protected boolean filterItem(VisitaAsignadas item, String filter) {
//
//        if (item.estacion == null) {
//            return false;
//        }
//
//        if (item.estacion.nombre == null) {
//            return false;
//        }
//
//        if (item.estacion.nombre.toLowerCase().contains(filter)) {
//            return true;
//        }
//
//        return false;
//    }

    public Date mToday = Calendar.getInstance().getTime();

    public int getItemCount() {
        return mItems.size();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_estaciones_list, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder h, int position) {

        VisitaAsignadas item = mItems.get(position);

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

            int red = mContext.getResources().getColor(R.color.b_actionBarTextColor);

            h.vEstacionName.setTextColor(red);
            h.vZone.setTextColor(red);
            h.vDateLastVisit.setTextColor(red);
            h.vDateNextVisit.setTextColor(red);
            h.vDaysWithoutVisit.setTextColor(red);
            h.vStatus.setTextColor(red);
            h.vDaysWithoutVisit.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_alert, 0);
        } else {

            int priColor = mContext.getResources().getColor(R.color.b_textColorPrimary);
            int secColor = mContext.getResources().getColor(R.color.b_textColorSecondary);

            h.vEstacionName.setTextColor(priColor);
            h.vZone.setTextColor(secColor);
            h.vDateLastVisit.setTextColor(secColor);
            h.vDateNextVisit.setTextColor(secColor);
            h.vDaysWithoutVisit.setTextColor(secColor);
            h.vStatus.setTextColor(secColor);
            h.vDaysWithoutVisit.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_alert_invisible, 0);
        }

        if (position % 2 == 0) {
            h.itemView.setBackgroundResource(R.drawable.even_list_selector_holo_light);
        } else {
            h.itemView.setBackgroundResource(R.drawable.odd_list_selector_holo_light);
        }

        h.vStatus.setText(item.estado.toUpperCase());
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView vEstacionName;
        public TextView vZone;
        public TextView vDateLastVisit;
        public TextView vDateNextVisit;
        public TextView vDaysWithoutVisit;
        public TextView vStatus;

        public ViewHolder(View v) {
            super(v);

            vEstacionName = (TextView) v.findViewById(R.id.vEstacionName);
            vZone = (TextView) v.findViewById(R.id.vZone);
            vDateLastVisit = (TextView) v.findViewById(R.id.vDateLastVisit);
            vDateNextVisit = (TextView) v.findViewById(R.id.vDateNextVisit);
            vDaysWithoutVisit = (TextView) v.findViewById(R.id.vDaysWithoutVisit);
            vStatus = (TextView) v.findViewById(R.id.vStatus);
        }

    }
}
