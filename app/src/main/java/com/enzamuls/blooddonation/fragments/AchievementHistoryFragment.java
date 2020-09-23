package com.enzamuls.blooddonation.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.enzamuls.blooddonation.adapter.AllAchievemenrHistoryAdapter;
import com.enzamuls.blooddonation.R;
import com.enzamuls.blooddonation.model.userachievements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AchievementHistoryFragment extends Fragment {

    private RecyclerView achievementHistoryRecycler;

    private AllAchievemenrHistoryAdapter allAchievemenrHistoryAdapter;

    private FirebaseFirestore fStore;
    private FirebaseAuth mAuth;
    private String currUserID;
    private CollectionReference AchievementCollectionReference;
    DatePickerDialog.OnDateSetListener finalShowDateforEditAch;

    private int lastTotalDonate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.achievement_history_fragment, container, false);

        getActivity().setTitle("My Achievements History");

        achievementHistoryRecycler = view.findViewById(R.id.achievementHistoryRecyler);
        achievementHistoryRecycler.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        achievementHistoryRecycler.setLayoutManager(linearLayoutManager);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        currUserID = mAuth.getCurrentUser().getUid();

        AchievementCollectionReference = fStore.collection("userachievements");

        DisplayAllAchievements();

        allAchievemenrHistoryAdapter.setOnAchievementClickListener(new AllAchievemenrHistoryAdapter.OnAchievementClickListener() {
            @Override
            public void OnAchivementClickEdit(DocumentSnapshot documentSnapshot, int position) {

                userachievements userachieve = documentSnapshot.toObject(userachievements.class);
                final String uniqueAchieveId = documentSnapshot.getId();

                final String uniqueUserId = userachieve.getUserID();

                LayoutInflater inflater1 = LayoutInflater.from(getActivity());
                View view1 = inflater1.inflate(R.layout.edit_achievement_dialog, null);

                final EditText editAchPatientName, editAchLocation;
                final TextView editAchDate;
                TextView editAchCancel, editAchUpdate;
                DatePickerDialog.OnDateSetListener showDateforEditAch;

                editAchPatientName = view1.findViewById(R.id.editAchPatientName);
                editAchLocation = view1.findViewById(R.id.editAchLocation);

                editAchDate = view1.findViewById(R.id.editAchDate);
                editAchCancel = view1.findViewById(R.id.editAchCancelx);
                editAchUpdate = view1.findViewById(R.id.editAchUpdate);

                editAchPatientName.setText(userachieve.getPatientName());
                editAchLocation.setText(userachieve.getGivenLocation());
                editAchDate.setText(userachieve.getDonateDate());

                editAchDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar cal = Calendar.getInstance();
                        int year = cal.get(Calendar.YEAR);
                        int month = cal.get(Calendar.MONTH);
                        int day = cal.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                                android.R.style.Theme_Holo_Light_Dialog_MinWidth, finalShowDateforEditAch, year, month, day);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog.show();
                    }
                });

                finalShowDateforEditAch = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month+1;
                        String date = day+"/"+month+"/"+year;
                        editAchDate.setText(date);
                    }
                };



                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setView(view1)
                    .setCancelable(false);

                    final AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                editAchCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                editAchUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String editedPatientName, editedLocation, editedDate;
                        editedPatientName = editAchPatientName.getText().toString();
                        editedLocation = editAchLocation.getText().toString();
                        editedDate = editAchDate.getText().toString();

                        if(!editedDate.isEmpty()) {
                            SimpleDateFormat sdf3 = new SimpleDateFormat("dd/MM/yyyy");
                            Date newDonateDate = null;
                            try {
                                newDonateDate = sdf3.parse(editedDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                            if (System.currentTimeMillis() < newDonateDate.getTime()) {
                                Toast.makeText(getActivity(), "Greater than current Date isn't allowed!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        if (editedPatientName.isEmpty() || editedLocation.isEmpty() || editedDate.isEmpty()) {
                            Toast.makeText(getActivity(), "Give all information correctly!", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            DocumentReference documentReferenceach = fStore.collection("userachievements").document(uniqueAchieveId);

                            Map<String, Object> editedAchievement = new HashMap<>();
                            editedAchievement.put("patientName", editedPatientName);
                            editedAchievement.put("givenLocation", editedLocation);
                            editedAchievement.put("donateDate", editedDate);

                            documentReferenceach.update(editedAchievement).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    DocumentReference documentReference2 = fStore.collection("donors").document(uniqueUserId);
                                    Map<String, Object> newDonorT = new HashMap<>();
                                    newDonorT.put("lastDonate", editedDate);

                                    documentReference2.update(newDonorT).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getActivity(), "Achievement updated successfully!", Toast.LENGTH_SHORT).show();
                                            alertDialog.dismiss();
                                        }
                                    });


                                }
                            });

                        }
                    }
                });
            }
        });

        return view;
    }

    private void DisplayAllAchievements() {
        Query query = AchievementCollectionReference.whereEqualTo("userID", currUserID);
        FirestoreRecyclerOptions<userachievements> options = new FirestoreRecyclerOptions.Builder<userachievements>()
                .setQuery(query, userachievements.class)
                .build();
        allAchievemenrHistoryAdapter = new AllAchievemenrHistoryAdapter(options);
        achievementHistoryRecycler.setAdapter(allAchievemenrHistoryAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        allAchievemenrHistoryAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        allAchievemenrHistoryAdapter.stopListening();
    }
}
