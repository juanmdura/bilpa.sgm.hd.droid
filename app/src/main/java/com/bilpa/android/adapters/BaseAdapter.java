package com.bilpa.android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter<T extends Object> extends ArrayAdapter<T> {

    protected List<T> items;
    protected LayoutInflater mInflater;

    public BaseAdapter(Context context, int resource, List<T> items) {
        super(context, resource, items);
        this.items = items;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return items != null ? items.size() : 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        T item = items.get(position);
        if (convertView == null) {
            v = newView(position, parent, item);
        } else {
            v = convertView;
        }
        bindView(v, item, position);
        return v;
    }

    protected abstract View newView(int position, ViewGroup parent, T item);

    protected abstract void bindView(View v, T item, int position);

    public void addFirst(T item) {
        this.items.add(0, item);
        notifyDataSetChanged();
    }

    private final Object mLock = new Object();

    public void addAllItems(List<T> newitems) {

        if (newitems != null) {
            synchronized (mLock) {
                if (this.items != null) {
                    this.items.addAll(newitems);
                } else {
                    this.items = new ArrayList<T>(newitems);
                }
            }
            notifyDataSetChanged();
        }
    }


}
