package com.example.androidprojectresto.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.androidprojectresto.Adapter.RestoListAdapter;
import com.example.androidprojectresto.Adapter.RestoListAdminAdpater;
import com.example.androidprojectresto.Domain.Resto;
import com.example.androidprojectresto.Domain.Time;
import com.example.androidprojectresto.R;
import com.example.androidprojectresto.databinding.ActivityListRestoAdminBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListRestoAdminActivity extends AppCompatActivity {


    private ActivityListRestoAdminBinding binding;
    private FirebaseDatabase database;

    private RecyclerView.Adapter adapterList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityListRestoAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database=FirebaseDatabase.getInstance();
        initList();

        binding.AddRestoBtn.setOnClickListener(v -> {
            Intent i=new Intent(ListRestoAdminActivity.this, addRestoActivity.class);
            startActivity(i);
        });

    }

    private void initList() {
        DatabaseReference restoRef=database.getReference("resto");

        ArrayList<Resto> restos=new ArrayList<>();

        restoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                   for (DataSnapshot issue:snapshot.getChildren()){
                       Resto resto=issue.getValue(Resto.class);
                       if (resto != null) {
                           int id = issue.child("Id").getValue(Integer.class) != null
                                   ? issue.child("Id").getValue(Integer.class)
                                   : 0;
                           resto.setId(id);
                           restos.add(resto);
                       }
                   }
                }
                setupRecyclerView(restos);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                handleError("Failed to load restos: " + error.getMessage());
            }
        });
    }
    private void setupRecyclerView(ArrayList<Resto> restoList) {
        if (!restoList.isEmpty()) {
            // Set layout manager based on desired UI
            binding.recyclerViewAdmin.setLayoutManager(new LinearLayoutManager(this));

            adapterList=new RestoListAdminAdpater(restoList,(restoId,position)->{
                deleteResto(restoId,restoList,position);
            });
            binding.recyclerViewAdmin.setAdapter(adapterList);
        }
    }
    private void handleError(String errorMessage) {
        // Log error or display error message
        System.err.println(errorMessage); // Replace with a Toast or Snackbar if needed
    }
    private void deleteResto(int restoId, ArrayList<Resto> restos, int position) {
        DatabaseReference restoRef=database.getReference("resto").child(String.valueOf(restoId));
        Log.i("deleteddd", String.valueOf(restoId));
        restoRef.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Remove the item from the list and notify adapter
                restos.remove(position);
                adapterList.notifyItemRemoved(position);
                Log.i("deletedddd", "deleteResto with success ");
            } else {
                Log.i("deletedddd", "Failed to delete resto: " + task.getException().getMessage());
            }
        });
    }

}