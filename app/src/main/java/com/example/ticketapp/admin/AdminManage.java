package com.example.psm.admin;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.psm.R;
import com.example.psm.Upload;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class AdminManage extends AppCompatActivity implements AdminManageAdapter.OnItemClickListener {

    private static final int PICK_IMAGE_REQUEST = 1;
    private RecyclerView mRecyclerView;
    private AdminManageAdapter mAdapter;
    private ProgressBar mProgressCircle;
    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private List<Upload> mUploads;
    private Uri mImageUri;
    Button mInsertButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_manage);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mProgressCircle = findViewById(R.id.progress_circle);

        mUploads = new ArrayList<>();
        mAdapter = new AdminManageAdapter(AdminManage.this, mUploads);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(AdminManage.this);

        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        mInsertButton = findViewById(R.id.InsertButton);
        mInsertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent insert = new Intent(AdminManage.this, AdminInsert.class);
                startActivity(insert);
            }
        });

        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mUploads.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    upload.setKey(postSnapshot.getKey());
                    mUploads.add(upload);
                }

                mAdapter.notifyDataSetChanged();

                mProgressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AdminManage.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onItemClick(int positionAdmin) {
        Toast.makeText(this, "Normal click at position: " + positionAdmin, Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }

    public void adminUpdate(View view) {

        int positionAdmin = (int) view.getTag();
        Upload selectedItem = mUploads.get(positionAdmin);
        String IDmakanan = selectedItem.getKey();

        String pos = String.valueOf(positionAdmin);
        Intent i = new Intent(AdminManage.this, AdminUpdate.class);
        i.putExtra("ItemID",IDmakanan);
        startActivity(i);

    }

    private void kemaskini(Dialog dialog) {

        EditText mUpdateNama = (EditText) dialog.findViewById(R.id.UpdateNama);
        EditText mUpdateHarga = (EditText) dialog.findViewById(R.id.UpdateHarga);

        mUpdateNama.getText().toString().trim();
        mUpdateHarga.getText().toString().trim();

        Toast.makeText(AdminManage.this, mUpdateNama + "Upload", Toast.LENGTH_SHORT).show();

    }


    public void adminDelete(View view){

        int positionAdmin = (int) view.getTag();
        Upload selectedItem = mUploads.get(positionAdmin);

        final String selectedKey = selectedItem.getKey();

        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getImageUrl());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mDatabaseRef.child(selectedKey).removeValue();
                Toast.makeText(AdminManage.this, "Item deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }

}