package com.bilpa.android.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.bilpa.android.services.actions.LoginResult;

public class SessionStore {

    private static String KEY = "acarreo-session-store";

    public static boolean setSession(Context context, String user, String pass, LoginResult.Session s) {
        Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
        editor.putString("nombre", s.nombre);
        editor.putString("apellido", s.apellido);
        editor.putString("user", user);
        editor.putString("pass", pass);
        editor.putInt("rol", s.rol);
        editor.putInt("userId", s.id);
        return editor.commit();
    }

    public static LoginResult.Session getSession(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        LoginResult.Session session = new LoginResult.Session();
        session.nombre = prefs.getString("nombre", null);
        session.apellido = prefs.getString("apellido", null);
        session.id = prefs.getInt("userId", -1);
        session.rol = prefs.getInt("rol", -1);
        return session;
    }

    public static String getUser(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        LoginResult.Session session = new LoginResult.Session();
        return prefs.getString("user", null);
    }

    public static String getPass(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        LoginResult.Session session = new LoginResult.Session();
        return prefs.getString("pass", null);
    }


    public static int getDefaultSection(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        return prefs.getInt("defaultSection", 0);
    }

    public static boolean setDefaultSection(Context context, int defaultSection) {
        Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
        editor.putInt("defaultSection", defaultSection);
        return editor.commit();
    }


    public static void clearSession(Context context) {
        Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
    }

    public static boolean isLogged(Context context) {
        LoginResult.Session session = SessionStore.getSession(context);
        String user = SessionStore.getUser(context);
        String pass = SessionStore.getPass(context);
        if (session != null && session.id != -1 && user != null && pass != null) {
            return true;
        }
        return false;
    }
}
