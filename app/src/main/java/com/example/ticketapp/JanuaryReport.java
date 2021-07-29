package com.samit1.sysamit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.samit1.sysamit.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class JanuaryReport extends AppCompatActivity {

    AnyChartView anyChartView;
    String months[] = {"hardware", "software"};
    int earnings[] = {0, 0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_january_report);



        DatabaseReference checkFBvalue = FirebaseDatabase.getInstance()
                .getReference("january");

       // DatabaseReference checkFBvalue = FirebaseDatabase.getInstance()
               // .getReference("/Monthly Complaint/JANUARY");

        checkFBvalue.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int xJanuary = (int) dataSnapshot.child("hardware").getValue();
                int xFebruary = (int) dataSnapshot.child("software").getValue();

                int earnings[] = {xJanuary, xFebruary};

                anyChartView = findViewById(R.id.any_chart_january);

                Pie pie = AnyChart.pie();
                List<DataEntry> dataentries = new ArrayList<>();

                for (int i = 0; i<months.length; i++){
                    dataentries.add(new ValueDataEntry(months[i], earnings[i]));

                }

                pie.data(dataentries);
                anyChartView.setChart(pie);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



}

