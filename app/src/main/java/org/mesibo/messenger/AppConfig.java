package org.mesibo.messenger;

import com.google.gson.Gson;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import java.util.UUID;

public class AppConfig {
    private static final String TAG = "AppSettings";
    public static final String sharedPrefKey = "org.mesibo.messenger";
    private static final String systemPreferenceKey = "mesibo-app-settings";

    public static class Configuration {
        public String token = "01domusb10uspu9ferb7rpstdp79djtvf2wvdch9ak3ugi4qklfic2low8gi3aj4";
        public String phone = "8770081415";
        public SampleAPI.Invite invite = null;
        public String uploadurl = null;
        public String downloadurl = null;
        public String uniqueid = null;

        public void reset() {
            token = "";
            phone = "";
            invite = null;
            uploadurl = "";
            downloadurl = "";
        }
    }

    public static Configuration mConfig = new Configuration();

    final SharedPreferences mSharedPref;
    
    public static AppConfig getInstance() {
        return _instance;
    }

    public static Configuration getConfig() {
        return mConfig;
    }

    private static AppConfig _instance  = null;
    public AppConfig(Context c) {
        _instance = this;
        mSharedPref = c.getSharedPreferences(sharedPrefKey, Context.MODE_PRIVATE);
        boolean firstTime = false;
        if (!mSharedPref.contains(systemPreferenceKey)) {
            firstTime = true;
        }
        getAppSetting();
        if(TextUtils.isEmpty(mConfig.uniqueid)) {
            mConfig.uniqueid = UUID.randomUUID().toString();
            saveSettings();
        } else if (firstTime) {
            saveSettings();
        }
    }

    private void backup() {
    }
    
    public void getAppSetting() {
        Gson gson = new Gson();
        String json = mSharedPref.getString(systemPreferenceKey, "");
        mConfig = gson.fromJson(json, Configuration.class);

        if(null == mConfig)
            mConfig = new Configuration();
    }

    private void putAppSetting(SharedPreferences.Editor spe) {
        Gson gson = new Gson();

        String json = gson.toJson(mConfig);
        spe.putString(systemPreferenceKey, json);
        spe.commit();
    }

    public static void reset() {
        mConfig.reset();
        save();
        getInstance().backup();
    }


    public static void save() {
        getInstance().saveSettings();
    }

    public void saveSettings() {
        Log.d(TAG, "Updating RMS .. ");
        try {
            synchronized (mSharedPref) {
                SharedPreferences.Editor spe = mSharedPref.edit();
                putAppSetting(spe);
                backup();
            }
        } catch (Exception e) {
            Log.d(TAG, "Unable to updateRMS(): " + e.getMessage());
        }

    }
}

