package com.example.androidprojectresto.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.example.androidprojectresto.Domain.Category;
import com.example.androidprojectresto.Domain.Location;
import com.example.androidprojectresto.Domain.Time;
import com.example.androidprojectresto.R;
import com.example.androidprojectresto.databinding.ActivityModifRestoBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ModifResto extends AppCompatActivity {

    private FirebaseDatabase database;
    private HashMap<Integer, Location> locations;
    private HashMap<Integer, Time> times;
    private HashMap<Integer, Category> categories;
    private ActivityModifRestoBinding binding;
    private int RestoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityModifRestoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();

        initCategory();
        initLocation();
        initTime();
        loadRestoData(); // Load existing data into UI

        binding.btnModifyRestaurant.setOnClickListener(v -> {
            updateResto(); // Update data on button click
        });
    }

    private void loadRestoData() {
        // Get the Intent that started this activity
        Intent intent = getIntent();

        // Retrieve the values passed through the Intent
        RestoId = intent.getIntExtra("id", -1);
        Log.i("ModifResto", "Resto ID: " + RestoId);

        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");
        int categoryId = intent.getIntExtra("category", -1);
        int locationId = intent.getIntExtra("location", -1);
        int timeId = intent.getIntExtra("time", -1);
        String budget = intent.getStringExtra("budget");
        String imagePath = intent.getStringExtra("imagePath");

        // Set the values to the UI elements
        if (title != null) binding.etTitle.setText(title);
        if (description != null) binding.etDescription.setText(description);
        if (budget != null) binding.etBudget.setText(budget);
        if (imagePath != null) binding.etImagePath.setText(imagePath);

        // Set spinner selections after data is loaded in each init method
    }

    private void initTime() {
        DatabaseReference timeRef = database.getReference("Time");
        times = new HashMap<>();

        timeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> timesValues = new ArrayList<>();
                if (snapshot.exists()) {
                    for (DataSnapshot timeSnapshot : snapshot.getChildren()) {
                        Time time = timeSnapshot.getValue(Time.class);
                        if (time != null) {
                            int id = timeSnapshot.child("Id").getValue(Integer.class) != null
                                    ? timeSnapshot.child("Id").getValue(Integer.class)
                                    : 0;
                            times.put(id, time);
                            timesValues.add(time.getValue());
                        }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(ModifResto.this, android.R.layout.simple_spinner_item, timesValues);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.spinnerTime.setAdapter(adapter);

                    // Pre-select the time based on intent extra
                    int timeId = getIntent().getIntExtra("time", -1);
                    if (times.containsKey(timeId)) {
                        String timeValue = times.get(timeId).getValue();
                        int position = adapter.getPosition(timeValue);
                        if (position >= 0) binding.spinnerTime.setSelection(position);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("ModifResto", "Failed to load times: " + error.getMessage());
            }
        });
    }

    private void initLocation() {
        DatabaseReference locationRef = database.getReference("Location");
        locations = new HashMap<>();

        locationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> locationsNames = new ArrayList<>();
                if (snapshot.exists()) {
                    for (DataSnapshot locationSnapshot : snapshot.getChildren()) {
                        Location location = locationSnapshot.getValue(Location.class);
                        if (location != null) {
                            int id = locationSnapshot.child("Id").getValue(Integer.class) != null
                                    ? locationSnapshot.child("Id").getValue(Integer.class)
                                    : 0;
                            locations.put(id, location);
                            locationsNames.add(location.getLoc());
                        }
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(ModifResto.this, android.R.layout.simple_spinner_item, locationsNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.spinnerLocation.setAdapter(adapter);

                // Pre-select the location based on intent extra
                int locationId = getIntent().getIntExtra("location", -1);
                if (locations.containsKey(locationId)) {
                    String locationName = locations.get(locationId).getLoc();
                    int position = adapter.getPosition(locationName);
                    if (position >= 0) binding.spinnerLocation.setSelection(position);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("ModifResto", "Failed to load locations: " + error.getMessage());
            }
        });
    }

    private void initCategory() {
        DatabaseReference categoryRef = database.getReference("Category");
        categories = new HashMap<>();

        categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> categoryNames = new ArrayList<>();

                if (snapshot.exists()) {
                    for (DataSnapshot categorySnapshot : snapshot.getChildren()) {
                        Category category = categorySnapshot.getValue(Category.class);
                        if (category != null) {
                            int id = categorySnapshot.child("Id").getValue(Integer.class) != null
                                    ? categorySnapshot.child("Id").getValue(Integer.class)
                                    : 0;
                            categories.put(id, category);
                            categoryNames.add(category.getName());
                        }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(ModifResto.this, android.R.layout.simple_spinner_item, categoryNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.spinnerCategory.setAdapter(adapter);

                    // Pre-select the category based on intent extra
                    int categoryId = getIntent().getIntExtra("category", -1);
                    if (categories.containsKey(categoryId)) {
                        String categoryName = categories.get(categoryId).getName();
                        int position = adapter.getPosition(categoryName);
                        if (position >= 0) binding.spinnerCategory.setSelection(position);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("ModifResto", "Failed to load categories: " + error.getMessage());
            }
        });
    }

    private void updateResto() {
        // Retrieve updated data
        String title = binding.etTitle.getText().toString().trim();
        String description = binding.etDescription.getText().toString().trim();
        String budget = binding.etBudget.getText().toString().trim();
        String imagePath = binding.etImagePath.getText().toString().trim();

        int categoryId = binding.spinnerCategory.getSelectedItemPosition();
        int locationId = binding.spinnerLocation.getSelectedItemPosition();
        int timeId = binding.spinnerTime.getSelectedItemPosition();

        // Validate inputs
        if (title.isEmpty() || description.isEmpty() || budget.isEmpty() || imagePath.isEmpty()) {
            Log.e("ModifResto", "All fields must be filled");
            return;
        }

        // Update data in Firebase
        DatabaseReference restoRef = database.getReference("resto").child(String.valueOf(RestoId));
        Map<String, Object> updatedData = new HashMap<>();
        updatedData.put("Title", title);
        updatedData.put("Description", description);
        updatedData.put("Budget", budget);
        updatedData.put("ImagePath", imagePath);
        updatedData.put("CategoryId", categoryId);
        updatedData.put("LocationId", locationId);
        updatedData.put("TimeId", timeId);

        restoRef.updateChildren(updatedData)
                .addOnSuccessListener(aVoid -> {
                    Log.i("ModifResto", "Restaurant updated successfully");
                    finish();
                })
                .addOnFailureListener(e ->{
                    Log.e("ModifResto", "Failed to update restaurant: " + e.getMessage());
                    showErrorDialog("Error adding restaurant", e.getMessage());
                });
    }
    private void showErrorDialog(String title, String message) {
        new android.app.AlertDialog.Builder(ModifResto.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .setCancelable(false)
                .show();
    }
}
