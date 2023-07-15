package com.bilpa.android.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bilpa.android.R;
import com.bilpa.android.model.Corregido;
import com.bilpa.android.model.Pico;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PicosAdapter extends BaseAdapter<Pico> {

    public PicosAdapter(Context context, List<Pico> items) {
        super(context, 0, items);
    }

    public Date mToday = Calendar.getInstance().getTime();

    @Override
    protected View newView(int position, ViewGroup parent, Pico item) {
        View v = mInflater.inflate(R.layout.row_picos_list, parent, false);

        ViewHolder holder = new ViewHolder();

        holder.vIndex = (TextView) v.findViewById(R.id.vIndex);
        holder.vTypeLbl = (TextView) v.findViewById(R.id.vTypeLbl);
        holder.vType = (TextView) v.findViewById(R.id.vType);

        v.setTag(holder);

        return v;
    }

    @Override
    protected void bindView(View v, Pico item, int position) {
        ViewHolder h = (ViewHolder) v.getTag();

        h.vIndex.setText(String.valueOf(item.numeroPico));
        h.vType.setText(item.tipoCombusitble);
    }

    private class ViewHolder {
        public TextView vIndex;
        public TextView vTypeLbl;
        public TextView vType;
    }
}
