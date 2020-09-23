package com.enzamuls.blooddonation.fragments;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.enzamuls.blooddonation.R;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EditUpdateBloodRequestPost extends Fragment {

    private EditText editpatientName, editpatientAge, editpatientBloodGroupGiven, editpatientProbelm,
            editpatientNeedBloodBags, editpatientHospital, editpatientLocation, editpatientContactPerson,
            editgetPatientContactPersonPhone, editpatientBloodGroup;

    private TextView editpatientBloodNeedDate;
    private DatePickerDialog.OnDateSetListener editshowDateforPatientBloodNeed;
    private Button editbtnPostRequest;
    private ProgressBar editpostProgressBar;

    private String uniqBloodPostId;

    private FirebaseFirestore fStore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_update_bloodrequest, container, false);

        getActivity().setTitle("Update Your Post!");

        editpatientName = view.findViewById(R.id.editpatientName);
        editpatientAge = view.findViewById(R.id.editpatientAge);
        editpatientBloodGroup = view.findViewById(R.id.editpatientBloodGroup);
        editpatientProbelm = view.findViewById(R.id.editpatientProblemDetails);
        editpatientNeedBloodBags = view.findViewById(R.id.editpatientBloodNeed);
        editpatientHospital = view.findViewById(R.id.editpatientHospitalName);
        editpatientLocation = view.findViewById(R.id.editpatientLocation);
        editpatientContactPerson = view.findViewById(R.id.editpatientContactPerson);
        editgetPatientContactPersonPhone = view.findViewById(R.id.editpatientPhone);
        editpatientBloodNeedDate = view.findViewById(R.id.editpatientBloodNeedDate);

        editpatientBloodNeedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, editshowDateforPatientBloodNeed, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        editshowDateforPatientBloodNeed = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month+1;
                String date = day+"/"+month+"/"+year;
                editpatientBloodNeedDate.setText(date);
            }
        };

        editbtnPostRequest = view.findViewById(R.id.editbtnPostRequest);
        editpostProgressBar = view.findViewById(R.id.editpostReqProgressBar);

        fStore = FirebaseFirestore.getInstance();


        uniqBloodPostId = getArguments().getString("currentBloodPostID");
        Log.d("PostID", uniqBloodPostId);
        DocumentReference documentReference = fStore.collection("requestposts").document(uniqBloodPostId);

        documentReference.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                editpatientName.setText(value.getString("postPatientName"));
                editpatientAge.setText(value.getString("postPatientAge"));
                editpatientBloodGroup.setText(value.getString("postPatientBloodGroup"));
                editpatientProbelm.setText(value.getString("postPatientProblem"));
                editpatientNeedBloodBags.setText(value.getString("postBloodBagsNeeded"));
                editpatientHospital.setText(value.getString("postHospitalName"));
                editpatientLocation.setText(value.getString("postPatientLocation"));
                editpatientContactPerson.setText(value.getString("postContactPersonName"));
                editgetPatientContactPersonPhone.setText(value.getString("postContactPersonPhone"));
                editpatientBloodNeedDate.setText(value.getString("postPatientBloodNeedDate"));

            }
        });

        editbtnPostRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String newPatientName, newpatientAge, newpatientBloodGroupGiven, newpatientProbelm,
                        newpatientNeedBloodBags, newpatientHospital, newpatientLocation, newpatientContactPerson,
                        newgetPatientContactPersonPhone, newpatientBloodNeedDate;

                newPatientName = editpatientName.getText().toString();
                newpatientAge = editpatientAge.getText().toString();
                newpatientBloodGroupGiven = editpatientBloodGroup.getText().toString();
                newpatientProbelm = editpatientProbelm.getText().toString();
                newpatientNeedBloodBags = editpatientNeedBloodBags.getText().toString();
                newpatientHospital = editpatientHospital.getText().toString();
                newpatientLocation = editpatientLocation.getText().toString();
                newpatientContactPerson = editpatientContactPerson.getText().toString();
                newgetPatientContactPersonPhone = editgetPatientContactPersonPhone.getText().toString();

                newpatientBloodNeedDate = editpatientBloodNeedDate.getText().toString();

                if(newPatientName.isEmpty() || newpatientAge.isEmpty() || newpatientBloodGroupGiven.isEmpty() ||
                        newpatientProbelm.isEmpty() || newpatientNeedBloodBags.isEmpty() || newpatientHospital.isEmpty() ||
                        newpatientLocation.isEmpty() || newpatientContactPerson.isEmpty() ||
                        newgetPatientContactPersonPhone.isEmpty() || newpatientBloodNeedDate.isEmpty()){
                    Toast.makeText(getActivity(), "One or many field is empty!!", Toast.LENGTH_SHORT ).show();
                    return;
                }


                Map<String, Object> editedNewBloodPost = new HashMap<>();
                editedNewBloodPost.put("postPatientName", newPatientName);
                editedNewBloodPost.put("postPatientAge", newpatientAge);
                editedNewBloodPost.put("postPatientBloodGroup", newpatientBloodGroupGiven);
                editedNewBloodPost.put("postPatientProblem", newpatientProbelm);
                editedNewBloodPost.put("postBloodBagsNeeded", newpatientNeedBloodBags);
                editedNewBloodPost.put("postHospitalName", newpatientHospital);
                editedNewBloodPost.put("postPatientLocation", newpatientLocation);
                editedNewBloodPost.put("postContactPersonName", newpatientContactPerson);
                editedNewBloodPost.put("postContactPersonPhone", newgetPatientContactPersonPhone);
                editedNewBloodPost.put("postPatientBloodNeedDate", newpatientBloodNeedDate);

                editpostProgressBar.setVisibility(View.VISIBLE);

                DocumentReference documentReferenceed = fStore.collection("requestposts").document(uniqBloodPostId);

                documentReferenceed.update(editedNewBloodPost).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        editpostProgressBar.setVisibility(View.GONE);

                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.container_fragment, new HomeFragment());
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        Toast.makeText(getActivity(), "Post Updated Successfully!!", Toast.LENGTH_SHORT ).show();
                    }
                });

            }
        });

        return view;
    }
}
