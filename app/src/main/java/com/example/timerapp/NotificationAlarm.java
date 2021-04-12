package com.example.timerapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;


import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class NotificationAlarm {

    private static final String CHANNEL_ID_TIMER = "Menu_Timer";
    private static final String CHANNEL_NAME_TIMER = "Timer_App";
    private static final int TIMER_ID = 0;
    private static Ringtone ringTone;

    private static NotificationCompat.Builder getTemplateNotificationBuilder(Context context, String channelId, Boolean playSound) {
        Uri uriSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_baseline_timer_24)
                .setAutoCancel(true)
                .setDefaults(0);
        if (playSound) {
            ringTone = RingtoneManager.getRingtone(context, uriSound);
        }
        return mBuilder;
    }

    private static <T> PendingIntent getPendingIntentWithStack(Context context, Class<T> javaClass) {
        Intent resultIntent = new Intent(context, javaClass);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(javaClass);
        stackBuilder.addNextIntent(resultIntent);
        return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void showTimerExpired(Context context) {

        NotificationCompat.Builder mBuilder = getTemplateNotificationBuilder(context, CHANNEL_ID_TIMER, true);
        mBuilder.setContentTitle("Timer Expired!")
                .setContentText("Click to launch again")
                .setContentIntent(getPendingIntentWithStack(context, MainActivity.class));
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID_TIMER, CHANNEL_NAME_TIMER, NotificationManager.IMPORTANCE_HIGH);

        notificationChannel.enableLights(true);
        notificationChannel.enableVibration(true);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.createNotificationChannel(notificationChannel);
        mNotificationManager.notify(TIMER_ID, mBuilder.build());
        ringTone.play();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void showTimerRunning(Context context) {
        NotificationCompat.Builder mBuilder = getTemplateNotificationBuilder(context, CHANNEL_ID_TIMER, false);
        mBuilder.setContentTitle("Timer is Running!")
                .setContentText("Click to launch app")
                .setContentIntent(getPendingIntentWithStack(context, MainActivity.class))
                .setOngoing(true);
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID_TIMER, CHANNEL_NAME_TIMER, NotificationManager.IMPORTANCE_LOW);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.createNotificationChannel(notificationChannel);
        mNotificationManager.notify(TIMER_ID, mBuilder.build());
    }

    public static void hideTimerNotification(Context context) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(TIMER_ID);
    }

}
