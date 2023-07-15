package com.bilpa.android.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.bilpa.android.BilpaApp;
import com.bilpa.android.R;
import com.bilpa.android.model.Activo;
import com.bilpa.android.model.Corregido;
import com.bilpa.android.model.Repuesto;
import com.bilpa.android.model.RepuestoItem;
import com.mautibla.utils.ViewUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CorregidosAdapter extends BaseAdapter<Corregido> {

    public Date mToday = Calendar.getInstance().getTime();

    private View.OnClickListener mOnDeleteListener;
    private final ImageLoader mImageLoader;

    public CorregidosAdapter(Context context, List<Corregido> items) {
        super(context, 0, items);

        mImageLoader = BilpaApp.getInstance().getImageLoader();
    }

    public void setOnDeleteListener(View.OnClickListener onDeleteListener) {
        this.mOnDeleteListener = onDeleteListener;
    }


    @Override
    protected View newView(int position, ViewGroup parent, Corregido item) {
        View v = mInflater.inflate(R.layout.row_corregidos_list, parent, false);

        ViewHolder holder = new ViewHolder();

        holder.vIndex = (TextView) v.findViewById(R.id.vIndex);
        holder.vChequeoLbl = (TextView) v.findViewById(R.id.vChequeoLbl);
        holder.vFalla = (TextView) v.findViewById(R.id.vFalla);
        holder.vTarea = (TextView) v.findViewById(R.id.vTarea);
        holder.vLblRepuestos = (TextView) v.findViewById(R.id.vLblRepuestos);
        holder.vRepuestosCount = (TextView) v.findViewById(R.id.vRepuestosCount);
        holder.vDelete = (ImageView) v.findViewById(R.id.vDelete);
        holder.vPhotoPanel = (RelativeLayout) v.findViewById(R.id.vPhotoPanel);
        holder.vPhoto = (ImageView) v.findViewById(R.id.vPhoto);

        v.setTag(holder);

        return v;
    }

    @Override
    protected void bindView(View v, Corregido item, int position) {
        ViewHolder h = (ViewHolder) v.getTag();

        h.vIndex.setText(String.valueOf(position+1));
        h.vChequeoLbl.setText(item.textoItemChequado);
        h.vFalla.setText(item.falla.descripcion);
        h.vTarea.setText(item.tarea.descripcion);
        h.vDelete.setTag(item);
        h.vDelete.setOnClickListener(mOnDeleteListener);

        String url = item.urlFoto2;
        if (url == null) {
            url = item.urlFoto;
        }

        if (url != null) {
            ViewUtils.visible(h.vPhotoPanel);
            mImageLoader.get(url, ImageLoader.getImageListener(h.vPhoto, R.drawable.ic_perm_media_white_24dp, R.drawable.ic_perm_media_white_24dp));
        } else {
            ViewUtils.gone(h.vPhotoPanel);
        }


        List<RepuestoItem> repuestos = item.repuestos;
        if (repuestos.isEmpty()) {
            ViewUtils.gone(h.vLblRepuestos, h.vRepuestosCount);
        } else {
            ViewUtils.visible(h.vLblRepuestos, h.vRepuestosCount);
            h.vRepuestosCount.setText(repuestos.get(0).descripcion);
            for (int i = 1; i < repuestos.size(); i++) {
                h.vRepuestosCount.append( "\n" + repuestos.get(i).descripcion);
            }
        }

        if (position % 2 == 0) {
            v.setBackgroundResource(R.drawable.even_list_selector_holo_light);
        } else {
            v.setBackgroundResource(R.drawable.odd_list_selector_holo_light);
        }
    }

    private class ViewHolder {
        public TextView vIndex;
        public TextView vChequeoLbl;
        public TextView vFalla;
        public TextView vTarea;
        public TextView vLblRepuestos;
        public TextView vRepuestosCount;
        public ImageView vDelete;
        public RelativeLayout vPhotoPanel;
        public ImageView vPhoto;
    }
}
