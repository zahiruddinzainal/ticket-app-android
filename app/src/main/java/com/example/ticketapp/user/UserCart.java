package com.example.ticketapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nullable;

public class UserCart extends AppCompatActivity {

    TextView mTotal;
    String userID;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    private DatabaseReference mCartDatabase;
    private Button mCheckoutButton;
    private RecyclerView mRecyclerView;
    private UserCartAdapter mAdapter;
    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private TextView mTextEmpty;
    private LottieAnimationView mLottieEmpty;
    private ValueEventListener mDBListener;
    private List<Cart> mCart;

    String mNama;
    String mAlamat;
    String mPhone;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_cart);


        fAuth  = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();


        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mTextEmpty = findViewById(R.id.textEmpty);
        mTextEmpty.setVisibility(View.VISIBLE);

        mLottieEmpty = findViewById(R.id.lottieEmpty);
        mLottieEmpty.setVisibility(View.VISIBLE);

        DocumentReference  documentReference = fStore.collection("App user").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                mNama = documentSnapshot.getString("Fullname");
                mAlamat = documentSnapshot.getString("Address");
                mPhone = documentSnapshot.getString("Phone");

            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mCart = new ArrayList<>();
        mAdapter = new UserCartAdapter(UserCart.this, mCart);
        mRecyclerView.setAdapter(mAdapter);
        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("cart/" + userID); //GET PATH REFERENCE OF USER CART

        //---------------------------------------------------- SET TOTAL PRICE IN CART ------------------------------

        FirebaseDatabase x = FirebaseDatabase.getInstance();
        DatabaseReference y = x.getReference().child("cart/" + userID).push(); //GET PATH REFERENCE OF USER CART

        String TempCartPath = "cart/" + userID;

        mTotal = findViewById(R.id.totalCart);
        mCartDatabase = FirebaseDatabase.getInstance().getReference().child(TempCartPath);
        mTotal.setText("0.00");

        mCartDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double sum = 0.00;

                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    Map<String, Object> map  = (Map<String, Object>) ds.getValue();
                    assert map != null;
                    Object price = map.get("cumulativeTotal");
                    double pValue = Double.parseDouble(String.valueOf(price));
                    sum += pValue;
                    @SuppressLint("DefaultLocale") String tempSum = String.format("%.2f", sum);
                    mTotal.setText(tempSum);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference emptyRef = FirebaseDatabase.getInstance().getReference();
        emptyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild("cart")){
                    mTextEmpty.setVisibility(View.INVISIBLE);
                    mLottieEmpty.setVisibility(View.INVISIBLE);
                }
                else {
                    mTextEmpty.setVisibility(View.VISIBLE);
                    mLottieEmpty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //---------------------------------------------------- DISPLAY DATA TO RECYCLERVIEW ------------------------------
        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(mDatabaseRef != null){
                    mCart.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Cart carts = postSnapshot.getValue(Cart.class);
                        carts.setKey(postSnapshot.getKey());
                        mCart.add(carts);
                    }
                    //  mRecyclerView.setBackgroundColor(ContextCompat.getColor(UserCart.this, R.color.white));
                    mAdapter.notifyDataSetChanged();
                    mTextEmpty.setVisibility(View.INVISIBLE);
                    mLottieEmpty.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(UserCart.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mTextEmpty.setVisibility(View.VISIBLE);
                mLottieEmpty.setVisibility(View.VISIBLE);

            }
        });


        //------------------------------------------------------------------ CHECKOUT ALERT -----------------------------------------
        mCheckoutButton = findViewById(R.id.CheckoutButton);
        mCheckoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmPaymentAlert();
            }
        });

    }

    //------------------------------------------------------------------ CHECKOUT BUTTON -----------------------------------------
    private void confirmPaymentAlert() {
        final Dialog dialog = new Dialog(UserCart.this); // Context, this, etc.
        dialog.setContentView(R.layout.user_payment_alert);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent); // set adapter background jadi transparent

        final TextView mdialogName;
        final TextView mdialogAddress;
        final TextView mdialogTotal;

        mdialogName = (TextView)dialog.findViewById(R.id.dialog_name);
        mdialogAddress = (TextView)dialog.findViewById(R.id.dialog_address);
        mdialogTotal = (TextView)dialog.findViewById(R.id.dialog_total);

        fAuth  = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userID = fAuth.getCurrentUser().getUid();
        DocumentReference documentReference = fStore.collection("App user").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                mdialogName.setText("Nama: " + documentSnapshot.getString("Fullname"));
                mdialogAddress.setText("Alamat: " + documentSnapshot.getString("Address"));
                mdialogTotal.setText("Jumlah perlu dibayar: RM " + mTotal.getText());

            }
        });

        dialog.show();
    }

    public void alertProceed(View v) {

        copyFirebaseData();

        //---------------------------------------------------------------------DELETE CART ENTRY-------------------------------------------
        FirebaseDatabase p = FirebaseDatabase.getInstance();
        p.getReference("cart/" + userID).removeValue();

        //---------------------------------------------------------------------------TOAST--------------------------------------------------
        Toast.makeText(UserCart.this, "Tempahan berjaya"  , Toast.LENGTH_SHORT).show(); //toast text
        Intent confirmOrder = new Intent(UserCart.this, UserOrder.class);
        confirmOrder.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(confirmOrder);



    }

    public void copyFirebaseData() {


        fAuth  = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();


        String orderPath = "Pesanan/user ID:" + userID;
        String orderRefPath = "Pesanan (Details)/user ID:" + userID;

        DatabaseReference createOrderNode = FirebaseDatabase.getInstance().getReference();
        createOrderNode.child(orderPath);

        //get date
        final String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        //get time
        DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        final String currentTime = dateFormat.format(new Date());

        // ------------------- database

        final DatabaseReference toOrderNode = FirebaseDatabase.getInstance().getReference().child(orderPath).push();
        final DatabaseReference toOrderRef = FirebaseDatabase.getInstance().getReference().child(orderRefPath).child(toOrderNode.getKey());
        final String userOrderID = toOrderNode.getKey();
        final DatabaseReference toPesananRefPekerja = FirebaseDatabase.getInstance().getReference().child("Pesanan (Rujukan pekerja)");


        // Copy cart node

        DatabaseReference cartNode = FirebaseDatabase.getInstance().getReference().child("cart/" + userID);
        cartNode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot cartCode : dataSnapshot.getChildren()) {
                    String cartCodeKey = cartCode.getKey();
                    String name = cartCode.child("name").getValue(String.class);
                    String harga = cartCode.child("harga").getValue(String.class);
                    String imageUrl = cartCode.child("imageUrl").getValue(String.class);
                    Integer jumlah = cartCode.child("jumlah").getValue(Integer.class);
                    String cumulativeTotal = cartCode.child("cumulativeTotal").getValue(String.class);

                    toOrderRef.child("orderID").setValue(toOrderNode.getKey());
                    toOrderRef.child("orderDate").setValue(currentDate);
                    toOrderRef.child("orderTime").setValue(currentTime);
                    toOrderRef.child("status").setValue("Pending");
                    toOrderRef.child("needToPay").setValue("RM " + mTotal.getText().toString());

                    toPesananRefPekerja.child(userOrderID).child("userID").setValue(userID);
                    toPesananRefPekerja.child(userOrderID).child("nama").setValue(mNama);
                    toPesananRefPekerja.child(userOrderID).child("alamat").setValue(mAlamat);
                    toPesananRefPekerja.child(userOrderID).child("phone").setValue(mPhone);
                    toPesananRefPekerja.child(userOrderID).child("orderDate").setValue(currentDate);
                    toPesananRefPekerja.child(userOrderID).child("orderTime").setValue(currentTime);

                    toOrderNode.child(cartCodeKey).child("name").setValue(name);
                    toOrderNode.child(cartCodeKey).child("status").setValue("Pending");
                    toOrderNode.child(cartCodeKey).child("harga").setValue(harga);
                    toOrderNode.child(cartCodeKey).child("imageUrl").setValue(imageUrl);
                    toOrderNode.child(cartCodeKey).child("jumlah").setValue(jumlah);
                    toOrderNode.child(cartCodeKey).child("cumulativeTotal").setValue(cumulativeTotal);
                    toOrderNode.child(cartCodeKey).child("orderDate").setValue(currentDate);
                    toOrderNode.child(cartCodeKey).child("orderTime").setValue(currentTime);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //------------------------------------------------------------------ BACK PRESS -----------------------------------------
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }

    //---------------------------------------------------- INCREASE JUMLAH TO CURRENT CART ----------------------
    public  void onIncreaseAction(View view){
        int pos = (int) view.getTag(); //get tag from previous set tag

        Cart selectedItem = mCart.get(pos); // initialize db class
        final String selectedKey = selectedItem.getKey(); // get position key from db

        int j = selectedItem.getJumlah(); // get temporary current jumlah
        String namaMenu = selectedItem.getDestination().toLowerCase(); // get name and convert it to lower space
        String hargaMenu = selectedItem.getHarga(); // get name and convert it to lower space

        float hargaCumTemp = Float.parseFloat(hargaMenu);
        float tempCTHdarab = hargaCumTemp*(j+1);
        String tempCTH = String.format("%.2f", tempCTHdarab);

        fAuth  = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();


        FirebaseDatabase x = FirebaseDatabase.getInstance();  // initialize db to get value
        x.getReference("cart/" + userID).child(selectedKey).child("jumlah").setValue(j+1); // get value and implement incrementation
        x.getReference("cart/" + userID).child(selectedKey).child("cumulativeTotal").setValue(tempCTH); // get value and implement incrementation

        mTotal = findViewById(R.id.totalCart);
        mCartDatabase = FirebaseDatabase.getInstance().getReference().child("cart/" + userID);

        mCartDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double sum = 0.00;

                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    Map<String, Object> map  = (Map<String, Object>) ds.getValue();
                    Object price = map.get("cumulativeTotal");
                    double pValue = Double.parseDouble(String.valueOf(price));
                    sum += pValue;
                    String tempSum = String.format("%.2f", sum);
                    mTotal.setText(tempSum);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    //---------------------------------------------------- DECREASE JUMLAH TO CURRENT CART ----------------------
    public  void onDecreaseAction(View view){
        int pos = (int) view.getTag();

        Cart selectedItem = mCart.get(pos);
        final String selectedKey = selectedItem.getKey();

        int j = selectedItem.getJumlah();
        String namaMenu = selectedItem.getDestination().toLowerCase();
        String hargaMenu = selectedItem.getHarga(); // get name and convert it to lower space

        fAuth  = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        float hargaFloatTemp = Float.parseFloat(hargaMenu);
        float tempCTHdarab = hargaFloatTemp*(j-1);
        String tempCTH = String.format("%.2f", tempCTHdarab);

        String pushCartNamePlusID = "cart/" + userID;

        FirebaseDatabase x = FirebaseDatabase.getInstance();

        if (j > 1){
            x.getReference(pushCartNamePlusID).child(selectedKey).child("jumlah").setValue(j-1);
            x.getReference(pushCartNamePlusID).child(selectedKey).child("cumulativeTotal").setValue(tempCTH); // get value and implement incrementation
            String TempCartPath = "cart/" + userID;

            mTotal = findViewById(R.id.totalCart);
            mCartDatabase = FirebaseDatabase.getInstance().getReference().child(TempCartPath);

            mCartDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    double sum = 0.00;

                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        Map<String, Object> map  = (Map<String, Object>) ds.getValue();
                        Object price = map.get("cumulativeTotal");
                        double pValue = Double.parseDouble(String.valueOf(price));
                        sum += pValue;
                        String tempSum = String.format("%.2f", sum);
                        mTotal.setText(tempSum);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        else {
            Toast.makeText(UserCart.this, "Logical error"  , Toast.LENGTH_SHORT).show();
        }

    }


    public void onDeleteCart(View view) {

        // nak jadikan cumulative total == 0.00 & jadikan "your cart is empty" VISIBLE
        DatabaseReference emptyRef = FirebaseDatabase.getInstance().getReference();
        emptyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild("Cart")){
                    mTextEmpty.setVisibility(View.INVISIBLE);
                    mLottieEmpty.setVisibility(View.INVISIBLE);
                }
                else {
                    mTotal.setText("0.00");
                    mTextEmpty.setVisibility(View.VISIBLE);
                    mLottieEmpty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        int pos = (int) view.getTag();

        Cart selectedItem = mCart.get(pos);
        final String selectedKey = selectedItem.getKey();

        fAuth  = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        String pushCartNamePlusID = "cart/" + userID;

        FirebaseDatabase x = FirebaseDatabase.getInstance();
        x.getReference(pushCartNamePlusID).child(selectedKey).removeValue();

        String nameTemp = selectedItem.getDestination();

        Toast.makeText(UserCart.this, nameTemp + " deleted from cart"  , Toast.LENGTH_SHORT).show();

        mTotal = findViewById(R.id.totalCart);
        mCartDatabase = FirebaseDatabase.getInstance().getReference().child(pushCartNamePlusID);

        mCartDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double sum = 0.00;

                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    Map<String, Object> map  = (Map<String, Object>) ds.getValue();
                    Object price = map.get("cumulativeTotal");
                    double pValue = Double.parseDouble(String.valueOf(price));
                    sum += pValue;
                    String tempSum = String.format("%.2f", sum);
                    mTotal.setText(tempSum);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


}
