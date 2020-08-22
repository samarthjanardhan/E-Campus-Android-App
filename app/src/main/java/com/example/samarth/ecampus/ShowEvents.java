package com.example.samarth.ecampus;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.samarth.ecampus.adapters.EventAdapter;
import com.example.samarth.ecampus.model.UploadEvent;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ShowEvents extends AppCompatActivity implements EventAdapter.OnItemClickListener{

    private RecyclerView mRecyclerView;
    private EventAdapter mAdapter;

    private ProgressBar mProgressCircle;

    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private List<UploadEvent> mUploads;

    private LinearLayoutManager mLayoutManager;
    private ValueEventListener mDBListener;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_events);

        mLayoutManager = new LinearLayoutManager(ShowEvents.this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        // mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setLayoutManager(mLayoutManager);


        mProgressCircle = findViewById(R.id.progress_circle);

        mUploads = new ArrayList<UploadEvent>();

        mAdapter = new EventAdapter(ShowEvents.this, mUploads);

        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(ShowEvents.this);

        mStorage = FirebaseStorage.getInstance();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads_events");

        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mUploads.clear();


                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    UploadEvent upload_events = postSnapshot.getValue(UploadEvent.class);
                    upload_events.setkey(postSnapshot.getKey());
                    mUploads.add(upload_events);
                }

                mAdapter.notifyDataSetChanged();

                mProgressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ShowEvents.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });

    }

    @Override
    public void onItemClick(int position) {

        Toast.makeText(this, "Long press for other options ", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onWhatEverClick(int position) {

        UploadEvent selectedItem = mUploads.get(position);

        mContext = ShowEvents.this;
        Intent intent = new Intent();
        intent.setType(Intent.ACTION_VIEW);  //denotes that we are going to view something
        String urls = selectedItem.getImageUrl();
        intent.setData(Uri.parse(selectedItem.getImageUrl()));
        mContext.startActivity(intent);

    }

    @Override
    public void onDeleteClick(int position) {

        UploadEvent selectedItem = mUploads.get(position);
        final String selectedKey = selectedItem.getkey();

        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getImageUrl());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mDatabaseRef.child(selectedKey).removeValue();
                Toast.makeText(ShowEvents.this, "Item deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
