package com.bilpa.android.adapters;

import android.content.Context;

import com.bilpa.android.R;
import com.bilpa.android.model.TipoFalla;
import com.bilpa.android.model.TipoTarea;

import java.util.List;


public class TipoFallaSpinnerAdapter extends AbstractAllSpinnerAdapter<TipoFalla> {

	public TipoFallaSpinnerAdapter(Context context, List<TipoFalla> items) {
		super(context, items);
	}

    @Override
    protected String getDescription(TipoFalla item) {
        return item.descripcion;
    }

    @Override
    protected String getAllText() {
        return getContext().getString(R.string.fallas_search_filter_all);
    }


}
