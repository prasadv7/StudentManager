package com.example.studentactivitysystem;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class fetch_marks extends AppCompatActivity {

    private static final String TAG = "fetch_marks";
    private FirebaseFirestore db;
    private TextView marksTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_marks);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize TextView to display marks
        marksTextView = findViewById(R.id.marksTextView);

        // Get the current logged-in student's class and roll number
        getLoggedInStudentDetails();
    }

    private void getLoggedInStudentDetails() {
        // Get the current user from Firebase Authentication
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            // User is signed in
            String userId = currentUser.getUid();

            // Reference to Firestore
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Retrieve the student document from Firestore
            db.collection("students")
                    .document(userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Check if the document exists
                            if (task.getResult() != null && task.getResult().exists()) {
                                // Get the class and roll number information from the document
                                String loggedInClass = task.getResult().getString("class");
                                String rollNumber = task.getResult().getString("rollNumber");

                                // Call the method to fetch marks based on the logged-in class and roll number
                                fetchMarksForStudent(loggedInClass, rollNumber);
                            } else {
                                // Handle the case where the document does not exist or is null
                                // You might redirect to the login screen or take appropriate action
                            }
                        } else {
                            // Handle exceptions or errors
                            Log.e(TAG, "Error getting student document: ", task.getException());
                        }
                    });
        } else {
            // User is not signed in
            // Handle the case where the user is not logged in
            // You might redirect to the login screen or take appropriate action
        }
    }

    private void fetchMarksForStudent(String className, String rollNumber) {
        // Assuming you have a "marks" collection in Firestore
        db.collection("marks")
                .whereEqualTo("class", className)
                .whereEqualTo("rollNumber", rollNumber)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        StringBuilder marksText = new StringBuilder();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Access the data in the document
                            String subject = document.getString("subject");
                            String marks = document.getString("marks");

                            // Append the marks information to the StringBuilder
                            marksText.append(subject).append(": ").append(marks).append("\n");
                        }

                        // Set the marks text to the TextView
                        if (marksText.length() > 0) {
                            // Marks are available, display them
                            marksTextView.setText(marksText.toString());
                        } else {
                            // No marks available
                            marksTextView.setText("No marks available.");
                        }
                    } else {
                        Log.e(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }
}
