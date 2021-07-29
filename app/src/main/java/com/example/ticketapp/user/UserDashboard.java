package com.example.ticketapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.widget.TextView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class UserDashboard extends AppCompatActivity {


    private CardView mViewCard;
    private CardView mMyCart;
    private CardView mOrder;
    private CardView mProfileCard;

    TextView mwelcomeTXT;
    String userID;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_dashboard);


        fAuth  = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        mwelcomeTXT = findViewById(R.id.welcomeTXT);
        DocumentReference documentReference = fStore.collection("App user").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                assert documentSnapshot != null;
                mwelcomeTXT.setText("Hi " + documentSnapshot.getString("Fullname"));

            }
        });


        mViewCard = findViewById(R.id.viewCard);
        mViewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent view = new Intent(UserDashboard.this, UserView.class);
                startActivity(view);
            }
        });


        mMyCart = findViewById(R.id.myCartCard);
        mMyCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cart = new Intent(UserDashboard.this, UserCart.class);
                startActivity(cart);
            }
        });

        mOrder = findViewById(R.id.orderCard);
        mOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent order = new Intent(UserDashboard.this, UserOrder.class);
                startActivity(order);
            }
        });

        mProfileCard = findViewById(R.id.profileCard);
        mProfileCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profile = new Intent(UserDashboard.this, UserProfile.class);
                startActivity(profile);
            }
        });
    }
}
