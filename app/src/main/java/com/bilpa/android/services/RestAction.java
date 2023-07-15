package com.bilpa.android.services;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bilpa.android.BilpaApp;
import com.bilpa.android.utils.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public abstract class RestAction<T> {

    private static final String mEncoding = "UTF-8";
    public static final String tag = "RestApi";
    static String defaultDatePattern = "MMM dd',' yyyy hh:mm:ss a";

    private GsonBuilder gsonb;
    protected Gson gson;
    private Class<T> result;

    protected RestAction(Class<T> result) {
        this.result = result;
        gsonb = new GsonBuilder();
        gson = gsonb.create();
        gsonb.setDateFormat(defaultDatePattern);
        gson = gsonb.create();
    }

    protected RestAction(Class<T> result, String datePattern) {
        this.result = result;
        gsonb = new GsonBuilder();
        gson = gsonb.create();
        gsonb.setDateFormat(datePattern);
        gson = gsonb.create();
    }

    public void get(String tag, Callback<T> callback) {
        get(tag, new CallbackAdapter<T>(callback));
    }

    private void get(String tag, CallbackListener<JSONObject> callbackListener) {

        // Add params request
        String url = getBaseUrl();

        if (getOpertaion() != null) {
            url += "?" + "operacion=" +  getOpertaion();
        }

        url += toUrlParams(getParams());

        // create request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, callbackListener, callbackListener);

        // Adding request to request queue
        BilpaApp.getInstance().addToRequestQueue(request, tag);
    }

    public void post(String tag, final Callback<T> callback) {
        post(tag, new CallbackAdapter<T>(callback));
    }

    private void post(String tag, final CallbackListener<JSONObject> callbackListener) {

        // Add params request
        String url = getBaseUrl();

        if (getOpertaion() != null) {
            url += "?" + "operacion=" +  getOpertaion();
        }

        // create request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null, callbackListener, callbackListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return RestAction.this.getParams();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            public byte[] getBody() {
                try {

                    String bodyPost = RestAction.this.getBodyPost();
                    return bodyPost.getBytes();

//                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//                    StringEntity entity = new StringEntity(RestAction.this.getBodyPost(), "UTF-8");
//                    entity.writeTo(outputStream);
//                    Log.i("RestAction", entity.toString());
//                    return outputStream.toByteArray();
                } catch (Exception e) {
                    callbackListener.onErrorResponse(new VolleyError(e));
                }
                return null;
            }


        };

        // Adding request to request queue
        BilpaApp.getInstance().addToRequestQueue(request, tag);
    }

    protected abstract String getBaseUrl();
//    {
//        return BuildConfig.SERVICE_URL;
//    }

    protected abstract String getOpertaion();
    protected abstract Map<String, String> getParams();
    protected String getBodyPost() {
        return null;
    }

    protected String doSafeUrl(String url) {
        String[] split = url.split("\\?");
        if (split.length > 0) {
            url = split[0];
        }
        return url;
    }

    private String toUrlParams(Map<String, String> params) {

        String urlParams = "";

        int index = 0;
        Set<Map.Entry<String, String>> entrySet = params.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            String paramName = entry.getKey();
            String paramValue = entry.getValue();
            if (index == 0) {
                urlParams += "&";
            } else {
                urlParams += "&";
            }
            try {

                urlParams += paramName + "=" + URLEncoder.encode(paramValue.toString(), mEncoding);
            } catch (UnsupportedEncodingException e) {
                urlParams += paramName + "=" + paramValue.toString();
            }

            index++;
        }
        return urlParams;
    }

    private String PATTERN = "\\{\"response\":(\\{.*\\})\\}";

    public String onFixData(String string) {

        // Direct use of Pattern:
        Pattern p = Pattern.compile(PATTERN);
        Matcher m = p.matcher(string);
        if (m.find()) { // Find each match in turn; String can't do this.
            string = m.group(1); // Access a submatch group; String can't do this.
        }
        return string;
    }

    private class CallbackAdapter<T> implements CallbackListener<JSONObject> {

        private Callback<T> callback;

        CallbackAdapter(Callback<T> callback) {
            this.callback = callback;
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            callback.fail(error);
        }

        @Override
        public void onResponse(JSONObject response) {
            String msg = onFixData(response.toString());
            printResponse(msg);
            try {
                T t = (T) gson.fromJson(msg, result);
                callback.success(t);
            } catch (Exception e) {
                e.printStackTrace();
                callback.fail(e);
            }
        }

        private void printResponse(String msg) {
            try {
                JSONObject jsonObject = new JSONObject(msg);
                Logger.v(tag, jsonObject.toString(4));
            } catch (Throwable t) {
                t.printStackTrace();
            }

        }
    }


}

