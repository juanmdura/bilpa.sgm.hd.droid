package com.bilpa.android.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bilpa.android.R;
import com.bilpa.android.model.Comentario;

import java.util.List;

public class CheckCommentsAdapter extends BaseAdapter<Comentario> {

    private View.OnClickListener mOnDeleteListener;
    private CompoundButton.OnCheckedChangeListener mOnCheckPrintableListener;

    public CheckCommentsAdapter(Context context, List<Comentario> items) {
        super(context, 0, items);
    }

    public void setOnDeleteListener(View.OnClickListener onDeleteListener) {
        this.mOnDeleteListener = onDeleteListener;
    }

    public void setOnCheckPrintableListener(CompoundButton.OnCheckedChangeListener onCheckPrintableListener) {
        this.mOnCheckPrintableListener = onCheckPrintableListener;
    }

    @Override
    protected View newView(int position, ViewGroup parent, Comentario item) {
        View v = mInflater.inflate(R.layout.row_checkcomments_list, parent, false);

        ViewHolder holder = new ViewHolder();

        holder.vIndex = (TextView) v.findViewById(R.id.vIndex);
        holder.vCommentIco = (ImageView) v.findViewById(R.id.vCommentIco);
        holder.vComment = (TextView) v.findViewById(R.id.vComment);
        holder.vCheckImprimible = (CheckBox) v.findViewById(R.id.vCheckImprimible);
        holder.vDelete = (ImageView) v.findViewById(R.id.vDelete);

        v.setTag(holder);

        return v;
    }

    @Override
    protected void bindView(View v, Comentario item, int position) {
        ViewHolder h = (ViewHolder) v.getTag();

        h.vIndex.setText(String.valueOf(position+1));
        h.vComment.setText(item.texto);
        h.vDelete.setTag(item);
        h.vDelete.setOnClickListener(mOnDeleteListener);
        h.vCheckImprimible.setOnCheckedChangeListener(null);
        h.vCheckImprimible.setChecked(item.visible);
        h.vCheckImprimible.setOnCheckedChangeListener(mOnCheckPrintableListener);
        h.vCheckImprimible.setTag(item);

        if (position % 2 == 0) {
            v.setBackgroundResource(R.drawable.even_list_selector_holo_light);
        } else {
            v.setBackgroundResource(R.drawable.odd_list_selector_holo_light);
        }
    }

    private class ViewHolder {
        public TextView vIndex;
        public ImageView vCommentIco;
        public TextView vComment;
        public CheckBox vCheckImprimible;
        public ImageView vDelete;
    }
}
