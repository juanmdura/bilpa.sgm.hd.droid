package com.bilpa.android.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bilpa.android.R;
import com.bilpa.android.model.TipoRepuesto;

import java.util.List;


public abstract class AbstractAllSpinnerAdapter<T extends Object> extends SpinnerBaseAdapter<T> {


	public AbstractAllSpinnerAdapter(Context context, List<T> items) {
		super(context, 0, items);
	}

    @Override
    public int getCount() {
        return super.getCount() + 1;
    }

    @Override
    public T getItem(int position) {
        if (position == 0) {
            return null;
        } else {
            return super.getItem(position-1);
        }
    }


    @Override
	protected View newView(int position, ViewGroup parent, T item) {
		View v = mInflater.inflate(R.layout.row_spinner_filter, parent, false);

        ViewHolder holder = new ViewHolder();
        holder.vText = (TextView) v.findViewById(R.id.vText);
        v.setTag(holder);
		return v;
	}

	@Override
	protected void bindView(View v, T item, int position) {
        ViewHolder holder = (ViewHolder) v.getTag();
        if (position == 0) {
            holder.vText.setText(getAllText());
        } else {
            holder.vText.setText(getDescription(item));
        }

	}

	private static class ViewHolder {
		TextView vText;
	}

	@Override
	protected View newViewDropdown(int position, ViewGroup parent, T item) {
		View v = null;
		v = mInflater.inflate(R.layout.support_simple_spinner_dropdown_item, parent, false);
		ViewHolder holder = new ViewHolder();
		holder.vText = (TextView) v.findViewById(android.R.id.text1);
		v.setTag(holder);
		return v;
	}

	@Override
	protected void bindViewDropdown(View v, T item, int position) {
		ViewHolder holder = (ViewHolder) v.getTag();

        if (position == 0) {
            holder.vText.setText(getAllText());
        } else {
            holder.vText.setText(getDescription(item));
        }

	}

    protected abstract String getDescription(T item);
    protected abstract String getAllText();

}
