package com.enzamuls.blooddonation.fragments;

import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.enzamuls.blooddonation.adapter.AllBloodPostAdapter;
import com.enzamuls.blooddonation.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditDeleteBloodRequestPostFragment extends Fragment {

    CircleImageView pdetailImageShow;
    TextView pdetailUserName, pdetailRequestFor, pdetailPostTime, pdetailPostDate, pdetailPatientName, getPdetailPatientAge,
            pdetailPatientBloodGroup, pdetailPatientProblem, pdetailPatientBloodNeedBags, pdetailPatientBloodNeedDate,
            pdetailPatientHospitalName, pdetailPatientLocation, pdetailContactPerson, pdetailContactPersonPhone,
            pdetailStatus;
    Button btnUpdateDetails, btnDeleteDetails, btnManagedBlood;

    String currentUserID, currentPostID;

    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    private StorageReference storageReference;
    private String currUserId;

    private AllBloodPostAdapter allBloodPostAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_patient_details_layout, container, false);

        getActivity().setTitle("Update or Delete Your Post!");

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        currUserId = mAuth.getUid();

        pdetailImageShow = view.findViewById(R.id.pdetailsImageView);
        pdetailUserName = view.findViewById(R.id.pdetailsUserName);
        pdetailRequestFor = view.findViewById(R.id.pdetailsRequestFor);
        pdetailPostTime = view.findViewById(R.id.pdetailsTime);
        pdetailPostDate  = view.findViewById(R.id.pdetailsDate);
        pdetailPatientName  = view.findViewById(R.id.pdetailsPatientName);
        getPdetailPatientAge  = view.findViewById(R.id.pdetailsPatientAge);
        pdetailPatientBloodGroup  = view.findViewById(R.id.pdetailsPatientBloodGroup);
        pdetailPatientProblem  = view.findViewById(R.id.pdetailsPatientProblem);
        pdetailPatientBloodNeedBags  = view.findViewById(R.id.pdetailsPatientNeedBloodBags);
        pdetailPatientBloodNeedDate  = view.findViewById(R.id.pdetailsPatientBloodNeedDate);
        pdetailPatientHospitalName  = view.findViewById(R.id.pdetailsPatientHospitalName);
        pdetailPatientLocation  = view.findViewById(R.id.pdetailsPatientLocation);
        pdetailContactPerson  = view.findViewById(R.id.pdetailsContactPerson);
        pdetailContactPersonPhone  = view.findViewById(R.id.pdetailsContactPersonPhone);
        pdetailStatus  = view.findViewById(R.id.pdetailsStatus);

        btnUpdateDetails  = view.findViewById(R.id.pdetailsUpdatePost);
        btnDeleteDetails  = view.findViewById(R.id.pdetailsDeletePost);
        btnManagedBlood   = view.findViewById(R.id.pdetailsManagedBlood);

        pdetailUserName.setText(getArguments().getString("pdetailUserNames"));
        pdetailRequestFor.setText("Request For: "+getArguments().getString("pdetailRequestFors"));
        pdetailPostTime.setText("Time: "+getArguments().getString("pdetailPostTimes"));
        pdetailPostDate.setText(getArguments().getString("pdetailPostDates"));
        pdetailPatientName.setText("Name: "+getArguments().getString("pdetailPatientNames"));
        getPdetailPatientAge.setText("Age: "+getArguments().getString("getPdetailPatientAges"));
        pdetailPatientBloodGroup.setText("Blood Group: "+getArguments().getString("pdetailPatientBloodGroups"));
        pdetailPatientProblem.setText("Problem Details: "+getArguments().getString("pdetailPatientProblems"));
        pdetailPatientBloodNeedBags.setText("Blood Need: "+getArguments().getString("pdetailPatientBloodNeedBagss")+" Bags");
        pdetailPatientBloodNeedDate.setText("Need On: "+getArguments().getString("pdetailPatientBloodNeedDates"));
        pdetailPatientHospitalName.setText("Hospital: "+getArguments().getString("pdetailPatientHospitalNames"));
        pdetailPatientLocation.setText("Location: "+getArguments().getString("pdetailPatientLocations"));
        pdetailContactPerson.setText("Contact Person: "+getArguments().getString("pdetailContactPersons"));
        pdetailContactPersonPhone.setText("Phone: "+getArguments().getString("pdetailContactPersonPhones"));
        pdetailStatus.setText("Current Status: "+getArguments().getString("pdetailStatuss"));

        if(getArguments().getString("pdetailStatuss").equals("Managed!")){
            pdetailStatus.setTextColor(Color.parseColor("#00FF00"));
        }

        currentPostID = getArguments().getString("uniqueBloodPostID");

        currentUserID = getArguments().getString("pdetailUserIDs");

        if(!currUserId.equals(currentUserID)){
            btnUpdateDetails.setVisibility(View.GONE);
            btnDeleteDetails.setVisibility(View.GONE);
            btnManagedBlood.setVisibility(View.GONE);
        }

        btnDeleteDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setMessage("Are you sure want to delete?")
                        .setCancelable(false)
                        .setTitle("Delete Post")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                DocumentReference documentReferenceed = fStore.collection("requestposts").document(currentPostID);
                                documentReferenceed.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                        fragmentTransaction.replace(R.id.container_fragment, new HomeFragment());
                                        fragmentTransaction.addToBackStack(null);
                                        fragmentTransaction.commit();
                                        Toast.makeText(getActivity(), "Post Deleted Successfully!!", Toast.LENGTH_SHORT ).show();
                                    }
                                });
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
        });

        btnManagedBlood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference documentReferenceed = fStore.collection("requestposts").document(currentPostID);
                Map<String, Object> editedManagedBloodPost = new HashMap<>();
                editedManagedBloodPost.put("status", "Managed!");

                documentReferenceed.update(editedManagedBloodPost).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.container_fragment, new HomeFragment());
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        Toast.makeText(getActivity(), "Post Updated to Managed Successfully!!", Toast.LENGTH_SHORT ).show();
                    }
                });
            }
        });

        StorageReference showProfileImageRef = storageReference.child(currentUserID+".jpg");
        if(showProfileImageRef != null){
            showProfileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(pdetailImageShow);
                }
            });

        }

        btnUpdateDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("currentBloodPostID", currentPostID);

                EditUpdateBloodRequestPost editUpdateBloodRequestPost = new EditUpdateBloodRequestPost();

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                editUpdateBloodRequestPost.setArguments(bundle);
                fragmentTransaction.replace(R.id.container_fragment, editUpdateBloodRequestPost);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


        return view;
    }
}
