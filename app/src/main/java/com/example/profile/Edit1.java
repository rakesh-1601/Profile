package com.example.profile;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.example.profile.Model.Info;
import com.example.profile.Model.New;
import com.example.profile.Model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Edit1 extends AppCompatActivity {

    EditText name, mobile, email;
    TextView dob;
    Button submit,dateButton;
    RadioGroup gender;
    ImageView edit_icon;
    StorageReference storageReference;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    CircleImageView image_profile;
    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageTask uploadTask;
    boolean change = false;
    New n;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Edit1.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit1);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Profile");

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_black);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        image_profile = findViewById(R.id.profile_image);
        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                if (user.getImageURL().equals("default")){

                } else{
                    Glide.with(Edit1.this).load(user.getImageURL()).into(image_profile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        getId();
    }
    public void getId(){
        edit_icon = findViewById(R.id.edit_icon);
        dateButton = findViewById(R.id.dateButton);
        dob = findViewById(R.id.date);
        edit_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImage();
            }
        });

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(Edit1.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                dob.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });


        name = findViewById(R.id.nameText);
        mobile = findViewById(R.id.mobileText);
        email = findViewById(R.id.emailText);
        dob = findViewById(R.id.date);
        submit = findViewById(R.id.button);
        gender = findViewById(R.id.genderRadio);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gen = "";
                int checkedRadioButtonId = gender.getCheckedRadioButtonId();
                if (checkedRadioButtonId == -1) {
                    // No item selected
                }
                else{
                    if (checkedRadioButtonId == R.id.Male) {
                        gen = "Male";
                    }else {
                        gen = "Female";
                    }
                }

                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                assert firebaseUser != null;
                final String userid = firebaseUser.getUid();
                Boolean valid = validate(gen);
                if(valid){
                    reference = FirebaseDatabase.getInstance().getReference("Info").child(userid);
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("name", name.getText().toString());
                    map.put("mobile", mobile.getText().toString());
                    map.put("email", email.getText().toString());
                    map.put("dob", dob.getText().toString());
                    map.put("gen", gen);
                    reference.updateChildren(map);

                    reference = FirebaseDatabase.getInstance().getReference("New").child(userid);
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            n = dataSnapshot.getValue(New.class);
                            HashMap<String, Object> mapT = new HashMap<>();

                            mapT.put("imageURL", n.getS());
                            DatabaseReference referenceT = FirebaseDatabase.getInstance().getReference("Users").child(userid);
                            referenceT.updateChildren(mapT);
                            Intent intent = new Intent(Edit1.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

    }

    Boolean validate(String gen){
        ConstraintLayout constraintLayout = findViewById(R.id.cs);
        if(name.getText().toString().equals("")){
            Snackbar.make(constraintLayout, "Name cannot be empty", Snackbar.LENGTH_LONG).show();
            return false;
        }else if(mobile.getText().toString().equals("") || mobile.getText().toString().length()!=10){
            Snackbar.make(constraintLayout, "Invalid Mobile Number", Snackbar.LENGTH_LONG).show();
            return false;
        }else if(TextUtils.isEmpty(email.getText().toString()) || !Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
            Snackbar.make(constraintLayout, "Email invalid", Snackbar.LENGTH_LONG).show();
            return false;
        }else if(dob.getText().toString().equals("Choose Date")){
            Snackbar.make(constraintLayout, "Date of Birth cannot be empty", Snackbar.LENGTH_LONG).show();
            return false;
        }else if(gen.equals("")){
            Snackbar.make(constraintLayout, "Select gender", Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage(){
        final ProgressDialog pd = new ProgressDialog(Edit1.this);
        pd.setMessage("Uploading");
        pd.show();

        if (imageUri != null){
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    +"."+getFileExtension(imageUri));

            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw  task.getException();
                    }

                    return  fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();

                        reference = FirebaseDatabase.getInstance().getReference("New").child(firebaseUser.getUid());
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("s", ""+mUri);
                        Glide.with(Edit1.this).load(""+mUri).into(image_profile);
                        reference.updateChildren(map);
                        pd.dismiss();
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Edit1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        } else {
            Toast.makeText(Edit1.this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            imageUri = data.getData();

            if (uploadTask != null && uploadTask.isInProgress()){
                Toast.makeText(Edit1.this, "Upload in progress", Toast.LENGTH_SHORT).show();
            } else {
                uploadImage();
            }
        }
    }
}


