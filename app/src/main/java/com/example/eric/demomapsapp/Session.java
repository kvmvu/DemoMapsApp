package com.example.eric.demomapsapp;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Eric on 9/28/2016.
 */

public class Session {

    public void setPreferences(Context context, String key, String value){
        SharedPreferences.Editor editor = context.getSharedPreferences("demomapsapp", Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getPreferences(Context context, String key){
        SharedPreferences preferences = context.getSharedPreferences("demomapsapp", Context.MODE_PRIVATE);
        String position = preferences.getString(key, "");
        return position;
    }
}
