package com.example.psm.user;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.psm.PesananDetails;
import com.example.psm.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;


public class UserOrderActiveAdapter extends RecyclerView.Adapter<UserOrderActiveAdapter.ImageViewHolder> {
    private Context mContext;
    private List<PesananDetails> mPesananDetails;

    String userID;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    public UserOrderActiveAdapter(Context context, List<PesananDetails> pesananDetails) {
        mContext = context;
        mPesananDetails = pesananDetails;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.user_order_adapter, parent, false);
        return new ImageViewHolder(v);



    }

    @Override
    public void onBindViewHolder(final ImageViewHolder holder , int pos) {

        holder.mSeeDetails.setTag(pos); // set and store unique position of button

        fAuth  = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        PesananDetails uploadCurrent = mPesananDetails.get(pos);

        holder.yOrderID.setText("ID pesanan: " + uploadCurrent.getOrderID()); //nak retrieve nama dari database
        holder.yDateOrder.setText("Masa pesanan: " + uploadCurrent.getOrderDate() + " (" + uploadCurrent.getOrderTime() + ")" ); //nak retrieve harga dari database
        holder.yStatus.setText("Status: " + uploadCurrent.getStatus());
        if(uploadCurrent.getStatus().equals("Pending")){
            holder.ylottieStatus.setAnimation("pending.json");
        }
        else if(uploadCurrent.getStatus().equals("Cooking")){
            holder.ylottieStatus.setAnimation("cooking.json");
        }
        else if(uploadCurrent.getStatus().equals("Sedang dihantar")){
            holder.ylottieStatus.setAnimation("delivery.json");
        }

    }

    @Override
    public int getItemCount() {

        return mPesananDetails.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        public TextView yOrderID;
        public TextView yDateOrder;
        public TextView yStatus;
        public LottieAnimationView ylottieStatus;

        CardView mSeeDetails;


        public ImageViewHolder(View itemView) {
            super(itemView);



            yOrderID = itemView.findViewById(R.id.xOrderID);
            yDateOrder = itemView.findViewById(R.id.xDate);
            yStatus = itemView.findViewById(R.id.xStatus);
            ylottieStatus = itemView.findViewById(R.id.lottieStatus);

            mSeeDetails = itemView.findViewById(R.id.seeDetails);



        }

    }




}