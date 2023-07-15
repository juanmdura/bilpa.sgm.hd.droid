package com.bilpa.android.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.bilpa.android.BilpaApp;
import com.bilpa.android.R;
import com.bilpa.android.model.Pendiente;
import com.mautibla.utils.ViewUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PendientesAdapter extends BaseAdapter<Pendiente> {

    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    private View.OnClickListener mMoreListener;
    private View.OnClickListener mOnRepairListener;
    private final ImageLoader mImageLoader;

    public PendientesAdapter(Context context, List<Pendiente> items) {
        super(context, 0, items);

        mImageLoader = BilpaApp.getInstance().getImageLoader();
    }

    public void setOnMoreListener(View.OnClickListener onMoreListener) {
        this.mMoreListener = onMoreListener;
    }

    public void setOnRepairListener(View.OnClickListener onRepairListener) {
        this.mOnRepairListener = onRepairListener;
    }

    @Override
    protected View newView(int position, ViewGroup parent, Pendiente item) {
        View v = mInflater.inflate(R.layout.row_pendientes_list, parent, false);

        ViewHolder holder = new ViewHolder();

        holder.vIndex = (TextView) v.findViewById(R.id.vIndex);
        holder.vChequeoLbl = (TextView) v.findViewById(R.id.vChequeoLbl);
        holder.vDesc = (TextView) v.findViewById(R.id.vDesc);
        holder.vSumary = (TextView) v.findViewById(R.id.vSumary);
        holder.vMore = (ImageView) v.findViewById(R.id.vMore);
        holder.vRepair = (ImageView) v.findViewById(R.id.vRepair);
        holder.vPhotoPanel = (RelativeLayout) v.findViewById(R.id.vPhotoPanel);
        holder.vPhoto = (ImageView) v.findViewById(R.id.vPhoto);

        v.setTag(holder);

        return v;
    }

    @Override
    protected void bindView(View v, Pendiente item, int position) {
        ViewHolder h = (ViewHolder) v.getTag();

        h.vIndex.setText(String.valueOf(position+1));
        h.vChequeoLbl.setText(item.textoItemChequado);
        h.vDesc.setText(item.comentario);

        h.vSumary.setTextColor(Color.GRAY);

        switch (item.getStatus()) {

            case INICIADO:
                ViewUtils.visible(h.vMore, h.vRepair);

                if (item.plazo != null) {
                    String plazo = sdf.format(item.plazo);
                    h.vSumary.setText(String.format(getContext().getString(R.string.pendientes_plazo_lbl), plazo));
                    ViewUtils.visible(h.vSumary);
                } else {
                    ViewUtils.gone(h.vSumary);
                }
                if (item.plazo != null && item.plazo.before(Calendar.getInstance().getTime())) {
                    h.vSumary.setTextColor(Color.RED);
                } else {
                    h.vSumary.setTextColor(Color.GRAY);
                }

                h.vMore.setTag(item);
                h.vMore.setOnClickListener(mMoreListener);

                h.vRepair.setTag(item);
                h.vRepair.setOnClickListener(mOnRepairListener);

                break;

            case REPARADO:
                ViewUtils.invisible(h.vMore, h.vRepair);
                String reparado = null;
                if (item.fechaReparado != null) {
                    reparado = sdf.format(item.fechaReparado);
                } else {
                    reparado = getContext().getString(R.string.pendientes_reparado_empty);
                }
                h.vSumary.setText(String.format(getContext().getString(R.string.pendientes_reparado_lbl), reparado));
                h.vSumary.setTextColor(Color.GRAY);
                h.vMore.setOnClickListener(null);
                h.vRepair.setOnClickListener(null);
                break;
        }

        if (item.urlFoto != null) {
            ViewUtils.visible(h.vPhotoPanel);
            mImageLoader.get(item.urlFoto, ImageLoader.getImageListener(h.vPhoto, R.drawable.ic_perm_media_white_24dp, R.drawable.ic_perm_media_white_24dp));
        } else {
            ViewUtils.gone(h.vPhotoPanel);
        }

        if (position % 2 == 0) {
            v.setBackgroundResource(R.drawable.even_list_selector_holo_light);
        } else {
            v.setBackgroundResource(R.drawable.odd_list_selector_holo_light);
        }

    }

    private static class ViewHolder {
        public TextView vIndex;
        public TextView vDesc;
        public TextView vSumary;
        public TextView vChequeoLbl;
        public ImageView vMore;
        public ImageView vRepair;
        public RelativeLayout vPhotoPanel;
        public ImageView vPhoto;
    }
}
