package com.example.birhdaykeeper.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class NotificationService extends Service {

    public final String CHANNEL_ID = "notificationServiceChannel";
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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy");
    }


    void someTask() {
       calendar = Calendar.getInstance();
       simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
       String dateFormat = simpleDateFormat.format(calendar.getTime());
        new Thread(new Runnable() {
          @Override
          public void run() {
            if(sqLiteDataBase == null){
                sqLiteDataBase = BirthdayManSQLiteDataBase.getInstance(getApplicationContext());
            }
              try {
                  List<BirthDayMan> birthDayManList = sqLiteDataBase.takeMenByBirth(dateFormat);
                  Log.d(LOG_TAG,"size:" + birthDayManList.size() );
                  madeNotification(birthDayManList);
              } catch (SQLDBException e) {
                  e.printStackTrace();
              }
              stopSelf();
          }
      }).start();
    }


    void madeNotification(List<BirthDayMan> birthDayManList){
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        int countChannel = 0;

        Intent intent = new Intent(this, ShowInfoPersonActivity.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent,0);

        for (BirthDayMan man:birthDayManList) {

            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(this, CHANNEL_ID)
                            .setSmallIcon(R.drawable.ic_baseline_event_24)
                            .setContentTitle("Знаменательный день")
                            .setContentText("День рождения у " + man.getName()
                                    + " " + man.getSurname())
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            Notification notification = builder.build();
            countChannel++;
            notificationManager.notify(countChannel, notification);
        }

    }
}
