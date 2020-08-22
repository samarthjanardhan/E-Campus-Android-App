package com.example.samarth.ecampus;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminAttendancePage extends AppCompatActivity {

    Button _btnAttendance, _btnAddStd, _btnAddTeach, _btnViewStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_attendance_page);

        _btnAttendance= (Button) findViewById(R.id.btnAdminAttendance);
        _btnAddStd= (Button) findViewById(R.id.btnAdminAddStd);
        _btnAddTeach= (Button) findViewById(R.id.btnAdminAddTeach);
        _btnViewStudent= (Button) findViewById(R.id.btnAdminViewDetails);

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

        _btnAddTeach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),TeacherRegistration.class));
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
