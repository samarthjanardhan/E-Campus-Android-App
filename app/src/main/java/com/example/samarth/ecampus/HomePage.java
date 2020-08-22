package com.example.samarth.ecampus;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

public class HomePage extends AppCompatActivity {

    private CardView noticeCard, attendanceCard, aboutusCard, notesCard, eventsCard, forumCard;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReferenceStd, databaseReferenceTeach;
    FirebaseAuth firebaseAuth;

    String uid, roll, div, year;

    boolean Student, Teacher;

    public boolean isTeacher() {
        return Teacher;
    }

    public void setTeacher(boolean teacher) {
        Teacher = teacher;
    }

    public boolean isStudent() {
        return Student;
    }

    public void setStudent(boolean student) {
        Student = student;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //defining cards
        noticeCard = (CardView) findViewById(R.id.notice_card);
        attendanceCard = (CardView) findViewById(R.id.attendance_card);
        aboutusCard = (CardView) findViewById(R.id.aboutus_card);
        notesCard = (CardView) findViewById(R.id.notes_card);
        eventsCard = (CardView) findViewById(R.id.events_card);
        forumCard = (CardView) findViewById(R.id.forum_card);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("MyNotifications", "MyNotifications", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        FirebaseMessaging.getInstance().subscribeToTopic("general")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Successfull Login";
                        if (!task.isSuccessful()) {
                            msg = "Failed to connect with firebase database";
                        }
                        Toast.makeText(HomePage.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getCurrentUser().getUid();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceStd = firebaseDatabase.getReference("Students");

        databaseReferenceStd.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(uid).exists()) {
                    setStudent(true);
                    roll = dataSnapshot.child(uid).child("rollno").getValue(String.class);
                    div = dataSnapshot.child(uid).child("div").getValue(String.class);
                    year = dataSnapshot.child(uid).child("year").getValue(String.class);
                } else {
                    setStudent(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (!isStudent()) {
            databaseReferenceTeach = firebaseDatabase.getReference("Teachers");
            databaseReferenceTeach.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(uid).exists()) {
                        setTeacher(true);
                    } else {
                        setTeacher(false);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        //add click listener to the cards
        noticeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isStudent()) {
                    startActivity(new Intent(getApplicationContext(), ShowNotice.class));
                } else {
                    startActivity(new Intent(getApplicationContext(), NoticeMain.class));
                }
            }
        });
        attendanceCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isStudent()) {
                    Intent intent = new Intent(getApplicationContext(), StudentInformationActivity.class);
                    intent.putExtra("RollNo", roll);
                    intent.putExtra("Division", div);
                    intent.putExtra("Year", year);
                    startActivity(intent);
                } else if (isTeacher()) {
                    startActivity(new Intent(getApplicationContext(), TeacherAttendancePage.class));
                } else {
                    startActivity(new Intent(getApplicationContext(), AdminAttendancePage.class));
                }
            }
        });
        aboutusCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AboutUs.class));
            }
        });
        notesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isStudent()) {
                    startActivity(new Intent(getApplicationContext(), ShowNotes.class));
                } else {
                    startActivity(new Intent(getApplicationContext(), NotesMain.class));
                }
            }
        });
        eventsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isStudent()) {
                    startActivity(new Intent(getApplicationContext(), ShowEvents.class));
                } else {
                    startActivity(new Intent(getApplicationContext(), EventsMain.class));
                }
            }
        });
        forumCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ChatActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuLogout:

                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this, LoginActivity.class));

                break;
        }
        return true;
    }
}
