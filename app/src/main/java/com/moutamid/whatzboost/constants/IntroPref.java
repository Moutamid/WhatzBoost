package com.moutamid.whatzboost.constants;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IntroPref {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "WS";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String MODE = "mode";
    private static final String DATE = "date";

    public IntroPref(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    } public void setBoolean(String key,boolean isFirstTime) {
        editor.putBoolean(key, isFirstTime);
        editor.commit();
    }
    public boolean getBoolean(String key) {
        return pref.getBoolean(key, false);

    }
    public void setIntExtra(String key,int val) {
        editor.putInt(key, val);
        editor.commit();
    }
    public int getIntExtra(String key) {
        return pref.getInt(key, 0);

    }  public int getIntExtra(String key,int defaultVal) {
        return pref.getInt(key, defaultVal);

    }
    public void setStringExtra(String key,String val) {
        editor.putString(key, val);
        editor.commit();
    }
    public String getStringExtra(String key) {
        return pref.getString(key, "");

    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setDarkMode(boolean isFirstTime) {
        editor.putBoolean(MODE, isFirstTime);
        editor.commit();
    }

    public boolean getDarkMode() {
        return pref.getBoolean(MODE, false);
    }

    public void setDate(String key){
        editor.putString(DATE,key);
        editor.commit();
    }



    public String getDate(){
        return pref.getString(DATE,null);
    }
    public void setString(String key,String value){
        editor.putString(key,value);
        editor.commit();
    }

    public String getStringValues(String key){
        return pref.getString(key,"");
    }
    public List<String> getList() {
        Set<String> set = new HashSet<>();
        set = pref.getStringSet("set", null);
        if (set == null) {
            return null;
        } else
            return new ArrayList<>(set);
    }

    public void setList(List <String> list) {
        Set <String> set = new HashSet <>();
        set.addAll(list);
        editor.putStringSet("set", set).commit();
    }
}
