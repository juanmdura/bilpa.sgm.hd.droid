package com.bilpa.android.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class PreviewAdapter extends BaseAdapter<Integer> {

    private int layout_row_id;

    public PreviewAdapter(Context context, int count, int layout_row_id) {
        super(context, 0, new ArrayList<Integer>());
        for (int i = 0; i < count; i++) {
            items.add(i);
        }
        this.layout_row_id = layout_row_id;
        notifyDataSetChanged();
    }

    @Override
    protected View newView(int position, ViewGroup parent, Integer item) {
        View v = null;
        v = mInflater.inflate(layout_row_id, parent, false);
        return v;
    }

    @Override
    protected void bindView(View v, Integer item, int position) {
    }


}
