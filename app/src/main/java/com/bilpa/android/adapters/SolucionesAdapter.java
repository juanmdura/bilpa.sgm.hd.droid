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
import com.bilpa.android.model.Solucion;
import com.mautibla.utils.ViewUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SolucionesAdapter extends BaseAdapter<Solucion> {

    public Date mToday = Calendar.getInstance().getTime();

    private View.OnClickListener mOnDeleteListener;
    private final ImageLoader mImageLoader;

    public SolucionesAdapter(Context context, List<Solucion> items) {
        super(context, 0, items);

        mImageLoader = BilpaApp.getInstance().getImageLoader();
    }

    public void setOnDeleteListener(View.OnClickListener onDeleteListener) {
        this.mOnDeleteListener = onDeleteListener;
    }


    @Override
    protected View newView(int position, ViewGroup parent, Solucion item) {
        View v = mInflater.inflate(R.layout.row_corregidos_list, parent, false);

        ViewHolder holder = new ViewHolder();

        holder.vIndex = (TextView) v.findViewById(R.id.vIndex);
        holder.vFalla = (TextView) v.findViewById(R.id.vFalla);
        holder.vTarea = (TextView) v.findViewById(R.id.vTarea);
        holder.vLblRepuestos = (TextView) v.findViewById(R.id.vLblRepuestos);
        holder.vRepuestosCount = (TextView) v.findViewById(R.id.vRepuestosCount);
        holder.vDelete = (ImageView) v.findViewById(R.id.vDelete);
        holder.vPhotoPanel = (RelativeLayout) v.findViewById(R.id.vPhotoPanel);
        holder.vPhoto = (ImageView) v.findViewById(R.id.vPhoto);

        TextView vChequeoLbl = (TextView) v.findViewById(R.id.vChequeoLbl);
        vChequeoLbl.setVisibility(View.GONE);

        v.setTag(holder);

        return v;
    }

    @Override
    protected void bindView(View v, Solucion item, int position) {
        ViewHolder h = (ViewHolder) v.getTag();


        h.vIndex.setText(String.valueOf(position+1));
        h.vFalla.setText(item.falla.descripcion);
        h.vTarea.setText(item.tarea.descripcion);
        h.vDelete.setTag(item);
        h.vDelete.setOnClickListener(mOnDeleteListener);

        String photo2 = item.urlFoto2;
        String photo1 = item.urlFoto;
        String url = null;
        if (photo2 != null) {
            url = photo2;
        }
        if (url == null && photo1 != null) {
            url = photo1;
        }

        if (url != null) {
            ViewUtils.visible(h.vPhotoPanel);
            mImageLoader.get(url, ImageLoader.getImageListener(h.vPhoto, R.drawable.ic_perm_media_white_24dp, R.drawable.ic_perm_media_white_24dp));
        } else {
            ViewUtils.gone(h.vPhotoPanel);
        }


        List<Solucion.RepuestoRow> repuestos = item.repuestosRows;
        if (repuestos.isEmpty()) {
            ViewUtils.gone(h.vLblRepuestos, h.vRepuestosCount);
        } else {
            ViewUtils.visible(h.vLblRepuestos, h.vRepuestosCount);
            h.vRepuestosCount.setText(repuestos.get(0).repuesto.descripcion);
            for (int i = 1; i < repuestos.size(); i++) {
                h.vRepuestosCount.append( "\n" + repuestos.get(i).repuesto.descripcion);
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
        public TextView vFalla;
        public TextView vTarea;
        public TextView vLblRepuestos;
        public TextView vRepuestosCount;
        public ImageView vDelete;
        public RelativeLayout vPhotoPanel;
        public ImageView vPhoto;
    }
}
