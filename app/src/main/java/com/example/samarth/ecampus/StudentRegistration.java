package com.example.samarth.ecampus;

import android.app.ProgressDialog;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.samarth.ecampus.model.StudentInformation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StudentRegistration extends AppCompatActivity {

    Button _btnreg;
    EditText _txtFirstName, _txtSurname,_txtRoll_No, _txtPass, _txtPhone, _txtEmail;
    Spinner div,std;
    ProgressDialog pd;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    String _txtDiv, _txtYear;
    String fname,lname,year,roll,phn,mail,pass,div1;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    boolean flag;

    Iterable<DataSnapshot> children;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_registration);


        _btnreg=(Button) findViewById(R.id.btnStudentreg);

        _txtFirstName=(EditText) findViewById(R.id.txtStudentFirstName);
        _txtSurname=(EditText) findViewById(R.id.txtStudentSurname);
        _txtRoll_No=(EditText) findViewById(R.id.txtStudentRollNo);
        _txtPass=(EditText) findViewById(R.id.txtStudentPass);
        _txtEmail=(EditText) findViewById(R.id.txtStudentEmail);
        _txtPhone=(EditText) findViewById(R.id.txtStudentPhone);

        div= (Spinner) findViewById(R.id.spinStudentDiv);
        std= (Spinner) findViewById(R.id.spinStudentYear);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("Students");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                children = dataSnapshot.getChildren();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        firebaseAuth=FirebaseAuth.getInstance();
        pd=new ProgressDialog(this);

        List<String> years=new ArrayList<>();
        years.add(0,"Choose a year");
        years.add(1,"SE");
        years.add(2,"TE");

        //Style and populate the spinner

        ArrayAdapter<String> stdAdapter= new ArrayAdapter(this, android.R.layout.simple_spinner_item, years);

        //Dropdown layour style

        stdAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Attaching data adapter to spinner

        std.setAdapter(stdAdapter);

        std.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).equals("Choose a year")) {
                    _txtYear ="Choose a year";
                }
                else {
                    _txtYear=parent.getItemAtPosition(position).toString();
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

        div.setAdapter(divAdapter);

        div.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).equals("Choose a divison")) {
                    _txtDiv ="Choose a division";
                }
                else {
                    _txtDiv=parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        _btnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fnregister();
                //finish();
            }
        });
    }

    public void fnregister() {


        fname=_txtFirstName.getText().toString().trim();
        lname=_txtSurname.getText().toString().trim();
        year=_txtYear;
        div1=_txtDiv;
        roll=_txtRoll_No.getText().toString().trim();
        phn=_txtPhone.getText().toString().trim();
        mail=_txtEmail.getText().toString().trim();
        pass=_txtPass.getText().toString().trim();


        if(TextUtils.isEmpty(fname)) {
            _txtFirstName.setError("Can't be empty!");
            _txtFirstName.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(lname)) {
            _txtSurname.setError("Can't be empty!");
            _txtSurname.requestFocus();
            return;
        }

        if(year=="Choose a year") {
            Toast.makeText(getApplicationContext(),"Select a year!",Toast.LENGTH_SHORT).show();
            return;
        }

        if(div1=="Choose a division") {
            Toast.makeText(getApplicationContext(),"Select a division!",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(roll)) {
            _txtRoll_No.setError("Can't be empty!");
            _txtRoll_No.requestFocus();
            return;
        }

        if(Integer.parseInt(roll)<1) {
            _txtRoll_No.setError("Enter valid Roll No!");
            _txtRoll_No.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(phn)) {
            _txtPhone.setError("Can't be empty!");
            _txtPhone.requestFocus();
            return;
        }

        if(!Patterns.PHONE.matcher(phn).matches()) {
            _txtPhone.setError("Enter Valid Phone Number!");
            _txtPhone.requestFocus();
            return;
        }

        if(phn.length()!=10) {
            _txtPhone.setError("Enter Valid Phone Number!");
            _txtPhone.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(mail)) {
            _txtEmail.setError("Can't be empty!");
            _txtEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            _txtEmail.setError("Enter Valid EMail ID!");
            _txtEmail.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(pass)) {
            _txtPass.setError("Can't be empty!");
            _txtPass.requestFocus();
            return;
        }

        if(pass.length()<6) {
            _txtPass.setError("Password should be atleast of length 6");
            _txtPass.requestFocus();
            return;
        }

        ArrayList<Integer> subject=new ArrayList<>();
        for(int i=0;i<10;i++) {
            subject.add(i,0);
        }

        Toast.makeText(getApplicationContext(),"Validation Successful!",Toast.LENGTH_LONG).show();

        pd.setMessage("Registering...");
        pd.setCancelable(false);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.show();

        setFlag(true);
        checkRoll();

        if(isFlag()) {

            final StudentInformation stdinfo = new StudentInformation(fname, lname, year, div1, roll, phn, mail, pass, subject);

            try {
                firebaseAuth.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            saveInformation(stdinfo);

                            Toast.makeText(getApplicationContext(), "User Registered Successfully", Toast.LENGTH_SHORT).show();

                            pd.dismiss();
                            //startActivity(new Intent());
                            finish();
                        } else {

                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                _txtEmail.setError("EMail already registered!");
                                _txtEmail.requestFocus();
                            } else {
                                Toast.makeText(getApplicationContext(), "Unable to register", Toast.LENGTH_SHORT).show();
                                Log.e("Exception is", task.getException().toString());
                            }
                            pd.dismiss();
                        }
                    }
                });
            } catch (Exception e) {
                Log.e("Exception is ", e.toString());
            }
        }
        else {
            pd.dismiss();
            Toast.makeText(getApplicationContext(),"Roll Number already exists...",Toast.LENGTH_SHORT).show();
            _txtRoll_No.setError("Roll No already exists..");
            _txtRoll_No.requestFocus();
        }

    }

    private void checkRoll() {

        for ( DataSnapshot child:children) {
            StudentInformation std1=child.getValue(StudentInformation.class);
            if(std1.getRollno().equals(roll) && std1.getDiv().equals(div1) && std1.getYear().equals(year) ) {
                setFlag(false);
                break;
            }
        }
    }

    public void saveInformation(StudentInformation stdinfo) {

        try {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            databaseReference = FirebaseDatabase.getInstance().getReference("Students");
            databaseReference.child(user.getUid()).setValue(stdinfo);
        }catch (Exception e){
            Log.e("Exception is",e.toString());
        }

        firebaseAuth.signOut();
    }
}
