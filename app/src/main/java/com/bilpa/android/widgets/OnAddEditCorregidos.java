package com.bilpa.android.widgets;

import com.bilpa.android.fragments.CorregidoDetailFragment;
import com.bilpa.android.model.Corregido;
import com.bilpa.android.model.Pendiente;

/**
 * Author: by santilod.
 */
public interface OnAddEditCorregidos {

    public Long getIdPreventivo();
    public void goToCorregidos();
    public void goToAddCorregido();
    public void goToEditCorregido(Corregido corregido);
    public void invalidateCorregidos();

}
