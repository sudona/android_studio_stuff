package com.example.finalproject;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Utility {
    private static Gson gson = new Gson();
    private static final String prefName = "DATA", dataID = "userList";
    private static final int notificationId = 0;
    private static ListUsers listUsers = ListUsers.getInstance();

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

    public static void createNotification(Context context, Class user_class, NotificationManagerCompat notificationManagerCompat) {
        Intent notificationIntent = new Intent(context, user_class);
        notificationIntent.addCategory(Intent. CATEGORY_LAUNCHER ) ;
        notificationIntent.setAction(Intent. ACTION_MAIN ) ;
        notificationIntent.setFlags(Intent. FLAG_ACTIVITY_CLEAR_TOP | Intent. FLAG_ACTIVITY_SINGLE_TOP );
        PendingIntent pendingIntent = PendingIntent. getActivity (context, 0 , notificationIntent , PendingIntent.FLAG_IMMUTABLE ) ;

        String channelId = context.getString(R.string.channel_id);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Final Project")
                .setContentText("don't forget about me")
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);
        notificationManagerCompat.notify(notificationId, builder.build());
    }

    public static void setData(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Type type = new TypeToken<ArrayList<LinkedHashMap<String, String>>>(){}.getType();
        String userListJson = gson.toJson(listUsers.getUserList(), type);
        editor.putString(dataID, userListJson);
        editor.apply();
        return;
    }

    public static void getData(Context context) {
        Type type = new TypeToken<ArrayList<LinkedHashMap<String, String>>>(){}.getType();
        SharedPreferences sharedPref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        List<Map<String, String>> users = gson.fromJson(sharedPref.getString(dataID, ""), type);
        listUsers.replaceUserList(users);
    }

    public static void removeNotification(NotificationManagerCompat notificationManagerCompat) {
        notificationManagerCompat.cancel(notificationId);
    }
}
