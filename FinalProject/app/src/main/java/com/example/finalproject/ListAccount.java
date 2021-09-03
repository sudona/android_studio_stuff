package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ListAccount extends AppCompatActivity {

    private static final String CHANNEL_ID = "FINAL_PROJ_CHANNEL";
    private ListUsers listUsers = ListUsers.getInstance();
    private final static String url_const = "http://jsonplaceholder.typicode.com/users",
            TAG = "LISTACCOUNT",
            robo_hash = "https://robohash.org/";
    private RecyclerView recyclerUsers;
    private Button signOutButton;
    private GoogleSignInClient googleSignInClient;
    private Random random;
    private OkHttpClient client;
    private Gson gson;
    private NotificationManagerCompat notificationManagerCompat;
    private boolean changedActivities;
    private int display_rotation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changedActivities = false;
        Display display = ((WindowManager) this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        display_rotation = display.getRotation();

        setContentView(R.layout.activity_list_account);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account == null) {
            swapToMainActivity();
        }
        client = new OkHttpClient();
        gson = new Gson();
        random = new Random();
        recyclerUsers = findViewById(R.id.list_users);

        signOutButton = findViewById(R.id.sign_out_button);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changedActivities = true;
                signOut();
            }
        });

        if (getIntent().getBooleanExtra("fromInfoPage", false)) {
            UserListAdapter cardAdapter = new UserListAdapter(listUsers.getUserList(), () -> {
                changedActivities = false;
            });
            recyclerUsers.setAdapter(cardAdapter);
            recyclerUsers.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        } else {
            addHTTPtoUserList();
        }
        createNotificationChannel(NotificationManager.IMPORTANCE_HIGH);
        notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
    }

    private void signOut() {
        googleSignInClient.signOut()
        .addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                swapToMainActivity();
            }
        });
    }

    private void swapToMainActivity() {
        changedActivities = true;
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void addHTTPtoUserList() {
        Request request = new Request.Builder()
                .url(url_const)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response){
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                    String response_string = responseBody.string();
                    User[] users = gson.fromJson(response_string, User[].class);
                    runOnUiThread(() -> {
                        addtoList(users);
                        UserListAdapter cardAdapter = new UserListAdapter(listUsers.getUserList(),
                                () -> { changedActivities = true; });
                        recyclerUsers.setAdapter(cardAdapter);
                        recyclerUsers.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    });
                } catch (IOException ignored) {
                    Log.d(TAG, ignored.toString());
                    return;
                }
            }
        });
    }

    private void addtoList(User[] users) {
        for (User user: users) {
            LinkedHashMap<String, String> user_add = new LinkedHashMap<String, String>() {{
                put("Image", robo_hash + random.nextInt());
                put("Name", user.getName());
                put("Email", user.getEmail());
                put("Website", user.getWebsite());
                put("Street", user.getAddress().getStreet());
            }};
            listUsers.addUser(user_add);
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

    private void createNotification() {
        Intent notificationIntent = new Intent(this, ListAccount.class );
        notificationIntent.addCategory(Intent. CATEGORY_LAUNCHER ) ;
        notificationIntent.setAction(Intent. ACTION_MAIN ) ;
        notificationIntent.setFlags(Intent. FLAG_ACTIVITY_CLEAR_TOP | Intent. FLAG_ACTIVITY_SINGLE_TOP );
        PendingIntent pendingIntent = PendingIntent. getActivity (this, 0 , notificationIntent , 0 ) ;

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
        if (!changedActivities && display.getRotation() == display_rotation) {
            createNotification();
        }
    }

    @Override
    public void onBackPressed() {
        changedActivities = true;
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("fromListAccount", true);
        startActivity(intent);
        super.onBackPressed();
    }
}