package com.bilpa.android.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public abstract class SpinnerBaseAdapter<T extends Object> extends ArrayAdapter<T> {

	protected List<T> items;
	protected LayoutInflater mInflater;
	
	public SpinnerBaseAdapter(Context context, int resource, List<T> items) {
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
		T item = getItem(position);
        if (convertView == null) {
			v = newView(position, parent, item);
        } else {
            v = convertView;
        }
        bindView(v, item, position);
        return v;
	}
	
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		View v;
		T item = getItem(position);
        if (convertView == null) {
			v = newViewDropdown(position, parent, item);
        } else {
            v = convertView;
        }
        bindViewDropdown(v, item, position);
        return v;
	}

	protected abstract View newView(int position, ViewGroup parent, T item);
	protected abstract void bindView(View v, T item, int position);
	protected abstract View newViewDropdown(int position, ViewGroup parent, T item);
	protected abstract void bindViewDropdown(View v, T item, int position);

	public void addFirst(T item) {
		this.items.add(0, item);
		notifyDataSetChanged();
	}
}
