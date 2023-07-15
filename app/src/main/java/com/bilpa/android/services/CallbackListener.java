package com.bilpa.android.services;

import com.android.volley.Response;

/**
 * Created by santilod on 17/12/14.
 */
public interface CallbackListener<T> extends Response.Listener<T>, Response.ErrorListener {
}
