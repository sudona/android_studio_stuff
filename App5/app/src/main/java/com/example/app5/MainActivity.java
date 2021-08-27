package com.example.app5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MAINACTIVITY";
    private RecyclerView list_cards;
    private OkHttpClient client;
    private Gson gson;
    private static final String url = "https://imdb8.p.rapidapi.com/auto-complete?q=game%20of%20thr";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list_cards = findViewById(R.id.cards_list);
        client = new OkHttpClient();
        gson = new Gson();
        run(url);
    }

    void run(String url) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("x-rapidapi-host", "imdb8.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "ea5d601774msh1e9634e197b9bbcp15c681jsnab95db0feaf1")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    GsonClass tempList = gson.fromJson(responseBody.string(),
                            GsonClass.class);
                    CardInfo[] cardInfoList = new CardInfo[tempList.getMovieList().length];
                    for (int i = 0; i < tempList.getMovieList().length; i++) {
                        cardInfoList[i] = new CardInfo(
                                tempList.getMovieList()[i].getName(),
                                tempList.getMovieList()[i].getImage(),
                                tempList.getMovieList()[i].getType(),
                                tempList.getMovieList()[i].getActors(),
                                tempList.getMovieList()[i].getYear());
                    }
                    runOnUiThread(() -> {
                        CardAdapter cardAdapter = new CardAdapter(cardInfoList);
                        list_cards.setAdapter(cardAdapter);
                        list_cards.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    });
                } catch (IOException e) {
                    Log.d(TAG, "threw IO exception");
                }
            }
        });
    }
}