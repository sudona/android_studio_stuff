package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.finalproject.database.RoomDB;
import com.example.finalproject.utils.ClassHolder;
import com.example.finalproject.user.User;
import com.example.finalproject.user.UserBuilder;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.Random;
import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int SIGN_IN_CODE = 10, PERMISSION_LOCATION = 11;
    private static final String TAG = "MAIN",
            robo_hash = "https://robohash.org/";
    private GoogleSignInClient googleSignInClient;
    private SignInButton signInButton;
    private Random random;
    private RoomDB database;
    private final ClassHolder classHolder = ClassHolder.getInstance();
    private UserBuilder userBuilder = new UserBuilder();
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        random = new Random();
        database = RoomDB.getInstance(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(this);

        makeAlarm();

        Intent intent = new Intent(this, NotifService.class);
        startService(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.sign_in_button):
                signIn();
                break;
        }
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, SIGN_IN_CODE);
    }

    private void makeAlarm() {
        alarmMgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        Intent alarmRegIntent = new Intent(this, BroadcastReceiver.class);
        alarmRegIntent.setAction(getString(R.string.alarm_broadcast));
        alarmIntent = PendingIntent.getBroadcast(this, 0, alarmRegIntent, 0);

        alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() +
                        60 * 1000, 60 * 1000, alarmIntent);
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
        classHolder.addIntentInfo(this.getClass());
    }

    public void switchTo(GoogleSignInAccount account) {
        if (account != null) {
            Intent intent = new Intent(this, ListAccount.class);
            Task<Location> task_before = requestLocation();
            String imagePut = "";
            if (account.getPhotoUrl() != null) {
                imagePut = account.getPhotoUrl().toString();
            } else {
                imagePut = robo_hash + random.nextInt();
            }

            if (task_before != null) {
                String finalImagePut = imagePut;
                task_before.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        User newUser = userBuilder.image(finalImagePut).id(-1)
                                .name(account.getDisplayName()).email(account.getEmail()).build();
                        database.userDao().insert(newUser);
                        startActivity(intent);
                    }
                });
            } else {
                User newUser = userBuilder.image(imagePut).id(-1)
                        .name(account.getDisplayName()).email(account.getEmail()).build();
                database.userDao().insert(newUser);
                startActivity(intent);
            }
        } else {
            Toast toast = Toast.makeText(this, "Please Sign In!", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public Task<Location> requestLocation() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                PERMISSION_LOCATION);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            FusedLocationProviderClient locationClient = LocationServices.getFusedLocationProviderClient(this);
            return locationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, null)
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        userBuilder = userBuilder.geo(location.getLatitude(), location.getLongitude());
                    }
                }
            });
        }
        return null;
    }
}