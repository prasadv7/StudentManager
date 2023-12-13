package com.example.studentactivitysystem;

import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class study_Material extends AppCompatActivity {

    private static final String TAG = "study_Material";
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private FileAdapter fileAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_material2);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize RecyclerView and its adapter
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fileAdapter = new FileAdapter();
        recyclerView.setAdapter(fileAdapter);

        // Call the method to fetch files
        fetchFilesFromFirestore();
    }

    private void fetchFilesFromFirestore() {
        // Assuming you have a "files" collection in Firestore
        db.collection("files")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<FileItem> fileItemList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Access the data in the document
                            String fileName = document.getString("fileName");
                            String fileUrl = document.getString("downloadURL");
                            String classn = document.getString("class");

                            // Add the file information to the list
                            fileItemList.add(new FileItem(fileName, fileUrl));
                        }

                        // Update the adapter with the fetched files
                        fileAdapter.setFileList(fileItemList);
                    } else {
                        Log.e(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

    // Custom RecyclerView Adapter
    private class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileViewHolder> {

        private List<FileItem> fileList = new ArrayList<>();

        public void setFileList(List<FileItem> fileList) {
            this.fileList = fileList;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_file, parent, false);
            return new FileViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
            FileItem fileItem = fileList.get(position);
            holder.fileNameTextView.setText(fileItem.getFileName());
            holder.fileUrlTextView.setText(fileItem.getFileUrl());

            // Set click listener to open/download the file
            holder.cardView.setOnClickListener(v -> {
                // Modify this part based on your file opening/downloading logic
                String fileUrl = fileItem.getFileUrl();
                String fileName = fileItem.getFileName();
                openOrDownloadFile(fileUrl, fileName);
            });
        }

        @Override
        public int getItemCount() {
            return fileList.size();
        }

        public class FileViewHolder extends RecyclerView.ViewHolder {
            TextView fileNameTextView;
            TextView fileUrlTextView;
            CardView cardView; // Add CardView

            public FileViewHolder(@NonNull View itemView) {
                super(itemView);
                fileNameTextView = itemView.findViewById(R.id.fileNameTextView);
                fileUrlTextView = itemView.findViewById(R.id.fileUrlTextView);
                cardView = itemView.findViewById(R.id.cardView); // Initialize CardView
            }
        }
    }

    // Method to open or download the file
    private void openOrDownloadFile(String fileUrl, String fileName) {
        // Replace this with your file opening/downloading logic
        // For example, you can open the file using an Intent or download it
        // and then open it using appropriate libraries or apps.

        // Check if the file is already downloaded or not
        if (isFileDownloaded(fileName)) {
            // File is downloaded, open it
            openDownloadedFile(fileName);
        } else {
            // File is not downloaded, initiate the download
            downloadFile(fileUrl, fileName);
        }
    }

    // Check if the file is already downloaded
    private boolean isFileDownloaded(String fileName) {
        // Implement your logic to check if the file with the given fileName is already downloaded
        // For example, you might check if the file exists in a specific directory
        // Return true if downloaded, false otherwise
        // For simplicity, this example assumes the file is always not downloaded
        return false;
    }

    // Download the file
    private void downloadFile(String fileUrl, String fileName) {
        // Implement your logic to download the file from the fileUrl
        // Save the downloaded file with the given fileName
        // You can use libraries like Retrofit, OkHttp, or Android's DownloadManager for downloading

        // For simplicity, using Android's DownloadManager
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(fileUrl));
        request.setTitle(fileName);
        request.setDescription("Downloading file...");

        // Set the download path to the Downloads directory
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

        // Get download service and enqueue the request
        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        long downloadId = downloadManager.enqueue(request);

        // Register a BroadcastReceiver to receive notifications when the download is complete
        registerReceiver(new DownloadCompleteReceiver(fileName), new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    // Open the downloaded file
    private void openDownloadedFile(String fileName) {
        // Implement your logic to open the downloaded file with the given fileName
        // For example, you might use Intent.ACTION_VIEW with a content URI
        Log.d(TAG, "Opening downloaded file: " + fileName);

        File downloadedFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);

        if (downloadedFile.exists()) {
            Uri fileUri = Uri.fromFile(downloadedFile);

            Intent viewIntent = new Intent(Intent.ACTION_VIEW);
            viewIntent.setDataAndType(fileUri, getMimeType(fileUri));
            viewIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            // Use FileProvider for better security
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri contentUri = FileProvider.getUriForFile(this, "com.example.studentactivitysystem.fileprovider", downloadedFile);
                viewIntent.setDataAndType(contentUri, getMimeType(contentUri));
                viewIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }

            try {
                startActivity(viewIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "No app found to open this file", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e(TAG, "File not found: " + downloadedFile.getAbsolutePath());
        }
    }

    // Get MIME type of a file
    private String getMimeType(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        return contentResolver.getType(uri);
    }

    // BroadcastReceiver to receive notifications when the download is complete
    private class DownloadCompleteReceiver extends BroadcastReceiver {

        private String fileName;

        DownloadCompleteReceiver(String fileName) {
            this.fileName = fileName;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            // Handle the download complete event
            long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (downloadId != -1) {
                // Check if the completed download matches the one we initiated
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(downloadId);
                DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                try {
                    android.database.Cursor cursor = downloadManager.query(query);
                    if (cursor.moveToFirst()) {
                        int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                        if (columnIndex >= 0) {
                            int status = cursor.getInt(columnIndex);
                            if (status == DownloadManager.STATUS_SUCCESSFUL) {
                                // File downloaded successfully, now you can open it
                                openDownloadedFile(fileName);
                            } else {
                                // Handle download failure
                                Toast.makeText(study_Material.this, "Download failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // Unregister the receiver
            context.unregisterReceiver(this);
        }
    }
}