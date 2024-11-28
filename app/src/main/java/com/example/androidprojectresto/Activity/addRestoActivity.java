package com.example.androidprojectresto.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.example.androidprojectresto.Domain.Category;
import com.example.androidprojectresto.Domain.Location;
import com.example.androidprojectresto.Domain.Resto;
import com.example.androidprojectresto.Domain.Time;
import com.example.androidprojectresto.R;
import com.example.androidprojectresto.databinding.ActivityAddRestoBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class addRestoActivity extends AppCompatActivity {

    private FirebaseDatabase database;

    private HashMap<Integer,Location> locations;

    private HashMap<Integer, Time> times;

    private HashMap<Integer, Category> categories;
    private ActivityAddRestoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAddRestoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database=FirebaseDatabase.getInstance();
        initCategory();
        initLocation();
        initTime();
        binding.AddRestoBtn.setOnClickListener(v -> {
            addResto();
        });
    }


    private void initTime() {
        DatabaseReference timeRef=database.getReference("Time");
        times=new HashMap<>();


        timeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> timesValues=new ArrayList<>();

                if (snapshot.exists()){
                    for (DataSnapshot timeSnapshot:snapshot.getChildren()){
                        Time time=timeSnapshot.getValue(Time.class);
                        if(time!=null){
                            int id = timeSnapshot.child("Id").getValue(Integer.class) != null
                                    ? timeSnapshot.child("Id").getValue(Integer.class)
                                    : 0;
                            times.put(id,time);
                            timesValues.add(time.getValue());
                        }
                    }
                    ArrayAdapter<String> adapter=new ArrayAdapter<>(addRestoActivity.this, android.R.layout.simple_spinner_item,timesValues);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.spinnerTime.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("errordropdown", "Failed to load times: " + error.getMessage());

            }
        });


    }

    private void initLocation() {
        DatabaseReference locationRef=database.getReference("Location");
        locations=new HashMap<>();

        locationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> locationsNames=new ArrayList<>();
                if(snapshot.exists()){
                    for (DataSnapshot locationSnapshot:snapshot.getChildren()){
                        Location location=locationSnapshot.getValue(Location.class);
                        if(location!=null){
                            int id = locationSnapshot.child("Id").getValue(Integer.class) != null
                                    ? locationSnapshot.child("Id").getValue(Integer.class)
                                    : 0;
                            locations.put(id,location);
                            locationsNames.add(location.getLoc());
                        }
                    }
                }
                ArrayAdapter<String> adapter=new ArrayAdapter<>(addRestoActivity.this, android.R.layout.simple_spinner_item,locationsNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.spinnerLocation.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("errordropdown", "Failed to load loc: " + error.getMessage());
                ;
            }
        });

    }

    private void initCategory() {
        DatabaseReference categoryRef=database.getReference("Category");
        categories=new HashMap<>();

        categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> categoryNames=new ArrayList<>();

                if(snapshot.exists()){
                    for (DataSnapshot categorySnapshot:snapshot.getChildren()){
                        Category category=categorySnapshot.getValue(Category.class);
                        if(category!=null){
                            int id = categorySnapshot.child("Id").getValue(Integer.class) != null
                                    ? categorySnapshot.child("Id").getValue(Integer.class)
                                    : 0;
                            categories.put(id,category);
                            categoryNames.add(category.getName());
                        }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            addRestoActivity.this,
                            android.R.layout.simple_spinner_item,
                            categoryNames
                    );
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.spinnerCategory.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("errordropdown", "Failed to load cat: " + error.getMessage());

            }
        });




    }
    private void addResto() {

        String Title = binding.etTitle.getText().toString().trim();
        String Description = binding.etDescription.getText().toString().trim();
        String Budget = binding.etBudget.getText().toString().trim();
        String ImagePath = binding.etImagePath.getText().toString().trim();

        // Get the selected IDs from the spinners
        int CategoryId = binding.spinnerCategory.getSelectedItemPosition();
        int LocationId = binding.spinnerLocation.getSelectedItemPosition();
        int TimeId = binding.spinnerTime.getSelectedItemPosition();

        // Validate inputs
        if (Title.isEmpty() || Description.isEmpty() || Budget.isEmpty() || ImagePath.isEmpty()) {
            Log.e("AddResto", "All fields must be filled");
            return;
        }

        // Prepare the data as a HashMap for Firebase
        Map<String, Object> restoData = new HashMap<>();
        restoData.put("Title", Title);
        restoData.put("Description", Description);
        restoData.put("Budget", Budget);
        restoData.put("ImagePath", ImagePath);
        restoData.put("CategoryId", CategoryId);
        restoData.put("LocationId", LocationId);
        restoData.put("TimeId", TimeId);
        restoData.put("Recomanded", false); // Default value
        restoData.put("Star", 4.8); // Default value

        // Get a reference to the "resto" node in Firebase
        DatabaseReference restoRef = database.getReference("resto");

        // Fetch all existing restos and determine the highest ID
        restoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int newId = 0;

                // Iterate over the existing entries to find the highest ID
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Integer currentId = dataSnapshot.child("Id").getValue(Integer.class);
                    if (currentId != null && currentId >= newId) {
                        newId = currentId + 1; // Increment the ID for the new entry
                    }
                }

                // Add the new ID to the restaurant data
                restoData.put("Id", newId);

                // Save the new restaurant entry to Firebase
                restoRef.child(String.valueOf(newId)).setValue(restoData)
                        .addOnSuccessListener(aVoid -> {
                            Log.i("AddResto", "Restaurant added successfully");
                            finish(); // Finish the activity if successful
                        })
                        .addOnFailureListener(e -> {
                            Log.e("AddResto", "Failed to add restaurant: " + e.getMessage());
                            showErrorDialog("Error adding restaurant", e.getMessage());
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("AddResto", "Error fetching data: " + error.getMessage());
            }
        });
    }

    // Show an error dialog
    private void showErrorDialog(String title, String message) {
        new android.app.AlertDialog.Builder(addRestoActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .setCancelable(false)
                .show();
    }

}