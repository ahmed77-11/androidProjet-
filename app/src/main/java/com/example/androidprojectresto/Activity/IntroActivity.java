package com.example.androidprojectresto.Activity;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.example.androidprojectresto.R;
import com.example.androidprojectresto.databinding.ActivityIntroBinding;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class IntroActivity extends BaseActivity {

    ActivityIntroBinding binding;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth=FirebaseAuth.getInstance();
        setVarabile();
        getWindow().setStatusBarColor(Color.parseColor("#FFE4B5"));
        if (mAuth.getCurrentUser() != null) {
            // User is logged in, navigate to MainActivity
            Intent i = new Intent(IntroActivity.this, MainActivity.class);
            startActivity(i);
            finish();  // Ensure the IntroActivity is not in the back stack
        }
    }

    private void setVarabile() {
        binding.log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAuth.getCurrentUser()!=null){
                    Intent i=new Intent(IntroActivity.this,MainActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    Intent i=new Intent(IntroActivity.this,LoginActivity.class);
                    startActivity(i);

                }
            }
        });
        binding.inscri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(IntroActivity.this, SignupActivity.class);
                startActivity(i);

            }
        });
    }
}