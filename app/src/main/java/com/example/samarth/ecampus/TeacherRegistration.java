package com.example.samarth.ecampus;

import android.app.ProgressDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.samarth.ecampus.model.TeacherInformation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class TeacherRegistration extends AppCompatActivity {

    Button _btnreg;
    EditText _txtFirstName, _txtSurname, _txtDesg, _txtPass, _txtPhone, _txtEmail;
    ProgressDialog pd;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_registration);

        _btnreg=(Button) findViewById(R.id.btnTeacherreg);

        _txtFirstName=(EditText) findViewById(R.id.txtTeacherFirstName);
        _txtSurname=(EditText) findViewById(R.id.txtTeacherSurname);
        _txtDesg=(EditText) findViewById(R.id.txtTeacherDesgn);
        _txtPass=(EditText) findViewById(R.id.txtTeacherPass);
        _txtEmail=(EditText) findViewById(R.id.txtTeacherEmail);
        _txtPhone=(EditText) findViewById(R.id.txtTeacherPhone);

        firebaseAuth=FirebaseAuth.getInstance();
        pd=new ProgressDialog(this);

        _btnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fnregister();
            }
        });
    }

    public void fnregister() {
        String fname,lname,desgn,phn,mail,pass;

        fname=_txtFirstName.getText().toString().trim();
        lname=_txtSurname.getText().toString().trim();
        desgn=_txtDesg.getText().toString().trim();
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

        if(TextUtils.isEmpty(desgn)) {
            _txtDesg.setError("Can't be empty!");
            _txtDesg.requestFocus();
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

        Toast.makeText(getApplicationContext(),"Validation Successfull!",Toast.LENGTH_LONG).show();

        pd.setMessage("Registering...");
        pd.setCancelable(false);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.show();

        final TeacherInformation tchinfo=new TeacherInformation(fname,lname,desgn,phn,mail,pass);

        try{
            firebaseAuth.createUserWithEmailAndPassword(mail,pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "User Registered Successfully", Toast.LENGTH_SHORT).show();
                        saveInformation(tchinfo);
                        pd.dismiss();
                    } else {

                        if(task.getException() instanceof FirebaseAuthUserCollisionException) {
                            _txtEmail.setError("EMail already registered!");
                            _txtEmail.requestFocus();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Unable to register", Toast.LENGTH_SHORT).show();
                            Log.e("Exception is", task.getException().toString());
                        }
                        pd.dismiss();
                    }
                }
            });
        }catch(Exception e){
            Log.e("Exception is ",e.toString());
        }

    }

    public void saveInformation(TeacherInformation tchinfo) {

        try {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            databaseReference = FirebaseDatabase.getInstance().getReference("Teachers");
            databaseReference.child(user.getUid()).setValue(tchinfo);
            //Toast.makeText(AddStudentActivity.this, "Information Stored", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Log.e("Exception is",e.toString());
        }

        firebaseAuth.signOut();
    }
}
