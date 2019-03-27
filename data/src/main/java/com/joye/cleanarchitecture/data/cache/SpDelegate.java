package com.joye.cleanarchitecture.data.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.joye.cleanarchitecture.domain.utils.MyLog;

import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

/**
 * SharePreference封装类
 * <p>
 * Created by joye on 2018/8/21.
 */

public class SpDelegate implements SharedPreferences {
    private static final String SP_NAME = "app_sp_cache";
    private SharedPreferences sharedPreferences;

    @Inject
    public SpDelegate(Context context) {
        MyLog.d("---SpManager constructor---");
        context = context.getApplicationContext() == null ? context : context.getApplicationContext();
        this.sharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public void putString(String key, String value) {
        edit().putString(key, value).apply();
    }

    public void putInt(String key, int value) {
        edit().putInt(key, value).apply();
    }

    public void putLong(String key, long value) {
        edit().putLong(key, value).apply();
    }

    public void putBoolean(String key, boolean value) {
        edit().putBoolean(key, value).apply();
    }

    public void putFloat(String key, float value) {
        edit().putFloat(key, value).apply();
    }

    public void putStringSet(String key, Set<String> value) {
        edit().putStringSet(key, value).apply();
    }

    public void remove(String key) {
        edit().remove(key).apply();
    }

    @Override
    public Map<String, ?> getAll() {
        return sharedPreferences.getAll();
    }

    @Nullable
    @Override
    public String getString(String key, @Nullable String defValue) {
        return sharedPreferences.getString(key, defValue);
    }

    @Nullable
    @Override
    public Set<String> getStringSet(String key, @Nullable Set<String> defValues) {
        return sharedPreferences.getStringSet(key, defValues);
    }

    @Override
    public int getInt(String key, int defValue) {
        return sharedPreferences.getInt(key, defValue);
    }

    @Override
    public long getLong(String key, long defValue) {
        return sharedPreferences.getLong(key, defValue);
    }

    @Override
    public float getFloat(String key, float defValue) {
        return sharedPreferences.getFloat(key, defValue);
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        return sharedPreferences.getBoolean(key, defValue);
    }

    @Override
    public boolean contains(String key) {
        return sharedPreferences.contains(key);
    }

    @Override
    public Editor edit() {
        return sharedPreferences.edit();
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
    }
}
