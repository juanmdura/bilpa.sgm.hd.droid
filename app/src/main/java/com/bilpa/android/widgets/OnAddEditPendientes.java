package com.bilpa.android.widgets;

import com.bilpa.android.model.Pendiente;

/**
 * Author: by santilod.
 */
public interface OnAddEditPendientes {

    public long getIdActivo();
    public Long getIdPreventivo();
    public void goToAddPendiente();
    public void goToEditPendiente(Pendiente pendiente);
    public void invalidatePendientes();
    public void goToRepairPendinete(Pendiente pendiente);

}
