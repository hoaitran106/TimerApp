package com.example.timerapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView mTextViewCountDown;
    private Button mButtonSetStart;
    private Button mButtonPauseResume;
    private Button mButtonStop;
    private CountDownTimer mCountDownTimer;
    private MaterialProgressBar mProgressBar;
    private TimePicker mTimePicker;
    private boolean mTimerRunning;
    private long mStartTimeInMillis;
    private long mTimeLeftInMillis;
    private long mEndTime;

    private static final String TAG = "Main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextViewCountDown = findViewById(R.id.textView_countdown);
        mButtonSetStart = findViewById(R.id.button_set_start);
        mButtonPauseResume = findViewById(R.id.button_pause_resume);
        mButtonStop = findViewById(R.id.button_stop);
        mProgressBar = findViewById(R.id.progress_countdown);
        mTimePicker = findViewById(R.id.timePicker);
        mTimePicker.setIs24HourView(true);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mButtonSetStart.setOnClickListener(v -> {
            if (mButtonSetStart.getText().equals("Set")) {
                mTextViewCountDown.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.GONE);
                mButtonPauseResume.setVisibility(View.GONE);
                mButtonStop.setVisibility(View.GONE);
                mTimePicker.setVisibility(View.VISIBLE);
                mButtonSetStart.setText("Start");
            } else if (mButtonSetStart.getText().equals("Start")) {
                mButtonSetStart.setText("Set");
                mProgressBar.setVisibility(View.VISIBLE);
                mTextViewCountDown.setVisibility(View.VISIBLE);
                mButtonPauseResume.setVisibility(View.VISIBLE);
                mButtonStop.setVisibility(View.VISIBLE);
                mTimePicker.setVisibility(View.GONE);
                setTime();
                startTimer();
            }
        });

        mButtonPauseResume.setOnClickListener(v -> {
            if (mTimerRunning) {
                pauseTimer();
            } else {
                startTimer();
            }
        });

        mButtonStop.setOnClickListener(v -> resetTimer());
    }

    private void startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
                Log.e(TAG + "mTimeLeftInMillis", String.valueOf(mTimeLeftInMillis));
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onFinish() {
                mTimerRunning = false;
                updateWatchInterface();
                NotificationAlarm.showTimerExpired(getApplicationContext());
            }
        }.start();
        mTimerRunning = true;
        updateWatchInterface();
    }

    private void setTime() {
        int hour = mTimePicker.getHour();
        int min = mTimePicker.getMinute();

        long millisInput = (hour * 60 + min) * 60000;
        if (millisInput == 0) {
            Toast.makeText(MainActivity.this, "Please enter a positive number", Toast.LENGTH_SHORT).show();
            return;
        }
        mStartTimeInMillis = millisInput;
        mTimeLeftInMillis = mStartTimeInMillis;
        mProgressBar.setMax((int) mTimeLeftInMillis);
        updateCountDownText();
        updateWatchInterface();
        closeKeyboard();

    }
    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        updateWatchInterface();
    }

    private void resetTimer() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        mTimerRunning = false;
        mTimeLeftInMillis = 0L;
        mButtonPauseResume.setVisibility(View.GONE);
        mButtonStop.setVisibility(View.GONE);
        mButtonSetStart.setText("Set");
        mButtonSetStart.setVisibility(View.VISIBLE);
        updateCountDownText();
        updateWatchInterface();
    }

    private void updateCountDownText() {
        int hours = (int) (mTimeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((mTimeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
        String timeLeftFormatted;
        if (hours > 0) {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%d:%02d:%02d", hours, minutes, seconds);
        } else {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%02d:%02d", minutes, seconds);
        }
        mTextViewCountDown.setText(timeLeftFormatted);
        Log.e(TAG + "mTimeLeftInMillis", String.valueOf(mTimeLeftInMillis));
        mProgressBar.setProgress((int) (mTimeLeftInMillis));
    }

    private void updateWatchInterface() {
        if (mTimerRunning) {
            mButtonSetStart.setVisibility(View.GONE);
            mButtonStop.setVisibility(View.VISIBLE);
            mButtonPauseResume.setText("Pause");
        } else {
            //mButtonSetStart.setVisibility(View.VISIBLE);
            mButtonPauseResume.setText("Resume");
            if (mTimeLeftInMillis < 1000) {
                //Timeout expired
                mButtonPauseResume.setVisibility(View.GONE);
                mProgressBar.setProgress(0);
            } else {
                //Timeout has not expired
                mButtonPauseResume.setVisibility(View.VISIBLE);
            }
            if (mTimeLeftInMillis < mStartTimeInMillis) {
                Log.e(TAG, "mTimeLeftInMillis" + mTimeLeftInMillis + "mStartTimeInMillis" + mStartTimeInMillis);
                mButtonStop.setVisibility(View.VISIBLE);
            } else {
                mButtonStop.setVisibility(View.GONE);
            }
        }
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onPause() {
        super.onPause();

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }

        if (mTimerRunning) {
            //Show notification
            NotificationAlarm.showTimerRunning(this);
            setAlarmManager(this, System.currentTimeMillis(), mTimeLeftInMillis);
        }

        PreferenceTimer.setStartTimeInMillis(mStartTimeInMillis, this);
        PreferenceTimer.setTimeLeftInMillis(mTimeLeftInMillis, this);
        PreferenceTimer.setTimerRunning(mTimerRunning, this);
        PreferenceTimer.setEndTime(mEndTime, this);

    }


    @Override
    protected void onResume() {
        super.onResume();
        mStartTimeInMillis = PreferenceTimer.getStartTimeInMillis(this);
        mTimeLeftInMillis = PreferenceTimer.getTimeLeftInMillis(this);
        mTimerRunning = PreferenceTimer.getTimerRunning(this);


        if (mTimerRunning) {
            mEndTime = PreferenceTimer.getEndTime(this);
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();
            if (mTimeLeftInMillis < 0) {
                mTimeLeftInMillis = 0;
                mTimerRunning = false;
            } else {
                startTimer();
            }
        }

        updateCountDownText();
        updateWatchInterface();

        //Remove Alarm Manager
        removeAlarmManager(this);
        //Hide notification
        NotificationAlarm.hideTimerNotification(this);
    }

    public void setAlarmManager(Context context, Long nowMillis, Long millisRemaining) {
        long wakeUpTime = nowMillis + millisRemaining;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(context, NotificationAction.class);
        alarmIntent.setAction(AppConstants.ACTION_SHOW_TIME_EXPIRED_NOTIFY);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, wakeUpTime, pendingIntent);
    }

    public void removeAlarmManager(Context context) {
        Intent alarmIntent = new Intent(context, NotificationAction.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

}