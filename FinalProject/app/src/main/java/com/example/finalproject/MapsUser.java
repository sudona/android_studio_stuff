package com.example.finalproject;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.example.finalproject.database.RoomDB;
import com.example.finalproject.user.Geo;
import com.example.finalproject.user.User;
import com.example.finalproject.utils.ClassHolder;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.finalproject.databinding.ActivityMapsUserBinding;

public class MapsUser extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsUserBinding binding;
    private RoomDB database;
    private ClassHolder classHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = RoomDB.getInstance(this);
        classHolder = ClassHolder.getInstance();
        classHolder.addIntentInfo(this.getClass());

        binding = ActivityMapsUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        for (User user: database.userDao().getAll()) {
            Geo location = user.getAddress().getGeo();
            LatLng latLng = new LatLng(location.getLat(), location.getLng());
            mMap.addMarker(new MarkerOptions().position(latLng).title(user.getName()));
        }
    }
}