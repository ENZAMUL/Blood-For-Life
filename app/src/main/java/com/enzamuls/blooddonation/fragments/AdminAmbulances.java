package com.enzamuls.blooddonation.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.enzamuls.blooddonation.R;
import com.enzamuls.blooddonation.adapter.AmbulanceAdapter;
import com.enzamuls.blooddonation.model.Ambulance;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AdminAmbulances extends Fragment {

    private TextView addAmbulance;
    private RecyclerView ambulanceRecycler;

    private AmbulanceAdapter ambulanceAdapter;

    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    private CollectionReference ambulanceCollectionRef;
    private String userId, saveCurrentTime, saveCurrentDate, randomName, ambulanceRandomName;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_ambulance, container, false);

        getActivity().setTitle("Manage ambulances info");

        addAmbulance = view.findViewById(R.id.addNewAmbulance);
        ambulanceRecycler = view.findViewById(R.id.ambulanceRecycleView);

        ambulanceRecycler.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        ambulanceRecycler.setLayoutManager(linearLayoutManager);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        ambulanceCollectionRef = fStore.collection("Ambulances");

        userId = mAuth.getCurrentUser().getUid();

        Calendar calForDateTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");

        saveCurrentTime = currentTime.format(calForDateTime.getTime());
        saveCurrentDate = currentDate.format(calForDateTime.getTime());

        randomName = saveCurrentDate+saveCurrentTime;

        ambulanceRandomName = userId+randomName;

        DisplayAllAmbulance();


        addAmbulance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater1 = LayoutInflater.from(getActivity());
                View view1 = inflater1.inflate(R.layout.add_ambulance_form, null);

                final EditText ambulanceCompanyName, ambulanceContact, ambulanceLocation, ambulanceDescription;
                final TextView ambulanceCancel, ambulanceAdd;
                final ProgressBar ambulanceProgress;

                ambulanceCompanyName = view1.findViewById(R.id.ambulanceCompanyName);
                ambulanceContact = view1.findViewById(R.id.ambulanceContactNo);
                ambulanceLocation = view1.findViewById(R.id.ambulanceLocation);
                ambulanceDescription = view1.findViewById(R.id.ambulanceDescription);

                ambulanceCancel = view1.findViewById(R.id.ambulanceCancel);
                ambulanceAdd = view1.findViewById(R.id.ambulanceAdd);
                ambulanceProgress = view1.findViewById(R.id.addAmbulancePBar);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(view1)
                        .setCancelable(false);

                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                ambulanceAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ambulanceProgress.setVisibility(View.VISIBLE);

                        final String ambulanceName, ambulanceContactP, ambulanceLocationP, ambulanceDescriptionP;

                        ambulanceName = ambulanceCompanyName.getText().toString();
                        ambulanceContactP = ambulanceContact.getText().toString();
                        ambulanceLocationP = ambulanceLocation.getText().toString();
                        ambulanceDescriptionP = ambulanceDescription.getText().toString();

                        if(!ambulanceName.isEmpty() || !ambulanceContactP.isEmpty() ||
                                !ambulanceLocationP.isEmpty() || !ambulanceDescriptionP.isEmpty()){

                            DocumentReference documentReference = fStore.collection("Ambulances").document(ambulanceRandomName);

                            Map<String, Object> newAmbulance = new HashMap<>();
                            newAmbulance.put("ambulanceName", ambulanceName);
                            newAmbulance.put("ambulanceContact", ambulanceContactP);
                            newAmbulance.put("ambulanceLocation", ambulanceLocationP);
                            newAmbulance.put("ambulanceDescription", ambulanceDescriptionP);

                            documentReference.set(newAmbulance).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getActivity(), "Ambulance Saved Successfully!", Toast.LENGTH_SHORT).show();
                                    ambulanceProgress.setVisibility(View.GONE);
                                    alertDialog.dismiss();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), "Ambulance Saved Failed!", Toast.LENGTH_SHORT).show();
                                    ambulanceProgress.setVisibility(View.GONE);
                                }
                            });

                        }
                    }
                });

                ambulanceCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
            }
        });

        return view;
    }

    private void DisplayAllAmbulance() {
        Query query = ambulanceCollectionRef;

        FirestoreRecyclerOptions<Ambulance> options = new FirestoreRecyclerOptions.Builder<Ambulance>()
                .setQuery(query, Ambulance.class)
                .build();
        ambulanceAdapter = new AmbulanceAdapter(options);
        ambulanceRecycler.setAdapter(ambulanceAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        ambulanceAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        ambulanceAdapter.stopListening();
    }
}
