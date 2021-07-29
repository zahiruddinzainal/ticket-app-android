package com.example.psm.user;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.psm.Pesanan;
import com.example.psm.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;


public class UserRecyclerOrderAdapter extends RecyclerView.Adapter<UserRecyclerOrderAdapter.ImageViewHolder> {
    private Context mContext;
    private List<Pesanan> mPesanan;

    String userID;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    public UserRecyclerOrderAdapter(Context context, List<Pesanan> pesanans) {
        mContext = context;
        mPesanan = pesanans;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.user_recycler_order_adapter, parent, false);
        return new ImageViewHolder(v);



    }

    @Override
    public void onBindViewHolder(final ImageViewHolder holder , int pos) {


        fAuth  = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        Pesanan uploadCurrent = mPesanan.get(pos);
        String gambarmenu = uploadCurrent.getImageUrl();

        holder.yNama.setText(uploadCurrent.getName()); //nak retrieve nama dari database
        holder.yHarga.setText("RM " + uploadCurrent.getHarga()); //nak retrieve harga dari database
        holder.yJumlah.setText("X " + uploadCurrent.getJumlah().toString());
          Picasso.get()
                .load(gambarmenu)
                .placeholder(R.drawable.default_placeholder)
                .fit()
                .centerCrop()
                .into(holder.yGambar);


    }

    @Override
    public int getItemCount() {

        return mPesanan.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public TextView yNama;
        public TextView yHarga;
        public ImageView yGambar;
        public TextView yJumlah;


        public ImageViewHolder(View itemView) {
            super(itemView);



            yNama = itemView.findViewById(R.id.xNama);
            yHarga = itemView.findViewById(R.id.xHarga);
            yGambar = itemView.findViewById(R.id.xGambar);
            yJumlah = itemView.findViewById(R.id.xJumlah);

        }

    }




}