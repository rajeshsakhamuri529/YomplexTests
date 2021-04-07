package com.yomplex.tests.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.Nullable;

public class ContentVersionUpdateService extends Service {
    private static final long UPDATES_PERIOD = 1000 * 60; // 1 minute
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("ContentVersionUpdate", "onStartCommand");
        try {
            startTimer();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return START_STICKY;
    }

    public void startTimer() {
        AlarmManager alarmManager;
        PendingIntent pendingIntent;
        Log.e("ContentVersionUpdate", "BGRunningService broadcast receivers");
        alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        PackageManager pm = this.getPackageManager();
        ComponentName componentName = new ComponentName(ContentVersionUpdateService.this, ContentVersionUpdateReceiver.class);
        pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

        Intent intent0 = new Intent(this, ContentVersionUpdateReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent0, PendingIntent.FLAG_UPDATE_CURRENT);
        if (alarmManager != null)
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), UPDATES_PERIOD, pendingIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("EXIT", "onDestroy!");
        stopService(new Intent(this, ContentVersionUpdateService.class));

        PendingIntent service = PendingIntent.getService(getApplicationContext(), 1001, new Intent(getApplicationContext(), ContentVersionUpdateService.class), PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null)
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, 1000, service);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        //Code here
        Log.e("ContentVersionUpdate", "TASK REMOVED");

        PendingIntent service = PendingIntent.getService(getApplicationContext(), 1001, new Intent(getApplicationContext(), ContentVersionUpdateService.class), PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null)
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, 1000, service);
    }
}
