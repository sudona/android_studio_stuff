package com.example.app5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class Listings extends AppCompatActivity {
    private TextView name, actor, year, type;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listings);

        Intent intent = getIntent();
        name = findViewById(R.id.name);
        actor = findViewById(R.id.actors);
        year = findViewById(R.id.year);
        type = findViewById(R.id.type);
        image = findViewById(R.id.card_pic);

        name.setText(intent.getCharSequenceExtra("name"));
        actor.setText(intent.getCharSequenceExtra("actors"));
        year.setText(intent.getCharSequenceExtra("year"));
        type.setText(intent.getCharSequenceExtra("type"));
        Picasso.with(this).load(String.valueOf(intent.getCharSequenceExtra("picture"))).into(image);
    }
}