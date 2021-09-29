package com.example.finalproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.finalproject.adapters.UserPageAdapter;
import com.example.finalproject.database.RoomDB;
import com.example.finalproject.utils.ClassHolder;
import com.example.finalproject.user.User;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class UserInfoPage extends AppCompatActivity {
    private User user;
    private ImageView imageUser;
    private RecyclerView infoUserView;
    private final static int REQUEST_IMAGE_CAPTURE = 10;
    private String CHANNEL_ID = "FINAL_PROJ_CHANNEL";
    private NotificationManagerCompat notificationManagerCompat;
    int position;
    private RoomDB database;
    private final ClassHolder classHolder = ClassHolder.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_page);

        imageUser = findViewById(R.id.image_user_page);
        infoUserView = findViewById(R.id.info_user_page);

        database = RoomDB.getInstance(this);

        position = getIntent().getIntExtra("position", -1);

        if (position != -1){
            user = database.userDao().getAll().get(position);
            UserPageAdapter cardAdapter = new UserPageAdapter(this, position);
            infoUserView.setAdapter(cardAdapter);
            infoUserView.setLayoutManager(new LinearLayoutManager(this));

            Picasso.with(this).load(user.getImage()).into(imageUser);
            imageUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(UserInfoPage.this);
                    builder.setMessage("Do you want to take a new photo?").setTitle("Picture")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    classHolder.addSinglePass();
                                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                                }
                            })
                            .setNegativeButton("No", null);
                    builder.create().show();
                }
            });
        }

        Intent intent = new Intent(this, NotifService.class);
        startService(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        HashMap<String, Object> newExtras = new HashMap<>();
        newExtras.put("position", position);
        classHolder.addIntentInfo(this.getClass(), newExtras);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(getContentResolver(), imageBitmap,
                    "Title", null);
            user.addFromRep("Image", path);
            database.userDao().update(user);
            Picasso.with(this).load(user.getImage()).into(imageUser);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, ListAccount.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.user_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        if (item.getItemId() == R.id.send_email_button) {
            String[] sendTo = {user.getEmail()};
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setData(Uri.parse("mailto:"));
            emailIntent.setType("text/plain");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, sendTo);
            try {
                classHolder.addSinglePass();
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(this,
                        "There is no email client installed.", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
