package com.example.mannas.ytask;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Mannas on 6/19/2017.
 */

public class PreferencesManager {


    PreferencesManager mManager;
    final static String orderBy_key = "orderBy";

    private PreferencesManager(){

    }

    public PreferencesManager getInstence(){
        if(mManager==null){
            mManager = new PreferencesManager();
        }
        return mManager;
    }

    public static void putOrderBy(Context context, Integer OrderBy, String Name){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt( orderBy_key, OrderBy);
        editor.apply();
    }

    public static Integer getOrderBy(Context context){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getInt(orderBy_key,0);
    }
}
