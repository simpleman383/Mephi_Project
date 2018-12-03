package com.example.simpleman383.monitoring;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class CommonMonitoringService extends Service {

    SystemProcessMonitor monitor;


    public CommonMonitoringService() {
        monitor = new SystemProcessMonitor();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(this.getClass().getName(), "On service bind...");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(this.getClass().getName(), "Starting service...");
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i(this.getClass().getName(), "Destroying service...");
        super.onDestroy();
    }
}
