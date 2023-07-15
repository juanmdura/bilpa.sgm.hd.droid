package com.bilpa.android.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bilpa.android.R;

import java.util.List;


public class FilterSpinnerAdapter extends SpinnerBaseAdapter<String> {


	public FilterSpinnerAdapter(Context context, List<String> items) {
		super(context, 0, items);
	}

	@Override
	protected View newView(int position, ViewGroup parent, String item) {
		View v = mInflater.inflate(R.layout.row_spinner_filter, parent, false);
		ViewHolder holder = new ViewHolder();
		holder.vText = (TextView) v.findViewById(R.id.vText);
		v.setTag(holder);
		return v;
	}

	@Override
	protected void bindView(View v, String item, int position) {
		ViewHolder holder = (ViewHolder) v.getTag();
		holder.vText.setText(item);
	}

	private static class ViewHolder {
		TextView vText;
	}

	@Override
	protected View newViewDropdown(int position, ViewGroup parent, String item) {
		View v = null;
		v = mInflater.inflate(R.layout.support_simple_spinner_dropdown_item, parent, false);
		ViewHolder holder = new ViewHolder();
		holder.vText = (TextView) v.findViewById(android.R.id.text1);
		v.setTag(holder);
		return v;
	}

	@Override
	protected void bindViewDropdown(View v, String item, int position) {
		ViewHolder holder = (ViewHolder) v.getTag();
		holder.vText.setText(item);
	}

}
