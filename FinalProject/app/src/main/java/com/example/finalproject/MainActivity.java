package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int SIGN_IN_CODE = 10;
    private static final String TAG = "MAIN",
            robo_hash = "https://robohash.org/",
            CHANNEL_ID = "FINAL_PROJ_CHANNEL";;
    private GoogleSignInClient googleSignInClient;
    private SignInButton signInButton;
    private Random random;
    private NotificationManagerCompat notificationManagerCompat;
    private boolean wentBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wentBack = getIntent().getBooleanExtra("fromListAccount", false);
        setContentView(R.layout.activity_main);

        random = new Random();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(this);

        Utility.createNotificationChannel(this, NotificationManager.IMPORTANCE_HIGH);
        notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case (R.id.sign_in_button):
                signIn();
                break;
        }
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        wentBack = true;
        startActivityForResult(signInIntent, SIGN_IN_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == SIGN_IN_CODE) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            switchTo(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.d(TAG, "signInResult:failed code=" + e.getStatusCode());
            switchTo(null);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            switchTo(account);
        }
    }

    public void switchTo(GoogleSignInAccount account) {
        if (account != null) {
            wentBack = true;
            Intent intent = new Intent(this, ListAccount.class);
            ListUsers userList = ListUsers.getInstance();
            //clear just in case for new person
            userList.getUserList().clear();
            userList.addUser(new LinkedHashMap<String, String>(){{
                if (account.getPhotoUrl() != null){
                    put("Image", account.getPhotoUrl().toString());
                } else {
                    put("Image", robo_hash + random.nextInt());
                }
                put("Name", account.getDisplayName());
                put("Email", account.getEmail());
                put("Website", "");
                put("Street", "");
            }});
            startActivity(intent);
        } else {
            Toast toast = Toast.makeText(this, "Please Sign In!", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utility.removeNotification(notificationManagerCompat);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!wentBack) {
            Utility.createNotification(this, MainActivity.class, notificationManagerCompat);
        } else {
            wentBack = false;
        }
    }
}