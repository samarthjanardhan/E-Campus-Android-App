package com.example.samarth.ecampus;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TeacherAttendancePage extends AppCompatActivity {

    Button _btnAttendance, _btnAddStd, _btnViewStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_attendance_page);

        _btnAttendance= (Button) findViewById(R.id.btnTeachAttendance);
        _btnAddStd= (Button) findViewById(R.id.btnTeachAddStd);
        _btnViewStudent= (Button) findViewById(R.id.btnTeachViewDetails);

        _btnAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),SubjectList.class));
            }
        });

        _btnAddStd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),StudentRegistration.class));
            }
        });

        _btnViewStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),SelectRollNo.class));
            }
        });
    }
}
