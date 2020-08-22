package com.example.samarth.ecampus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    TextView tvforgot;
    EditText email,pass;
    Button login,signup;
    ProgressDialog pd;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        email= (EditText) findViewById(R.id.txtEmailLogin);
        pass= (EditText) findViewById(R.id.txtPassLogin);

        login= (Button) findViewById(R.id.btnLogin);
        signup= (Button) findViewById(R.id.btnReg);

        tvforgot = (TextView) findViewById(R.id.txtForgotPassword);

        pd= new ProgressDialog(this);


        firebaseAuth= FirebaseAuth.getInstance();


        if(firebaseAuth.getCurrentUser()!=null)
        {
            startActivity(new Intent(getApplicationContext(),HomePage.class));
            finish();
        }

        //Login Action
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String mail = email.getText().toString().trim();
                String password = pass.getText().toString().trim();

                if (TextUtils.isEmpty(mail)) {
                    email.setError("EMail ID required...");
                    email.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
                    email.setError("Please enter a valid EMail ID");
                    email.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    pass.setError("Password required...");
                    pass.requestFocus();
                    return;
                }

                pd.setMessage("Logging in...");
                pd.setCancelable(false);
                pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pd.show();

                firebaseAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(LoginActivity.this,HomePage.class);
                            startActivity(intent);
                            finish();
                            pd.dismiss();
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "Oops!! Wrong credentials", Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                        }
                    }
                });
            }
        });

        //Student Registration
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, StudentRegistration.class));
            }
        });


        //Resetting password
        tvforgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String mail = email.getText().toString().trim();

                if (TextUtils.isEmpty(mail)) {
                    email.setError("EMail ID required...");
                    email.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
                    email.setError("Please enter a valid EMail ID");
                    email.requestFocus();
                    return;
                }

                firebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),"Reset Mail succesfully sent!",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Reset Mail error!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
