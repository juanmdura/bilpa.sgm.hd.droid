package com.bilpa.android.services.actions;

import java.io.Serializable;
import java.util.List;


public abstract class ListResult<T> extends BaseResult {

    public abstract List<T> getItems();
}
