package org.mesibo.messenger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mesibo.api.Mesibo;

import java.util.Objects;

public class AutoStart extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (Objects.equals(intent.getAction(), Intent.ACTION_BOOT_COMPLETED) ||
                Objects.equals(intent.getAction(), MainApplication.getRestartIntent())) {
            StartUpActivity.newInstance(context, true);
        }
    }
}
