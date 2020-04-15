package com.example.ticketapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import java.util.ArrayList;
import java.util.List;

public class UserView extends AppCompatActivity {


    String userID;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    private DatabaseReference mTicketDatabase;
    private RecyclerView mRecyclerView;
    private UserViewAdapter mAdapter;
    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private List<Upload> mUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_view);


        fAuth  = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();


        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUpload = new ArrayList<>();
        mAdapter = new UserViewAdapter(UserView.this, mUpload);
        mRecyclerView.setAdapter(mAdapter);
        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("tickets"); //GET PATH REFERENCE OF TICKETS DATABASE


        //---------------------------------------------------- DISPLAY DATA TO RECYCLERVIEW ------------------------------
        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(mDatabaseRef != null){
                    mUpload.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Upload upload = postSnapshot.getValue(Upload.class);
                        upload.setKey(postSnapshot.getKey());
                        mUpload.add(upload);
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(UserView.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    //------------------------------------------------------------------ BACK PRESS -----------------------------------------
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }


    public void onAddToCart(View view) {

        int position = (int) view.getTag();
        int j = 1;
        fAuth  = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();


        FirebaseDatabase x = FirebaseDatabase.getInstance();
        DatabaseReference y = x.getReference("cart").push();


        Upload uploadCurrent = mUpload.get(position);
        String destinationTemp = uploadCurrent.getDestination();
        String masaTemp = uploadCurrent.getTime();
        String hargaTemp = uploadCurrent.getHarga();
        String gambar = uploadCurrent.getImageUrl();

        y.child("jumlah").setValue(j);
        y.child("destination").setValue(destinationTemp);
        y.child("time").setValue(masaTemp);
        y.child("harga").setValue(hargaTemp);
        y.child("imageUrl").setValue(gambar);
        y.child("cumulativeTotal").setValue(hargaTemp);

        Toast.makeText(this,  "Ticket: " + destinationTemp  + " added to cart", Toast.LENGTH_SHORT).show();

    }


}
