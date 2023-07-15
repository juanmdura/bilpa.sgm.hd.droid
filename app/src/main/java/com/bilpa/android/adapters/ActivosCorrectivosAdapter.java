package com.bilpa.android.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bilpa.android.R;
import com.bilpa.android.model.correctivos.CActivo;
import com.mautibla.utils.ViewUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ActivosCorrectivosAdapter extends BaseAdapter<CActivo> {

    private int rowLayout;

    public ActivosCorrectivosAdapter(Context context, List<CActivo> items) {
        super(context, 0, items);
        this.rowLayout = 0;
    }

    public ActivosCorrectivosAdapter(Context context, int rowLayout, List<CActivo> items) {
        super(context, 0, items);
        this.rowLayout = rowLayout;
    }

    public Date mToday = Calendar.getInstance().getTime();

    @Override
    protected View newView(int position, ViewGroup parent, CActivo item) {
        int row = 0;
        if (rowLayout == 0) {
            row = R.layout.row_cactivos_list;
        } else {
            row = rowLayout;
        }

        View v = mInflater.inflate(row, parent, false);

        ViewHolder holder = new ViewHolder();
        holder.vActivoName = (TextView) v.findViewById(R.id.vActivoName);
        holder.vActivoType = (TextView) v.findViewById(R.id.vActivoType);
        holder.vActivoQR = (TextView) v.findViewById(R.id.vActivoQR);
        holder.vActionStatus = (TextView) v.findViewById(R.id.vActionStatus);
        holder.vIcoPendiente = (ImageView) v.findViewById(R.id.vIcoPendiente);
        v.setTag(holder);

        return v;
    }

    @Override
    protected void bindView(View v, CActivo item, int position) {
        ViewHolder h = (ViewHolder) v.getTag();

        if (position % 2 == 0) {
            v.setBackgroundResource(R.drawable.row_even_selector);
        } else {
            v.setBackgroundResource(R.drawable.row_odd_selector);
        }

        h.vActivoName.setText(item.display);
        h.vActivoType.setText(item.getTipoLabel());

        if (item.qr != null && item.qr.codigo != null) {
            h.vActivoQR.setText(String.format(getContext().getString(R.string.visita_activos_code), item.qr.codigo));
        } else {
            h.vActivoQR.setText(R.string.visita_activos_code_empty);
        }

        if (item.tienePendientes) {
            ViewUtils.visible(h.vIcoPendiente);
        } else {
            ViewUtils.invisible(h.vIcoPendiente);
        }

        if (item.tieneReparaciones) {
            ViewUtils.visible(h.vActionStatus);
            h.vActionStatus.setText(getContext().getString(R.string.correctivo_detail_status_reparado));
            h.vActionStatus.setBackgroundResource(R.drawable.bg_status_activo_reparado);
        } else {
            ViewUtils.gone(h.vActionStatus);
        }

    }

    private class ViewHolder {
        public TextView vActivoName;
        public TextView vActivoType;
        public TextView vActivoQR;
        public TextView vActionStatus;
        public ImageView vIcoPendiente;
    }
}
