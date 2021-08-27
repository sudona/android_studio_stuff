package com.example.onsaveinstancestate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView textOne, textTwo, textThree;
    final String holdOne = "One", holdTwo = "Two", holdThree = "Three",
        tag = "MAIN_ACTIVITY";
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(tag, "OnCreate called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textOne = findViewById(R.id.textOne);
        textTwo = findViewById(R.id.textTwo);
        textThree = findViewById(R.id.textThree);

        sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);

        if (savedInstanceState != null) {
            textOne.setText(savedInstanceState.getString(holdOne));
            textTwo.setText(savedInstanceState.getString(holdTwo));
        }
    }

    @Override
    protected void onPause() {
        Log.d(tag, "OnPause called");
        super.onPause();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(holdThree, textThree.getText().toString());
        editor.apply();
    }

    @Override
    protected void onResume() {
        Log.d(tag, "OnResume called");
        super.onResume();
        textThree.setText(sharedPref.getString(holdThree, ""));
    }

    @Override
    protected void onStart() {
        Log.d(tag, "OnStart called");
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.d(tag, "OnStop called");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(tag, "OnDestroy called");
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle savedState) {
        Log.d(tag, "OnSaveInstanceState called");
        savedState.putString(holdOne, textOne.getText().toString());
        savedState.putString(holdTwo, textTwo.getText().toString());
    }
}