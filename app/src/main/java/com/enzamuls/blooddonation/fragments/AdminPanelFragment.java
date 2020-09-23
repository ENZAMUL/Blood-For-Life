package com.enzamuls.blooddonation.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.enzamuls.blooddonation.R;

public class AdminPanelFragment extends Fragment {

    TextView manageUser, manageHospital, manageAmbulance;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_panel_fragment, container, false);

        getActivity().setTitle("Admin Panel");

        manageUser = view.findViewById(R.id.manageUsers);
        manageHospital = view.findViewById(R.id.manageHospitals);
        manageAmbulance = view.findViewById(R.id.manageAmbulances);

        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        manageUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentTransaction.replace(R.id.container_fragment, new AdminUser());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        manageHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransaction.replace(R.id.container_fragment, new AdminHospital());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        manageAmbulance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransaction.replace(R.id.container_fragment, new AdminAmbulances());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return view;
    }
}
