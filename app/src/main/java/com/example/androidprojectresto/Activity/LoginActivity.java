
package com.example.androidprojectresto.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import com.example.androidprojectresto.R;
import com.example.androidprojectresto.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLoginBinding.inflate(getLayoutInflater());
        mAuth=FirebaseAuth.getInstance();

        setContentView(binding.getRoot());
        setVarible();
    }

    private void setVarible() {
        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=binding.emailEdit.getText().toString();
                String password=binding.passEdit.getText().toString();
                if(!email.isEmpty() && !password.isEmpty()){
                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                // Save the current login time
                                long loginTime = System.currentTimeMillis();
                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                                preferences.edit().putLong("lastLoginTime", loginTime).apply();
                                Intent i =new Intent(LoginActivity.this,MainActivity.class);
                                startActivity(i);
                                finish();
                            }else{
                                Toast.makeText(LoginActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }else {
                    Toast.makeText(LoginActivity.this, "Please fill username and password", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}