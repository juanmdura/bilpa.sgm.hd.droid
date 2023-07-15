package com.bilpa.android.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bilpa.android.R;
import com.bilpa.android.model.Activo;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SurtidorAdapter extends BaseAdapter<Activo> {

    public SurtidorAdapter(Context context, List<Activo> items) {
        super(context, 0, items);
    }

    public Date mToday = Calendar.getInstance().getTime();

    @Override
    protected View newView(int position, ViewGroup parent, Activo item) {
        View v = mInflater.inflate(R.layout.row_activos_list, parent, false);


        ViewHolder holder = new ViewHolder();
        holder.vActivoName = (TextView) v.findViewById(R.id.vActivoName);
        holder.vActivoType = (TextView) v.findViewById(R.id.vActivoType);
        v.setTag(holder);

        return v;
    }

    @Override
    protected void bindView(View v, Activo item, int position) {
        ViewHolder h = (ViewHolder) v.getTag();
        h.vActivoName.setText(item.descripcion);
        h.vActivoType.setText(item.tipo);
        if (position % 2 == 0) {
            v.setBackgroundResource(R.drawable.even_list_selector_holo_light);
        } else {
            v.setBackgroundResource(R.drawable.odd_list_selector_holo_light);
        }
    }

    private class ViewHolder {
        public TextView vActivoName;
        public TextView vActivoType;
    }
}
