package com.example.androidprojectresto.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.example.androidprojectresto.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private static final long TEN_DAYS_MILLIS = 10 * 24 * 60 * 60 * 1000L; // 10 days in milliseconds
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth= FirebaseAuth.getInstance();

        authStateListener=firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user == null || !isSessionValid()) {
                // Redirect to login if user is not authenticated or session expired
                Intent intent = new Intent(MainActivity.this, IntroActivity.class);
                startActivity(intent);
                finish();
            }
        };

        setContentView(R.layout.activity_main);
    }
    private boolean isSessionValid() {
        // Get the saved login time
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        long lastLoginTime = preferences.getLong("lastLoginTime", -1);

        // If no login time is saved, assume session is invalid
        if (lastLoginTime == -1) {
            return false;
        }

        // Check if current time is within 10 days of the last login time
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