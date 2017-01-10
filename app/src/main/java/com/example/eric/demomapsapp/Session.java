package com.example.eric.demomapsapp;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Eric on 9/28/2016.
 */

public class Session {
    SharedPreferences prefs;
    static SharedPreferences.Editor loginEditor;
    Context context;

    public Session(Context context){
        this.context = context;
        prefs = context.getSharedPreferences("demomapsapp", Context.MODE_PRIVATE);
        loginEditor = prefs.edit();
    }

    public static void setLoggedIn(boolean loggedIn){
        loginEditor.putBoolean("LoggedIn", loggedIn);
        loginEditor.commit();
    }

    public boolean loggedIn(){
        return prefs.getBoolean("LoggedIn", false);
    }
}
