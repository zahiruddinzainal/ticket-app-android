package com.example.ticketapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class UserDashboard extends AppCompatActivity {


    private CardView mViewCard;
    private CardView mMyCart;
    private CardView mOrder;
    private CardView mProfileCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_dashboard);


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
