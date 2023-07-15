package com.bilpa.android.adapters;

import android.content.Context;

import com.bilpa.android.R;
import com.bilpa.android.model.TipoFalla;

import java.util.List;


public class CounterAdapter extends SimpleSpinnerAdapter<Integer> {

	public CounterAdapter(Context context, List<Integer> items) {
		super(context, items);
	}

    @Override
    protected String getDescription(Integer item) {
        return String.format(getContext().getString(R.string.app_count_cantidad), item);
    }

    @Override
    protected String getDescriptionDropDown(Integer item) {
        return String.valueOf(item);
    }


}
