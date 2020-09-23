package com.enzamuls.blooddonation.fragments;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.enzamuls.blooddonation.R;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EditProfileFragment extends Fragment {

    private EditText editName, editContact, editDivision, editDistrict, editThana, editArea, editBloodGroup,
            editEmail;
    private TextView editDOB;
    private CheckBox editHideContact, editBeDonor, editHasProblem;
    private Button btnUpdateProfile;

    private ProgressBar editProgressBar;

    private DatePickerDialog.OnDateSetListener showDateforEdit;

    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    private FirebaseUser currentUser;
    private String userId, isDonorn, hasProblemn, isHidePhone, editedisDonor, editedHidePhone, editedhasProblem;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_profile_fragment, container, false);
        getActivity().setTitle("Edit Your Profile");

        editProgressBar = view.findViewById(R.id.editProgressBar);

        editName = view.findViewById(R.id.editFullName);
        editEmail = view.findViewById(R.id.editEmail);
        editContact = view.findViewById(R.id.editContactno);
        editDivision = view.findViewById(R.id.editDivision);
        editDistrict = view.findViewById(R.id.editDistrict);
        editThana = view.findViewById(R.id.editThana);
        editArea = view.findViewById(R.id.editAreaname);

        editDOB = view.findViewById(R.id.editDateofbirth);
        editBloodGroup = view.findViewById(R.id.editBloodGroupSpinner);

        editHideContact = view.findViewById(R.id.editHideContact);
        editBeDonor = view.findViewById(R.id.eidtBeDonor);
        editHasProblem = view.findViewById(R.id.editHasProblem);

        btnUpdateProfile = view.findViewById(R.id.btnUpdateUser);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();
        userId = mAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fStore.collection("users").document(userId);
        DocumentReference documentReference2 = fStore.collection("donors").document(userId);

        documentReference2.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if(value.getString("hasDisease").equals("true")){
                    editHasProblem.setText("Can you Donate now? Unmark this!");
                    editHasProblem.setChecked(true);
                } else {
                    editHasProblem.setText("Have any Problem to Donate? Mark this!");
                    editHasProblem.setChecked(false);
                }
            }
        });

        documentReference.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                editName.setText(value.getString("userName"));
                editEmail.setText(value.getString("email"));
                editBloodGroup.setText(value.getString("bloodGroup"));
                editContact.setText(value.getString("contactNo"));
                editDOB.setText(value.getString("birthDate"));
                editArea.setText(value.getString("uArea"));
                editThana.setText(value.getString("policeStation"));
                editDistrict.setText(value.getString("uDistrict"));
                editDivision.setText(value.getString("uDivision"));

                if(value.getString("isDonor").equals("true")){
                    editBeDonor.setChecked(true);
                    editBeDonor.setVisibility(View.GONE);
                } else {
                    editBeDonor.setText("Want to be a Donor to save life?");
                }

                if(value.getString("hideContact").equals("true")){
                    editHideContact.setChecked(true);
                } else {
                    editHideContact.setChecked(false);
                }

            }
        });





        editDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, showDateforEdit, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        showDateforEdit = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month+1;
                String date = day+"/"+month+"/"+year;
                editDOB.setText(date);
            }
        };


        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String editedName, editedContact, editedDivision, editedDistrict, editedThana, editedArea, editedBloodGroup,
                        editedEmail, editedDOB, feditedisDonor, feditedhasProblem, feditedHidePhone;

                editedName = editName.getText().toString();
                editedContact = editContact.getText().toString();
                editedDivision = editDivision.getText().toString();
                editedDistrict = editDistrict.getText().toString();
                editedThana = editThana.getText().toString();
                editedArea = editArea.getText().toString();
                editedBloodGroup = editBloodGroup.getText().toString();
                editedEmail = editEmail.getText().toString();
                editedDOB = editDOB.getText().toString();

                if(editHideContact.isChecked()){
                    feditedHidePhone = "true";
                } else {
                    feditedHidePhone = "false";
                }

                if(editBeDonor.isChecked()){
                    feditedisDonor = "true";
                } else {
                    feditedisDonor = "false";
                }

                if(editHasProblem.isChecked()){
                    feditedhasProblem = "true";
                } else {
                    feditedhasProblem = "false";
                }





                if(editedName.isEmpty() || editedContact.isEmpty() || editedDivision.isEmpty() || editedDistrict.isEmpty() ||
                         editedThana.isEmpty() || editedArea.isEmpty() || editedBloodGroup.isEmpty() || editedEmail.isEmpty() ||
                         editedDOB.isEmpty()){
                    Toast.makeText(getActivity(), "One or More Fields are Empty", Toast.LENGTH_SHORT ).show();
                    return;
                }

                editProgressBar.setVisibility(View.VISIBLE);

                currentUser.updateEmail(editedEmail).addOnSuccessListener(new OnSuccessListener<Void>() {

                    @Override
                    public void onSuccess(Void aVoid) {

                        DocumentReference documentReference = fStore.collection("users").document(userId);
                        final DocumentReference documentReference2 = fStore.collection("donors").document(userId);

                        Map<String, Object> editedUser = new HashMap<>();
                        editedUser.put("userName", editedName);
                        editedUser.put("email", editedEmail);
                        editedUser.put("contactNo", editedContact);
                        editedUser.put("hideContact", feditedHidePhone);
                        editedUser.put("birthDate", editedDOB);
                        editedUser.put("bloodGroup", editedBloodGroup);
                        editedUser.put("uDivision", editedDivision);
                        editedUser.put("uDistrict", editedDistrict);
                        editedUser.put("policeStation", editedThana);
                        editedUser.put("uArea", editedArea);
                        editedUser.put("isDonor", feditedisDonor);

                        documentReference.update(editedUser).addOnSuccessListener(new OnSuccessListener<Void>() {

                            @Override
                            public void onSuccess(Void aVoid) {
                                Map<String, Object> editedDonor = new HashMap<>();
                                editedDonor.put("hasDisease", feditedhasProblem);
                                editedDonor.put("userName", editedName);

                                documentReference2.update(editedDonor).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        editProgressBar.setVisibility(View.GONE);

                                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                        fragmentTransaction.replace(R.id.container_fragment, new ProfileFragment());
                                        fragmentTransaction.addToBackStack(null);
                                        fragmentTransaction.commit();

                                        Toast.makeText(getActivity(), "Updated Successfully!!", Toast.LENGTH_SHORT ).show();
                                    }
                                });
                            }

                        });

                    }
                });
                
            }
        });


        return view;
    }
}
