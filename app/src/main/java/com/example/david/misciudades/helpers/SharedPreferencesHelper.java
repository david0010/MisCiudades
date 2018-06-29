package com.example.david.misciudades.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.david.misciudades.constants.Constants;

public class SharedPreferencesHelper {

    public static void updateSharedPreference(Context context, String key, Boolean value) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getSharedPreference(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        boolean value = prefs.getBoolean(key, false);

        return value;
    }
}
