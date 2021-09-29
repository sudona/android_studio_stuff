package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ImageViewer extends AppCompatActivity {
    ImageView image_pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);
        image_pic = findViewById(R.id.image_viewer);
        String imageURL =  getIntent().getStringExtra("image");
        if (imageURL != null) {
            Picasso.with(this).load(imageURL).into(image_pic);
        } else {
            Intent intent = new Intent(this, ListAccount.class);
            startActivity(intent);
        }
    }
}