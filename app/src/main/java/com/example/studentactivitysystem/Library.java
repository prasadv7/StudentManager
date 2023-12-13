package com.example.studentactivitysystem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Library extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        Button libraryButton = findViewById(R.id.libraryButton);
        RecyclerView libraryRecyclerView = findViewById(R.id.libraryRecyclerView);

        // Sample static data
        List<Book> bookList = new ArrayList<>();
        bookList.add(new Book("The Great Gatsby"));
        bookList.add(new Book("To Kill a Mockingbird"));
        bookList.add(new Book("1984"));
        // Add more books as needed

        LibraryAdapter libraryAdapter = new LibraryAdapter(bookList);
        libraryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        libraryRecyclerView.setAdapter(libraryAdapter);

        libraryButton.setOnClickListener(v -> {
            // Toggle visibility of the RecyclerView
            int visibility = libraryRecyclerView.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
            libraryRecyclerView.setVisibility(visibility);
        });
    }

    // Book model class
    private static class Book {
        private String title;

        public Book(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
    }

    // RecyclerView Adapter
    private static class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.BookViewHolder> {

        private List<Book> bookList;

        public LibraryAdapter(List<Book> bookList) {
            this.bookList = bookList;
        }

        @NonNull
        @Override
        public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
            return new BookViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
            Book book = bookList.get(position);
            holder.titleTextView.setText(book.getTitle());
        }

        @Override
        public int getItemCount() {
            return bookList.size();
        }

        public static class BookViewHolder extends RecyclerView.ViewHolder {
            TextView titleTextView;

            public BookViewHolder(@NonNull View itemView) {
                super(itemView);
                titleTextView = itemView.findViewById(R.id.titleTextView);
            }
        }
    }
}
