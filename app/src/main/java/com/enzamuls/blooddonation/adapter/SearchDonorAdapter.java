package com.enzamuls.blooddonation.adapter;

import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.enzamuls.blooddonation.R;
import com.enzamuls.blooddonation.model.users;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchDonorAdapter extends FirestoreRecyclerAdapter<users, SearchDonorAdapter.SearchedDonorViewHolder> {

    private FirebaseFirestore fStore;
    private DocumentReference documentReference;
    public String userID, hasproblem;

    private OnFindDonorClickListener onFindDonorClickListener;


    public SearchDonorAdapter(@NonNull FirestoreRecyclerOptions<users> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final SearchedDonorViewHolder holder, int position, @NonNull users model) {

        if(position != RecyclerView.NO_POSITION){
            userID = getSnapshots().getSnapshot(position).getId();
        }
        Log.d("userid", userID);

        fStore = FirebaseFirestore.getInstance();
        documentReference = fStore.collection("donors").document(userID);
        if(documentReference != null){
            documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (value.getString("lastDonate").equalsIgnoreCase("Didnt donate yet")){

                        if(value.getString("hasDisease").equals("false")){
                            holder.searchStatus.setText("Available Now!");
                            holder.searchStatus.setTextColor(Color.parseColor("#00FF00"));
                        } else {
                            holder.searchStatus.setText("Can't Donate Now!");
                            holder.searchStatus.setTextColor(Color.parseColor("#FF0000"));
                        }


                    } else {


                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                            Date apreviousDonateDate = null;
                            Date calculatedDate = null;
                            Calendar calForFindNextDate = Calendar.getInstance();

                            apreviousDonateDate = sdf.parse(value.getString("lastDonate"));

                            calForFindNextDate.setTime(apreviousDonateDate);
                            calForFindNextDate.add(Calendar.DATE, 120);
                            String nextDonateDate = sdf.format(calForFindNextDate.getTime());

                            calculatedDate = sdf.parse(nextDonateDate);

                            long diff = calculatedDate.getTime() - (System.currentTimeMillis() - apreviousDonateDate.getTime()) - apreviousDonateDate.getTime();
                            long seconds = diff / 1000;
                            long minutes = seconds / 60;
                            long hours = minutes / 60;
                            long days = (hours / 24)+1;

                            if(days > 0) {
                                if(value.getString("hasDisease").equals("false")){
                                    holder.searchStatus.setText("Recently Donated!");
                                    holder.searchStatus.setTextColor(Color.parseColor("#000FFF"));
                                } else {
                                    holder.searchStatus.setText("Can't Donate Now!");
                                    holder.searchStatus.setTextColor(Color.parseColor("#FF0000"));
                                }
                            } else {
                                if(value.getString("hasDisease").equals("false")){
                                    holder.searchStatus.setText("Available Now!");
                                    holder.searchStatus.setTextColor(Color.parseColor("#00FF00"));
                                } else {
                                    holder.searchStatus.setText("Can't Donate Now!");
                                    holder.searchStatus.setTextColor(Color.parseColor("#FF0000"));
                                }
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });
        }
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference showProfileImageRef = storageReference.child(userID+".jpg");
        if(showProfileImageRef != null){
            showProfileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {

                    Picasso.get().load(uri).into(holder.searchImage);
                }
            });

        }
        holder.searchName.setText(model.getUserName());
        if(model.getHideContact().equals("false")){
            holder.searchPhone.setText(model.getContactNo());
        } else {
            holder.searchPhone.setText("Hidden");
        }
        holder.searchLocation.setText(model.getuArea()+", "+model.getuDistrict());

    }

    @NonNull
    @Override
    public SearchedDonorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.find_donor_single_view, parent,false);
        return new SearchDonorAdapter.SearchedDonorViewHolder(view);
    }

    public class SearchedDonorViewHolder extends RecyclerView.ViewHolder{

        CircleImageView searchImage;
        TextView searchName, searchPhone, searchLocation, searchStatus, searchDetails;

        public SearchedDonorViewHolder(@NonNull View itemView) {
            super(itemView);
            searchImage = itemView.findViewById(R.id.searchViewImage);
            searchName = itemView.findViewById(R.id.searchViewName);
            searchPhone = itemView.findViewById(R.id.searchViewPhone);
            searchLocation =itemView.findViewById(R.id.searchViewLocation);
            searchStatus = itemView.findViewById(R.id.searchViewStatus);
            searchDetails = itemView.findViewById(R.id.searchViewForward);

            searchDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && onFindDonorClickListener!= null){
                        onFindDonorClickListener.OnFindDonorClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }

    public interface OnFindDonorClickListener{
        void OnFindDonorClick(DocumentSnapshot documentSnapshot, int position);
    }
    public void setOnFindDonorClickListener(OnFindDonorClickListener onFindDonorClickListener){
        this.onFindDonorClickListener = onFindDonorClickListener;
    }
}
