package com.example.app4a;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String
            TAG = "USERS",
            keyUsers = "users",
            url_const = "http://jsonplaceholder.typicode.com/users";
    private OkHttpClient client;
    private Gson gson;
    private Button dataButton;
    private TextView dataView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        client = new OkHttpClient();
        gson = new Gson();

        dataButton = findViewById(R.id.button);
        dataButton.setOnClickListener(this);
        dataView = findViewById(R.id.app_data);
        dataView.setMovementMethod(new ScrollingMovementMethod());

        if (savedInstanceState != null) {
            String values = savedInstanceState.getString(keyUsers);
            if (values != null) {
                changeView(values);
            }

        }
    }

    public void run(String url){
        Request request = new Request.Builder()
                .url(url)
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
                    runOnUiThread(() -> changeView(users));
                } catch (IOException ignored) {
                    Log.d(TAG, ignored.toString());
                    return;
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case (R.id.button):
                run(url_const);
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(keyUsers, dataView.getText().toString());
    }

    void changeView(User[] users) {
        StringBuilder string_data = new StringBuilder();
        for (User user: users){
            String adding = String.format("Name: %s\nUsername: %s\nEmail: %s\nWebsite: %s\nStreet: %s\n\n",
                    user.getName(), user.getUsername(), user.getEmail(), user.getWebsite(),
                    user.getAddress().getStreet());
            string_data.append(adding);
        }
        dataView.setText(string_data.toString());
    }

    //overloaded for onSaveInstanceState
    void changeView(String users) {
        dataView.setText(users);
    }
}