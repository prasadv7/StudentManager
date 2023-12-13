package com.example.studentactivitysystem;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class StudyMaterialActivity extends AppCompatActivity implements StudyMaterialAdapter.OnItemClickListener {

    private StudyMaterialAdapter adapter;
    private CollectionReference studyMaterialsRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_material);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewStudyMaterials);
        if (recyclerView != null) {
            adapter = new StudyMaterialAdapter(new ArrayList<>(), this);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            studyMaterialsRef = db.collection("files");

            fetchStudyMaterials();
        }
    }

    private void fetchStudyMaterials() {
        studyMaterialsRef.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<StudyMaterial> studyMaterials = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String title = document.getString("fileName");
                        String description = document.getString("class");

                        StudyMaterial studyMaterial = new StudyMaterial(title, description);
                        studyMaterials.add(studyMaterial);
                    }

                    adapter.setStudyMaterials(studyMaterials); // Set the study materials in the adapter
                })
                .addOnFailureListener(e -> {
                    // Handle errors while fetching data
                });
    }



    @Override
    public void onItemClick(StudyMaterial studyMaterial) {
        if (studyMaterial != null) {
            // Handle item click, e.g., navigate to a details page
            Toast.makeText(this, "Study Card clicked!", Toast.LENGTH_SHORT).show();

            // Launch the StudyMaterialDetailsActivity and pass study material details
            Intent intent = new Intent(this, StudyMaterialDetailsActivity.class);
            intent.putExtra("title", studyMaterial.getTitle());
            intent.putExtra("description", studyMaterial.getDescription());
            startActivity(intent);
        } else {
            // Handle the case where studyMaterial is null (optional)
            Toast.makeText(this, "Study Material is null", Toast.LENGTH_SHORT).show();
        }
    }
}
