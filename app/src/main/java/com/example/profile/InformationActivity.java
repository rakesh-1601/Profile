package com.example.profile;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.profile.Model.Info;
import com.example.profile.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class InformationActivity extends AppCompatActivity {
    Button dateButton;
    TextView date,tags;
    EditText name, mobile, email,aboutus;
    TextView dob;
    Button mOrder, submit;
    TextView mItemSelected;
    String[] listItems;
    Spinner personality;
    boolean[] checkedItems;
    RadioGroup gender;
    ArrayList<Integer> mUserItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user.status.equals("Yes")) {
                    Intent intent = new Intent(InformationActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Set up your Profile");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_black);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getId();

        mOrder = (Button) findViewById(R.id.btnOrder);
        mItemSelected = (TextView) findViewById(R.id.tagsText);

        listItems = getResources().getStringArray(R.array.shopping_item);
        checkedItems = new boolean[listItems.length];

        mOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(InformationActivity.this);
                mBuilder.setTitle("Select tags");
                mBuilder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                        if (isChecked) {
                            if (!mUserItems.contains(position)) {
                                mUserItems.add(position);
                            }
                        } else {
                            if (mUserItems.contains(position)) {
                                mUserItems.remove(mUserItems.indexOf(position));
                            }
                        }

                    }
                });
                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String item = "";
                        for (int i = 0; i < mUserItems.size(); i++) {
                            item = item + listItems[mUserItems.get(i)];
                            if (i != mUserItems.size() - 1) {
                                item = item + ", ";
                            }
                        }
                        mItemSelected.setText(item);
                    }
                });

                mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                mBuilder.setNeutralButton("Clear", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        for (int i = 0; i < checkedItems.length; i++) {
                            checkedItems[i] = false;
                            mUserItems.clear();
                            mItemSelected.setText("");
                        }
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });


    }

    private void getId() {
        dateButton = findViewById(R.id.dateButton);
        date = findViewById(R.id.date);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(InformationActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        name = findViewById(R.id.nameText);
        mobile = findViewById(R.id.mobileText);
        email = findViewById(R.id.emailText);
        dob = findViewById(R.id.date);
        submit = findViewById(R.id.submit);
        gender = findViewById(R.id.genderRadio);
        aboutus = findViewById(R.id.aboutUsText);
        personality = findViewById(R.id.personality);
        tags = findViewById(R.id.tagsText);
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

                Info info = new Info(name.getText().toString(),mobile.getText().toString(),email.getText().toString(),
                dob.getText().toString(),aboutus.getText().toString(),personality.getSelectedItem().toString(),tags.getText().toString(),
                gen);
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                assert firebaseUser != null;
                final String userid = firebaseUser.getUid();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Info").child(userid);

                reference.setValue(info).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("status", "Yes");
                            reference.updateChildren(map);
                            Intent intent = new Intent(InformationActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                });
            }
        });
    }
}


