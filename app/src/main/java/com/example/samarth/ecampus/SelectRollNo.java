package com.example.samarth.ecampus;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SelectRollNo extends AppCompatActivity {

    Spinner std,div;
    EditText et;
    String year,division;

    Button _btnSearxh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_roll_no);

        std= (Spinner) findViewById(R.id.spinRollYear);
        div= (Spinner) findViewById(R.id.spinRollDiv);
        et=(EditText) findViewById(R.id.txtRollNoSearch);
        _btnSearxh= (Button) findViewById(R.id.btnSearch);


        List<String> years=new ArrayList<>();
        years.add(0,"Choose a year");
        years.add(1,"SE");
        years.add(2,"TE");

        //Style and populate the spinner

        ArrayAdapter<String> stdAdapter= new ArrayAdapter(SelectRollNo.this, android.R.layout.simple_spinner_item, years);

        //Dropdown layour style

        stdAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Attaching data adapter to spinner

        std.setAdapter(stdAdapter);

        std.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).equals("Choose a year")) {
                    year ="Choose a year";
                }
                else {
                    year=parent.getItemAtPosition(position).toString();
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

        ArrayAdapter<String> divAdapter= new ArrayAdapter(SelectRollNo.this, android.R.layout.simple_spinner_item, divs);

        //Dropdown layour style

        divAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Attaching data adapter to spinner

        div.setAdapter(divAdapter);

        div.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).equals("Choose a divison")) {
                    division ="Choose a division";
                }
                else {
                    division=parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        _btnSearxh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    if(year == "Choose a year") {
                        Toast.makeText(getApplicationContext(),"Select a year!",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(division== "Choose a division") {
                        Toast.makeText(getApplicationContext(),"Select a division!",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String roll=et.getText().toString();

                    if(TextUtils.isEmpty(roll)){
                        et.setError("Roll No required!");
                        et.requestFocus();
                        return;
                    }
                    else {
                        Intent intent = new Intent(SelectRollNo.this, StudentInformationActivity.class);
                        //intent.putExtra("Name", "RollNo");
                        intent.putExtra("Year", year);
                        intent.putExtra("Division", division);
                        intent.putExtra("RollNo", roll);
                        startActivity(intent);
                        finish();
                    }
                }catch (Exception e){
                    Log.e("Error is ",e.toString());
                }
            }
        });
    }
}
