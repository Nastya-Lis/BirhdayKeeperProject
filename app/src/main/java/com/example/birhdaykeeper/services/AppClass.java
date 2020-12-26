package com.example.birhdaykeeper.services;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;

public class AppClass extends Application {

    public final String CHANNEL_ID = "notificationServiceChannel";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    private void createNotificationChannel(){
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,
                "Notify Service Channel", NotificationManager.IMPORTANCE_DEFAULT);
        notificationChannel.setDescription("mine channel notify");

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(notificationChannel);
    }
}
