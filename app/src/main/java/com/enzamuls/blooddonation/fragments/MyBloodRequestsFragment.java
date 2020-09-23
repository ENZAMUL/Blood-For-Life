package com.enzamuls.blooddonation.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import com.enzamuls.blooddonation.R;
import com.enzamuls.blooddonation.adapter.AllBloodPostAdapter;
import com.enzamuls.blooddonation.model.requestposts;

public class MyBloodRequestsFragment extends Fragment {

    RecyclerView myallbloodRequests;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    private CollectionReference bloodPostCollectionRef;
    private AllBloodPostAdapter allBloodPostAdapter2;
    private String cuserId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_blood_request_fragment, container, false);
        myallbloodRequests = view.findViewById(R.id.myAllBloodRequestss);

        getActivity().setTitle("My All Blood Requests");


        myallbloodRequests.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        myallbloodRequests.setLayoutManager(linearLayoutManager);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        cuserId = mAuth.getCurrentUser().getUid();

        bloodPostCollectionRef = fStore.collection("requestposts");

        DisplayMyAllBloodRequestPost();

        allBloodPostAdapter2.setOnBloodPostClickListener(new AllBloodPostAdapter.OnBloodPostClickListener() {
            @Override
            public void OnBloodPostClick(DocumentSnapshot documentSnapshot, int position) {
                requestposts bloodRequestPost = documentSnapshot.toObject(requestposts.class);
                String uniquePostId = documentSnapshot.getId();

                Bundle bundle = new Bundle();
                bundle.putString("pdetailUserNames", bloodRequestPost.getPostUserName());
                bundle.putString("pdetailUserIDs", bloodRequestPost.getPostUserID());

                bundle.putString("pdetailRequestFors", bloodRequestPost.getPostPatientBloodGroup());
                bundle.putString("pdetailPostTimes", bloodRequestPost.getPostTime());
                bundle.putString("pdetailPostDates", bloodRequestPost.getPostDate());
                bundle.putString("pdetailPatientNames", bloodRequestPost.getPostPatientName());
                bundle.putString("getPdetailPatientAges", bloodRequestPost.getPostPatientAge());
                bundle.putString("pdetailPatientBloodGroups", bloodRequestPost.getPostPatientBloodGroup());
                bundle.putString("pdetailPatientProblems", bloodRequestPost.getPostPatientProblem());
                bundle.putString("pdetailPatientBloodNeedBagss", bloodRequestPost.getPostBloodBagsNeeded());
                bundle.putString("pdetailPatientBloodNeedDates", bloodRequestPost.getPostPatientBloodNeedDate());
                bundle.putString("pdetailPatientHospitalNames", bloodRequestPost.getPostPatientName());
                bundle.putString("pdetailPatientLocations", bloodRequestPost.getPostPatientLocation());
                bundle.putString("pdetailContactPersons", bloodRequestPost.getPostContactPersonName());
                bundle.putString("pdetailContactPersonPhones", bloodRequestPost.getPostContactPersonPhone());
                bundle.putString("pdetailStatuss", bloodRequestPost.getStatus());

                bundle.putString("uniqueBloodPostID", uniquePostId);


                EditDeleteBloodRequestPostFragment editDeleteBloodRequestPostFragment = new EditDeleteBloodRequestPostFragment();

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                editDeleteBloodRequestPostFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.container_fragment, editDeleteBloodRequestPostFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }

            @Override
            public void onCallButtonClick(DocumentSnapshot documentSnapshot) {
                requestposts bloodRequestPost = documentSnapshot.toObject(requestposts.class);
                final String ContactPersonPhone = bloodRequestPost.getPostContactPersonPhone();
                final String status = bloodRequestPost.getStatus();

                if (status.equalsIgnoreCase("Managed!")) {
                    Toast.makeText(getActivity(), "This request is managed already! Thanks", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    builder.setMessage("Are you sure want to call?")
                            .setCancelable(false)
                            .setTitle("Call to the contact person")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    String phoneNo = "tel:" + ContactPersonPhone;
                                    Intent intent = new Intent(Intent.ACTION_CALL);
                                    intent.setData(Uri.parse(phoneNo));
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        });

        return view;

    }

    private void DisplayMyAllBloodRequestPost() {
        Query query = bloodPostCollectionRef.whereEqualTo("postUserID", cuserId);

        FirestoreRecyclerOptions<requestposts> options = new FirestoreRecyclerOptions.Builder<requestposts>()
                .setQuery(query, requestposts.class)
                .build();
        allBloodPostAdapter2 = new AllBloodPostAdapter(options);
        myallbloodRequests.setAdapter(allBloodPostAdapter2);
    }

    @Override
    public void onStart() {
        super.onStart();
        allBloodPostAdapter2.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        allBloodPostAdapter2.stopListening();
    }
}
