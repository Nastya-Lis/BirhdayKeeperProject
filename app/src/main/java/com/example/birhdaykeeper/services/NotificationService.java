package com.example.birhdaykeeper.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.renderscript.RenderScript;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.birhdaykeeper.R;
import com.example.birhdaykeeper.activities.ShowInfoPersonActivity;
import com.example.birhdaykeeper.dataBaseManager.BirthdayManDataBaseContract;
import com.example.birhdaykeeper.dataBaseManager.BirthdayManSQLiteDataBase;
import com.example.birhdaykeeper.dataBaseManager.SQLDBException;
import com.example.birhdaykeeper.unit.BirthDayMan;
import com.example.birhdaykeeper.unit.ExceptionBirth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class NotificationService extends Service {

    public final String CHANNEL_ID = "notificationServiceChannel";
    public final String GROUP_KEY = "mineGroup";
    final String LOG_TAG = "mineLogs";
    BirthdayManSQLiteDataBase sqLiteDataBase;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "onCreate");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(LOG_TAG, "onBind");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "onStartCommand");
        someTask();
        return super.onStartCommand(intent, flags, startId);
       // return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy");
    }


    void someTask(){
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String dateFormat = simpleDateFormat.format(calendar.getTime());

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (sqLiteDataBase == null) {
                    sqLiteDataBase = BirthdayManSQLiteDataBase.getInstance(getApplicationContext());
                }
                try {
                    List<BirthDayMan> birthDayManList = sqLiteDataBase.takeMenByBirth(dateFormat);
                    Log.d(LOG_TAG, "size:" + birthDayManList.size());
                    madeNotification(birthDayManList);
                } catch (SQLDBException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }



    void madeNotification(List<BirthDayMan> birthDayManList){
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        int countChannel = 0;
        List<Notification> groups = new ArrayList<>();

        for(int i = 0; i < birthDayManList.size();i++){
            countChannel++;

            Intent intent = new Intent(this, ShowInfoPersonActivity.class);
            intent.putExtra(BirthDayMan.class.getSimpleName(),birthDayManList.get(i));
            intent.putExtra("NotificationID",countChannel);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(ShowInfoPersonActivity.class);
            stackBuilder.addNextIntent(intent);

            PendingIntent pendingIntent =
                    stackBuilder.getPendingIntent(countChannel, PendingIntent.FLAG_UPDATE_CURRENT);
          //  PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,0);

            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(this, CHANNEL_ID)
                            .setSmallIcon(R.drawable.ic_baseline_event_24)
                            .setContentTitle("Знаменательный день")
                            .setContentText("День рождения у " + birthDayManList.get(i).getName()
                                    + " " + birthDayManList.get(i).getSurname())
                            .setContentIntent(pendingIntent)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setGroup(GROUP_KEY);

            Notification notification = builder.build();
            groups.add(notification);
        }

        int mainId = 245;

        Notification summaryNotification = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_event_24)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setGroup(GROUP_KEY)
                .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_CHILDREN)
                .setGroupSummary(true)
                .build();

        for(int i = 0; i < groups.size();i++){
            notificationManager.notify(i+1,groups.get(i));
            SystemClock.sleep(1000);
        }

        notificationManager.notify(mainId,summaryNotification);
    }
}
