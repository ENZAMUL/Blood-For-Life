package com.enzamuls.blooddonation.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.enzamuls.blooddonation.R;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {

    TextView showName, showBloodGroup, showLeftDonate, showLastDonate, showTotalDonate, showDisease, showBeDonor,
            showEmail, showContact, showBirthDate, showGender, showArea, showThana, showDistrict, showDivision,
            editProfileImage ;

    Button editProfile, changePassword;
    CircleImageView circleProfileImageView;
    private ProgressBar progressBarP;

    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    private StorageReference storageReference;
    private FirebaseUser currentLogedUser;

    private String userId, donor;
    private Uri resultImageUri;

    final static int Gallery_Pick = 1;

    private onProfileFragmentBtnSelected onProfileFragmentBtnSelectedListener;

    private String donorDisease;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);

        getActivity().setTitle("Your Profile");


        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        userId = mAuth.getCurrentUser().getUid();
        currentLogedUser = mAuth.getCurrentUser();

        showName = view.findViewById(R.id.showProfileName);
        showBloodGroup = view.findViewById(R.id.showProfileBloodGroup);
        showLeftDonate = view.findViewById(R.id.showProfileLeftDonate);

        showLastDonate = view.findViewById(R.id.showProfileLastDonate);
        showTotalDonate = view.findViewById(R.id.showProfileTotalDonate);
        showDisease = view.findViewById(R.id.showProfileDisease);

        showBeDonor = view.findViewById(R.id.showProfileBeDonor);

        showEmail = view.findViewById(R.id.showProfileEmail);
        showContact = view.findViewById(R.id.showProfileContact);
        showBirthDate = view.findViewById(R.id.showProfileDOB);
        showGender = view.findViewById(R.id.showProfileGender);
        showArea = view.findViewById(R.id.showProfileAddressArea);
        showThana = view.findViewById(R.id.showProfileAddressThana);
        showDistrict = view.findViewById(R.id.showProfileAddressDistrict);
        showDivision = view.findViewById(R.id.showProfileAddressDivision);

        changePassword = view.findViewById(R.id.btnChangePassword);

        editProfileImage = view.findViewById(R.id.editProfileImage);
        circleProfileImageView = view.findViewById(R.id.profileCircleImage);
        progressBarP = view.findViewById(R.id.progressBarProfile);

        //show profile Image
        StorageReference showProfileImageRef = storageReference.child(userId+".jpg");
            progressBarP.setVisibility(View.VISIBLE);
            showProfileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(circleProfileImageView);
                    progressBarP.setVisibility(View.GONE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "Please upload a image!", Toast.LENGTH_SHORT).show();
                    progressBarP.setVisibility(View.GONE);
                }
            });




        editProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, Gallery_Pick);
            }
        });



        editProfile = view.findViewById(R.id.btnEditProfile);

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onProfileFragmentBtnSelectedListener.onProfileFragmentEditProfileButtonSelected();
            }
        });



        DocumentReference documentReference = fStore.collection("users").document(userId);
        DocumentReference documentReference2 = fStore.collection("donors").document(userId);

        documentReference2.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                showTotalDonate.setText(String.valueOf(value.getLong("totalDonate").intValue())+" times");

                if (value.getString("lastDonate").equalsIgnoreCase("Didnt donate yet")){

                    if(value.getString("hasDisease").equals("false")){
                        showDisease.setVisibility(View.GONE);
                    } else {
                        showDisease.setText("Have some problem! Sorry, Can't Donate now!");
                        showBeDonor.setVisibility(View.GONE);
                    }

                    showLastDonate.setText(value.getString("lastDonate"));

                } else {

                    if(value.getString("hasDisease").equals("false")){
                        showDisease.setVisibility(View.GONE);
                        donorDisease = "false";
                    } else {
                        showDisease.setText("Have some problem! Sorry, Can't Donate now!");
                        showBeDonor.setVisibility(View.GONE);
                        donorDisease = "true";
                    }

                    showLastDonate.setText(value.getString("lastDonate"));

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
                showName.setText(value.getString("userName"));
                showBloodGroup.setText(value.getString("bloodGroup"));
                showEmail.setText(value.getString("email"));
                showContact.setText(value.getString("contactNo"));
                showBirthDate.setText(value.getString("birthDate"));
                showGender.setText(value.getString("gender"));
                showArea.setText(value.getString("uArea")+",");
                showThana.setText(value.getString("policeStation")+",");
                showDistrict.setText(value.getString("uDistrict")+",");
                showDivision.setText(value.getString("uDivision"));
                donor = value.getString("isDonor");
                if(donor.equals("true")){
                    showBeDonor.setText("A Blood Hero!");
                } else {
                    showBeDonor.setText("Become a Donor and save Lives, by edit your profile!");
                    showDisease.setVisibility(View.GONE);
                }
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater inflater1 = LayoutInflater.from(getActivity());
                View view1 = inflater1.inflate(R.layout.change_password_layout, null);

                final EditText oldPassword, newPassword, reTypePassword;
                final TextView passCancel, passUpdate;
                final ProgressBar passProgress;

                oldPassword = view1.findViewById(R.id.passOldPassWord);
                newPassword = view1.findViewById(R.id.passNewPassword);
                reTypePassword = view1.findViewById(R.id.passRetypePassword);
                passCancel = view1.findViewById(R.id.passCancel);
                passUpdate = view1.findViewById(R.id.passUpdate);
                passProgress = view1.findViewById(R.id.changePassProgressBar);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setView(view1)
                        .setCancelable(false);

                final AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                passUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        passProgress.setVisibility(View.VISIBLE);

                        final String oldPass, newPass, retypePass;

                        oldPass = oldPassword.getText().toString().trim();
                        newPass = newPassword.getText().toString().trim();
                        retypePass = reTypePassword.getText().toString().trim();

                        if(newPass.length() < 6){
                            newPassword.setError("New password should greater than 6digit");
                            newPassword.requestFocus();
                            return;
                        }

                        if(!oldPass.isEmpty() || !newPass.isEmpty() || !retypePass.isEmpty()){

                            AuthCredential credential = EmailAuthProvider.getCredential(currentLogedUser.getEmail(), oldPass);

                            currentLogedUser.reauthenticate(credential)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                if(newPass.equals(retypePass)){

                                                    currentLogedUser.updatePassword(retypePass).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            passProgress.setVisibility(View.GONE);
                                                            Toast.makeText(getActivity(), "Password updated successfully!!", Toast.LENGTH_SHORT).show();
                                                            alertDialog.dismiss();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            passProgress.setVisibility(View.GONE);
                                                            Toast.makeText(getActivity(), "Update Password Failed!!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

                                                } else {
                                                    passProgress.setVisibility(View.GONE);
                                                    reTypePassword.setError("Not Matched!");
                                                    reTypePassword.requestFocus();
                                                    return;
                                                }
                                            } else {
                                                passProgress.setVisibility(View.GONE);
                                                oldPassword.setError("Wrong Password!");
                                                oldPassword.requestFocus();
                                                return;
                                            }
                                        }
                                    });
                        }
                    }
                });

                passCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
            }

        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof onProfileFragmentBtnSelected){
            onProfileFragmentBtnSelectedListener = (onProfileFragmentBtnSelected) context;
        } else {
            throw new ClassCastException(context.toString()+" must implement listener");
        }
    }

    public interface onProfileFragmentBtnSelected{
        public void onProfileFragmentEditProfileButtonSelected();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            final Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setAspectRatio(1, 1)
                    .setMinCropWindowSize(500, 500)
                    .start(getContext(), this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                resultImageUri = result.getUri();
                circleProfileImageView.setImageURI(resultImageUri);

                progressBarP.setVisibility(View.VISIBLE);

                uploadImageToFirebaseStorage(resultImageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }

        }

    }

    private void uploadImageToFirebaseStorage(Uri imageUri) {
        final StorageReference profileImageRef = storageReference.child(userId+".jpg");
        profileImageRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getActivity(), "Profile Image Uploaded Successfully!!", Toast.LENGTH_SHORT ).show();

                profileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Picasso.get().load(uri).into(circleProfileImageView);

                        progressBarP.setVisibility(View.GONE);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Profile Image Upload Failed!!", Toast.LENGTH_SHORT ).show();
            }
        });
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
            if (donorDisease.equalsIgnoreCase("false")) {
                showLeftDonate.setText("Next Donate Left " + String.valueOf(days) + " days\nEstimated Next Donate: " + nextDonateDate);
                showLeftDonate.setTextColor(Color.parseColor("#00FF00"));
            } else {
                showLeftDonate.setText("Problem! Can Donate Now!");
                showLeftDonate.setTextColor(Color.parseColor("#FF0000"));
            }
        } else{
            if (donorDisease.equalsIgnoreCase("false")) {
                showLeftDonate.setText("Donate Now\nEstimated Date was: " + nextDonateDate);
                showLeftDonate.setTextColor(Color.parseColor("#00FF00"));
            } else {
                showLeftDonate.setText("Problem! Can Donate Now!");
                showLeftDonate.setTextColor(Color.parseColor("#FF0000"));
            }
        }

    }
}
