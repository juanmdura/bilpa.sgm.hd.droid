package com.bilpa.android.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bilpa.android.R;
import com.bilpa.android.model.Producto;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ProductosAdapter extends BaseAdapter<Producto> {

    public ProductosAdapter(Context context, List<Producto> items) {
        super(context, 0, items);
    }

    public Date mToday = Calendar.getInstance().getTime();

    @Override
    protected View newView(int position, ViewGroup parent, Producto item) {
        View v = mInflater.inflate(R.layout.row_productos_list, parent, false);

        ViewHolder holder = new ViewHolder();
        holder.vTitle = (TextView) v.findViewById(R.id.vTitle);
        v.setTag(holder);
        return v;
    }

    @Override
    protected void bindView(View v, Producto item, int position) {
        ViewHolder h = (ViewHolder) v.getTag();
        h.vTitle.setText(String.valueOf(item.nombre));

        if (position % 2 == 0) {
            v.setBackgroundResource(R.drawable.list_selector_even);
        } else {
            v.setBackgroundResource(R.drawable.list_selector_odd);
        }
    }

    private class ViewHolder {
        public TextView vTitle;
    }
}
