package com.jikabao.android.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.jikabao.android.AppApplication;


/**
 * Created by pengjianqing on 7/2/14.
 */
public class AppPreference {
    private static AppPreference INSTANCE;

    private static final String PREF_VISITNUM = "visitNum";
    private static final String PREFERENCE = "app_preference";
    private static final String PREF_userId= "userId";

    //Cache some values.
    private int visitNum;
    private String userId;

    public AppPreference() {
        visitNum = get(PREF_VISITNUM, 0);
        userId = get(PREF_userId, "");
    }

    public static AppPreference getInstance(){
        if (null == INSTANCE){
            INSTANCE = new AppPreference();
        }

        return INSTANCE;
    }

    private SharedPreferences.Editor editor() {
        SharedPreferences.Editor editor = preferences().edit();
        return editor;
    }

    private SharedPreferences preferences() {
        Context context = AppApplication.getContext();
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);

        return preferences;
    }

    private void set(String key, String values) {
        editor().putString(key, values).commit();
    }

    private void set(String key, int values) {
        editor().putInt(key, values).commit();
    }

    private void set(String key, boolean values) {
        editor().putBoolean(key, values).commit();
    }

    private int get(String key, int defValue) {
        return preferences().getInt(key, defValue);
    }

    private String get(String key, String defValue) {
        return preferences().getString(key, defValue);
    }

    private boolean get(String key, boolean defValue) {
        return preferences().getBoolean(key, defValue);
    }

    public void increaseVisitNum() {
        int value = get(PREF_VISITNUM, 0);
        value += 1;
        set(PREF_VISITNUM, value);

        visitNum = value;
    }

    public int getVisitNum() {
        return visitNum;
    }

    public String getUserId(){
        return userId;
    }

    public void setUserId(String userId){
        set(PREF_userId, userId);
        this.userId = userId;
    }

    public void clear() {
        editor().clear().commit();
    }

}
