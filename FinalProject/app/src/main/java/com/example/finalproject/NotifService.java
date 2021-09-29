package com.example.finalproject;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.example.finalproject.utils.ClassHolder;
import com.example.finalproject.utils.Utility;

public class NotifService extends Service implements LifecycleObserver{
    int startMode;       // indicates how to behave if the service is killed
    final int ONGOING_NOTIFICATION_ID = 10;
    ClassHolder classInstance = ClassHolder.getInstance();
    NotificationManagerCompat notificationManagerCompat;

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    void makeNotif() {
        Utility.removeBroadcast(getApplicationContext());
        if (!classInstance.checkSinglePass()){
            if (classInstance.extras == null){
                startForeground(ONGOING_NOTIFICATION_ID, Utility.createNotification(getApplicationContext(), classInstance.classAt,
                        notificationManagerCompat));

            } else {
                startForeground(ONGOING_NOTIFICATION_ID, Utility.createNotification(getApplicationContext(), classInstance.classAt,
                        notificationManagerCompat, classInstance.extras));
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    void removeNotif(){
        Utility.createBroadcast(getApplicationContext());
        stopForeground(true);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Utility.createNotificationChannel(this, NotificationManager.IMPORTANCE_HIGH);
        notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return startMode;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
