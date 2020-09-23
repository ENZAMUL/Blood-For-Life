package com.enzamuls.blooddonation.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.enzamuls.blooddonation.R;
import com.enzamuls.blooddonation.adapter.AmbulanceAdapter;
import com.enzamuls.blooddonation.model.Ambulance;

public class AmbulanceFragment extends Fragment {

    private RecyclerView ambulanceRecycler;

    private AmbulanceAdapter ambulanceAdapter;

    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    private CollectionReference ambulanceCollectionRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ambulance_fragment, container, false);

        getActivity().setTitle("Ambulance Services");

        ambulanceRecycler = view.findViewById(R.id.showAmbulanceuser);

        ambulanceRecycler.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        ambulanceRecycler.setLayoutManager(linearLayoutManager);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        ambulanceCollectionRef = fStore.collection("Ambulances");

        DisplayAllAmbulanceUser();

        return view;

    }

    private void DisplayAllAmbulanceUser() {
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
