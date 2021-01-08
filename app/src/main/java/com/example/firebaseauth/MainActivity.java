package com.example.firebaseauth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static maes.tech.intentanim.CustomIntent.customType;

public class MainActivity extends AppCompatActivity {
    private EditText editTextEmail;
    private EditText editTextPassword;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //defining view objects
        Window mWindow = getWindow();
        mWindow.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            //close this activity
            finish();
            //opening profile activity
            startActivity(new Intent(getApplicationContext(), HomeActivty.class));
            customType(MainActivity.this, "fadein-to-fadeout");

            finish();
        }

        editTextEmail = findViewById(R.id.email_login);
        editTextPassword = findViewById(R.id.parol_login);
        Button sign_up_btn = findViewById(R.id.kirish);
        TextView sign_in = findViewById(R.id.login);

        progressDialog = new ProgressDialog(this);


        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser();
            }
        });

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                customType(MainActivity.this, "fadein-to-fadeout");
                finish();
            }
        });
    }

    private void createUser() {

        //EditTextlardan email va parolni oladi
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
            editTextEmail.setError("Emailni kiriting");
            editTextPassword.setError("Parolni kiriting");
            return;
        }

        //checking if email and passwords are empty
        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Emailni kiriting");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Parolni kiriting");
            return;
        }

        //if the email and password are not empty
        //displaying a progress dialog

        progressDialog.setMessage("Ro'yxatdan o'tkazilmoqda...");
        progressDialog.show();

        //creating a new user
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //if the task is successfull
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    //start the profile activity
                    finish();
                    startActivity(new Intent(getApplicationContext(), HomeActivty.class));
                    customType(MainActivity.this, "fadein-to-fadeout");
                    finish();
                } else {
                    progressDialog.dismiss();
                    task.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "Error: " + e, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }
}