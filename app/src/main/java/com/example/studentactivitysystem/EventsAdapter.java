package com.example.studentactivitysystem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {

    private List<DocumentSnapshot> eventsList;

    public EventsAdapter(List<DocumentSnapshot> eventsList) {
        this.eventsList = eventsList;
    }

    public EventsAdapter() {
        this.eventsList = new ArrayList<>();
    }

    public EventsAdapter(ArrayList<Event> eventsList) {

    }

    public void setEventsList(List<DocumentSnapshot> eventsList) {
        this.eventsList = eventsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DocumentSnapshot event = eventsList.get(position);
        holder.eventNameTextView.setText(event.getString("eventName"));
        // Add more bindings as needed
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView eventNameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventNameTextView = itemView.findViewById(R.id.eventNameTextView);
            // Add more view bindings as needed
        }
    }
}
