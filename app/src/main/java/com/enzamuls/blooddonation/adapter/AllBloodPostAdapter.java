package com.enzamuls.blooddonation.adapter;

import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.enzamuls.blooddonation.R;
import com.enzamuls.blooddonation.model.requestposts;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllBloodPostAdapter extends FirestoreRecyclerAdapter<requestposts, AllBloodPostAdapter.bloosPostsViewHolder> {

    String BloodRequestPostKey;
    private OnBloodPostClickListener onBloodPostClickListener;

    public AllBloodPostAdapter(@NonNull FirestoreRecyclerOptions<requestposts> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull final bloosPostsViewHolder holder, final int position, @NonNull requestposts model) {

        holder.adUserName.setText(model.getPostUserName());
        holder.adUserRequestFor.setText("Request For "+model.getPostPatientBloodGroup() +" Blood");
        holder.adPostTime.setText("Time: "+model.getPostTime());
        holder.adPostDate.setText(model.getPostDate());
        holder.adPatientName.setText("Patient Name: "+model.getPostPatientName());
        holder.adPatientBloodGroup.setText("Blood Group: "+model.getPostPatientBloodGroup());
        holder.adPatientBloodNeed.setText(" Need: "+model.getPostBloodBagsNeeded()+" Bags!");
        holder.adPatientLocation.setText("Location: "+model.getPostPatientLocation());
        if (model.getStatus().equalsIgnoreCase("Managed!")) {
            holder.adManageUpdate.setText(model.getStatus());
            holder.adManageUpdate.setTextColor(Color.parseColor("#00FF00"));;
        } else {
            holder.adManageUpdate.setText(model.getStatus());
            holder.adManageUpdate.setTextColor(Color.parseColor("#FF0000"));;
        }



        holder.adClickForPatientDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(position != RecyclerView.NO_POSITION && onBloodPostClickListener!= null){
                    onBloodPostClickListener.OnBloodPostClick(getSnapshots().getSnapshot(position), position);
                }

            }
        });

        holder.btnBloodPostCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position != RecyclerView.NO_POSITION && onBloodPostClickListener!= null){
                    onBloodPostClickListener.onCallButtonClick(getSnapshots().getSnapshot(position));
                }
            }
        });



        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference showProfileImageRef = storageReference.child(model.getPostUserID()+".jpg");
        if(showProfileImageRef != null){
            showProfileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {

                    Picasso.get().load(uri).into(holder.adImageView);
                }
            });

        }


    }

    @NonNull
    @Override
    public bloosPostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_blood_requestpost_layout, parent,false);
        return new bloosPostsViewHolder(view);
    }

    public class bloosPostsViewHolder extends RecyclerView.ViewHolder {

        CircleImageView adImageView;
        TextView adUserName, adUserRequestFor, adPostTime, adPostDate, adPatientName, adPatientBloodGroup, adPatientBloodNeed,
                    adPatientLocation, adManageUpdate, adClickForPatientDetails, btnBloodPostCall;

        public bloosPostsViewHolder(@NonNull View itemView) {
            super(itemView);
            adImageView = itemView.findViewById(R.id.recycleShowImage);
            adUserName = itemView.findViewById(R.id.recycleUserName);
            adUserRequestFor = itemView.findViewById(R.id.recycleRequestFor);
            adPostTime  = itemView.findViewById(R.id.recyclePostTime);
            adPostDate = itemView.findViewById(R.id.recyclePostDate);
            adPatientName = itemView.findViewById(R.id.recyclePatientName);
            adPatientBloodGroup  = itemView.findViewById(R.id.recyclePatientBloodGroup);
            adPatientBloodNeed = itemView.findViewById(R.id.recyclePatientBloodBagNeeds);
            adPatientLocation = itemView.findViewById(R.id.recyclePatientLocation);
            adManageUpdate = itemView.findViewById(R.id.recycleManageUpdate);
            adClickForPatientDetails  = itemView.findViewById(R.id.recyleClickForDetails);
            btnBloodPostCall = itemView.findViewById(R.id.bloodPostPhoneButton);
        }
    }

    public interface OnBloodPostClickListener{
        void OnBloodPostClick(DocumentSnapshot documentSnapshot, int position);
        void onCallButtonClick(DocumentSnapshot documentSnapshot);
    }
    public void setOnBloodPostClickListener(OnBloodPostClickListener onBloodPostClickListener){
        this.onBloodPostClickListener = onBloodPostClickListener;
    }
}
