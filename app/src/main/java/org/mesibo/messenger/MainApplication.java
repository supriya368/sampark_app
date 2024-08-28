package org.mesibo.messenger;

import android.app.Application;
import androidx.lifecycle.LifecycleObserver;
import android.content.Context;
import android.util.Log;

import com.mesibo.api.Mesibo;
import com.mesibo.calls.api.MesiboCall;
import com.mesibo.calls.api.MesiboGroupCallUiProperties;
import com.mesibo.mediapicker.ImagePicker;
import com.mesibo.mediapicker.MediaPicker;
import com.mesibo.calls.ui.MesiboCallUi;
import com.mesibo.messaging.MesiboUI;
import com.mesibo.messaging.MesiboUiDefaults;

public class MainApplication extends Application implements Mesibo.RestartListener, LifecycleObserver {
    public static final String TAG = "MesiboDemoApplication";
    private static Context mContext = null;
    private static MesiboCallUi mCallUi = null;
    private static AppConfig mConfig = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        Mesibo.setRestartListener(this);
        mConfig = new AppConfig(this);
        SampleAPI.init(getApplicationContext());

        mCallUi = MesiboCallUi.getInstance();
        MesiboCall.getInstance().init(mContext);

        MesiboUiDefaults opt = MesiboUI.getUiDefaults();
        opt.mToolbarColor = 0xff00868b;
        opt.emptyUserListMessage = "No messages! Click on the message icon above to start messaging!";
        opt.showAddressInProfileView = true;
        opt.showAddressAsPhoneInProfileView = true;
        MediaPicker.setToolbarColor(opt.mToolbarColor);
        ImagePicker.getInstance().setApp(this);

        // customize call screen
        MesiboCall.UiProperties up = MesiboCall.getInstance().getDefaultUiProperties();
        //up.showScreenSharing = true;

        // customize conference call screen
        MesiboGroupCallUiProperties gcp = MesiboCall.getInstance().getDefaultGroupCallUiProperties();
        //gcp.exitPrompt = "Exit?";
    }

    public static String getRestartIntent() {
        return "com.mesibo.sampleapp.restart";
    }

    public static Context getAppContext() {
        return mContext;
    }

    @Override
    public void Mesibo_onRestart() {
        Log.d(TAG, "OnRestart");
        StartUpActivity.newInstance(this, true);
    }

}

