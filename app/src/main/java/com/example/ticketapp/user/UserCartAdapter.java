package com.example.ticketapp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;


public class UserCartAdapter extends RecyclerView.Adapter<UserCartAdapter.ImageViewHolder> {
    private Context mContext;
    private List<Cart> mCart;

    String userID;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    public UserCartAdapter(Context context, List<Cart> carts) {
        mContext = context;
        mCart = carts;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.user_cart_adapter, parent, false);
        return new ImageViewHolder(v);

    }

    @Override
    public void onBindViewHolder(final ImageViewHolder holder , int pos) {

        holder.mDeleteCartItem.setTag(pos); // set and store unique position of button
        holder.mIncrease.setTag(pos); // set and store unique position of button
        holder.mDecrease.setTag(pos); // set and store unique position of button

        fAuth  = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        Cart uploadCurrent = mCart.get(pos);
        String gambarmenu = uploadCurrent.getImageUrl();

        holder.yNama.setText(uploadCurrent.getDestination()); //nak retrieve nama dari database
        holder.yHarga.setText("RM " + uploadCurrent.getHarga()); //nak retrieve harga dari database
        holder.yJumlah.setText(uploadCurrent.getJumlah().toString());
        Picasso.get()
                .load(gambarmenu)
                .placeholder(R.drawable.default_placeholder)
                .fit()
                .centerCrop()
                .into(holder.yGambar);
    }

    @Override
    public int getItemCount() {

        return mCart.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public TextView yNama;
        public TextView yHarga;
        public ImageView yGambar;
        public TextView yJumlah;
        Button mDeleteCartItem;
        Button mIncrease;
        Button mDecrease;

        public ImageViewHolder(View itemView) {
            super(itemView);



            yNama = itemView.findViewById(R.id.xNama);
            yHarga = itemView.findViewById(R.id.xHarga);
            yGambar = itemView.findViewById(R.id.xGambar);
            yJumlah = itemView.findViewById(R.id.integer_number);
            mDeleteCartItem = itemView.findViewById(R.id.deleteCartItem);
            mIncrease = itemView.findViewById(R.id.increase);
            mDecrease = itemView.findViewById(R.id.decrease);



        }

    }




}