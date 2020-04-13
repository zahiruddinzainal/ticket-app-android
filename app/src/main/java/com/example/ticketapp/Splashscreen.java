package com.example.ticketapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class Splashscreen extends AppCompatActivity {


    TextView mRoleNumber;
    FirebaseFirestore fStore;
    String userID;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);


        fAuth  = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        mRoleNumber = findViewById(R.id.roleNumber);


        if(fAuth.getCurrentUser() != null){

            userID = fAuth.getCurrentUser().getUid();
            DocumentReference documentReference = fStore.collection("App user").document(userID);
            documentReference.addSnapshotListener(Splashscreen.this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                    mRoleNumber.setText(documentSnapshot.getString("Role"));

                    String temporaryRoleHolder = mRoleNumber.getText().toString();

                    if(temporaryRoleHolder.equals("1")){
                        Intent admin = new Intent (Splashscreen.this, AdminDashboard.class);
                        startActivity(admin);
                        Animatoo.animateZoom(Splashscreen.this);
                    }
                    if(temporaryRoleHolder.equals("2")){
                        Intent startUser = new Intent (Splashscreen.this, UserDashboard.class);
                        startActivity(startUser);
                        Animatoo.animateZoom(Splashscreen.this);
                    }

                }
            });
        }
        else {
            Intent startRegister = new Intent (Splashscreen.this, Register.class);
            startActivity(startRegister);
        }

    }
}

