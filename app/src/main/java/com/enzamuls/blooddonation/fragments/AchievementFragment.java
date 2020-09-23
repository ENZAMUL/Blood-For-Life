package com.enzamuls.blooddonation.fragments;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.enzamuls.blooddonation.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class AchievementFragment extends Fragment {

    private DatePickerDialog.OnDateSetListener showDateforAcc;
    private String saveCurrentTime, saveCurrentDate, achievementRandomID, currentUserId, previousTotalDonate, hasDiease, userlastDonateDate;
    private Date estimatedNextDonaet;

    private TextView achPatientName, achDonateLocation, achDonateDate, achAddAchievement, achLastDonateDate, achTotalDonate,
            achNextDonateLeft, achNextDonateDate, achAchievmentHistory;

    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.achievement_fragment, container, false);

        getActivity().setTitle("My Achievements");

        achPatientName = view.findViewById(R.id.achPatientName);
        achDonateLocation = view.findViewById(R.id.achLocation);
        achDonateDate = view.findViewById(R.id.achDonateDate);
        achAddAchievement = view.findViewById(R.id.achAddAchievement);

        achLastDonateDate = view.findViewById(R.id.achLastDonateDate);
        achTotalDonate = view.findViewById(R.id.achTotalDonate);
        achNextDonateLeft = view.findViewById(R.id.achNextDonateLeft);
        achNextDonateDate = view.findViewById(R.id.achNextDonateDate);
        achAchievmentHistory = view.findViewById(R.id.achDonateHistory);

        achAchievmentHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_fragment, new AchievementHistoryFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        achDonateDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, showDateforAcc, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        showDateforAcc = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month+1;
                String date = day+"/"+month+"/"+year;
                achDonateDate.setText(date);
            }
        };

        Calendar calForDateTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");

        saveCurrentTime = currentTime.format(calForDateTime.getTime());
        saveCurrentDate = currentDate.format(calForDateTime.getTime());

        achievementRandomID = saveCurrentDate+saveCurrentTime;

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        final DocumentReference documentReferencedonor = fStore.collection("donors").document(currentUserId);


        documentReferencedonor.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                previousTotalDonate = String.valueOf(value.getLong("totalDonate").intValue());
                hasDiease = value.getString("hasDisease");
                achLastDonateDate.setText(value.getString("lastDonate"));
                achTotalDonate.setText(String.valueOf(value.getLong("totalDonate").intValue())+" times");

                userlastDonateDate = value.getString("lastDonate");

                if (userlastDonateDate.equalsIgnoreCase("Didnt donate yet")){

                    if(hasDiease.equals("true")){
                        achNextDonateDate.setText("You Didn't Donate Yet!");
                        achNextDonateDate.setTextColor(Color.parseColor("#FF0000"));
                        achNextDonateLeft.setText("Can't Donate Now!");
                        achNextDonateLeft.setTextColor(Color.parseColor("#FF0000"));
                    } else {
                        achNextDonateDate.setText("You Didn't Donate Yet!");
                        achNextDonateDate.setTextColor(Color.parseColor("#FF0000"));
                        achNextDonateLeft.setText("Donate Blood to save life!");
                        achNextDonateLeft.setTextColor(Color.parseColor("#FF0000"));
                    }



                } else {

                    try {
                        setNextDonateDateAndDay(userlastDonateDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }

            }
        });




        achAddAchievement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                final String aachPatientName, aachLocation, aachDonateDate, aachUserId, uniqueAchievementId;

                aachPatientName = achPatientName.getText().toString();
                aachLocation = achDonateLocation.getText().toString();
                aachDonateDate = achDonateDate.getText().toString();
                aachUserId = currentUserId;
                uniqueAchievementId = currentUserId+achievementRandomID;

                if(!aachDonateDate.isEmpty()) {
                    SimpleDateFormat sdf3 = new SimpleDateFormat("dd/MM/yyyy");
                    Date newDonateDate = null;
                    try {
                        newDonateDate = sdf3.parse(aachDonateDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                    if (System.currentTimeMillis() < newDonateDate.getTime()) {
                        Toast.makeText(getActivity(), "Greater than current Date isn't allowed!", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(estimatedNextDonaet != null) {
                        if (estimatedNextDonaet.getTime() > newDonateDate.getTime()) {
                            Toast.makeText(getActivity(), "You can donate after: "+estimatedNextDonaet, Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), "Please Choose a date", Toast.LENGTH_SHORT).show();
                    return;
                }


                if(!aachPatientName.isEmpty() || !aachLocation.isEmpty() || !aachDonateDate.isEmpty() || !aachUserId.isEmpty() || !uniqueAchievementId.isEmpty()) {

                    DocumentReference documentReferenceach = fStore.collection("userachievements").document(uniqueAchievementId);

                    if (hasDiease.equalsIgnoreCase("true")) {
                        Toast.makeText(getActivity(), "You Have Disease! You Can't Donate now!", Toast.LENGTH_LONG).show();
                    } else {

                        Map<String, Object> newAchievement = new HashMap<>();
                        newAchievement.put("userID", aachUserId);
                        newAchievement.put("patientName", aachPatientName);
                        newAchievement.put("givenLocation", aachLocation);
                        newAchievement.put("donateDate", aachDonateDate);

                        documentReferenceach.set(newAchievement).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Map<String, Object> newDonorInfo = new HashMap<>();
                                newDonorInfo.put("lastDonate", aachDonateDate);
                                newDonorInfo.put("totalDonate", parseInt(previousTotalDonate) + 1);

                                documentReferencedonor.update(newDonorInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                        fragmentTransaction.replace(R.id.container_fragment, new AchievementFragment());
                                        fragmentTransaction.addToBackStack(null);
                                        fragmentTransaction.commit();
                                        Toast.makeText(getActivity(), "Achievement added successfully!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
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
        estimatedNextDonaet = calculatedDate;

        long diff = calculatedDate.getTime() - (System.currentTimeMillis() - apreviousDonateDate.getTime()) - apreviousDonateDate.getTime();
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = (hours / 24)+1;

        if(days > 0) {

            if(hasDiease.equals("true")){
                achNextDonateDate.setText("Can't Donate Now!");
                achNextDonateLeft.setText("Can't Donate Now!");
                achNextDonateLeft.setTextColor(Color.parseColor("#FF0000"));
                achNextDonateDate.setTextColor(Color.parseColor("#FF0000"));
            } else {
                achNextDonateDate.setText("You should Donate next after: " + nextDonateDate);
                achNextDonateLeft.setText("Next Donate Left " + String.valueOf(days) + " days");
                achNextDonateLeft.setTextColor(Color.parseColor("#00FF00"));
                achNextDonateDate.setTextColor(Color.parseColor("#00FF00"));
            }


        } else {
            if(hasDiease.equals("true")){
                achNextDonateDate.setText("Can't Donate Now!");
                achNextDonateLeft.setText("Can't Donate Now!");
                achNextDonateLeft.setTextColor(Color.parseColor("#FF0000"));
                achNextDonateDate.setTextColor(Color.parseColor("#FF0000"));
            } else {
                achNextDonateDate.setText("Estimated Donate Date Was "+ nextDonateDate);
                achNextDonateLeft.setText("Donate Now!");
                achNextDonateLeft.setTextColor(Color.parseColor("#00FF00"));
                achNextDonateDate.setTextColor(Color.parseColor("#00FF00"));
            }

        }
    }
}
