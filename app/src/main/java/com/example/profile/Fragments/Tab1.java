package com.example.profile.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.profile.InformationActivity;
import com.example.profile.MainActivity;
import com.example.profile.Model.Info;
import com.example.profile.Model.User;
import com.example.profile.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class Tab1 extends Fragment {

    CircleImageView profile_image;
    TextView name,mobile,email,dob,gender;
    ImageView imageView;

    FirebaseUser firebaseUser;
    DatabaseReference reference;
    ValueEventListener eventListener;

    public Tab1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab1, container, false);

        name = view.findViewById(R.id.nameText);
        email = view.findViewById(R.id.emailText);
        dob = view.findViewById(R.id.date);
        mobile = view.findViewById(R.id.mobileText);
        gender = view.findViewById(R.id.genderText);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        imageView = view.findViewById(R.id.profile_image);
        reference = FirebaseDatabase.getInstance().getReference("Info").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Info info = dataSnapshot.getValue(Info.class);
                    name.setText(info.getName());
                    email.setText(info.getEmail());
                    dob.setText(info.getDob());
                    mobile.setText(info.getMobile());
                    gender.setText(info.getGen());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                if (user.getImageURL().equals("default")){

                } else if(user.getStatus().equals("Yes")){
                    Glide.with(Tab1.this).load(user.getImageURL()).into(imageView);
                }
                reference.removeEventListener(eventListener);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        reference.addValueEventListener(eventListener);

        return view;
    }
}
