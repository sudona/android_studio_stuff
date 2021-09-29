package com.example.finalproject;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.adapters.UserListAdapter;
import com.example.finalproject.database.RoomDB;
import com.example.finalproject.user.User;
import com.example.finalproject.utils.ClassHolder;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ListAccount extends AppCompatActivity {

    private final static String url_const = "http://jsonplaceholder.typicode.com/users",
            TAG = "LISTACCOUNT",
            robo_hash = "https://robohash.org/";
    private static final int PERMISSION_STORAGE = 11, SELECT_PHOTO = 12;
    private RecyclerView recyclerUsers;
    private Button signOutButton;
    private GoogleSignInClient googleSignInClient;
    private Random random;
    private OkHttpClient client;
    private Gson gson;
    private RoomDB database;
    private final ClassHolder classHolder = ClassHolder.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = new OkHttpClient();
        gson = new Gson();
        random = new Random();

        database = RoomDB.getInstance(this);

        Display display = ((WindowManager) this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        setContentView(R.layout.activity_list_account);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account == null) {
            swapToMainActivity();
        }
        recyclerUsers = findViewById(R.id.list_users);

        signOutButton = findViewById(R.id.sign_out_button);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

        if (getIntent().getBooleanExtra("fromInfoPage", false)) {
            UserListAdapter cardAdapter = new UserListAdapter(this);
            recyclerUsers.setAdapter(cardAdapter);
            recyclerUsers.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        } else if (database.userDao().size() <= 1){
            addHTTPtoUserList();
        } else {
            createAdapter();
        }

        Intent intent = new Intent(this, NotifService.class);
        startService(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        classHolder.addIntentInfo(this.getClass());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
            case R.id.get_file:
                requestStoragePermission();
                return true;
            case R.id.send_intent_button:
                Intent intent = new Intent(getString(R.string.broadcast_click));
                sendBroadcast(intent);
                return true;
            case R.id.image_show_button:
                classHolder.addSinglePass();
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                return true;
            case R.id.map_locations_button:
                Intent mapLocation = new Intent(this, MapsUser.class);
                startActivity(mapLocation);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void signOut() {
        googleSignInClient.signOut()
        .addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                swapToMainActivity();
            }
        });
    }

    private void swapToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void addHTTPtoUserList() {
        Request request = new Request.Builder()
                .url(url_const)
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
                    runOnUiThread(() -> {
                        addtoList(users);
                        createAdapter();
                    });
                } catch (IOException ignored) {
                    Log.d(TAG, ignored.toString());
                    return;
                }
            }
        });
    }

    private void createAdapter() {
        UserListAdapter cardAdapter = new UserListAdapter(getApplicationContext());
        recyclerUsers.setAdapter(cardAdapter);
        recyclerUsers.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    private void addtoList(User[] users) {
        for (User user: users) {
            user.image = robo_hash + random.nextInt();
            database.userDao().insert(user);
        }
    }

    public void requestStoragePermission() {
        boolean hasPermission = (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PERMISSION_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case PERMISSION_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    String filename = "info.txt";
                    File external_storage;
                    if (this.getExternalMediaDirs().length > 0) {
                        external_storage = this.getExternalMediaDirs()[0];
                    } else {
                        external_storage = getFilesDir();
                    }

                    File file = new File(external_storage, filename);
                    FileOutputStream outputStream;
                    Gson gson = new Gson();
                    try {
                        outputStream = new FileOutputStream(file);

                        outputStream.write(gson.toJson(database.userDao().getAll()).getBytes(StandardCharsets.UTF_8));
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else
                {
                    Toast.makeText(this,
                            "You have denied write permissions.", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data != null) {
            Uri pickedImage = data.getData();
            Intent intent = new Intent(this, ImageViewer.class);
            intent.putExtra("image", pickedImage.toString());
            startActivity(intent);
        }
    }
}