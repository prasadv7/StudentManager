package com.example.studentactivitysystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.studentactivitysystem.DashboardActivity;
import com.example.studentactivitysystem.LoginActivity;
import com.example.studentactivitysystem.RegistrationActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        // Check if the user is already logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is already logged in, redirect to DashboardActivity
            startActivity(new Intent(MainActivity.this, DashboardActivity.class));
            finish(); // Finish the MainActivity to prevent going back to it
        } else {
            // User is not logged in, show registration and login options

            Button registerButton = findViewById(R.id.registerButton);
            Button loginButton = findViewById(R.id.loginButton);

            registerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Open RegistrationActivity
                    startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
                }
            });

            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Open LoginActivity
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
            });
        }
    }
}
