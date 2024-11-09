package com.example.androidprojectresto.Activity;


import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.example.androidprojectresto.R;
import com.example.androidprojectresto.databinding.ActivityIntroBinding;
import com.google.firebase.FirebaseApp;

public class IntroActivity extends BaseActivity {

    ActivityIntroBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setVarabile();
        getWindow().setStatusBarColor(Color.parseColor("#FFE4B5"));
        FirebaseApp.initializeApp(this);
    }

    private void setVarabile() {
        binding.log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        binding.inscri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}