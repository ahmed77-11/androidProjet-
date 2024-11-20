package com.example.androidprojectresto.Activity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import com.example.androidprojectresto.Adapter.CategoryAdpater;
import com.example.androidprojectresto.Adapter.RecomandedRestoAdapter;
import com.example.androidprojectresto.Domain.Category;
import com.example.androidprojectresto.Domain.Resto;
import com.example.androidprojectresto.Domain.Time;
import com.example.androidprojectresto.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    private static final long TEN_DAYS_MILLIS = 10 * 24 * 60 * 60 * 1000L; // 10 days in milliseconds
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private FirebaseAuth.AuthStateListener authStateListener;

    private ActivityMainBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mAuth= FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();

        authStateListener=firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user == null || !isSessionValid()) {
                // Redirect to login if user is not authenticated or session expired
                Intent intent = new Intent(MainActivity.this, IntroActivity.class);
                startActivity(intent);
                finish();
            }
        };
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        initRecommended();
        initCategory();
        setVaraible();
        initLocation();
    }

    private void initLocation() {
        binding.LocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this, Maps.class);
                startActivity(i);
            }
        });
    }

    private void setVaraible() {
        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent i=new Intent(MainActivity.this,IntroActivity.class);
                startActivity(i);
            }
        });
        binding.searchBtn.setOnClickListener(v -> {
            String text=binding.searchEdit.getText().toString();
            if (!text.isEmpty()){
                Intent i=new Intent(MainActivity.this,ListRestosActivity.class);
                i.putExtra("text",text);
                i.putExtra("isSearch",true);
                startActivity(i);
            }
        });
    }

    private void initRecommended() {
        DatabaseReference restoRef = database.getReference("resto");
        DatabaseReference timeRef = database.getReference("Time");

        binding.progressBarRecommendedResto.setVisibility(View.VISIBLE);

        ArrayList<Resto> restoList = new ArrayList<>();
        ArrayList<Time> timeList = new ArrayList<>();

        // Query for recommended restaurants
        Query restoQuery = restoRef.orderByChild("Recomanded").equalTo(true);
        restoQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {

                        restoList.add(issue.getValue(Resto.class));

                        Log.i("FirebaseDebug", "Resto data snapshot:"+issue.getValue(Resto.class));

                    }
                }else{

                }

                // Once restaurants are fetched, fetch Time data
                timeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot timeSnapshot) {
                        if (timeSnapshot.exists()) {
                            for (DataSnapshot timeData : timeSnapshot.getChildren()) {
                                timeList.add(timeData.getValue(Time.class));
                            }
                        }

                        // Initialize RecyclerView with fetched data
                        if (!restoList.isEmpty()) {
                            binding.recommendedRestoView.setLayoutManager(
                                    new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false)
                            );
                            RecyclerView.Adapter<RecomandedRestoAdapter.ViewHolder> adapter = new RecomandedRestoAdapter(restoList, timeList);
                            binding.recommendedRestoView.setAdapter(adapter);
                        }


                        binding.progressBarRecommendedResto.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle error for Time data
                        binding.progressBarRecommendedResto.setVisibility(View.GONE);
                        // Log error
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error for Resto data
                binding.progressBarRecommendedResto.setVisibility(View.GONE);
                // Log error
            }
        });
    }
    private void initCategory() {
        DatabaseReference categoryRef = database.getReference("Category");

        binding.progressBarCategory.setVisibility(View.VISIBLE);

        ArrayList<Category> categoriesList = new ArrayList<>();

        // Query for recommended restaurants
        categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        int id = issue.child("Id").getValue(Integer.class) != null
                                ? issue.child("Id").getValue(Integer.class)
                                : 0;
                        String name = issue.child("Name").getValue(String.class);
                        String imagePath = issue.child("ImagePath").getValue(String.class);

                        Category category = new Category();
                        category.setId(id);
                        category.setName(name);
                        category.setImagePath(imagePath);

                        categoriesList.add(category);
                        Log.d("ManualMapping", "Category ID: " + id + ", Name: " + name);

                    }
                }
                if(categoriesList.size()>0){
                    binding.categoryView.setLayoutManager(new GridLayoutManager(MainActivity.this,3));
                    RecyclerView.Adapter<CategoryAdpater.ViewHolder> adapter=new CategoryAdpater(categoriesList);
                    binding.categoryView.setAdapter(adapter);

                }
                binding.progressBarCategory.setVisibility(View.GONE);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error for Resto data
                binding.progressBarCategory.setVisibility(View.GONE);
                // Log error
            }
        });
    }


    private boolean isSessionValid() {
        // Get the saved login time
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        long lastLoginTime = preferences.getLong("lastLoginTime", -1);

        if (lastLoginTime == -1) {
            return false;
        }

        long currentTime = System.currentTimeMillis();
        return (currentTime - lastLoginTime) < TEN_DAYS_MILLIS;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(authStateListener!=null){
            mAuth.removeAuthStateListener(authStateListener);
        }
    }
}