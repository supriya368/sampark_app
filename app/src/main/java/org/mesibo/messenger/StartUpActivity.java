package org.mesibo.messenger;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.mesibo.api.Mesibo;
import com.mesibo.messaging.MesiboUI;
import com.mesibo.uihelper.MesiboUiHelperConfig;

public class StartUpActivity extends AppCompatActivity {

    private static final String TAG = "MesiboStartupActivity";
    public final static String INTENTEXIT="exit";
    public final static String SKIPTOUR="skipTour";
    public final static String STARTINBACKGROUND ="startinbackground";
    private boolean mRunInBackground = false;

    public static void newInstance(Context context, boolean startInBackground) {
        Intent i = new Intent(context, StartUpActivity.class);  //MyActivity can be anything which you want to start on bootup...
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        i.putExtra(StartUpActivity.STARTINBACKGROUND, startInBackground);

        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getIntent().getBooleanExtra(INTENTEXIT, false)) {
            Log.d(TAG, "onCreate closing");
            finish();
            return;
        }



        mRunInBackground = getIntent().getBooleanExtra(STARTINBACKGROUND, false);

        long elapsed = Mesibo.getTimestamp() - Mesibo.getStartTimestamp();

        Log.e(TAG, "Elapsed " + elapsed + ", " + (mRunInBackground?"background":"foreground"));
        if(elapsed > 30000)
            mRunInBackground = false;

        if(mRunInBackground) {
            Log.e(TAG, "Moving app to background");
            moveTaskToBack(true);
        } else {
            Log.e(TAG, "Not Moving app to background");
        }


        setContentView(R.layout.activity_blank_launcher);
        startNextActivity();
    }

    void startNextActivity() {

        if(TextUtils.isEmpty(SampleAPI.getToken())) {
            MesiboUiHelperConfig.mDefaultCountry = 1;
            MesiboUiHelperConfig.mPhoneVerificationBottomText = "Note, Mesibo may call instead of sending an SMS if SMS delivery to your phone fails.";


            if(getIntent().getBooleanExtra(SKIPTOUR, false)) {
                UIManager.launchLogin(this, MesiboListeners.getInstance());
            } else {
                UIManager.launchWelcomeactivity(this, true, MesiboListeners.getInstance(), MesiboListeners.getInstance());
            }

            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        } else {
            MesiboUI.MesiboUserListScreenOptions opts = new MesiboUI.MesiboUserListScreenOptions();
            opts.keepRunning = true;
            opts.startInBackground = mRunInBackground;
            UIManager.launchMesibo(this, opts);
        }

        finish();
    }

    // since this activity is singleTask, intent will be delivered here if it's running
    @Override
    protected void onNewIntent(Intent intent) {
        Log.d(TAG, "onNewIntent");
        if(intent.getBooleanExtra(INTENTEXIT, false)) {
            finish();
        }

        super.onNewIntent(intent);
    }
}
