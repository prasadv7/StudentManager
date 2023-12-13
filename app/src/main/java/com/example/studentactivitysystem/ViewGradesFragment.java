package com.example.studentactivitysystem;

import static androidx.core.content.PermissionChecker.checkSelfPermission;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class ViewGradesFragment extends Fragment {

    private static final String TAG = "ViewGradesFragment";
    private static final int YOUR_PERMISSION_REQUEST_CODE = 123; // Replace with any unique code

    public static ViewGradesFragment newInstance() {
        return new ViewGradesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_grades, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Call the method to fetch and display grades
        fetchAndDisplayGrades();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == YOUR_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with the operation (e.g., show the notification)
                showNotification("Grades have been updated!");
            } else {
                // Permission denied, handle the case (e.g., inform the user)
                Toast.makeText(requireContext(), "Permission denied. Cannot show notification.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showNotification(String contentText) {
        // Check if the permission is granted
        if (checkSelfPermission(requireContext(), Manifest.permission.VIBRATE) == PermissionChecker.PERMISSION_GRANTED) {
            // Permission is already granted, proceed with the notification
            NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), "default")
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle("Grades Updated")
                    .setContentText(contentText)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(requireContext());
            notificationManager.notify(1, builder.build());
        } else {
            // Permission is not granted, request it from the user
            requestPermissions(new String[]{Manifest.permission.VIBRATE}, YOUR_PERMISSION_REQUEST_CODE);
        }
    }

    private void fetchAndDisplayGrades() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String currentUserId = currentUser.getUid();

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Reference to the "students" collection
            CollectionReference studentsRef = db.collection("students");

            // Reference to the "grades" collection
            CollectionReference gradesRef = db.collection("grades");

            // Fetch student details for the current user
            studentsRef.document(currentUserId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            // Get the student's class and roll number
                            String studentClass = documentSnapshot.getString("class");
                            String studentRollNumber = documentSnapshot.getString("rollNo"); // Updated field name

                            // Fetch grades for the student's class and roll number
                            gradesRef.whereEqualTo("class", studentClass)
                                    .whereEqualTo("rollNumber", studentRollNumber) // Updated field name
                                    .get()
                                    .addOnSuccessListener(queryDocumentSnapshots -> {
                                        boolean gradesUpdated = false; // Initialize the variable

                                        for (QueryDocumentSnapshot gradeDocument : queryDocumentSnapshots) {
                                            // Assuming there's a "gradeMarks" field in the grades document
                                            String gradeMarks = gradeDocument.getString("gradeMarks");

                                            // Display the grades in the UI (modify as needed)
                                            TextView gradesTextView = requireView().findViewById(R.id.gradesTextView);
                                            gradesTextView.setText("Grade Marks: " + gradeMarks);

                                            // You need to implement the logic to check if grades were updated
                                            // For now, we assume grades are always updated
                                            gradesUpdated = true;
                                        }

                                        if (gradesUpdated) {
                                            // Show a notification
                                            showNotification("Grades have been updated!");
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        // Handle errors
                                        Log.e(TAG, "Error fetching grades from Firestore", e);
                                    });
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Handle errors
                        Log.e(TAG, "Error fetching student details from Firestore", e);
                    });
        }
    }
}
