package com.enzamuls.blooddonation.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.enzamuls.blooddonation.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ShowDonorsDetail extends Fragment {

    private String currUserID, donorsts, phoneNo, hidePhone, donEmail,donBgrp;

    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    private StorageReference storageReference;
    private Uri resultImageUri;

    private ImageView donorImage;
    private TextView donorName, donorBloodGroup, donorTotalDonate, donorLastDateDonate, donorNextDonateLeft, donorStatus,
                    donorEmail, donorPhone, donorDOB, donorGender, donorArea, donorThana, donorDistrict, donorDivision,
                    donorSendEmail, donorMakeCall, bloodHero;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_donors_details_frag, container, false);

        donorImage = view.findViewById(R.id.donorImage);

        donorName = view.findViewById(R.id.donorName);
        donorBloodGroup = view.findViewById(R.id.donorBloodGroup);
        donorTotalDonate = view.findViewById(R.id.donorTotalDonate);
        donorLastDateDonate = view.findViewById(R.id.donorLastDonate);
        donorNextDonateLeft = view.findViewById(R.id.donorNextDonateLeft);
        donorStatus = view.findViewById(R.id.donorStatus);
        donorEmail = view.findViewById(R.id.donorEmail);
        donorPhone = view.findViewById(R.id.donorPhone);
        donorDOB = view.findViewById(R.id.donorBirthDate);
        donorGender = view.findViewById(R.id.donorGender);
        donorArea = view.findViewById(R.id.donorArea);
        donorThana = view.findViewById(R.id.donorThana);
        donorDistrict = view.findViewById(R.id.donorDistric);
        donorDivision = view.findViewById(R.id.donorDivision);
        bloodHero = view.findViewById(R.id.bloodherro);

        donorSendEmail = view.findViewById(R.id.donorSendEmail);
        donorMakeCall = view.findViewById(R.id.donorMakeCall);

        getActivity().setTitle("Blood Hero's Details!");

        currUserID = getArguments().getString("uniUserID");
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        DocumentReference documentReference = fStore.collection("users").document(currUserID);
        DocumentReference documentReference2 = fStore.collection("donors").document(currUserID);

        documentReference2.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                donorTotalDonate.setText("Donated "+String.valueOf(value.getLong("totalDonate").intValue())+" times");

                if (value.getString("lastDonate").equalsIgnoreCase("Didnt donate yet")){

                    if(value.getString("hasDisease").equals("false")){
                        donorStatus.setText("Available Now!");
                    } else {
                        donorStatus.setText("Can't Donate Now!");
                    }


                    donorLastDateDonate.setText("Donated: "+value.getString("lastDonate"));

                } else {

                    donorLastDateDonate.setText("Donated: "+value.getString("lastDonate"));

                    if(value.getString("hasDisease").equals("false")){
                        donorsts = "false";
                    } else {
                        donorsts ="true";
                    }

                    try {
                        setNextDonateDateAndDay(value.getString("lastDonate"));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        documentReference.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                donorName.setText(value.getString("userName"));
                donorBloodGroup.setText("Blood Group: "+value.getString("bloodGroup"));
                donBgrp = value.getString("bloodGroup");
                donorEmail.setText(value.getString("email"));
                donEmail = value.getString("email");
                if(value.getString("hideContact").equals("true")){
                    donorPhone.setText("Hidden");
                } else {
                    donorPhone.setText(value.getString("contactNo"));
                }

                phoneNo = value.getString("contactNo");
                hidePhone = value.getString("hideContact");

                donorDOB.setText(value.getString("birthDate"));
                donorGender.setText(value.getString("gender"));
                donorArea.setText(value.getString("uArea")+",");
                donorThana.setText(value.getString("policeStation")+",");
                donorDistrict.setText(value.getString("uDistrict")+",");
                donorDivision.setText(value.getString("uDivision"));
                if(value.getString("isDonor").equals("true")){
                    bloodHero.setVisibility(View.GONE);
                } else {
                    bloodHero.setText("Is not a Donor Yet!!");
                }
            }
        });

        StorageReference showProfileImageRef = storageReference.child(currUserID+".jpg");
        showProfileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(donorImage);
                }
            }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "He/She not upload any Photo!", Toast.LENGTH_SHORT ).show();
            }
        });

        donorMakeCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hidePhone.equalsIgnoreCase("true")){
                    Toast.makeText(getActivity(), "This donor hide his/her number! Please Contact an admin", Toast.LENGTH_SHORT).show();
                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    builder.setMessage("Are you sure want to call?")
                            .setCancelable(false)
                            .setTitle("Call to the Donor")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    String phoneNoDonor = "tel:" + phoneNo;
                                    Intent intent = new Intent(Intent.ACTION_CALL);
                                    intent.setData(Uri.parse(phoneNoDonor));
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

        donorSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{donEmail});
                i.putExtra(Intent.EXTRA_SUBJECT, "Request For Blood");
                i.putExtra(Intent.EXTRA_TEXT   , "I need "+ donBgrp + " Blood Emergency! If you can donate, Please help.");
                try {
                    startActivity(Intent.createChooser(i, "Choose email..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getActivity(), "No Email Chooser Found!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }

    private void setNextDonateDateAndDay(String lastDonateDate) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date apreviousDonateDate = null;
        Date calculatedDate = null;
        Calendar calForFindNextDate = Calendar.getInstance();

        apreviousDonateDate = sdf.parse(lastDonateDate);

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
            if(donorsts == "false"){
                donorStatus.setText("Recently Donated!");
                donorNextDonateLeft.setText("Next Donate Left " + String.valueOf(days) + " days");
            } else {
                donorStatus.setText("Problem! Can't Donate!");
                donorNextDonateLeft.setText("Can't Donate Now!");
            }

        } else {
            if(donorsts == "false"){
                donorStatus.setText("Estimated Date was "+ nextDonateDate);
                donorNextDonateLeft.setText("Available now!");
            } else {
                donorStatus.setText("Problem! Can't Donate!");
                donorNextDonateLeft.setText("Can't Donate Now!");
            }

        }

    }
}
