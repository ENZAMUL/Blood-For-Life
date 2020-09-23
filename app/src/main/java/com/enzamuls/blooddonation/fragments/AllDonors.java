package com.enzamuls.blooddonation.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.enzamuls.blooddonation.adapter.AllBloodDonorsAdapter;
import com.enzamuls.blooddonation.R;
import com.enzamuls.blooddonation.model.users;

public class AllDonors extends Fragment {

    private RecyclerView showAllDonorsRecycler;
    private AllBloodDonorsAdapter allBloodDonorsAdapter;

    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    private StorageReference storageReference;
    private CollectionReference userCollectionRef;
    private String userId;

    private EditText searchText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.all_donors_fragment, container, false);

        searchText = view.findViewById(R.id.searchFromAllDonor);

        showAllDonorsRecycler = view.findViewById(R.id.showAllDonorsRecycle);
        showAllDonorsRecycler.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        showAllDonorsRecycler.setLayoutManager(linearLayoutManager);

        getActivity().setTitle("All Blood Heros!");

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        userId = mAuth.getCurrentUser().getUid();

        userCollectionRef = fStore.collection("users");

        DisplayAllBloodDonors();

            searchText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                    public void afterTextChanged(Editable editable) {

                        if(editable.toString().isEmpty()){
                            DisplayAllBloodDonors();
                        } else {
                            DisplayFilteredBloodDonors(editable.toString());
                        }

                    }
            });



        return view;
    }

    private void DisplayFilteredBloodDonors(String bloodGroup) {
        Query query = userCollectionRef.whereEqualTo("bloodGroup", bloodGroup.toUpperCase());

        FirestoreRecyclerOptions<users> options = new FirestoreRecyclerOptions.Builder<users>()
                .setQuery(query, users.class)
                .build();
        allBloodDonorsAdapter = new AllBloodDonorsAdapter(options);
        showAllDonorsRecycler.setAdapter(allBloodDonorsAdapter);

        allBloodDonorsAdapter.startListening();

        allBloodDonorsAdapter.setOnDonorClickListener(new AllBloodDonorsAdapter.OnDonorClickListener() {
            @Override
            public void OnDonorClick(DocumentSnapshot documentSnapshot, int position) {
                String uniqueUserId = documentSnapshot.getId();

                Bundle bundle = new Bundle();
                bundle.putString("uniUserID", uniqueUserId);

                ShowDonorsDetail showDonorsDetail = new ShowDonorsDetail();

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                showDonorsDetail.setArguments(bundle);
                fragmentTransaction.replace(R.id.container_fragment, showDonorsDetail);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });
    }

    private void DisplayAllBloodDonors() {
        Query query = userCollectionRef;

        FirestoreRecyclerOptions<users> options = new FirestoreRecyclerOptions.Builder<users>()
                .setQuery(query, users.class)
                .build();
        allBloodDonorsAdapter = new AllBloodDonorsAdapter(options);
        showAllDonorsRecycler.setAdapter(allBloodDonorsAdapter);

        allBloodDonorsAdapter.startListening();

        allBloodDonorsAdapter.setOnDonorClickListener(new AllBloodDonorsAdapter.OnDonorClickListener() {
            @Override
            public void OnDonorClick(DocumentSnapshot documentSnapshot, int position) {
                String uniqueUserId = documentSnapshot.getId();

                Bundle bundle = new Bundle();
                bundle.putString("uniUserID", uniqueUserId);

                ShowDonorsDetail showDonorsDetail = new ShowDonorsDetail();

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                showDonorsDetail.setArguments(bundle);
                fragmentTransaction.replace(R.id.container_fragment, showDonorsDetail);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });
    }

}
