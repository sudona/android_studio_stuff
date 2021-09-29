package com.example.finalproject.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.finalproject.BroadcastReceiver;
import com.example.finalproject.R;
import com.example.finalproject.database.RoomDB;
import com.google.gson.Gson;

import java.util.Map;

public class Utility {
    private static Gson gson = new Gson();
    private static final String prefName = "DATA", dataID = "userList";
    private static final int notificationId = 0;
    private static RoomDB database = RoomDB.getInstance(null);
    private static BroadcastReceiver br;


    private Utility() {}

    public static void createNotificationChannel(Context context, int importance_level) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.channel_name);
            String description = context.getString(R.string.channel_description);
            String channelId = context.getString(R.string.channel_id);
            int importance = importance_level;
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static Notification createNotification(Context context, Class user_class,
                                                  NotificationManagerCompat notificationManagerCompat) {
        Intent notificationIntent = new Intent(context, user_class);
        notificationIntent.addCategory(Intent. CATEGORY_LAUNCHER ) ;
        notificationIntent.setAction(Intent. ACTION_MAIN ) ;
        notificationIntent.setFlags(Intent. FLAG_ACTIVITY_CLEAR_TOP | Intent. FLAG_ACTIVITY_SINGLE_TOP );
        PendingIntent pendingIntent = PendingIntent. getActivity (context, 0 ,
                notificationIntent , PendingIntent.FLAG_IMMUTABLE ) ;

        String channelId = context.getString(R.string.channel_id);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Final Project")
                .setContentText("don't forget about me")
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);
        return builder.build();
    }
    
    public static Notification createNotification(Context context, Class user_class,
                                          NotificationManagerCompat notificationManagerCompat,
                                          Map<String, Object> extraData) {
        Intent notificationIntent = new Intent(context, user_class);
        notificationIntent.addCategory(Intent. CATEGORY_LAUNCHER ) ;
        notificationIntent.setAction(Intent. ACTION_MAIN ) ;
        notificationIntent.setFlags(Intent. FLAG_ACTIVITY_CLEAR_TOP | Intent. FLAG_ACTIVITY_SINGLE_TOP );
        for (Map.Entry<String, Object> entry : extraData.entrySet()) {
            if (entry.getValue() instanceof Integer) {
                notificationIntent.putExtra(entry.getKey(), (int)entry.getValue());
            }
            else if (entry.getValue() instanceof String) {
                notificationIntent.putExtra(entry.getKey(), (String)entry.getValue());
            }
        }
        PendingIntent pendingIntent = PendingIntent. getActivity (context, 0 ,
                notificationIntent ,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE ) ;

        String channelId = context.getString(R.string.channel_id);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Final Project")
                .setContentText("don't forget about me")
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);
        return builder.build();
    }

    public static void createBroadcast(Context context){
        if (br == null) {
            br = new BroadcastReceiver();
            IntentFilter filter = new IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED);
            filter.addAction(context.getString(R.string.broadcast_click));
            context.getApplicationContext().registerReceiver(br, filter);
        }
    }

    public static void removeBroadcast(Context context) {
        if (br != null) {
            context.unregisterReceiver(br);
        }
        br = null;
    }
}
