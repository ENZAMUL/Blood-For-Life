package com.enzamuls.blooddonation.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.enzamuls.blooddonation.R;

import java.util.Locale;

public class StatisticsFragment extends Fragment {

    private TextView countUser, countDonor, aPositive, aPositivePercentage, aNegative, aNegativePercentage,
    bPositive, bPositivePercentage, bNegative, bNegativePercentage, abPositive, abPositivePercentage,
    abNegative, abNegativePercentage, oPositive, oPositivePercentage, oNegative, oNegativePercentage;
    private FirebaseFirestore fStore;
    private CollectionReference userCollectionRef, donorCollectionRef;
    private int totalUser;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.statistics_fragment, container, false);

        getActivity().setTitle("Donors Statistics");

        countUser = view.findViewById(R.id.countUser);
        countDonor = view.findViewById(R.id.countDonateUser);

        aPositive = view.findViewById(R.id.countAp);
        aPositivePercentage = view.findViewById(R.id.countApPer);
        aNegative = view.findViewById(R.id.countAn);
        aNegativePercentage = view.findViewById(R.id.countAnPer);
        bPositive = view.findViewById(R.id.countBp);
        bPositivePercentage = view.findViewById(R.id.countBpPer);
        bNegative = view.findViewById(R.id.countBn);
        bNegativePercentage = view.findViewById(R.id.countBnPer);
        abPositive = view.findViewById(R.id.countABp);
        abPositivePercentage = view.findViewById(R.id.countABper);
        abNegative = view.findViewById(R.id.countABn);
        abNegativePercentage = view.findViewById(R.id.countABnPer);
        oPositive = view.findViewById(R.id.countOp);
        oPositivePercentage = view.findViewById(R.id.countOpPer);
        oNegative = view.findViewById(R.id.countOn);
        oNegativePercentage = view.findViewById(R.id.countOnPer);



        fStore = FirebaseFirestore.getInstance();
        userCollectionRef = fStore.collection("users");
        donorCollectionRef = fStore.collection("donors");

        userCollectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int count = 0;
                    for (DocumentSnapshot document : task.getResult()) {
                        count++;
                    }
                    totalUser = count;


                    userCollectionRef.whereEqualTo("bloodGroup", "A+").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                int count = 0;
                                for (DocumentSnapshot document : task.getResult()) {
                                    count++;
                                }
                                aPositivePercentage.setText(String.format(Locale.US, "%.2f", ((float)count/totalUser)*100) +"%");
                                aPositive.setText(String.valueOf(count));
                            } else {
                                Toast.makeText(getActivity(), "Failed to count user", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    userCollectionRef.whereEqualTo("bloodGroup", "A-").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                int count = 0;
                                for (DocumentSnapshot document : task.getResult()) {
                                    count++;
                                }
                                aNegativePercentage.setText(String.format(Locale.US, "%.2f", ((float)count/totalUser)*100) +"%");
                                aNegative.setText(String.valueOf(count));
                            } else {
                                Toast.makeText(getActivity(), "Failed to count user", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    userCollectionRef.whereEqualTo("bloodGroup", "B+").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                int count = 0;
                                for (DocumentSnapshot document : task.getResult()) {
                                    count++;
                                }
                                bPositivePercentage.setText(String.format(Locale.US, "%.2f", ((float)count/totalUser)*100) +"%");
                                bPositive.setText(String.valueOf(count));
                            } else {
                                Toast.makeText(getActivity(), "Failed to count user", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    userCollectionRef.whereEqualTo("bloodGroup", "B-").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                int count = 0;
                                for (DocumentSnapshot document : task.getResult()) {
                                    count++;
                                }
                                bNegativePercentage.setText(String.format(Locale.US, "%.2f", ((float)count/totalUser)*100) +"%");
                                bNegative.setText(String.valueOf(count));
                            } else {
                                Toast.makeText(getActivity(), "Failed to count user", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    userCollectionRef.whereEqualTo("bloodGroup", "AB+").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                int count = 0;
                                for (DocumentSnapshot document : task.getResult()) {
                                    count++;
                                }
                                abPositivePercentage.setText(String.format(Locale.US, "%.2f", ((float)count/totalUser)*100) +"%");
                                abPositive.setText(String.valueOf(count));
                            } else {
                                Toast.makeText(getActivity(), "Failed to count user", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    userCollectionRef.whereEqualTo("bloodGroup", "AB-").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                int count = 0;
                                for (DocumentSnapshot document : task.getResult()) {
                                    count++;
                                }
                                abNegativePercentage.setText(String.format(Locale.US, "%.2f", ((float)count/totalUser)*100) +"%");
                                abNegative.setText(String.valueOf(count));
                            } else {
                                Toast.makeText(getActivity(), "Failed to count user", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    userCollectionRef.whereEqualTo("bloodGroup", "O+").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                int count = 0;
                                for (DocumentSnapshot document : task.getResult()) {
                                    count++;
                                }
                                oPositivePercentage.setText(String.format(Locale.US, "%.2f", ((float)count/totalUser)*100) +"%");
                                oPositive.setText(String.valueOf(count));
                            } else {
                                Toast.makeText(getActivity(), "Failed to count user", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    userCollectionRef.whereEqualTo("bloodGroup", "O-").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                int count = 0;
                                for (DocumentSnapshot document : task.getResult()) {
                                    count++;
                                }
                                oNegativePercentage.setText(String.format(Locale.US, "%.2f", ((float)count/totalUser)*100) +"%");
                                oNegative.setText(String.valueOf(count));
                            } else {
                                Toast.makeText(getActivity(), "Failed to count user", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });



                    countUser.setText(String.valueOf(count)+ " users");
                } else {
                    Toast.makeText(getActivity(), "Failed to count user", Toast.LENGTH_SHORT).show();
                }
            }
        });

        donorCollectionRef.whereEqualTo("hasDisease", "false").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int count = 0;
                    for (DocumentSnapshot document : task.getResult()) {
                        count++;
                    }
                    countDonor.setText(String.valueOf(count)+" donors");
                } else {
                    Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });




        return view;
    }
}
