package com.example.app4b;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button imageButton;
    private ImageView imageFrame;
    private static final String url = "https://robohash.org/ags";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageButton = findViewById(R.id.button);
        imageFrame = findViewById(R.id.image_pic);

        imageButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case (R.id.button):
                Picasso.with(this).load(url).into(imageFrame);
                break;
            default:
                break;
        }
    }

}