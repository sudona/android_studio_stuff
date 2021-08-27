package com.example.app6;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button notifOne, notifTwo, notifThree;
    private NotificationManagerCompat notificationManagerCompat;
    private String CHANNEL_ID = "mock_application";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notifOne = findViewById(R.id.notif_one);
        notifTwo = findViewById(R.id.notif_two);
        notifThree = findViewById(R.id.notif_three);

        notifOne.setOnClickListener(this);
        notifTwo.setOnClickListener(this);
        notifThree.setOnClickListener(this);

        createNotificationChannel(NotificationManager.IMPORTANCE_HIGH);
        notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
    }

    @Override
    public void onClick(View view) {
        boolean areNotificationsEnabled = notificationManagerCompat.areNotificationsEnabled();

        if (!areNotificationsEnabled) {
            // Because the user took an action to create a notification, we create a prompt to let
            // the user re-enable notifications for this application again.
            Snackbar snackbar = Snackbar
                    .make(
                            findViewById(R.id.main_layout),
                            "You need to enable notifications for this app",
                            Snackbar.LENGTH_LONG)
                    .setAction("ENABLE", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Links to this app's notification settings
                            openNotificationSettingsForApp();
                        }
                    });
            snackbar.show();
            return;
        }

        switch(view.getId()) {
            case R.id.notif_one:
                makeNotifOne();
                break;
            case R.id.notif_two:
                makeNotifTwo();
                break;
            case R.id.notif_three:
                makeNotifThree();
                break;
            default:
                break;
        }
    }

    private void createNotificationChannel(int importance_level) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = importance_level;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void makeNotifOne() {

        Intent intent = new Intent();
        intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
        intent.putExtra("app_package", getPackageName());
        intent.putExtra("app_uid", getApplicationInfo().uid);

        // for Android 8 and above
        intent.putExtra("android.provider.extra.APP_PACKAGE", getPackageName());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Sample Notif One")
                .setContentText("This is a sample of notif one")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("There is more of this than meets the eye: clicking on this item should pop up the same activity"))
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notificationManagerCompat.notify(0, builder.build());
    }

    private void makeNotifTwo() {

        Intent intent = new Intent();
        intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
        intent.putExtra("app_package", getPackageName());
        intent.putExtra("app_uid", getApplicationInfo().uid);

        // for Android 8 and above
        intent.putExtra("android.provider.extra.APP_PACKAGE", getPackageName());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Sample Notif Two")
                .setContentText("This is a sample of notif two")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("There is more of this than meets the eye: clicking on this item should pop up the same activity"))
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notificationManagerCompat.notify(1, builder.build());
    }

    private void makeNotifThree() {

        Intent intent = new Intent();
        intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
        intent.putExtra("app_package", getPackageName());
        intent.putExtra("app_uid", getApplicationInfo().uid);

        // for Android 8 and above
        intent.putExtra("android.provider.extra.APP_PACKAGE", getPackageName());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Sample Notif Three")
                .setContentText("This is a sample of notif three")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("There is more of this than meets the eye: clicking on this item should pop up the same activity"))
                .setContentIntent(pendingIntent)
                .setFullScreenIntent(fullScreenPendingIntent, true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        notificationManagerCompat.notify(2, builder.build());
    }

    private void openNotificationSettingsForApp() {
        // Links to this app's notification settings.
        Intent intent = new Intent();
        intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
        intent.putExtra("app_package", getPackageName());
        intent.putExtra("app_uid", getApplicationInfo().uid);

        // for Android 8 and above
        intent.putExtra("android.provider.extra.APP_PACKAGE", getPackageName());

        startActivity(intent);
    }
}