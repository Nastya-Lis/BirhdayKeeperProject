package com.example.birhdaykeeper.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.birhdaykeeper.activities.SortActivity;

public class NotifyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context, NotificationService.class);
        context.startForegroundService(intent1);
    }
}
