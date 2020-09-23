package com.enzamuls.blooddonation.fragments;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.enzamuls.blooddonation.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PostRequestFragment extends Fragment {

    private Spinner patientBloodGroupSpinner;
    private String[] patientBloodGroup;

    private EditText patientName, patientAge, patientBloodGroupGiven, patientProbelm, patientNeedBloodBags, patientHospital,
            patientLocation, patientContactPerson, getPatientContactPersonPhone;
    private TextView patientBloodNeedDate;
    private DatePickerDialog.OnDateSetListener showDateforPatientBloodNeed;
    private Button btnPostRequest, btnMyAllRequest, btnAllManagedHistory;
    private ProgressBar postProgressBar;

    private String saveCurrentTime, saveCurrentDate, postRandomName, currentUserId, currentUserName;

    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.post_request_fragment, container, false);

        getActivity().setTitle("Post A Blood Request!");

        patientName = view.findViewById(R.id.patientName);
        patientAge = view.findViewById(R.id.patientAge);
        patientProbelm = view.findViewById(R.id.patientProblemDetails);
        patientNeedBloodBags = view.findViewById(R.id.patientBloodNeed);
        patientHospital = view.findViewById(R.id.patientHospitalName);
        patientLocation = view.findViewById(R.id.patientLocation);
        patientContactPerson = view.findViewById(R.id.patientContactPerson);
        getPatientContactPersonPhone = view.findViewById(R.id.patientPhone);
        patientBloodNeedDate = view.findViewById(R.id.patientBloodNeedDate);

        patientBloodNeedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, showDateforPatientBloodNeed, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        showDateforPatientBloodNeed = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month+1;
                String date = day+"/"+month+"/"+year;
                patientBloodNeedDate.setText(date);
            }
        };

        btnPostRequest = view.findViewById(R.id.btnPostRequest);
        btnMyAllRequest = view.findViewById(R.id.myAllRequest);
        btnAllManagedHistory = view.findViewById(R.id.allBloodManagedHistory);

        postProgressBar = view.findViewById(R.id.postReqProgressBar);

        patientBloodGroupSpinner = view.findViewById(R.id.patientBloodGroup);
        patientBloodGroup = getResources().getStringArray(R.array.patientBloodGroup);

        ArrayAdapter<String> pbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, patientBloodGroup);
        patientBloodGroupSpinner.setAdapter(pbloodGroupAdapter);

        Calendar calForDateTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");

        saveCurrentTime = currentTime.format(calForDateTime.getTime());
        saveCurrentDate = currentDate.format(calForDateTime.getTime());

        postRandomName = saveCurrentDate+saveCurrentTime;

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fStore.collection("users").document(currentUserId);

        documentReference.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value.exists()) {
                    currentUserName = value.getString("userName");
                }
            }
        });

        btnPostRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String postUniqueId, postTime, postDate, postUserId, postUserName, postPatientName, postPatientAge,
                        postPatientBloodGroup, postPatientProblem, postBloodBagsNeeded, postHospitalName, postPatientLocation,
                        postContactPersonName, postContactPersonPhone, postPatientBloodNeedDate;

                postUniqueId = currentUserId+postRandomName;
                postTime = saveCurrentTime;
                postDate = saveCurrentDate;
                postUserId = currentUserId;
                postUserName = currentUserName;
                postPatientName = patientName.getText().toString();
                postPatientAge = patientAge.getText().toString();
                if(patientBloodGroupSpinner.getSelectedItemPosition() > 0){
                    postPatientBloodGroup = patientBloodGroupSpinner.getSelectedItem().toString();

                } else {
                    Toast.makeText(getActivity(), "Select Your Blood Group", Toast.LENGTH_SHORT ).show();
                    patientBloodGroupSpinner.requestFocus();
                    return;
                }
                postPatientProblem = patientProbelm.getText().toString();
                postBloodBagsNeeded = patientNeedBloodBags.getText().toString();
                postHospitalName = patientHospital.getText().toString();
                postPatientLocation = patientLocation.getText().toString();
                postContactPersonName = patientContactPerson.getText().toString();
                postContactPersonPhone = getPatientContactPersonPhone.getText().toString();
                postPatientBloodNeedDate = patientBloodNeedDate.getText().toString();

                if(postContactPersonPhone.length() < 11){
                    Toast.makeText(getActivity(), "Give a valid Phone no", Toast.LENGTH_SHORT ).show();
                    getPatientContactPersonPhone.setError("Phone no invalid");
                    return;
                }

                if(postUniqueId.isEmpty() || postTime.isEmpty() || postDate.isEmpty() || postUserId.isEmpty() ||
                        postUserName.isEmpty() || postPatientName.isEmpty() || postPatientAge.isEmpty() ||
                        postPatientBloodGroup.isEmpty() || postPatientProblem.isEmpty() ||
                        postBloodBagsNeeded.isEmpty() || postHospitalName.isEmpty() || postPatientLocation.isEmpty() ||
                        postContactPersonName.isEmpty() || postContactPersonPhone.isEmpty() || postPatientBloodNeedDate.isEmpty()){

                    Toast.makeText(getActivity(), "One or More Field is Empty!!", Toast.LENGTH_SHORT ).show();
                    return;
                } else {

                    postProgressBar.setVisibility(View.VISIBLE);

                    DocumentReference documentReference = fStore.collection("requestposts").document(postUniqueId);

                    Map<String, Object> newPost = new HashMap<>();
                    newPost.put("postUserID", postUserId);
                    newPost.put("postUserName", postUserName);
                    newPost.put("postTime", postTime);
                    newPost.put("postDate", postDate);
                    newPost.put("postPatientName", postPatientName);
                    newPost.put("postPatientAge", postPatientAge);
                    newPost.put("postPatientBloodGroup", postPatientBloodGroup);
                    newPost.put("postPatientProblem", postPatientProblem);
                    newPost.put("postBloodBagsNeeded", postBloodBagsNeeded);
                    newPost.put("postHospitalName", postHospitalName);
                    newPost.put("postPatientLocation", postPatientLocation);
                    newPost.put("postContactPersonName", postContactPersonName);
                    newPost.put("postContactPersonPhone", postContactPersonPhone);
                    newPost.put("postPatientBloodNeedDate", postPatientBloodNeedDate);
                    newPost.put("status", "Not Manage Yet!!");

                    documentReference.set(newPost).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            postProgressBar.setVisibility(View.GONE);

                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.container_fragment, new HomeFragment());
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();

                            Toast.makeText(getActivity(), "Blood Request Post Done!!", Toast.LENGTH_SHORT ).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Blood Request Post Failed!!", Toast.LENGTH_SHORT ).show();
                        }
                    });
                }

            }
        });

        btnAllManagedHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_fragment, new AllManagedHistoryFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        btnMyAllRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_fragment, new MyBloodRequestsFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return view;
    }
}
