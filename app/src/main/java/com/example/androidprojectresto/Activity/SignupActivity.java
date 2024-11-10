package com.example.androidprojectresto.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.androidprojectresto.R;
import com.example.androidprojectresto.databinding.ActivityIntroBinding;
import com.example.androidprojectresto.databinding.ActivitySignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class SignupActivity extends BaseActivity {

    private  final String TAG ="androidProject" ;
    private FirebaseAuth mAuth;

    ActivitySignupBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth=FirebaseAuth.getInstance();
        setVariable();
    }

    public boolean isValidPassword(String password){
        String regex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$";
        Pattern pattern=Pattern.compile(regex);
        return  pattern.matcher(password).matches() && password.length()>=8;
    }
    public static boolean isValidEmail(String email) {
        // Regex pattern for a basic email structure
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";

        // Compile the pattern and check if the email matches
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }
    private void setVariable() {
        binding.SignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=binding.emailEdit.getText().toString();
                String password = binding.passEdit.getText().toString();


                if (!isValidPassword(password)){
                    Log.i("okkkkkk",isValidPassword(password)?"true":"false");
                    Toast.makeText(SignupActivity.this,"your password must be 8 character and contain at least one uppercase ,at least one LowerCase , number and a speacail Caracter",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!isValidEmail(email)){
                    Toast.makeText(SignupActivity.this,"you need a valid email like this :example@email.com",Toast.LENGTH_SHORT).show();
                    return;

                }
                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isComplete()){
                            Log.i(TAG, "onComplete: ");
                            Intent i=new Intent(SignupActivity.this,LoginActivity.class);
                            startActivity(i);
                        }else {
                            Log.i(TAG, "failure: ");
                            Toast.makeText(SignupActivity.this,"Authentication failed",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }



        });
    }

}