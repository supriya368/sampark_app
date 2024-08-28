package org.mesibo.messenger.fcm;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.mesibo.messenger.MainApplication;

public class MesiboGcmListenerService extends FirebaseMessagingService {

    private static final String TAG = "FcmListenerService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());
        Bundle data = new Bundle();
        data.putString("body", "newPushNotification");
        MesiboRegistrationIntentService.sendMessageToListener(false);
        Intent intent = new Intent("com.mesibo.someintent");
        intent.putExtras(data);
        MesiboJobIntentService.enqueueWork(MainApplication.getAppContext(), intent);

    }


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("newToken", s);
        getSharedPreferences("_", MODE_PRIVATE).edit().putString("fb", s).apply();

    }
}
