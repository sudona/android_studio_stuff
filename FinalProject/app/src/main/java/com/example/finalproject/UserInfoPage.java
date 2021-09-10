package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Picture;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UserInfoPage extends AppCompatActivity {
    private ListUsers userList = ListUsers.getInstance();
    private Map<String, String> user;
    private ImageView imageUser;
    private RecyclerView infoUserView;
    private final static int REQUEST_IMAGE_CAPTURE = 10;
    private String CHANNEL_ID = "FINAL_PROJ_CHANNEL";
    private NotificationManagerCompat notificationManagerCompat;
    private boolean wentBack;
    private int display_rotation;
    int position;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_page);
        gson = new Gson();

        wentBack = false;
        Display display = ((WindowManager) this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        display_rotation = display.getRotation();

        imageUser = findViewById(R.id.image_user_page);
        infoUserView = findViewById(R.id.info_user_page);

        if (userList.getUserList().size() == 0) {
            getData();
        }
        position = getIntent().getIntExtra("position", -1);

        if (position != -1){
            user = userList.getUserList().get(position);
            UserPageAdapter cardAdapter = new UserPageAdapter((LinkedHashMap<String, String>) userList.getUserList().get(position));
            infoUserView.setAdapter(cardAdapter);
            infoUserView.setLayoutManager(new LinearLayoutManager(this));

            Picasso.with(this).load(user.get("Image")).into(imageUser);
            imageUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(UserInfoPage.this);
                    builder.setMessage("Do you want to take a new photo?").setTitle("Picture")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    wentBack = true;
                                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                                }
                            })
                            .setNegativeButton("No", null);
                    builder.create().show();
                }
            });
        }
        createNotificationChannel(NotificationManager.IMPORTANCE_HIGH);
        notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(getContentResolver(), imageBitmap, "Title", null);
            user.put("Image", path);
            Picasso.with(this).load(user.get("Image")).into(imageUser);
        }
    }

    @Override
    public void onBackPressed() {
        wentBack = true;
        Intent intent = new Intent(this, ListAccount.class);
        intent.putExtra("fromInfoPage", true);
        startActivity(intent);
        super.onBackPressed();
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

    private void createNotification() {
        Intent notificationIntent = new Intent(this, UserInfoPage.class );
        notificationIntent.addCategory(Intent. CATEGORY_LAUNCHER ) ;
        notificationIntent.setAction(Intent. ACTION_MAIN ) ;
        notificationIntent.setFlags(Intent. FLAG_ACTIVITY_CLEAR_TOP | Intent. FLAG_ACTIVITY_SINGLE_TOP );
        notificationIntent.putExtra("position", position);
        PendingIntent pendingIntent = PendingIntent. getActivity (this, 0 ,
                notificationIntent , PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE) ;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Final Project")
                .setContentText("don't forget about me")
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notificationManagerCompat.notify(0, builder.build());
    }

    @Override
    protected void onStop() {
        super.onStop();
        Display display = ((WindowManager) this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        setData();
        if (!wentBack && display.getRotation() == display_rotation) {
            createNotification();
        } else {
            wentBack = false;
        }
    }

    private void setData() {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Type type = new TypeToken<ArrayList<LinkedHashMap<String, String>>>(){}.getType();
        String userListJson = gson.toJson(userList.getUserList(), type);
        editor.putString("userList", userListJson);
        editor.apply();
    }

    private void getData() {
        Type type = new TypeToken<ArrayList<LinkedHashMap<String, String>>>(){}.getType();
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        userList.replaceUserList(gson.fromJson(sharedPref.getString("userList", ""), type));
    }
}