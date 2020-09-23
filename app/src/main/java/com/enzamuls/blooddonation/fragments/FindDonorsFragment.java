package com.enzamuls.blooddonation.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.enzamuls.blooddonation.R;
import com.enzamuls.blooddonation.adapter.SearchDonorAdapter;
import com.enzamuls.blooddonation.model.users;


public class FindDonorsFragment extends Fragment {

    private String[] allBloodGroup, allDivision, allDistrictBarishal, allDistrictChittagong, allDistrictDhaka,
            allDistrictMymensingh, allDistrictKhulna, allDistrictRajshahi, allDistrictRangpur, allDistrictSylhet,
            subDistrictBarguna, subDistrictBarishal, subDistrictBhola, subDistrictJhalokati, subDistrictPatuakhali,
            subDistrictPirojpur, subDistrictBandarban, subDistrictBrahmanbaria,
            subDistrictChandpur, subDistrictChattogram, subDistrictCumilla, subDistrictCoxBazar, subDistrictFeni,
            subDistrictKhagrachari, subDistrictLakshmipur, subDistrictNoakhali, subDistrictRangamati, subDistrictDhaka,
            subDistrictFaridpur, subDistrictGazipur, subDistrictGopalganj, subDistrictKishoreganj,
            subDistrictMadaripur, subDistrictManikganj, subDistrictMunshiganj, subDistrictNarayanganj,
            subDistrictNarsingdi, subDistrictRajbari, subDistrictShariatpur, subDistrictTangail, subDistrictMymensingh,
            subDistrictJamalpur, subDistrictNetrokona, subDistrictSherpur, subDistrictKhulna,
            subDistrictBagerhat, subDistrictChuadanga, subDistrictJashore, subDistrictJhenaidah, subDistrictKushtia,
            subDistrictMagura, subDistrictMeherpur, subDistrictNarail, subDistrictSatkhira, subDistrictRajshahi,
            subDistrictBogura, subDistrictJoypurhat, subDistrictNaogaon, subDistrictNatore, subDistrictNawabganj,
            subDistrictPabna, subDistrictRangpur, subDistrictSirajgonj, subDistrictDinajpur, subDistrictGaibandha,
            subDistrictKurigram, subDistrictLalmonirhat, subDistrictNilphamari, subDistrictPanchagarh,
            subDistrictThakurgaon, subDistrictSylhet, subDistrictHabiganj,
            subDistrictMaulvibazar, subDistrictSunamganj, noDistrict, noSubDistrict;
    private Spinner divisionSpin, districtSpin, subDistrictSpin, bloodGroupSpin;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private TextView findAllDonor, searchDonor;
    private RecyclerView searchedDonorsRecycler;

