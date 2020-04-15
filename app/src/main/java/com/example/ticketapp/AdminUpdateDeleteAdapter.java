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


public class AdminUpdateDeleteAdapter extends RecyclerView.Adapter<AdminUpdateDeleteAdapter.ImageViewHolder> {
    private Context mContext;
    private List<Upload> mUpload;

    String userID;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    public AdminUpdateDeleteAdapter(Context context, List<Upload> uploads) {
        mContext = context;
        mUpload = uploads;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.admin_update_delete_adapter, parent, false);
        return new ImageViewHolder(v);

    }

    @Override
    public void onBindViewHolder(final ImageViewHolder holder , int pos) {

        holder.mdeleteTicket.setTag(pos); // set and store unique position of button
        fAuth  = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        Upload uploadCurrent = mUpload.get(pos);
        String gambarmenu = uploadCurrent.getImageUrl();

        holder.yDestination.setText("Destination: " + uploadCurrent.getDestination()); //nak retrieve nama dari database
        holder.yMasa.setText("Departure time: " + uploadCurrent.getTime()); //nak retrieve nama dari database
        holder.yHarga.setText("Price: RM " + uploadCurrent.getHarga()); //nak retrieve harga dari database
        Picasso.get()
                .load(gambarmenu)
                .placeholder(R.drawable.placeholder)
                .fit()
                .centerCrop()
                .into(holder.yGambar);
    }

    @Override
    public int getItemCount() {

        return mUpload.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        public TextView yDestination;
        public TextView yMasa;
        public TextView yHarga;
        public ImageView yGambar;
        Button mdeleteTicket;

        public ImageViewHolder(View itemView) {
            super(itemView);

            yDestination = itemView.findViewById(R.id.xDestination);
            yMasa = itemView.findViewById(R.id.xMasa);
            yHarga = itemView.findViewById(R.id.xHarga);
            yGambar = itemView.findViewById(R.id.xGambar);

            mdeleteTicket = itemView.findViewById(R.id.deleteTicket);


        }

    }




}