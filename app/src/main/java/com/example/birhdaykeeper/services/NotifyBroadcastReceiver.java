package com.example.birhdaykeeper.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotifyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startForegroundService(new Intent(context,NotificationService.class));
    }
}
