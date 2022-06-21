package com.cotzero.ludo;

import android.content.Context;
import android.content.SharedPreferences;

public class Pref {
    Context context;

    public Pref(Context context) {
        this.context = context;
    }

    public void write(String key, String data){
        SharedPreferences sharedPreferences=context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString(key,data);
        editor.commit();

    }

    public String read(String key){
        SharedPreferences sharedPreferences=context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,"null");
    }
}
