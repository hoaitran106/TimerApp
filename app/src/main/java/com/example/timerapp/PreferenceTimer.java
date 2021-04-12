package com.example.timerapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceTimer {

    public static void setStartTimeInMillis(Long time, Context context) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putLong(AppConstants.START_TIME_IN_MILLIS, time);
        editor.apply();
    }

    public static long getStartTimeInMillis(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getLong(AppConstants.START_TIME_IN_MILLIS, 0);
    }

    public static void setTimeLeftInMillis(Long time, Context context) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putLong(AppConstants.TIME_LEFT_IN_MILLIS, time);
        editor.apply();
    }

    public static long getTimeLeftInMillis(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getLong(AppConstants.TIME_LEFT_IN_MILLIS, 0);
    }

    public static void setEndTime(Long time, Context context) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putLong(AppConstants.END_TIME, time);
        editor.apply();
    }

    public static long getEndTime(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getLong(AppConstants.END_TIME, 0);
    }

    public static void setTimerRunning(boolean isRunning, Context context) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean(AppConstants.TIMER_RUNNING, isRunning);
        editor.apply();
    }

    public static boolean getTimerRunning(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(AppConstants.TIMER_RUNNING, false);
    }

}
