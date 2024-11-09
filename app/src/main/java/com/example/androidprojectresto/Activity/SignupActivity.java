package com.example.androidprojectresto.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.androidprojectresto.R;
import com.example.androidprojectresto.databinding.ActivityIntroBinding;
import com.example.androidprojectresto.databinding.ActivitySignupBinding;

public class SignupActivity extends BaseActivity {

    ActivitySignupBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setVariable();
    }

    private void setVariable() {
        binding.SignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=binding.emailEdit.getText().toString();
                String password = binding.passEdit.getText().toString();

                if (password.length()<6){
                    Toast.makeText(SignupActivity.this,"your password must be 6 character",Toast.LENGTH_SHORT).show();
                    return;
                }
                
            }
        });
    }
}