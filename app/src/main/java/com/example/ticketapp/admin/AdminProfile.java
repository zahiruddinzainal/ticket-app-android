package com.example.ticketapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class AdminProfile extends AppCompatActivity {

    TextView mprofileName, mprofileEmail, mprofilePhone, mprofileAddress;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_profile);

        mprofileName = findViewById(R.id.profileName);
        mprofileAddress = findViewById(R.id.profileAddress);
        mprofileEmail = findViewById(R.id.profileEmail);
        mprofilePhone = findViewById(R.id.profilePhone);

        fAuth  = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userID = fAuth.getCurrentUser().getUid();
        DocumentReference documentReference = fStore.collection("App user").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                mprofileName.setText(documentSnapshot.getString("Fullname"));
                mprofileAddress.setText(documentSnapshot.getString("Address"));
                mprofileEmail.setText(documentSnapshot.getString("Email"));
                mprofilePhone.setText(documentSnapshot.getString("Phone"));

            }
        });

    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }
}