package com.example.firebaseauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static maes.tech.intentanim.CustomIntent.customType;

public class LoginActivity extends AppCompatActivity {

    //defining view objects
    private EditText editTextEmail;
    private EditText editTextPassword;
    private FirebaseAuth mAuth;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Window mWindow = getWindow();
        mWindow.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        mAuth = FirebaseAuth.getInstance();

        //if getCurrentUser does not returns null
        if (mAuth.getCurrentUser() != null) {
            //that means user is already logged in
            //so close this activity
            finish();

            //and open profile activity
            startActivity(new Intent(getApplicationContext(), HomeActivty.class));
            customType(LoginActivity.this, "fadein-to-fadeout");

            finish();
        }

        //initializing views
        editTextEmail = findViewById(R.id.email_login);
        editTextPassword = findViewById(R.id.parol_login);
        Button btn_sign_in = findViewById(R.id.kirish);
        TextView sign_up = findViewById(R.id.yangi);

        progressDialog = new ProgressDialog(this);

        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                customType(LoginActivity.this, "fadein-to-fadeout");
                finish();
            }
        });
    }

    private void registerUser() {

        //getting email and password from edit texts
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

        progressDialog.setMessage("Autentifikatsiya qilinmoqda...");
        progressDialog.show();

        //creating a new user
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if (task.isSuccessful()) {
                            finish();
                            startActivity(new Intent(getApplicationContext(), HomeActivty.class));
                            customType(LoginActivity.this, "fadein-to-fadeout");
                            finish();
                        } else {
                            //display some message here
                            Toast.makeText(LoginActivity.this, "Email yoki parol noto'g'ri", Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();

                        task.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this, "Error: " + e, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }
}