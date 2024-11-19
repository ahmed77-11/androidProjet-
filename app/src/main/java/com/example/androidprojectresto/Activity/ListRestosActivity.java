package com.example.androidprojectresto.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.androidprojectresto.Adapter.RecomandedRestoAdapter;
import com.example.androidprojectresto.Adapter.RestoListAdapter;
import com.example.androidprojectresto.Domain.Resto;
import com.example.androidprojectresto.Domain.Time;
import com.example.androidprojectresto.R;
import com.example.androidprojectresto.databinding.ActivityListRestoBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListRestosActivity extends AppCompatActivity {

    private ActivityListRestoBinding binding;
    private FirebaseDatabase database;

    private RecyclerView.Adapter adapterList;
    private int categoryId;
    private String categoryName;
    private String searchText;
    private boolean isSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListRestoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        getIntentExtras();
        initList();
        setVaraible();

    }

    private void setVaraible() {
    }

    private void getIntentExtras() {
        // Retrieve intent extras
        categoryId = getIntent().getIntExtra("CategoryId", 0);
        categoryName = getIntent().getStringExtra("CategoryName");
        searchText = getIntent().getStringExtra("text");
        isSearch = getIntent().getBooleanExtra("isSearch", false);

        // Set title and back button functionality
        binding.titleTxt.setText(categoryName);
        binding.backBtn.setOnClickListener(v -> finish());
    }

    private void initList() {
        DatabaseReference restoRef = database.getReference("resto");
        DatabaseReference timeRef = database.getReference("Time");

        // Show progress bar while loading
        binding.progressBar.setVisibility(View.VISIBLE);

        // Lists to hold fetched data
        ArrayList<Resto> restoList = new ArrayList<>();
        ArrayList<Time> timeList = new ArrayList<>();

        // Determine query based on search or category
        Query query = isSearch
                ? restoRef.orderByChild("Title").startAt(searchText).endAt(searchText + '\uf8ff')
                : restoRef.orderByChild("CategoryId").equalTo(categoryId);

        // Fetch resto data
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot restoSnapshot : snapshot.getChildren()) {
                        Resto resto = restoSnapshot.getValue(Resto.class);
                        if (resto != null) {
                            restoList.add(resto);
                        }
                    }

                    // Fetch time data
                    timeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot timeSnapshot) {
                            if (timeSnapshot.exists()) {
                                for (DataSnapshot timeData : timeSnapshot.getChildren()) {
                                    Time time = timeData.getValue(Time.class);
                                    if (time != null) {
                                        timeList.add(time);
                                    }
                                }
                            }

                            // Set up RecyclerView with fetched data
                            setupRecyclerView(restoList, timeList);

                            // Hide progress bar
                            binding.progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle error while fetching time data
                            handleError("Failed to load time data: " + error.getMessage());
                        }
                    });
                } else {
                    binding.progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error while fetching resto data
                handleError("Failed to load restos: " + error.getMessage());
            }
        });
    }

    private void setupRecyclerView(ArrayList<Resto> restoList, ArrayList<Time> timeList) {
        if (!restoList.isEmpty()) {
            // Set layout manager based on desired UI
            binding.RestoListView.setLayoutManager(new GridLayoutManager(this, 2));

            // Set adapter for the RecyclerView
            adapterList = new RestoListAdapter(restoList, timeList);
            binding.RestoListView.setAdapter(adapterList);
        }
    }

    private void handleError(String errorMessage) {
        // Log error or display error message
        binding.progressBar.setVisibility(View.GONE);
        System.err.println(errorMessage); // Replace with a Toast or Snackbar if needed
    }
}