    private CollectionReference userCollectionRef;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    private StorageReference storageReference;
    private SearchDonorAdapter searchDonorAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.find_donors_fragment, container, false);

        getActivity().setTitle("Find A Donor");

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        userCollectionRef = fStore.collection("users");

        findAllDonor = view.findViewById(R.id.findAllDonor);
        searchDonor = view.findViewById(R.id.findSearchDonor);
        searchedDonorsRecycler = view.findViewById(R.id.findSearchDonorRecycler);

        divisionSpin = view.findViewById(R.id.findDivision);
        districtSpin = view.findViewById(R.id.findDistrict);
        subDistrictSpin = view.findViewById(R.id.findSubDistrict);
        bloodGroupSpin = view.findViewById(R.id.findBloodGroupSp);

        allBloodGroup = getResources().getStringArray(R.array.bloodGroup);

        allDivision = getResources().getStringArray(R.array.All_Division);

        allDistrictBarishal = getResources().getStringArray(R.array.District_of_Barishal);
        allDistrictChittagong = getResources().getStringArray(R.array.District_of_Chittagong);
        allDistrictDhaka = getResources().getStringArray(R.array.District_of_Dhaka);
        allDistrictMymensingh = getResources().getStringArray(R.array.District_of_Mymensingh);
        allDistrictKhulna = getResources().getStringArray(R.array.District_of_Khulna);
        allDistrictRajshahi = getResources().getStringArray(R.array.District_of_Rajshahi);
        allDistrictRangpur = getResources().getStringArray(R.array.District_of_Rangpur);
        allDistrictSylhet = getResources().getStringArray(R.array.District_of_Sylhet);

        subDistrictBarguna = getResources().getStringArray(R.array.Sub_District_of_Barguna);
        subDistrictBarishal = getResources().getStringArray(R.array.Sub_District_of_Barishal);
        subDistrictBhola = getResources().getStringArray(R.array.Sub_District_of_Bhola);
        subDistrictJhalokati = getResources().getStringArray(R.array.Sub_District_of_Jhalokati);
        subDistrictPatuakhali = getResources().getStringArray(R.array.Sub_District_of_Patuakhali);
        subDistrictPirojpur = getResources().getStringArray(R.array.Sub_District_of_Pirojpur);
        subDistrictBandarban = getResources().getStringArray(R.array.Sub_District_of_Bandarban);
        subDistrictBrahmanbaria = getResources().getStringArray(R.array.Sub_District_of_Brahmanbaria);
        subDistrictChandpur = getResources().getStringArray(R.array.Sub_District_of_Chandpur);
        subDistrictChattogram = getResources().getStringArray(R.array.Sub_District_of_Chattogram);
        subDistrictCumilla = getResources().getStringArray(R.array.Sub_District_of_Cumilla);
        subDistrictCoxBazar = getResources().getStringArray(R.array.Sub_District_of_CoxBazar);
        subDistrictFeni = getResources().getStringArray(R.array.Sub_District_of_Feni);
        subDistrictKhagrachari = getResources().getStringArray(R.array.Sub_District_of_Khagrachari);
        subDistrictLakshmipur = getResources().getStringArray(R.array.Sub_District_of_Lakshmipur);
        subDistrictNoakhali = getResources().getStringArray(R.array.Sub_District_of_Noakhali);
        subDistrictRangamati = getResources().getStringArray(R.array.Sub_District_of_Rangamati);
        subDistrictDhaka = getResources().getStringArray(R.array.Sub_District_of_Dhaka);
        subDistrictFaridpur = getResources().getStringArray(R.array.Sub_District_of_Faridpur);
        subDistrictGazipur = getResources().getStringArray(R.array.Sub_District_of_Gazipur);
        subDistrictGopalganj = getResources().getStringArray(R.array.Sub_District_of_Gopalganj);
        subDistrictKishoreganj = getResources().getStringArray(R.array.Sub_District_of_Kishoreganj);
        subDistrictMadaripur = getResources().getStringArray(R.array.Sub_District_of_Madaripur);
        subDistrictManikganj = getResources().getStringArray(R.array.Sub_District_of_Manikganj);
        subDistrictMunshiganj = getResources().getStringArray(R.array.Sub_District_of_Munshiganj);
        subDistrictNarayanganj = getResources().getStringArray(R.array.Sub_District_of_Narayanganj);
        subDistrictNarsingdi = getResources().getStringArray(R.array.Sub_District_of_Narsingdi);
        subDistrictRajbari = getResources().getStringArray(R.array.Sub_District_of_Rajbari);
        subDistrictShariatpur = getResources().getStringArray(R.array.Sub_District_of_Shariatpur);
        subDistrictTangail = getResources().getStringArray(R.array.Sub_District_of_Tangail);
        subDistrictMymensingh = getResources().getStringArray(R.array.Sub_District_of_Mymensingh);
        subDistrictJamalpur = getResources().getStringArray(R.array.Sub_District_of_Jamalpur);
        subDistrictNetrokona = getResources().getStringArray(R.array.Sub_District_of_Netrokona);
        subDistrictSherpur = getResources().getStringArray(R.array.Sub_District_of_Sherpur);
        subDistrictKhulna = getResources().getStringArray(R.array.Sub_District_of_Khulna);
        subDistrictBagerhat = getResources().getStringArray(R.array.Sub_District_of_Bagerhat);
        subDistrictChuadanga = getResources().getStringArray(R.array.Sub_District_of_Chuadanga);
        subDistrictJashore = getResources().getStringArray(R.array.Sub_District_of_Jashore);
        subDistrictJhenaidah = getResources().getStringArray(R.array.Sub_District_of_Jhenaidah);
        subDistrictKushtia = getResources().getStringArray(R.array.Sub_District_of_Kushtia);
        subDistrictMagura = getResources().getStringArray(R.array.Sub_District_of_Magura);
        subDistrictMeherpur = getResources().getStringArray(R.array.Sub_District_of_Meherpur);
        subDistrictNarail = getResources().getStringArray(R.array.Sub_District_of_Narail);
        subDistrictSatkhira = getResources().getStringArray(R.array.Sub_District_of_Satkhira);
        subDistrictRajshahi = getResources().getStringArray(R.array.Sub_District_of_Rajshahi);
        subDistrictBogura = getResources().getStringArray(R.array.Sub_District_of_Barguna);
        subDistrictJoypurhat = getResources().getStringArray(R.array.Sub_District_of_Joypurhat);
        subDistrictNaogaon = getResources().getStringArray(R.array.Sub_District_of_Naogaon);
        subDistrictNatore = getResources().getStringArray(R.array.Sub_District_of_Natore);
        subDistrictNawabganj = getResources().getStringArray(R.array.Sub_District_of_Nawabganj);
        subDistrictPabna = getResources().getStringArray(R.array.Sub_District_of_Pabna);
        subDistrictRangpur = getResources().getStringArray(R.array.Sub_District_of_Rangpur);
        subDistrictSirajgonj = getResources().getStringArray(R.array.Sub_District_of_Sirajgonj);
        subDistrictDinajpur = getResources().getStringArray(R.array.Sub_District_of_Dinajpur);
        subDistrictGaibandha = getResources().getStringArray(R.array.Sub_District_of_Gaibandha);
        subDistrictKurigram = getResources().getStringArray(R.array.Sub_District_of_Kurigram);
        subDistrictLalmonirhat = getResources().getStringArray(R.array.Sub_District_of_Lalmonirhat);
        subDistrictNilphamari = getResources().getStringArray(R.array.Sub_District_of_Nilphamari);
        subDistrictPanchagarh = getResources().getStringArray(R.array.Sub_District_of_Panchagarh);
        subDistrictThakurgaon = getResources().getStringArray(R.array.Sub_District_of_Thakurgaon);
        subDistrictSylhet = getResources().getStringArray(R.array.Sub_District_of_Sylhet);
        subDistrictHabiganj = getResources().getStringArray(R.array.Sub_District_of_Habiganj);
        subDistrictMaulvibazar = getResources().getStringArray(R.array.Sub_District_of_Maulvibazar);
        subDistrictSunamganj = getResources().getStringArray(R.array.Sub_District_of_Sunamganj);

        noDistrict = getResources().getStringArray(R.array.No_District);
        noSubDistrict = getResources().getStringArray(R.array.No_SubDistrict);



        ArrayAdapter<String> DivisionGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allDivision);
        divisionSpin.setAdapter(DivisionGroupAdapter);

        ArrayAdapter<String> DistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, noDistrict);
        districtSpin.setAdapter(DistrictGroupAdapter);

        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, noSubDistrict);
        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

        ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
        bloodGroupSpin.setAdapter(fbloodGroupAdapter);

        divisionSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                if(i > 0) {

                    String item = adapterView.getItemAtPosition(i).toString();

                    if (item.equalsIgnoreCase("dhaka")) {
                        ArrayAdapter<String> DistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allDistrictDhaka);
                        districtSpin.setAdapter(DistrictGroupAdapter);

                        districtSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                if(i>0) {

                                    String itemDhaka = adapterView.getItemAtPosition(i).toString();

                                    if (itemDhaka.equalsIgnoreCase("Dhaka")) {

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictDhaka);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){

                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }
                                    if (itemDhaka.equalsIgnoreCase("Faridpur")) {
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictFaridpur);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){

                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }
                                    if (itemDhaka.equalsIgnoreCase("Gazipur")) {

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictGazipur);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){

                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }
                                    if (itemDhaka.equalsIgnoreCase("Gopalganj")) {

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictGopalganj);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){

                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }
                                    if (itemDhaka.equalsIgnoreCase("Kishoreganj")) {

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictKishoreganj);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){

                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }
                                    if (itemDhaka.equalsIgnoreCase("Madaripur")) {

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictMadaripur);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){

                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }
                                    if (itemDhaka.equalsIgnoreCase("Manikganj")) {

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictManikganj);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){

                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }
                                    if (itemDhaka.equalsIgnoreCase("Munshiganj")) {

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictMunshiganj);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                            subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                @Override
                                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                    if(i > 0){

                                                        ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                        bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                    }
                                                }

                                                @Override
                                                public void onNothingSelected(AdapterView<?> adapterView) {

                                                }
                                            });


                                    }
                                    if (itemDhaka.equalsIgnoreCase("Narayanganj")) {

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictNarayanganj);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){

                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }
                                    if (itemDhaka.equalsIgnoreCase("Narsingdi")) {

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictNarsingdi);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){

                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }
                                    if (itemDhaka.equalsIgnoreCase("Rajbari")) {

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictRajbari);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){

                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }
                                    if (itemDhaka.equalsIgnoreCase("Shariatpur")) {

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictShariatpur);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){

                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }
                                    if (itemDhaka.equalsIgnoreCase("Tangail")) {

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictTangail);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){

                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }
                                }

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                    }
                    if (item.equalsIgnoreCase("Barishal")) {

                        ArrayAdapter<String> DistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allDistrictBarishal);
                        districtSpin.setAdapter(DistrictGroupAdapter);

                        districtSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                if(i > 0){
                                    String itemBarishal = adapterView.getItemAtPosition(i).toString();

                                    if(itemBarishal.equalsIgnoreCase("Barguna")){

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictBarguna);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){

                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }
                                    if(itemBarishal.equalsIgnoreCase("Barishal")){

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictBarishal);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){

                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }
                                    if(itemBarishal.equalsIgnoreCase("Bhola")){

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictBhola);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){

                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }
                                    if(itemBarishal.equalsIgnoreCase("Jhalokati")){

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictJhalokati);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){

                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }
                                    if(itemBarishal.equalsIgnoreCase("Patuakhali")){

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictPatuakhali);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){

                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }
                                    if(itemBarishal.equalsIgnoreCase("Pirojpur")){

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictPirojpur);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){

                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }



                                }
                        }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                    }
                    if (item.equalsIgnoreCase("Chittagong")) {

                        ArrayAdapter<String> DistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allDistrictChittagong);
                        districtSpin.setAdapter(DistrictGroupAdapter);

                        districtSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                if(i > 0){

                                    String itemChittagong = adapterView.getItemAtPosition(i).toString();

                                    if(itemChittagong.equalsIgnoreCase("Bandarban")){

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictBandarban);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){

                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }
                                    if(itemChittagong.equalsIgnoreCase("Brahmanbaria")){

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictBrahmanbaria);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){

                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }
                                    if(itemChittagong.equalsIgnoreCase("Chandpur")){

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictChandpur);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){

                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }
                                    if(itemChittagong.equalsIgnoreCase("Chattogram")){

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictChattogram);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){

                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }
                                    if(itemChittagong.equalsIgnoreCase("Cumilla")){

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictCumilla);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){

                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }
                                    if(itemChittagong.equalsIgnoreCase("Coxs Bazar")){

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictCoxBazar);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){

                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }
                                    if(itemChittagong.equalsIgnoreCase("Feni")){

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictFeni);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){

                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }
                                    if(itemChittagong.equalsIgnoreCase("Khagrachari")){

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictKhagrachari);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){

                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }
                                    if(itemChittagong.equalsIgnoreCase("Lakshmipur")){

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictLakshmipur);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){

                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }
                                    if(itemChittagong.equalsIgnoreCase("Noakhali")){

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictNoakhali);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){

                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }
                                    if(itemChittagong.equalsIgnoreCase("Rangamati")){

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictRangamati);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){

                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }


                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }
                    if (item.equalsIgnoreCase("Mymensingh")) {

                        ArrayAdapter<String> DistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allDistrictMymensingh);
                        districtSpin.setAdapter(DistrictGroupAdapter);

                        districtSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                if(i > 0){

                                    String itemMymensingh = adapterView.getItemAtPosition(i).toString();

                                    if(itemMymensingh.equalsIgnoreCase("Jamalpur")){

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictJamalpur);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){

                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }
                                    if(itemMymensingh.equalsIgnoreCase("Mymensingh")){

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictMymensingh);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){

                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }
                                    if(itemMymensingh.equalsIgnoreCase("Netrokona")){

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictNetrokona);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){

                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }
                                    if(itemMymensingh.equalsIgnoreCase("Sherpur")){

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictSherpur);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){
                                                    bloodGroupSpin.setVisibility(View.VISIBLE);
                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }

                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }
                    if (item.equalsIgnoreCase("Khulna")) {

                        ArrayAdapter<String> DistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allDistrictKhulna);
                        districtSpin.setAdapter(DistrictGroupAdapter);

                        districtSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                if(i > 0){

                                    String itemKhulna = adapterView.getItemAtPosition(i).toString();
                                    if(itemKhulna.equalsIgnoreCase("Bagerhat")){

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictBagerhat);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){

                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }
                                    if(itemKhulna.equalsIgnoreCase("Chuadanga")){

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictChuadanga);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){

                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }
                                    if(itemKhulna.equalsIgnoreCase("Jashore")){

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictJashore);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){

                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }
                                    if(itemKhulna.equalsIgnoreCase("Jhenaidah")){

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictJhenaidah);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){

                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }
                                    if(itemKhulna.equalsIgnoreCase("Khulna")){

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictKhulna);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){

                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }
                                    if(itemKhulna.equalsIgnoreCase("Kushtia")){

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictKushtia);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){

                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }
                                    if(itemKhulna.equalsIgnoreCase("Magura")){

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictMagura);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){

                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }
                                    if(itemKhulna.equalsIgnoreCase("Meherpur")){

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictMeherpur);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){

                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }
                                    if(itemKhulna.equalsIgnoreCase("Narail")){

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictNarail);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){

                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }
                                    if(itemKhulna.equalsIgnoreCase("Satkhira")){

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictSatkhira);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){

                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }

                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }
                    if (item.equalsIgnoreCase("Rajshahi")) {

                        ArrayAdapter<String> DistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allDistrictRajshahi);
                        districtSpin.setAdapter(DistrictGroupAdapter);

                        districtSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                if(i > 0){

                                    String itemRajshahi = adapterView.getItemAtPosition(i).toString();
                                    if(itemRajshahi.equalsIgnoreCase("Bogura")){

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictBogura);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){

                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }
                                    if(itemRajshahi.equalsIgnoreCase("Joypurhat")){

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictJoypurhat);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){

                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }
                                    if(itemRajshahi.equalsIgnoreCase("Naogaon")){

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictNaogaon);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){

                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }
                                    if(itemRajshahi.equalsIgnoreCase("Natore")){

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictNatore);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){

                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }
                                    if(itemRajshahi.equalsIgnoreCase("Nawabganj")){

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictNawabganj);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){

                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }
                                    if(itemRajshahi.equalsIgnoreCase("Pabna")){

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictPabna);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){

                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }
                                    if(itemRajshahi.equalsIgnoreCase("Rajshahi")){

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictRajshahi);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){

                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }
                                    if(itemRajshahi.equalsIgnoreCase("Sirajgonj")){

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictSirajgonj);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){

                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }



                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }
                    if (item.equalsIgnoreCase("Rangpur")) {

                        ArrayAdapter<String> DistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allDistrictRangpur);
                        districtSpin.setAdapter(DistrictGroupAdapter);

                        districtSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                if(i > 0){

                                    String itemRangpur = adapterView.getItemAtPosition(i).toString();
                                    if(itemRangpur.equalsIgnoreCase("Dinajpur")){

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictDinajpur);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){

                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }
                                    if(itemRangpur.equalsIgnoreCase("Gaibandha")){

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictGaibandha);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){

                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }
                                    if(itemRangpur.equalsIgnoreCase("Kurigram")){

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictKurigram);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){

                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }
                                    if(itemRangpur.equalsIgnoreCase("Lalmonirhat")){

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictLalmonirhat);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){

                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }
                                    if(itemRangpur.equalsIgnoreCase("Nilphamari")){

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictNilphamari);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){

                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }
                                    if(itemRangpur.equalsIgnoreCase("Panchagarh")){

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictPanchagarh);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){

                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }
                                    if(itemRangpur.equalsIgnoreCase("Rangpur")){

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictRangpur);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){

                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }
                                    if(itemRangpur.equalsIgnoreCase("Thakurgaon")){

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictThakurgaon);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){

                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }

                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }
                    if (item.equalsIgnoreCase("Sylhet")) {

                        ArrayAdapter<String> DistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allDistrictSylhet);
                        districtSpin.setAdapter(DistrictGroupAdapter);

                        districtSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                if(i > 0){

                                    String itemSylhet = adapterView.getItemAtPosition(i).toString();
                                    if(itemSylhet.equalsIgnoreCase("Habiganj")){

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictHabiganj);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){

                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }
                                    if(itemSylhet.equalsIgnoreCase("Maulvibazar")){

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictMaulvibazar);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){

                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }
                                    if(itemSylhet.equalsIgnoreCase("Sunamganj")){

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictSunamganj);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){

                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }
                                    if(itemSylhet.equalsIgnoreCase("Sylhet")){

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictSylhet);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                        subDistrictSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i > 0){

                                                    ArrayAdapter<String> fbloodGroupAdapter = new ArrayAdapter<String>(getActivity(), R.layout.bloodgroupspinview, R.id.bloodGroupItem, allBloodGroup);
                                                    bloodGroupSpin.setAdapter(fbloodGroupAdapter);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }

                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




        findAllDonor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_fragment, new AllDonors()).addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        searchedDonorsRecycler.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        searchedDonorsRecycler.setLayoutManager(linearLayoutManager);

        searchDonor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String fdistrict, fdivision, fsubdistrict, fbloodgroup;

                if(divisionSpin.getSelectedItemPosition() > 0){
                    fdivision = divisionSpin.getSelectedItem().toString();
                } else {
                    Toast.makeText(getActivity(), "Please select a Division" , Toast.LENGTH_SHORT).show();
                    divisionSpin.requestFocus();
                    return;
                }

                if(districtSpin.getSelectedItemPosition() > 0){
                    fdistrict = districtSpin.getSelectedItem().toString();
                }
                else {
                    fdistrict = "";
//                    Toast.makeText(getActivity(), "Please select a District" , Toast.LENGTH_SHORT).show();
//                    districtSpin.requestFocus();
//                    return;
                }

                if(subDistrictSpin.getSelectedItemPosition() > 0){
                    fsubdistrict = subDistrictSpin.getSelectedItem().toString();
                }
                else {
                     fsubdistrict = "";
//                    Toast.makeText(getActivity(), "Please select a Sub-District" , Toast.LENGTH_SHORT).show();
//                    subDistrictSpin.requestFocus();
//                    return;
                }

                if(bloodGroupSpin.getSelectedItemPosition() > 0){
                    fbloodgroup = bloodGroupSpin.getSelectedItem().toString();
                }

                else {
                    fbloodgroup = "";
//                    Toast.makeText(getActivity(), "Please select a Sub-District" , Toast.LENGTH_SHORT).show();
//                    bloodGroupSpin.requestFocus();
//                    return;
                }

                if (!fdivision.isEmpty()|| !fdistrict.isEmpty()|| !fsubdistrict.isEmpty() || !fbloodgroup.isEmpty()){
                    SearchBloodDonorRView(fdivision, fdistrict, fsubdistrict, fbloodgroup);
                } else {
                    Toast.makeText(getActivity(), "Please select a Division", Toast.LENGTH_SHORT).show();
                }

            }
        });


        return view;
    }

    private void SearchBloodDonorRView(String division, String district, String subDistrict, String bloodGroup) {

        if (!division.isEmpty() && district.isEmpty() && subDistrict.isEmpty() && bloodGroup.isEmpty()){
            Query query = userCollectionRef.whereEqualTo("uDivision", division);
            FirestoreRecyclerOptions<users> options = new FirestoreRecyclerOptions.Builder<users>()
                    .setQuery(query, users.class)
                    .build();
            searchDonorAdapter = new SearchDonorAdapter(options);
            searchedDonorsRecycler.setAdapter(searchDonorAdapter);
            searchDonorAdapter.startListening();
        }
        else if (!division.isEmpty() && !district.isEmpty() && subDistrict.isEmpty() && bloodGroup.isEmpty()){
            Query query = userCollectionRef.whereEqualTo("uDivision", division).whereEqualTo("uDistrict", district);
            FirestoreRecyclerOptions<users> options = new FirestoreRecyclerOptions.Builder<users>()
                    .setQuery(query, users.class)
                    .build();
            searchDonorAdapter = new SearchDonorAdapter(options);
            searchedDonorsRecycler.setAdapter(searchDonorAdapter);
            searchDonorAdapter.startListening();
        }
        else if (!division.isEmpty() && !district.isEmpty() && !subDistrict.isEmpty() && bloodGroup.isEmpty()){
            Query query = userCollectionRef.whereEqualTo("uDivision", division).whereEqualTo("uDistrict", district)
                    .whereEqualTo("policeStation", subDistrict);
            FirestoreRecyclerOptions<users> options = new FirestoreRecyclerOptions.Builder<users>()
                    .setQuery(query, users.class)
                    .build();
            searchDonorAdapter = new SearchDonorAdapter(options);
            searchedDonorsRecycler.setAdapter(searchDonorAdapter);
            searchDonorAdapter.startListening();
        }
        else if (!division.isEmpty() && district.isEmpty() && subDistrict.isEmpty() && !bloodGroup.isEmpty()){
            Query query = userCollectionRef.whereEqualTo("uDivision", division).whereEqualTo("bloodGroup", bloodGroup);
            FirestoreRecyclerOptions<users> options = new FirestoreRecyclerOptions.Builder<users>()
                    .setQuery(query, users.class)
                    .build();
            searchDonorAdapter = new SearchDonorAdapter(options);
            searchedDonorsRecycler.setAdapter(searchDonorAdapter);
            searchDonorAdapter.startListening();
        }

        else if (!division.isEmpty() && !district.isEmpty() && subDistrict.isEmpty() && !bloodGroup.isEmpty()){
            Query query = userCollectionRef.whereEqualTo("uDivision", division).whereEqualTo("uDistrict", district).whereEqualTo("bloodGroup", bloodGroup);
            FirestoreRecyclerOptions<users> options = new FirestoreRecyclerOptions.Builder<users>()
                    .setQuery(query, users.class)
                    .build();
            searchDonorAdapter = new SearchDonorAdapter(options);
            searchedDonorsRecycler.setAdapter(searchDonorAdapter);
            searchDonorAdapter.startListening();
        }

        else if (!division.isEmpty() && !district.isEmpty() && !subDistrict.isEmpty() && bloodGroup.isEmpty()){
            Query query = userCollectionRef.whereEqualTo("bloodGroup", bloodGroup);
            FirestoreRecyclerOptions<users> options = new FirestoreRecyclerOptions.Builder<users>()
                    .setQuery(query, users.class)
                    .build();
            searchDonorAdapter = new SearchDonorAdapter(options);
            searchedDonorsRecycler.setAdapter(searchDonorAdapter);
            searchDonorAdapter.startListening();
        }

        else {

            Query query = userCollectionRef.whereEqualTo("uDivision", division).whereEqualTo("uDistrict", district)
                    .whereEqualTo("policeStation", subDistrict).whereEqualTo("bloodGroup", bloodGroup);
            FirestoreRecyclerOptions<users> options = new FirestoreRecyclerOptions.Builder<users>()
                    .setQuery(query, users.class)
                    .build();
            searchDonorAdapter = new SearchDonorAdapter(options);
            searchedDonorsRecycler.setAdapter(searchDonorAdapter);
            searchDonorAdapter.startListening();

        }


        searchDonorAdapter.setOnFindDonorClickListener(new SearchDonorAdapter.OnFindDonorClickListener() {
            @Override
            public void OnFindDonorClick(DocumentSnapshot documentSnapshot, int position) {
                String uniqueUserId = documentSnapshot.getId();

                Bundle bundle = new Bundle();
                bundle.putString("uniUserID", uniqueUserId);

                ShowDonorsDetail showDonorsDetail = new ShowDonorsDetail();

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                showDonorsDetail.setArguments(bundle);
                fragmentTransaction.replace(R.id.container_fragment, showDonorsDetail);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
}

}
