package com.example.studentactivitysystem;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private static final String TAG = "DashboardActivity";
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    // Declare TextViews for exam details
    private TextView examTypeTextView;
    private TextView examUpdateTextView;
    private TextView examTimeTextView;

    // Declare ListView for displaying multiple events
    private ListView eventsListView;
    private ArrayAdapter<String> eventsAdapter;
    private List<DocumentSnapshot> eventsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize TextViews with their respective IDs
        examTypeTextView = findViewById(R.id.examTitleTextView);
        examUpdateTextView = findViewById(R.id.examDetailsTextView);
        examTimeTextView = findViewById(R.id.examDateTextView);

        // Initialize ListView with its respective ID
        eventsListView = findViewById(R.id.eventsListView);

        // Initialize events list
        eventsList = new ArrayList<>();
        eventsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        eventsListView.setAdapter(eventsAdapter);

        // Set a click listener for items in the ListView
        eventsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Handle item click, update UI with selected event
                DocumentSnapshot selectedEvent = eventsList.get(position);
                updateExamDetails(selectedEvent);
            }
        });

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // Fetch user details, including class
            fetchUserDetails(user.getUid());
        }
    }

    private void fetchUserDetails(String userId) {
        db.collection("students")
                .document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // User document exists
                        String userClass = documentSnapshot.getString("class");
                        // Fetch events based on userClass
                        fetchEventsByClass(userClass);
                    } else {
                        Log.d(TAG, "User document does not exist");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching user details", e);
                });
    }

    private void fetchEventsByClass(String userClass) {
        db.collection("events")
                .whereEqualTo("class", userClass)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // Update the events list
                        eventsList = queryDocumentSnapshots.getDocuments();

                        // Update the ListView with event names
                        List<String> eventNames = new ArrayList<>();
                        for (DocumentSnapshot event : eventsList) {
                            eventNames.add(event.getString("eventName"));
                        }
                        eventsAdapter.clear();
                        eventsAdapter.addAll(eventNames);

                        // Display details of the first event
                        if (!eventsList.isEmpty()) {
                            updateExamDetails(eventsList.get(0));
                        }
                    } else {
                        Log.d(TAG, "No events found for the user's class");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching events", e);
                });
    }

    private void updateExamDetails(DocumentSnapshot event) {
        // Log event details
        Log.d(TAG, "Updating UI - Event Name: " + event.getString("eventName"));

        // Set the event details
        examTypeTextView.setText(event.getString("eventName"));
        examUpdateTextView.setText(event.getString("eventDetails"));
        examTimeTextView.setText(event.getString("eventDate"));
    }




    public void onElementClickStudyMaterial(View view) {
        fetchAndDisplayStudyMaterial();
        Toast.makeText(this, "Study Card clicked!", Toast.LENGTH_SHORT).show();
    }
    private void fetchAndDisplayStudyMaterial() {
        // Assuming you have a StudyMaterialActivity to display the fetched data
        Intent intent = new Intent(this, study_Material.class);
        startActivity(intent);
    }

    public void onElementClickMarks(View view) {
        fetchAndDisplayMarks();
        Toast.makeText(this, "Card clicked!", Toast.LENGTH_SHORT).show();
    }
    private void fetchAndDisplayMarks() {
        // Assuming you have a StudyMaterialActivity to display the fetched data
        Intent intent = new Intent(this, fetch_marks.class);
        startActivity(intent);
    }
    public void onElementClickEventHistory(View view) {
        fetchEventHistory();
        Toast.makeText(this, "Card clicked!", Toast.LENGTH_SHORT).show();

    }
    private void fetchEventHistory() {
        // Assuming you have a StudyMaterialActivity to display the fetched data
        Intent intent = new Intent(this, EventHistory.class);
        startActivity(intent);
    }

    public void onElementClickSPP(View view) {
        Toast.makeText(this, "Card clicked!", Toast.LENGTH_SHORT).show();

    }

    public void onElementClickLibrary(View view) {
        Toast.makeText(this, "Card clicked!", Toast.LENGTH_SHORT).show();
        fetchLibrary();
    }
    private void fetchLibrary() {
        // Assuming you have a StudyMaterialActivity to display the fetched data
        Intent intent = new Intent(this, Library.class);
        startActivity(intent);
    }
    public void onElementClickLogout(View view) {
        Toast.makeText(this, "Card clicked!", Toast.LENGTH_SHORT).show();
        Logout();
    }
    private void Logout() {

        FirebaseAuth.getInstance().signOut();

        // Redirect to your login or home activity
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void onElementClickLINK(View view) {
        Toast.makeText(this, "Card clicked!", Toast.LENGTH_SHORT).show();
        String url = "https://dypsomca.com/";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}