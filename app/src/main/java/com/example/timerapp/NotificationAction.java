package com.example.timerapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.RequiresApi;

import static android.content.Context.MODE_PRIVATE;

public class NotificationAction extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(AppConstants.ACTION_SHOW_TIME_EXPIRED_NOTIFY)) {
            NotificationAlarm.showTimerExpired(context);
            PreferenceTimer.setTimerRunning(false, context);
            PreferenceTimer.setTimeLeftInMillis(0L, context);
        }
    }
}
