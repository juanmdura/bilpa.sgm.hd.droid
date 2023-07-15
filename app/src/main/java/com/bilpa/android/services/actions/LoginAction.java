package com.bilpa.android.services.actions;

import java.util.HashMap;
import java.util.Map;


public class LoginAction extends PreventivosAction<LoginResult> {

    private String user;
    private String pass;

    public LoginAction(String user, String pass) {
        super(LoginResult.class);
        this.user = user;
        this.pass = pass;
    }

    @Override
    protected String getOpertaion() {
        return "login";
    }

    @Override
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("user", String.valueOf(user));
        params.put("pass", String.valueOf(pass));
        return params;
    }
}
