package com.bilpa.android.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.TextView;

import com.bilpa.android.R;
import com.bilpa.android.model.Falla;
import com.bilpa.android.model.TipoFalla;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FallasAdapter extends FiltrableAdapter<Falla> implements Filterable {

    public TipoFalla selected;

    public FallasAdapter(Context context, List<Falla> items) {
        super(context, 0, items);
    }

    public Date mToday = Calendar.getInstance().getTime();

    @Override
    protected View newView(int position, ViewGroup parent, Falla item) {
        View v = mInflater.inflate(R.layout.row_fallas_list, parent, false);

        ViewHolder holder = new ViewHolder();
        holder.vDesc = (TextView) v.findViewById(R.id.vDesc);
        v.setTag(holder);

        return v;
    }

    @Override
    protected void bindView(View v, Falla item, int position) {
        ViewHolder h = (ViewHolder) v.getTag();
        h.vDesc.setText(item.descripcion);
        if (position % 2 == 0) {
            v.setBackgroundResource(R.drawable.even_list_selector_holo_light);
        } else {
            v.setBackgroundResource(R.drawable.odd_list_selector_holo_light);
        }
    }

    private class ViewHolder {
        public TextView vDesc;
    }

    @Override
    protected boolean filterItem(Falla item, String filter) {

        if (item.descripcion == null) {
            return false;
        }

        if (selected != null && selected.id != item.tipo) {
            return false;
        }

        if (item.descripcion.toLowerCase().contains(filter)) {
            return true;
        }

        return false;
    }
}
