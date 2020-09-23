package com.enzamuls.blooddonation.adapter;

import android.graphics.Color;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.enzamuls.blooddonation.R;
import com.enzamuls.blooddonation.model.users;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AllBloodDonorsAdapter extends FirestoreRecyclerAdapter<users, AllBloodDonorsAdapter.DonorViewHolder> {

    private OnDonorClickListener onDonorClickListener;

    private FirebaseFirestore fStore;
    private DocumentReference documentReference;
    public String userID, hasproblem, donorsts;

    public AllBloodDonorsAdapter(@NonNull FirestoreRecyclerOptions<users> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final DonorViewHolder holder, int position, @NonNull users model) {

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
                            holder.allbdShowStatus.setText("Available Now!");
                            holder.allbdShowStatus.setTextColor(Color.parseColor("#00FF00"));
                        } else {
                            holder.allbdShowStatus.setText("Can't Donate Now!");
                            holder.allbdShowStatus.setTextColor(Color.parseColor("#FF0000"));
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
                                    holder.allbdShowStatus.setText("Recently Donated!");
                                    holder.allbdShowStatus.setTextColor(Color.parseColor("#000FFF"));
                                } else {
                                    holder.allbdShowStatus.setText("Can't Donate Now!");
                                    holder.allbdShowStatus.setTextColor(Color.parseColor("#FF0000"));
                                }
                            } else {
                                if(value.getString("hasDisease").equals("false")){
                                    holder.allbdShowStatus.setText("Available Now!");
                                    holder.allbdShowStatus.setTextColor(Color.parseColor("#00FF00"));
                                } else {
                                    holder.allbdShowStatus.setText("Can't Donate Now!");
                                    holder.allbdShowStatus.setTextColor(Color.parseColor("#FF0000"));
                                }
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });
        }


        holder.allbdShowBloodGroup.setText(model.getBloodGroup());
        holder.allbdShowName.setText(model.getUserName());
        if(model.getHideContact().equals("false")){
            holder.allbdShowPhone.setText(model.getContactNo());
        } else {
            holder.allbdShowPhone.setText("Hidden");
        }
        holder.allbdShowLocation.setText(model.getuArea()+", "+model.getuDistrict());

    }


    @NonNull
    @Override
    public DonorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_donors_view_single, parent,false);
        return new DonorViewHolder(view);
    }

    public class DonorViewHolder extends RecyclerView.ViewHolder {

        TextView allbdShowBloodGroup, allbdShowName, allbdShowPhone, allbdShowLocation, allbdShowStatus, allbdShowDetails;
        public DonorViewHolder(@NonNull View itemView) {
            super(itemView);
            allbdShowBloodGroup = itemView.findViewById(R.id.allbdShowBloodGroup);
            allbdShowName = itemView.findViewById(R.id.allbdShowName);
            allbdShowPhone  = itemView.findViewById(R.id.allbdShowPhone);
            allbdShowLocation = itemView.findViewById(R.id.allbdShowLocation);
            allbdShowStatus = itemView.findViewById(R.id.allbdShowStatus);
            allbdShowDetails = itemView.findViewById(R.id.allbdShowDetails);

            allbdShowDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && onDonorClickListener!= null){
                        onDonorClickListener.OnDonorClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }

    public interface OnDonorClickListener{
        void OnDonorClick(DocumentSnapshot documentSnapshot, int position);
    }
    public void setOnDonorClickListener(AllBloodDonorsAdapter.OnDonorClickListener onDonorClickListener){
        this.onDonorClickListener = onDonorClickListener;
    }
}
