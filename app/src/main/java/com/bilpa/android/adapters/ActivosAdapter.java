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

public class ActivosAdapter extends BaseAdapter<Activo> {

    public ActivosAdapter(Context context, List<Activo> items) {
        super(context, 0, items);
    }

    public Date mToday = Calendar.getInstance().getTime();

    @Override
    protected View newView(int position, ViewGroup parent, Activo item) {
        View v = mInflater.inflate(R.layout.row_activos_list, parent, false);


        ViewHolder holder = new ViewHolder();
        holder.vActivoName = (TextView) v.findViewById(R.id.vActivoName);
        holder.vActivoType = (TextView) v.findViewById(R.id.vActivoType);
        holder.vActivoQR = (TextView) v.findViewById(R.id.vActivoQR);
        holder.vActionStatus = (TextView) v.findViewById(R.id.vActionStatus);
        v.setTag(holder);

        return v;
    }

    @Override
    protected void bindView(View v, Activo item, int position) {
        ViewHolder h = (ViewHolder) v.getTag();

        h.vActivoName.setText(item.descripcion);
        h.vActivoType.setText(item.tipo);

        if (item.codigoQR != 0) {
            h.vActivoQR.setText(String.format(getContext().getString(R.string.visita_activos_code), item.codigoQR));
        } else {
            h.vActivoQR.setText(R.string.visita_activos_code_empty);
        }

        if (position % 2 == 0) {
            v.setBackgroundResource(R.drawable.even_list_selector_holo_light);
        } else {
            v.setBackgroundResource(R.drawable.odd_list_selector_holo_light);
        }

        switch (item.getStatus()) {

            case COMPLETO:
                h.vActionStatus.setText(R.string.visita_activo_status_completo);
                h.vActionStatus.setTextColor(getContext().getResources().getColor(R.color.activo_status_completo));
                break;

            case INICIADO:
                h.vActionStatus.setText(R.string.visita_activo_status_iniciado);
                h.vActionStatus.setTextColor(getContext().getResources().getColor(R.color.activo_status_iniciado));
                break;

            case SIN_INICIAR:
                h.vActionStatus.setText(R.string.visita_activo_status_sininciar);
                h.vActionStatus.setTextColor(getContext().getResources().getColor(R.color.activo_status_sininciar));
                break;

            case NONE:
                h.vActionStatus.setVisibility(View.GONE);
                break;
        }
    }

    private class ViewHolder {
        public TextView vActivoName;
        public TextView vActivoType;
        public TextView vActivoQR;
        public TextView vActionStatus;
    }
}
