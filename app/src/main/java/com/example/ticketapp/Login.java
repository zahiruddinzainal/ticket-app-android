package com.example.ticketapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class Login extends AppCompatActivity {

    TextView textView;
    TextView mRoleNumber;
    FirebaseFirestore fStore;
    String userID;

    EditText mEmail, mPassword;
    Button mLoginButton;

    ProgressBar progressBar;
    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fAuth  = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        mRoleNumber = findViewById(R.id.roleNumber);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);
        mLoginButton = findViewById(R.id.loginButton);


        if(fAuth.getCurrentUser() != null){

            userID = fAuth.getCurrentUser().getUid();
            DocumentReference  documentReference = fStore.collection("App user").document(userID);
            documentReference.addSnapshotListener(Login.this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                    mRoleNumber.setText(documentSnapshot.getString("Role"));

                    String temporaryRoleHolder = mRoleNumber.getText().toString();

                    if(temporaryRoleHolder.equals("1")){
                        Intent pemilik = new Intent (Login.this, AdminDashboard.class);
                        startActivity(pemilik);
                    }
                    if(temporaryRoleHolder.equals("2")){
                        Intent pelanggan = new Intent (Login.this, UserDashboard.class);
                        startActivity(pelanggan);
                    }

                }
            });

            finish();
        }


        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();



                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is required");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is required");
                    return;
                }

                if(password.length()<6){
                    mPassword.setError("Password must be more than 6 characters");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //authenticate the user
                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {



                        if(task.isSuccessful()){
                            Toast.makeText(Login.this, "Login success", Toast.LENGTH_SHORT).show();
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference  documentReference = fStore.collection("App user").document(userID);
                            documentReference.addSnapshotListener(Login.this, new EventListener<DocumentSnapshot>() {
                                @Override
                                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                                    mRoleNumber.setText(documentSnapshot.getString("Role"));

                                    String temporaryRoleHolder = mRoleNumber.getText().toString();

                                    if(temporaryRoleHolder.equals("1")){
                                        Intent pemilik = new Intent (Login.this, AdminDashboard.class);
                                        startActivity(pemilik);
                                        Toast.makeText(Login.this, "Selamat datang pemilik restoran", Toast.LENGTH_SHORT).show();
                                    }
                                    if(temporaryRoleHolder.equals("2")){
                                        Intent pelanggan = new Intent (Login.this, UserDashboard.class);
                                        startActivity(pelanggan);
                                        Toast.makeText(Login.this, "Selamat datang pelanggan", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });



                        }

                        else{
                            Toast.makeText(Login.this, "Error!", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);

                        }
                    }
                });


            }
        });


        textView=findViewById(R.id.registerHere);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });


    }
}
