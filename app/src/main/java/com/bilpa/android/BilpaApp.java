package com.bilpa.android;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.bilpa.android.services.LruBitmapCache;
import com.bilpa.android.services.VolleyRequestUtils;
import com.bilpa.android.services.actions.DestinoCargoResult;
import com.bilpa.android.services.actions.FallasResult;
import com.bilpa.android.services.actions.OrganizacionesResult;
import com.bilpa.android.services.actions.RepuestosResult;
import com.bilpa.android.services.actions.TareasResult;
import com.bilpa.android.services.actions.TipoDescargaResult;
import com.bilpa.android.services.actions.TiposFallaResult;
import com.bilpa.android.services.actions.TiposRepuestosResult;
import com.bilpa.android.services.actions.TiposTareaResult;
import com.crashlytics.android.Crashlytics;

public class BilpaApp extends Application {


    public static final String TAG = BilpaApp.class
            .getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private static BilpaApp mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        Crashlytics.start(this);
        mInstance = this;
        VolleyLog.DEBUG = false;

        mRepuestos = null;
        mTiposRepuestos = null;
    }

    public static synchronized BilpaApp getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        req.setRetryPolicy(new DefaultRetryPolicy(60 * 1000, 1, 1.0f));
        VolleyRequestUtils.logToCurlRequest(req);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        req.setRetryPolicy(new DefaultRetryPolicy(60 * 1000, 1, 1.0f));
        VolleyRequestUtils.logToCurlRequest(req);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public void cancelPendingRequestsAll() {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(new RequestQueue.RequestFilter() {
                @Override
                public boolean apply(Request<?> request) {
                    return true;
                }
            });
        }
    }




    public RepuestosResult mRepuestos;
    public TiposRepuestosResult mTiposRepuestos;

    public TareasResult mTareas;
    public TiposTareaResult mTiposTareas;

    public FallasResult mFallas;
    public TiposFallaResult mTiposFallas;

    public OrganizacionesResult mOrganizacionesResult;
    public DestinoCargoResult mDestinoCargoResult;
    public TipoDescargaResult mTipoDescargaResult;
}
