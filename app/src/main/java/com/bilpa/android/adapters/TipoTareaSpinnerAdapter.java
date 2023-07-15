package com.bilpa.android.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bilpa.android.R;
import com.bilpa.android.model.TipoRepuesto;
import com.bilpa.android.model.TipoTarea;

import java.util.List;


public class TipoTareaSpinnerAdapter extends AbstractAllSpinnerAdapter<TipoTarea> {

	public TipoTareaSpinnerAdapter(Context context, List<TipoTarea> items) {
		super(context, items);
	}

    @Override
    protected String getDescription(TipoTarea item) {
        return item.descripcion;
    }

    @Override
    protected String getAllText() {
        return getContext().getString(R.string.tareas_search_filter_all);
    }


}
