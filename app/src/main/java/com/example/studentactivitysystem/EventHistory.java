package com.example.studentactivitysystem;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class EventHistory extends AppCompatActivity {

    private static final String TAG = "EventHistory";
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_history);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize RecyclerView and its adapter
        recyclerView = findViewById(R.id.eventHistoryRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventAdapter = new EventAdapter();
        recyclerView.setAdapter(eventAdapter);

        // Call the method to fetch events
        fetchEventsFromFirestore();
    }

    private void fetchEventsFromFirestore() {
        // Assuming you have an "events" collection in Firestore
        db.collection("events")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<EventItem> eventItemList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Access the data in the document
                            String eventName = document.getString("eventName");
                            String eventDetails = document.getString("eventDetails");
                            String eventDate = document.getString("eventDate");

                            // Add the event information to the list
                            eventItemList.add(new EventItem(eventName, eventDetails, eventDate));
                        }

                        // Update the adapter with the fetched events
                        eventAdapter.setEventList(eventItemList);
                    } else {
                        Log.e(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

    // Custom RecyclerView Adapter
    private class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

        private List<EventItem> eventList = new ArrayList<>();

        public void setEventList(List<EventItem> eventList) {
            this.eventList = eventList;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event1, parent, false);
            return new EventViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
            EventItem eventItem = eventList.get(position);
            holder.eventNameTextView.setText(eventItem.getEventName());
            holder.eventDetailsTextView.setText(eventItem.getEventDetails());
            holder.eventDateTextView.setText(eventItem.getEventDate());
        }

        @Override
        public int getItemCount() {
            return eventList.size();
        }

        public class EventViewHolder extends RecyclerView.ViewHolder {
            TextView eventNameTextView;
            TextView eventDetailsTextView;
            TextView eventDateTextView;

            public EventViewHolder(@NonNull View itemView) {
                super(itemView);
                eventNameTextView = itemView.findViewById(R.id.eventNameTextView);
                eventDetailsTextView = itemView.findViewById(R.id.eventDetailsTextView);
                eventDateTextView = itemView.findViewById(R.id.eventDateTextView);
            }
        }
    }
}
