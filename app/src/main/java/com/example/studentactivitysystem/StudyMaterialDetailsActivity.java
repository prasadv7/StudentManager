package com.example.studentactivitysystem;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class StudyMaterialDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_material_details);

        TextView titleTextView = findViewById(R.id.titleTextView);
        TextView descriptionTextView = findViewById(R.id.descriptionTextView);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null && extras.containsKey("title") && extras.containsKey("description")) {
                String title = extras.getString("title");
                String description = extras.getString("description");

                if (titleTextView != null && descriptionTextView != null) {
                    titleTextView.setText(title);
                    descriptionTextView.setText(description);
                } else {
                    // Debugging statements
                    if (titleTextView == null) {
                        System.out.println("DEBUG: titleTextView is null");
                    }
                    if (descriptionTextView == null) {
                        System.out.println("DEBUG: descriptionTextView is null");
                    }
                }
            } else {
                // Debugging statement
                System.out.println("DEBUG: Intent extras do not contain 'title' or 'description'");
                Toast.makeText(this, "No data available", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            // Debugging statement
            System.out.println("DEBUG: Intent is null");
            Toast.makeText(this, "No intent available", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
