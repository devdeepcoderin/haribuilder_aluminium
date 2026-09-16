package com.deepcoder.hbalum.datalayers.storage;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

public final class AppPref {

    private static final String PREF_NAME = "user_data";
    private static final String PREF_HINT_NAME = "app_hint_data";

    public static final String IS_FOREGROUND_NOTIFICATION = "IS_FOREGROUND_NOTIFICATION";

    public static final String PROJECT_FRAME = "PROJECT_FRAME";
    public static final String PROJECT_MULLION = "PROJECT_MULLION";

    public static final String OTHER_LABOUR = "OTHER_LABOUR";

    public static final float PRICE_OTHER_LABOUR = 870;
    public static final float VALUE_VAT = 16;

    public static final String CURRENCY = "CURRENCY";
    public static final String VALUE_CURRENCY = "ksh";
    public static final String VAT = "VAT";

    public static final String key_Al_open_wSash_price = "Al_open_wSash_price";
    public static final String key_Al_open_dSash_price = "Al_open_dSash_price";
    public static final String key_Al_slid_frame_price = "Al_slid_frame_price";
    public static final String key_Al_slid_sash_price = "Al_slid_sash_price";
    public static final String ACCESSORY_TILT = "accessory_tilt";
    public static final String ACCESSORY_TURN = "accessory_turn";
    public static final String ACCESSORY_TILTTURN = "accessory_tiltTurn";
    public static final String ACCESSORY_DOOR = "accessory_door";
    public static final String ACCESSORY_SLIDING = "accessory_sliding";
    public static final String KEY_GLASS = "glass_price";
    public static final String KEY_PANEL = "panel_price";
    public static final String PROJECT_TAX = "project_tax";
    public static final String PROJECT_CURRENCY = "project_currency";

    private static SharedPreferences sharedPreferences;
    private static SharedPreferences sharedPreferencesHint;

    private static AppPref instance;
    private SharedPreferences.Editor edit;
    private SharedPreferences.Editor editHint;

    private AppPref(Context mContext) {
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        edit = sharedPreferences.edit();

        sharedPreferencesHint = mContext.getSharedPreferences(PREF_HINT_NAME, Activity.MODE_PRIVATE);
        editHint = sharedPreferencesHint.edit();
    }

    public static synchronized AppPref getInstance(Context context) {
        if (instance == null) {
            instance = new AppPref(context);
        }
        return instance;
    }


    public String getValue(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    public int getValue(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    public long getValue(String key, long defaultValue) {
        return sharedPreferences.getLong(key, defaultValue);
    }

    public boolean getValue(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public boolean getHintValue(String key, boolean defaultValue) {
        return sharedPreferencesHint.getBoolean(key, defaultValue);
    }

    public float getValue(String key, float defaultValue) {
        return sharedPreferences.getFloat(key, defaultValue);
    }

    public Set<String> getStringSet(String key, Set<String> stringSet) {
        return sharedPreferences.getStringSet(key, stringSet);
    }

    //############Set values################

    public void setValue(String key, String defaultValue) {
        edit.putString(key, defaultValue).commit();
    }

    public void setValue(String key, int defaultValue) {
        edit.putInt(key, defaultValue).commit();
    }

    public void setValue(String key, long defaultValue) {
        edit.putLong(key, defaultValue).commit();

    }

    public void setValue(String key, boolean defaultValue) {
        edit.putBoolean(key, defaultValue).commit();
    }

    public void setHintValue(String key, boolean defaultValue) {
        editHint.putBoolean(key, defaultValue).commit();
    }

    public void setValue(String key, float defaultValue) {
        edit.putFloat(key, defaultValue).commit();
    }

    public void setStringSet(String key, Set<String> stringSet) {
        edit.putStringSet(key, stringSet).commit();
    }

    public void clear() {
        edit.clear().commit();
    }

}
