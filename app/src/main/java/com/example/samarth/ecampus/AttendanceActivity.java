package com.example.samarth.ecampus;

import android.app.ProgressDialog;
import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class AttendanceActivity extends AppCompatActivity {

    ListView lv;
    Button submit;
    Integer position=-1;
    public List<StudentInformation> stdinfo=new ArrayList<StudentInformation>();
    public HashMap<String,Integer> hp=new HashMap<String,Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        final ProgressDialog pd=new ProgressDialog(this,ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Fetching Information");
        pd.setCancelable(false);
        pd.show();

        final String pos=getIntent().getStringExtra("position");
        final String div=getIntent().getStringExtra("division");
        final String year=getIntent().getStringExtra("year");

        Log.e("Position:- ",pos);
        Log.e("Division:- ", div);
        Log.e("year:- ",year);

        position=Integer.parseInt(pos.trim());
        lv=(ListView)findViewById(R.id.AttendanceListView);
        submit=(Button)findViewById(R.id.btnsubmitAttendace);

        try{
            FirebaseDatabase database=FirebaseDatabase.getInstance();
            DatabaseReference databaseReference=database.getReference("Students");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {

                        Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                        for (DataSnapshot child : children) {
                            StudentInformation std = child.getValue(StudentInformation.class);

                            //check for year & division
                            if(std.getDiv().equals(div) && std.getYear().equals(year))
                                stdinfo.add(std);
                            Log.e("String is", std.getFname());
                        }
                        //sorting in ascending order
                        Collections.sort(stdinfo);
                        //displaying the list
                        fn(pos);
                        pd.dismiss();
                    } catch (Exception e) {
                        Log.e("Exception is", e.toString());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }catch(Exception e){
            Log.e("Exception is",e.toString());
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int siz=stdinfo.size();

                // putting every students attendance details
                for(int i=0;i<siz;i++){
                    try{
                        hp.put(stdinfo.get(i).getRollno(),stdinfo.get(i).getSubjects().get(position));
                    }catch(Exception e){
                        Log.e("Exception is",e.toString());
                    }
                }
                for(int i=0;i<siz;i++){
                    View newView=lv.getChildAt(i);
                    String rollno=((TextView)newView.findViewById(R.id.txtRollView)).getText().toString().trim();
                    CheckBox cb=(CheckBox)newView.findViewById(R.id.checkBoxA);
                    Integer totalAttendance=hp.get(rollno);
                    if(cb.isChecked()){
                        hp.put(rollno,totalAttendance+10001);
                        Log.e(rollno, " is present, attendance is " + hp.get(rollno));
                    }
                    else{
                        hp.put(rollno, totalAttendance+10000);
                        Log.e(rollno," is absent");
                    }
                }
                // Entering attendance in database
                try{
                    FirebaseDatabase database=FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference=database.getReference("Students");
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            try {

                                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                                for (DataSnapshot child : children) {
                                    StudentInformation std = child.getValue(StudentInformation.class);
                                    if(std.getDiv().equals(div) && std.getYear().equals(year)) {
                                        String rollno = std.getRollno();
                                        if (hp.containsKey(rollno)) {
                                            int attendance = hp.get(rollno);
                                            child.getRef().child("subjects").child(String.valueOf(position)).setValue(attendance);

                                        }
                                    }
                                }
                            } catch (Exception e) {
                                Log.e("Exception is", e.toString());
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }catch(Exception e){
                    Log.e("Exception is",e.toString());
                }
                finish();
                Toast.makeText(getApplicationContext(), "Attendance Complete", Toast.LENGTH_SHORT).show();
            }
        });

    }

    void fn(String pos){


        //making hashmap for listItem selection
        int siz= stdinfo.size();

        // Adding arraylist to listView
        MyBaseAdapter mba=new MyBaseAdapter(AttendanceActivity.this);
        lv.setAdapter(mba);

        //changing attendance of database
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(AttendanceActivity.this, "Hello Dude", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public class MyBaseAdapter extends BaseAdapter {
        Context context;
        LayoutInflater inflater;

        MyBaseAdapter(Context context){
            this.context=context;
            inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return stdinfo.size();
        }

        @Override
        public StudentInformation getItem(int position) {
            return stdinfo.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater=getLayoutInflater();
            View view=inflater.inflate(R.layout.cust_list_attendance, null);
            final StudentInformation std=stdinfo.get(position);
            TextView tv1=(TextView)view.findViewById(R.id.txtNameView);
            tv1.setText(std.getFname() + " " + std.getLname());
            TextView tv2=(TextView)view.findViewById(R.id.txtRollView);
            tv2.setText(std.getRollno());
            final CheckBox cb=(CheckBox)view.findViewById(R.id.checkBoxA);
            return view;
        }
    }
}
