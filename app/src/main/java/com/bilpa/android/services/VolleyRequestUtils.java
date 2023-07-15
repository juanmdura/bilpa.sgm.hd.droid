package com.bilpa.android.services;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyLog;
import com.bilpa.android.utils.Logger;

/**
 * Created by santilod on 19/12/14.
 */
public class VolleyRequestUtils {

    public static void logToCurlRequest(Request<?> request) {

        StringBuilder builder = new StringBuilder();
        builder.append("HTTP ");
        switch (request.getMethod()) {
            case Request.Method.POST:
                builder.append("POST");
                break;
            case Request.Method.GET:
                builder.append("GET");
                break;
            case Request.Method.PUT:
                builder.append("PUT");
                break;
            case Request.Method.DELETE:
                builder.append("DELETE");
                break;
        }

        builder.append(" to ");
        builder.append(request.getUrl());
        builder.append("\n");

        try {
            if (request.getBody() != null) {
                builder.append(" params ");
                String data = new String(request.getBody());
                data = data.replaceAll("\"", "\\\"");
                builder.append("\"");
                builder.append(data);
                builder.append("\"");
                builder.append("\n");
            }
            for (String key : request.getHeaders().keySet()) {
                builder.append(" http header: '");
                builder.append(key);
                builder.append(": ");
                builder.append(request.getHeaders().get(key));
                builder.append("'");
                builder.append("\n");
            }

            Logger.v("RestApi", builder.toString());

        } catch (AuthFailureError e) {
            VolleyLog.wtf("Unable to get body of response or headers for curl logging");
        }
    }
}
