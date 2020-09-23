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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.enzamuls.blooddonation.R;
import com.enzamuls.blooddonation.adapter.AllBloodDonorsAdapter;
import com.enzamuls.blooddonation.model.users;

public class AdminUser extends Fragment {

    private RecyclerView showAdminUserRecycler;
    private AllBloodDonorsAdapter allUserAdapter;

    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    private CollectionReference userCollectionRef;
    private String userId;

    private EditText searchUserText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_user, container, false);

        getActivity().setTitle("Manage all user");

        searchUserText = view.findViewById(R.id.searchAdminUser);
        showAdminUserRecycler = view.findViewById(R.id.showAdminUserRecycle);

        showAdminUserRecycler.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        showAdminUserRecycler.setLayoutManager(linearLayoutManager);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = mAuth.getCurrentUser().getUid();

        userCollectionRef = fStore.collection("users");

        DisplayAllUsers();

        searchUserText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(editable.toString().isEmpty()){
                    DisplayAllUsers();
                } else {
                    DisplayFilteredUsers(editable.toString());
                }

            }
        });


        return view;
    }

    private void DisplayFilteredUsers(String toString) {
        Query query = userCollectionRef.whereGreaterThanOrEqualTo("userName", toString).whereLessThanOrEqualTo("userName", toString+ "\uf8ff");

        FirestoreRecyclerOptions<users> options = new FirestoreRecyclerOptions.Builder<users>()
                .setQuery(query, users.class)
                .build();
        allUserAdapter = new AllBloodDonorsAdapter(options);
        showAdminUserRecycler.setAdapter(allUserAdapter);

        allUserAdapter.startListening();
    }

    private void DisplayAllUsers() {
        Query query = userCollectionRef;

        FirestoreRecyclerOptions<users> options = new FirestoreRecyclerOptions.Builder<users>()
                .setQuery(query, users.class)
                .build();
        allUserAdapter = new AllBloodDonorsAdapter(options);
        showAdminUserRecycler.setAdapter(allUserAdapter);

        allUserAdapter.startListening();
    }
}
