package com.example.studentactivitysystem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentactivitysystem.StudyMaterial;

import java.util.List;

public class StudyMaterialAdapter extends RecyclerView.Adapter<StudyMaterialAdapter.ViewHolder> {

    private List<StudyMaterial> studyMaterials;
    private OnItemClickListener listener;

    // Constructor
    public StudyMaterialAdapter(List<StudyMaterial> studyMaterials, OnItemClickListener listener) {
        this.studyMaterials = studyMaterials;
        this.listener = listener;
    }

    // Setter method for studyMaterials
    public void setStudyMaterials(List<StudyMaterial> studyMaterials) {
        this.studyMaterials = studyMaterials;
        notifyDataSetChanged(); // Notify the adapter that the data has changed
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_study_material, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StudyMaterial studyMaterial = studyMaterials.get(position);
        holder.bind(studyMaterial, listener);
    }

    @Override
    public int getItemCount() {
        return studyMaterials.size();
    }

    public interface OnItemClickListener {
        void onItemClick(StudyMaterial studyMaterial);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private TextView descriptionTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
        }

        public void bind(final StudyMaterial studyMaterial, final OnItemClickListener listener) {
            titleTextView.setText(studyMaterial.getTitle());
            descriptionTextView.setText(studyMaterial.getDescription());

            itemView.setOnClickListener(v -> listener.onItemClick(studyMaterial));
        }
    }
}
