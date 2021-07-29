package com.example.ticketapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AdminDashboard extends AppCompatActivity {

    private CardView mAddCard;
    private CardView mUpdateDeleteCard;
    private CardView mReportCard;
    private CardView mProfileCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_dashboard);

        //------------------------------------------------- NAV ICON ---------------------------------------
        mAddCard = findViewById(R.id.addCard);
        mAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent add = new Intent (AdminDashboard.this, AdminAdd.class);
                startActivity(add);
            }
        });


        mUpdateDeleteCard = findViewById(R.id.updateDeleteCard);
        mUpdateDeleteCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent updatedelete = new Intent(AdminDashboard.this, AdminUpdateDelete.class);
                startActivity(updatedelete);
            }
        });

        mReportCard = findViewById(R.id.reportCard);
        mReportCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent report = new Intent(AdminDashboard.this, AdminSalesReport.class);
                startActivity(report);
            }
        });


        mProfileCard = findViewById(R.id.profileCard);
        mProfileCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profile = new Intent(AdminDashboard.this, AdminProfile.class);
                startActivity(profile);
            }
        });


    }
}
