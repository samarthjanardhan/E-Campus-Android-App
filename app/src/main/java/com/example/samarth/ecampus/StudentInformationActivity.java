package com.example.samarth.ecampus;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.samarth.ecampus.model.StudentInformation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StudentInformationActivity extends AppCompatActivity {

    StudentInformation std;

    String roll;
    String year;
    String div;
    TextView tvname,tvroll,tvclass,tvcontact,tvtotalattendance;
    ListView lv;
    boolean StudentFound;

    public boolean isStudentFound() {
        return StudentFound;
    }

    public void setStudentFound(boolean studentFound) {
        StudentFound = studentFound;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_information);

        std=new StudentInformation();
        setStudentFound(false);

        final ProgressDialog pd=new ProgressDialog(this,ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Fetching Information");
        pd.setCancelable(false);
        pd.show();

        Intent intent=getIntent();
        year=intent.getStringExtra("Year").trim();
        div=intent.getStringExtra("Division").trim();
        roll=intent.getStringExtra("RollNo").trim();

        try{
            FirebaseDatabase database=FirebaseDatabase.getInstance();
            DatabaseReference databaseReference=database.getReference("Students");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                        for (DataSnapshot child : children) {
                            StudentInformation std1=child.getValue(StudentInformation.class);
                            Log.e("name:- ",std1.getFname());
                            if(std1.getRollno().equals(roll) && std1.getYear().equals(year) && std1.getDiv().equals(div)) {
                                std = std1;
                                setStudentFound(true);
                            }
                        }
                        pd.dismiss();

                        fn();
                    } catch (Exception e) {
                        Log.e("Exception is", e.toString());
                        pd.dismiss();

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }catch(Exception e){
            Log.e("Exception is",e.toString());
        }
    }

    private void fn() {

        try {
            if (!isStudentFound()) {
                Toast.makeText(getApplicationContext(), "Roll No. not found", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(StudentInformationActivity.this, SelectRollNo.class);
                startActivity(intent);
                finish();
            }
            else {
                tvname = (TextView) findViewById(R.id.txtStudentInfoFullName);
                tvroll = (TextView) findViewById(R.id.etStudentInfoRollNo);
                tvclass = (TextView) findViewById(R.id.etStudentInfoClass);
                tvcontact = (TextView) findViewById(R.id.etStudentInfoContact);
                tvtotalattendance = (TextView) findViewById(R.id.etTotalAttendance);
                lv = (ListView) findViewById(R.id.StudentInfoListView);

                tvname.setText(std.getFname() + " " + std.getLname());
                tvroll.setText(std.getRollno());
                tvclass.setText(std.getYear() + "-" + std.getDiv());
                tvcontact.setText(std.getPhone());

                ArrayList<Integer> al = std.getSubjects();
                ArrayList<String> subjectList = new ArrayList<String>();

                String SESub[] = new String[]{"Discrete Mathematics",
                        "Digital Electronics & Logic Design",
                        "Data Structures & Algorithm",
                        "Computer Organization & Architecture",
                        "Object Oriented Programming",
                        "Engineering Mathematics - III",
                        "Computer Graphics",
                        "Advanced Data Structures",
                        "Microprocessor",
                        "Principles of Programming Language"};

                String TESub[] = new String[]{"Theory of Computation",
                        "Database Management System",
                        "Software Engineering & Project Management",
                        "Information System & Engineering Economics",
                        "Computer Networks",
                        "Design & Analysis of Algorithms",
                        "System Programming & Operating System",
                        "Embedded Systems & Internet of Things",
                        "Software Modeling & Design",
                        "Web Technology"};


                int count = 0;
                int countTot = 0;

                if (std.getYear().equals("SE")) {
                    for (int i = 0; i < 10; i++) {
                        int val = al.get(i);
                        count = count + val % 10000;
                        String str = SESub[i] + "   ----   " + val % 10000 + " / ";
                        val = val / 10000;
                        countTot = countTot + val % 10000;
                        str = str + val % 10000;
                        subjectList.add(str);
                    }
                }

                if (std.getYear().equals("TE")) {
                    for (int i = 0; i < 10; i++) {
                        int val = al.get(i);
                        count = count + val % 10000;
                        String str = TESub[i] + "   ----   " + val % 10000 + " / ";
                        val = val / 10000;
                        countTot = countTot + val % 10000;
                        str = str + val % 10000;
                        subjectList.add(str);
                    }
                }

                if (countTot == 0)
                    countTot = 1;
                Toast.makeText(StudentInformationActivity.this, "Your Total Attendance is--" + (count * 100) / countTot + "%", Toast.LENGTH_LONG).show();
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, subjectList);
                lv.setAdapter(arrayAdapter);
                tvtotalattendance.setText((count * 100) / countTot + "%");
            }
        } catch (Exception e) {
            Log.e("Exception is",e.toString());
        }
    }
}
