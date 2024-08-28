package org.mesibo.messenger.fcm;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

public class MesiboJobIntentService extends JobIntentService {
    static final int JOB_ID = 1000;
    static void enqueueWork(Context context, Intent work) {
        try {
        enqueueWork(context, MesiboJobIntentService.class, JOB_ID, work);
        } catch (Exception e) {

        }
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        try {
        MesiboRegistrationIntentService.sendMessageToListener( true);
        } catch (Exception ignored) {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
