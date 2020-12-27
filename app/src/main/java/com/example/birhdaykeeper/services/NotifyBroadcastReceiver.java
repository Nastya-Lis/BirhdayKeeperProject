package com.example.birhdaykeeper.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.birhdaykeeper.activities.SortActivity;

public class NotifyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
       // context.startForegroundService(new Intent(context,NotificationService.class));
        Intent intent1 = new Intent(context, NotificationService.class);
       // intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startForegroundService(intent1);
        //context.startActivity(intent1);
    }
}
