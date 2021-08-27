package com.example.app2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.EnumSet;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    enum Permissions{
        BLUETOOTH(Manifest.permission.BLUETOOTH, 0, "bluetooth"),
        CALENDER(Manifest.permission.READ_CALENDAR, 1, "calender"),
        CAMERA(Manifest.permission.CAMERA, 2, "camera");

        String permission_manifest, permission_name;
        int request_code;

        Permissions(String manifest, int code, String name) {
            permission_manifest = manifest;
            request_code = code;
            permission_name = name;
        }

        public String getPermission_manifest() {
            return permission_manifest;
        }

        public String getPermission_name() {
            return permission_name;
        }

        public int getRequest_code() {
            return request_code;
        }
    }

    private Button bluetoothButton, calenderButton, cameraButton;
    private String LOG_NAME = "PERMISSIONS";
    private EnumSet<Permissions> all_permission = EnumSet.allOf(Permissions.class);
    private View mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bluetoothButton = findViewById(R.id.buttonBluetooth);
        calenderButton = findViewById(R.id.buttonCalender);
        cameraButton = findViewById(R.id.buttonCamera);
        mLayout = findViewById(R.id.main_layout);

        bluetoothButton.setOnClickListener(this);
        calenderButton.setOnClickListener(this);
        cameraButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case(R.id.buttonBluetooth):
                requestPermissions(Permissions.BLUETOOTH);
                break;
            case(R.id.buttonCalender):
                requestPermissions(Permissions.CALENDER);
                break;
            case(R.id.buttonCamera):
                requestPermissions(Permissions.CAMERA);
                break;
            default:
                break;
        }
    }

    private void requestPermissions(Permissions permission) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission.getPermission_manifest()) ==
                PackageManager.PERMISSION_GRANTED) {
            Log.d(LOG_NAME, "Permission for " + permission.getPermission_name() + " already requested.");
            Toast toast = Toast.makeText(MainActivity.this,
                    "Permission for " + permission.getPermission_name() + " already requested.",
                    Toast.LENGTH_SHORT);
            toast.show();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
                // Provide an additional rationale to the user if the permission was not granted
                // and the user would benefit from additional context for the use of the permission.
                // Display a SnackBar with cda button to request the missing permission.
                Snackbar.make(mLayout, permission.getPermission_name().substring(0,1).toUpperCase() +
                                permission.getPermission_name().substring(1) + " permission required.",
                        Snackbar.LENGTH_INDEFINITE).setAction("ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Request the permission
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{permission.getPermission_manifest()},
                                permission.getRequest_code());
                    }
                }).show();
            } else {
                Snackbar.make(mLayout, permission.getPermission_name().substring(0,1).toUpperCase() +
                                permission.getPermission_name().substring(1) + " permission unavailable.",
                        Snackbar.LENGTH_SHORT).show();
                // Request the permission. The result will be received in onRequestPermissionResult().
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{permission.getPermission_manifest()}, permission.getRequest_code());
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for (Permissions curr_permission: all_permission) {
            if (curr_permission.getRequest_code() == requestCode) {
                if (grantResults.length > 0 &&
                 grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(LOG_NAME,
                            "Permission for " + curr_permission.getPermission_name() + " granted.");
                    Toast toast = Toast.makeText(MainActivity.this,
                            "Permission for " + curr_permission.getPermission_name() + " granted.",
                            Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Log.d(LOG_NAME,
                            "Permission for " + curr_permission.getPermission_name() + " denied.");
//                    Toast toast = Toast.makeText(MainActivity.this,
//                            "Permission for " + curr_permission.getPermission_name() + " denied.",
//                            Toast.LENGTH_SHORT);
//                    toast.show();
                }
                break;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}