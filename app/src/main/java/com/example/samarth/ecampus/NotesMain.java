package com.example.samarth.ecampus;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.samarth.ecampus.model.UploadNote;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.UUID;


public class NotesMain extends AppCompatActivity {

    Uri pdfUri; //uri are actual url that are meant for local storage
    Button selectFile,upload,fetch;
    TextView notification;

    private ProgressBar mProgressBar;

    private StorageTask mUploadTask;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private EditText mEditTextFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_main);

        mEditTextFileName = findViewById(R.id.edit_text_file_name);

        //objects for button n text view
        fetch = findViewById(R.id.fetchFiles);

        selectFile = findViewById(R.id.selectFile);
        upload = findViewById(R.id.upload);
        notification = findViewById(R.id.notification);
        mProgressBar = findViewById(R.id.progress_bar);

        ArrayList<String> urls = new ArrayList<>();

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads_pdf");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads_pdf");

        fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(NotesMain.this, ShowNotes.class));

            }
        });

        selectFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //to check persmission of phone
                if(ContextCompat.checkSelfPermission(NotesMain.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                {
                    selectPDF();
                }
                else
                {
                    ActivityCompat.requestPermissions(NotesMain.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
                }
            }
        });


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(NotesMain.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                }
                else if(pdfUri!=null) {//the user has selected the file
                    String name=mEditTextFileName.getText().toString();
                    if (name.isEmpty()) {
                        Toast.makeText(getApplicationContext(),"Enter the title first!",Toast.LENGTH_SHORT).show();;
                        mEditTextFileName.setError("Enter Title!");
                        mEditTextFileName.requestFocus();
                        return;
                    }
                    else
                        uploadFile(pdfUri);
                }
                else
                    Toast.makeText(NotesMain.this, "Select a FILE First", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


    private void uploadFile(Uri pdfUri) {

        if (pdfUri != null) {
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(pdfUri));

            String pdfName = UUID.randomUUID().toString();
            final StorageReference imageFolder = mStorageRef.child("/"+pdfName);

            mStorageRef.child(""+pdfName);

            final String fileName = System.currentTimeMillis() + "";


            mUploadTask = fileReference.putFile(pdfUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);
                                }
                            }, 500);

                            Toast.makeText(NotesMain.this, "Upload successful !!! ", Toast.LENGTH_LONG).show();
                            //return the url of uploaded file

                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //set value for newCategory if image Uploaded and we can get download link
                                    UploadNote upload_note = new UploadNote(mEditTextFileName.getText().toString(), uri.toString());

                                    String uploadId = mDatabaseRef.push().getKey();
                                    mDatabaseRef.child(uploadId).setValue(upload_note);
                                }
                            });


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(NotesMain.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                        }
                    });
        }
        else
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == 9 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            selectPDF();
        }
        else
            Toast.makeText(NotesMain.this, "Please Provide Permissions", Toast.LENGTH_SHORT).show();
    }

    private void selectPDF() {

        //To offer user to select file using file manager

        //We will use intent

        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT); //to fetch files
        startActivityForResult(intent, 86);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        //check if user successfully selected a file or not
        if(requestCode==86 && resultCode==RESULT_OK && data!=null)
        {
            pdfUri=data.getData();  //return the uri of selected file
            notification.setText("A file is selected : " + data.getData().getLastPathSegment());
        }
        else
        {
            Toast.makeText(NotesMain.this, "Please Select a FILE FIRST", Toast.LENGTH_SHORT).show();
        }
    }
}
