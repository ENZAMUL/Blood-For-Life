package com.enzamuls.blooddonation.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.enzamuls.blooddonation.R;
import com.enzamuls.blooddonation.model.Ambulance;

public class AmbulanceAdapter extends FirestoreRecyclerAdapter<Ambulance, AmbulanceAdapter.AmbulanceViewHolder> {


    public AmbulanceAdapter(@NonNull FirestoreRecyclerOptions<Ambulance> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull AmbulanceViewHolder holder, int position, @NonNull Ambulance model) {
        holder.ambulanceName.setText(model.getAmbulanceName());
        holder.ambulanceContact.setText(model.getAmbulanceContact());
        holder.ambulanceLocation.setText(model.getAmbulanceLocation());
    }

    @NonNull
    @Override
    public AmbulanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_ambulance_single, parent,false);
        return new AmbulanceViewHolder(view);
    }

    public class AmbulanceViewHolder extends RecyclerView.ViewHolder{

        TextView ambulanceName, ambulanceContact, ambulanceLocation, ambulanceDescription;

        public AmbulanceViewHolder(@NonNull View itemView) {
            super(itemView);

            ambulanceName = itemView.findViewById(R.id.oneAmbulanceName);
            ambulanceContact = itemView.findViewById(R.id.oneAmbulanceContact);
            ambulanceLocation = itemView.findViewById(R.id.oneAmbulanceLocation);
        }
    }
}
