package com.example.androidprojectresto.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.androidprojectresto.Domain.Location;
import com.example.androidprojectresto.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Maps extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap myMap;
    private FirebaseDatabase database;
    private Spinner locationSpinner;
    private ArrayList<Location> locations; // List to store Location objects

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Initialize Firebase and Spinner
        database = FirebaseDatabase.getInstance();
        locationSpinner = findViewById(R.id.spinnerMap);

        // Set up the map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Fetch and populate Spinner
        fetchAndPopulateSpinner();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;
    }

    private void fetchAndPopulateSpinner() {
        DatabaseReference locationRef = database.getReference("Location"); // Reference to "Location" in Firebase
        locations = new ArrayList<>();

        locationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> locationNames = new ArrayList<>(); // List to store location names for Spinner

                if (snapshot.exists()) {
                    for (DataSnapshot locationSnapshot : snapshot.getChildren()) {
                        Location location = locationSnapshot.getValue(Location.class);
                        if (location != null) {
                            locations.add(location); // Add the Location object to the list
                            locationNames.add(location.getLoc()); // Add the location name to the Spinner list
                        }
                    }

                    // Populate the Spinner with location names
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            Maps.this,
                            android.R.layout.simple_spinner_item,
                            locationNames
                    );
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    locationSpinner.setAdapter(adapter);

                    // Handle Spinner item selection
                    locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Location selectedLocation = locations.get(position);

                            // Move camera to the selected location and add a marker
                            LatLng latLng = new LatLng(selectedLocation.getLatitude(), selectedLocation.getLongitude());
                            myMap.clear(); // Clear existing markers
                            myMap.addMarker(new MarkerOptions().position(latLng).title(selectedLocation.getLoc()));
                            myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10)); // Zoom level 10
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // Do nothing
                        }
                    });
                } else {
                    Log.d("FirebaseSpinner", "No locations found in Firebase.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseSpinner", "Error fetching locations: " + error.getMessage());
            }
        });
    }
}
