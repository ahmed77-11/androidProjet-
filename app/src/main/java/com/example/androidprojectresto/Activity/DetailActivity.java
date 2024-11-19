package com.example.androidprojectresto.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.androidprojectresto.Domain.Resto;
import com.example.androidprojectresto.R;
import com.example.androidprojectresto.databinding.ActivityDetailBinding;

public class DetailActivity extends AppCompatActivity {
    ActivityDetailBinding binding;
    private Resto object;
    private int num = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(getResources().getColor(R.color.black));
        getIntentExtra();
        setVariable();
    }

    private void setVariable() {
        // Back button functionality
        binding.backBtn.setOnClickListener(v -> finish());

        // Load image using Glide
        Glide.with(DetailActivity.this)
                .load(object.getImagePath())
                .into(binding.pic);

        // Set the text for various views
        binding.BudgetTxt.setText("Budget: " + object.getBudget());
        binding.titleTxt.setText(object.getTitle());
        binding.textView13.setText(object.getDescription());
        binding.rateTxt.setText(object.getStar() + " Rating");
        binding.ratingBar.setRating((float) object.getStar()); // Corrected RatingBar usage
    }

    private void getIntentExtra() {
        // Retrieve the object passed via Intent
        object = (Resto) getIntent().getSerializableExtra("object");
    }
}
