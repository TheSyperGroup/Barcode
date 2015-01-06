package com.jikabao.android.common.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.jikabao.android.common.BaseApplication;


/**
 * Created by pengjianqing on 7/2/14.
 */
public class BaseAppPreference {
    protected static BaseAppPreference INSTANCE;

    protected static final String PREF_VISITNUM = "visitNum";
    protected static final String PREFERENCE = "app_preference";
    protected static final String PREF_userId= "userId";

    //Cache some values.
    protected int visitNum;
    protected String userId;

    public BaseAppPreference() {
        visitNum = get(PREF_VISITNUM, 0);
        userId = get(PREF_userId, "");
    }

    public static BaseAppPreference getInstance(){
        if (null == INSTANCE){
            INSTANCE = new BaseAppPreference();
        }

        return INSTANCE;
    }

    protected SharedPreferences.Editor editor() {
        SharedPreferences.Editor editor = preferences().edit();
        return editor;
    }

    protected SharedPreferences preferences() {
        Context context = BaseApplication.getContext();
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);

        return preferences;
    }

    protected void set(String key, String values) {
        editor().putString(key, values).commit();
    }

    protected void set(String key, int values) {
        editor().putInt(key, values).commit();
    }

    protected void set(String key, boolean values) {
        editor().putBoolean(key, values).commit();
    }

    protected int get(String key, int defValue) {
        return preferences().getInt(key, defValue);
    }

    protected String get(String key, String defValue) {
        return preferences().getString(key, defValue);
    }

    protected boolean get(String key, boolean defValue) {
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
