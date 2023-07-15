package com.bilpa.android.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bilpa.android.R;
import com.bilpa.android.model.correctivos.FallaActivo;
import com.mautibla.utils.InputUtils;
import com.mautibla.utils.ViewUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ActivosFallasAdapter extends BaseAdapter<FallaActivo> {

    private final int mColorBlack;
    private final int mColorGray;
    private View.OnClickListener mOnCorregirACtivoListener;

    public ActivosFallasAdapter(Context context, List<FallaActivo> items) {
        super(context, 0, items);


        mColorBlack = ContextCompat.getColor(context, R.color.black);
        mColorGray = ContextCompat.getColor(context, R.color.gray);
    }

    public Date mToday = Calendar.getInstance().getTime();



    @Override
    protected View newView(int position, ViewGroup parent, FallaActivo item) {
        View v = mInflater.inflate(R.layout.row_activos_fallas_list, parent, false);

        ViewHolder holder = new ViewHolder();

        holder.vActivoName = (TextView) v.findViewById(R.id.vActivoName);
        holder.vActivoType = (TextView) v.findViewById(R.id.vActivoType);
        holder.vActionStatus = (TextView) v.findViewById(R.id.vActionStatus);
        holder.vActivoQR = (TextView) v.findViewById(R.id.vActivoQR);
        holder.vFallaReportadaLbl = (TextView) v.findViewById(R.id.vFallaReportadaLbl);
        holder.vFallaReportada = (TextView) v.findViewById(R.id.vFallaReportada);
        holder.vComentarioLbl = (TextView) v.findViewById(R.id.vComentarioLbl);
        holder.vComentario = (TextView) v.findViewById(R.id.vComentario);
        holder.vIcoPendiente = (ImageView) v.findViewById(R.id.vIcoPendiente);
        holder.vBtnChangeActivo = (Button) v.findViewById(R.id.vBtnChangeActivo);

        v.setTag(holder);

        return v;
    }

    @Override
    protected void bindView(View v, FallaActivo item, int position) {
        ViewHolder h = (ViewHolder) v.getTag();

        if (position % 2 == 0) {
            v.setBackgroundResource(R.drawable.row_even_selector);
        } else {
            v.setBackgroundResource(R.drawable.row_odd_selector);
        }

        h.vActivoName.setText(item.activo.display);
        h.vActivoType.setText(item.activo.getTipoLabel());

        if (item.activo.qr != null && item.activo.qr.codigo != null) {
            h.vActivoQR.setText(String.format(getContext().getString(R.string.visita_activos_code), item.activo.qr.codigo));
        } else {
            h.vActivoQR.setText(R.string.visita_activos_code_empty);
        }


        if (item.activo.tienePendientes) {
            ViewUtils.visible(h.vIcoPendiente);
        } else {
            ViewUtils.invisible(h.vIcoPendiente);
        }

        if (item.activo.tieneReparaciones) {
            ViewUtils.visible(h.vActionStatus);
            h.vActionStatus.setText(getContext().getString(R.string.correctivo_detail_status_reparado));
            h.vActionStatus.setBackgroundResource(R.drawable.bg_status_activo_reparado);
        } else {
            ViewUtils.gone(h.vActionStatus);
        }



        if (!InputUtils.isEmpty(item.reparacion.fallaReportada.descripcion)) {
            h.vFallaReportada.setText(item.reparacion.fallaReportada.descripcion);
            h.vFallaReportada.setTextColor(mColorBlack);
        } else {
            h.vFallaReportada.setText(R.string.correctivo_detail_msg_sin_falla_reportada);
            h.vFallaReportada.setTextColor(mColorGray);
        }

        if (!InputUtils.isEmpty(item.reparacion.comentario)) {
            h.vComentario.setText(item.reparacion.comentario);
            h.vComentario.setTextColor(mColorBlack);
        } else {
            h.vComentario.setText(R.string.correctivo_detail_msg_comentario);
            h.vComentario.setTextColor(mColorGray);
        }


        h.vBtnChangeActivo.setOnClickListener(mOnCorregirACtivoListener);
        h.vBtnChangeActivo.setTag(item);



    }

    public void setOnCorregirActivoListener(View.OnClickListener onClickListener) {
        mOnCorregirACtivoListener = onClickListener;
    }

    private class ViewHolder {
        public TextView vActivoName;
        public TextView vActivoType;
        public TextView vActivoQR;
        public TextView vActionStatus;
        public ImageView vIcoPendiente;
        public TextView vFallaReportadaLbl;
        public TextView vFallaReportada;
        public TextView vComentarioLbl;
        public TextView vComentario;
        public Button vBtnChangeActivo;
    }
}
