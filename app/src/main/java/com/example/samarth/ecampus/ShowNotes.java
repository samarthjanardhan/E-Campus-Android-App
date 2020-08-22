package com.example.samarth.ecampus;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Toast;

import com.example.samarth.ecampus.adapters.NoteAdapter;
import com.example.samarth.ecampus.model.UploadNote;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShowNotes extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private NoteAdapter mAdapter;

    private DatabaseReference mDatabaseRef;
    private List<UploadNote> mUploads;
    private LinearLayoutManager mLayoutManager;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_notes);

        mLayoutManager = new LinearLayoutManager(ShowNotes.this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setLayoutManager(mLayoutManager);


        mUploads = new ArrayList<UploadNote>();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads_pdf");

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //actually called on individual items at the database reference objects is stored in dataSnapshot
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    UploadNote upload_pdf = postSnapshot.getValue(UploadNote.class);
                    mUploads.add(upload_pdf);

                }

                mRecyclerView.setAdapter(mAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ShowNotes.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();


            }
        });

        //custom adapters
        //populate the recycler view with items
        mAdapter = new NoteAdapter(mRecyclerView,ShowNotes.this, mUploads);

        //MyAdapterpdf myAdapterpdf =new MyAdapterpdf(recyclerView, pdfActivity.this, new ArrayList<String>());


    }
}
