package com.example.samarth.ecampus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class SubjectList extends AppCompatActivity {

    Spinner _spYear, _spDiv;

    ListView lv;

    String year,div;

    String arr[]=new String[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjecct_list);

        _spYear= (Spinner) findViewById(R.id.spinSubjectListYear);
        _spDiv= (Spinner) findViewById(R.id.spinSubjectListDiv);
        lv=(ListView)findViewById(R.id.SubjectListView);

        List<String> years=new ArrayList<>();
        years.add(0,"Choose a year");
        years.add(1,"SE");
        years.add(2,"TE");

        //Style and populate the spinner

        ArrayAdapter<String> stdAdapter= new ArrayAdapter(this, android.R.layout.simple_spinner_item, years);

        //Dropdown layout style

        stdAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Attaching data adapter to spinner

        _spYear.setAdapter(stdAdapter);

        _spYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).equals("Choose a year")) {
                    year ="Choose a year";
                    return;
                }
                else {
                    year=parent.getItemAtPosition(position).toString();

                    display(year);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        List<String> divs=new ArrayList<>();
        divs.add(0,"Choose a division");
        divs.add(1,"A");
        divs.add(2,"B");

        //Style and populate the spinner

        ArrayAdapter<String> divAdapter= new ArrayAdapter(this, android.R.layout.simple_spinner_item, divs);

        //Dropdown layour style

        divAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Attaching data adapter to spinner

        _spDiv.setAdapter(divAdapter);

        _spDiv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).equals("Choose a divison")) {
                    div ="Choose a division";
                    return;
                }
                else {
                    div=parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(div=="Choose a division") {
                    Toast.makeText(getApplicationContext(),"Select a division",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(year=="Choose a year") {
                    Toast.makeText(getApplicationContext(),"Select a year",Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    Intent intent=new Intent(SubjectList.this,AttendanceActivity.class);
                    intent.putExtra("position",position+"");
                    intent.putExtra("division",div);
                    intent.putExtra("year",year);
                    startActivity(intent);
                }
            }
        });
    }

    public void display(String year) {
        if(year=="SE") {
            arr[0]="Discrete Mathematics";
            arr[1]="Digital Electronics & Logic Design";
            arr[2]="Data Structures & Algorithm";
            arr[3]="Computer Organization & Architecture";
            arr[4]="Object Oriented Programming";
            arr[5]="Engineering Mathematics - III";
            arr[6]="Computer Graphics";
            arr[7]="Advanced Data Structures";
            arr[8]="Microprocessor";
            arr[9]="Principles of Programming Language";
        }

        if(year == "TE") {
            arr[0]="Theory of Computation";
            arr[1]="Database Management System";
            arr[2]="Software Engineering & Project Management";
            arr[3]="Information System & Engineering Economics";
            arr[4]="Computer Networks";
            arr[5]="Design & Analysis of Algorithms";
            arr[6]="System Programming & Operating System";
            arr[7]="Embedded Systems & Internet of Things";
            arr[8]="Software Modeling & Design";
            arr[9]="Web Technology";
        }

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,arr);
        lv.setAdapter(arrayAdapter);

    }
}
