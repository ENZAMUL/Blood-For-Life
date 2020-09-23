package com.enzamuls.blooddonation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private String[] allDivision, allDistrictBarishal, allDistrictChittagong, allDistrictDhaka,
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
    private Spinner divisionSpin, districtSpin, subDistrictSpin;





    private String[] bloodGroup;
    private Spinner bloodGroupSpinner;

    private EditText fullName, fullEmail, userContact, area, gpassword, regTotalDonate;
    private CheckBox hideContact, beDonor;
    private RadioGroup gender;
    private RadioButton btnGender;
    private TextView userBloodGroupTextView, birthDate, regLastDonateDate;
    private ProgressBar progressBar;
    private Button registerBtn;
    private DatePickerDialog.OnDateSetListener showDate, showLastDonate;

    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;

    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        if(mAuth.getCurrentUser() != null) {
            startActivity(new Intent(RegistrationActivity.this, UserDashboard.class));
            finish();
        }

        regTotalDonate = findViewById(R.id.giveTotalDonateReg);
        regLastDonateDate = findViewById(R.id.giveLastDonateReg);

        fullName = findViewById(R.id.fullName);
        fullEmail = findViewById(R.id.fullEmail);
        userContact = findViewById(R.id.contactno);
        birthDate = findViewById(R.id.dateofbirth);
        userBloodGroupTextView = findViewById(R.id.bloodGroupItem);

        area = findViewById(R.id.areaname);
        gpassword = findViewById(R.id.givePassword);
        hideContact = findViewById(R.id.hideContact);
        gender = findViewById(R.id.genderId);
        progressBar = findViewById(R.id.progressBar);
        registerBtn = findViewById(R.id.btnRegisterUser);
        beDonor = findViewById(R.id.beDonor);

        beDonor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b == true){
                    regTotalDonate.setVisibility(View.VISIBLE);
                    regLastDonateDate.setVisibility(View.VISIBLE);
                } else {
                    regTotalDonate.setVisibility(View.GONE);
                    regLastDonateDate.setVisibility(View.GONE);
                }
            }
        });

        divisionSpin = findViewById(R.id.divisionSpinnerReg);
        districtSpin = findViewById(R.id.districtSpinnerReg);
        subDistrictSpin = findViewById(R.id.thanaSpnnierReg);
        bloodGroupSpinner = findViewById(R.id.bloodGroupSpinner);

        bloodGroup = getResources().getStringArray(R.array.bloodGroup);

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



        ArrayAdapter<String> bloodGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, bloodGroup);
        bloodGroupSpinner.setAdapter(bloodGroupAdapter);


        ArrayAdapter<String> DivisionGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, allDivision);
        divisionSpin.setAdapter(DivisionGroupAdapter);

        ArrayAdapter<String> DistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, noDistrict);
        districtSpin.setAdapter(DistrictGroupAdapter);

        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, noSubDistrict);
        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

        divisionSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(i > 0) {

                    String item = adapterView.getItemAtPosition(i).toString();

                    if (item.equalsIgnoreCase("dhaka")) {
                        ArrayAdapter<String> DistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, allDistrictDhaka);
                        districtSpin.setAdapter(DistrictGroupAdapter);

                        districtSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                if(i>0) {

                                    String itemDhaka = adapterView.getItemAtPosition(i).toString();

                                    if (itemDhaka.equalsIgnoreCase("Dhaka")) {

                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictDhaka);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);
                                    }
                                    if (itemDhaka.equalsIgnoreCase("Faridpur")) {
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictFaridpur);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                    }
                                    if (itemDhaka.equalsIgnoreCase("Gazipur")) {
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictGazipur);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                    }
                                    if (itemDhaka.equalsIgnoreCase("Gopalganj")) {
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictGopalganj);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                    }
                                    if (itemDhaka.equalsIgnoreCase("Kishoreganj")) {
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictKishoreganj);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                    }
                                    if (itemDhaka.equalsIgnoreCase("Madaripur")) {
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictMadaripur);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);
                                    }
                                    if (itemDhaka.equalsIgnoreCase("Manikganj")) {
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictManikganj);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);
                                    }
                                    if (itemDhaka.equalsIgnoreCase("Munshiganj")) {
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictMunshiganj);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);


                                    }
                                    if (itemDhaka.equalsIgnoreCase("Narayanganj")) {
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictNarayanganj);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                    }
                                    if (itemDhaka.equalsIgnoreCase("Narsingdi")) {
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictNarsingdi);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                    }
                                    if (itemDhaka.equalsIgnoreCase("Rajbari")) {
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictRajbari);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);
                                    }
                                    if (itemDhaka.equalsIgnoreCase("Shariatpur")) {
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictShariatpur);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);
                                    }
                                    if (itemDhaka.equalsIgnoreCase("Tangail")) {
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictTangail);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);
                                    }
                                }

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                    }
                    if (item.equalsIgnoreCase("Barishal")) {
                        ArrayAdapter<String> DistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, allDistrictBarishal);
                        districtSpin.setAdapter(DistrictGroupAdapter);

                        districtSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                if(i > 0){
                                    String itemBarishal = adapterView.getItemAtPosition(i).toString();

                                    if(itemBarishal.equalsIgnoreCase("Barguna")){
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictBarguna);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                    }
                                    if(itemBarishal.equalsIgnoreCase("Barishal")){
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictBarishal);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                    }
                                    if(itemBarishal.equalsIgnoreCase("Bhola")){
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictBhola);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                    }
                                    if(itemBarishal.equalsIgnoreCase("Jhalokati")){
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictJhalokati);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                    }
                                    if(itemBarishal.equalsIgnoreCase("Patuakhali")){
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictPatuakhali);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                    }
                                    if(itemBarishal.equalsIgnoreCase("Pirojpur")){
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictPirojpur);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);
                                    }



                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                    }
                    if (item.equalsIgnoreCase("Chittagong")) {
                        ArrayAdapter<String> DistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, allDistrictChittagong);
                        districtSpin.setAdapter(DistrictGroupAdapter);

                        districtSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                if(i > 0){

                                    String itemChittagong = adapterView.getItemAtPosition(i).toString();

                                    if(itemChittagong.equalsIgnoreCase("Bandarban")){
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictBandarban);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                    }
                                    if(itemChittagong.equalsIgnoreCase("Brahmanbaria")){
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictBrahmanbaria);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                    }
                                    if(itemChittagong.equalsIgnoreCase("Chandpur")){
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictChandpur);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                    }
                                    if(itemChittagong.equalsIgnoreCase("Chattogram")){
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictChattogram);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);


                                    }
                                    if(itemChittagong.equalsIgnoreCase("Cumilla")){
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictCumilla);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                    }
                                    if(itemChittagong.equalsIgnoreCase("Coxs Bazar")){
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictCoxBazar);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                    }
                                    if(itemChittagong.equalsIgnoreCase("Feni")){
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictFeni);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                    }
                                    if(itemChittagong.equalsIgnoreCase("Khagrachari")){
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictKhagrachari);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                    }
                                    if(itemChittagong.equalsIgnoreCase("Lakshmipur")){
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictLakshmipur);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                    }
                                    if(itemChittagong.equalsIgnoreCase("Noakhali")){
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictNoakhali);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                    }
                                    if(itemChittagong.equalsIgnoreCase("Rangamati")){
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictRangamati);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                    }


                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }
                    if (item.equalsIgnoreCase("Mymensingh")) {
                        ArrayAdapter<String> DistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, allDistrictMymensingh);
                        districtSpin.setAdapter(DistrictGroupAdapter);

                        districtSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                if(i > 0){

                                    String itemMymensingh = adapterView.getItemAtPosition(i).toString();

                                    if(itemMymensingh.equalsIgnoreCase("Jamalpur")){
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictJamalpur);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                    }
                                    if(itemMymensingh.equalsIgnoreCase("Mymensingh")){
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictMymensingh);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                    }
                                    if(itemMymensingh.equalsIgnoreCase("Netrokona")){
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictNetrokona);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                    }
                                    if(itemMymensingh.equalsIgnoreCase("Sherpur")){
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictSherpur);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                    }

                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }
                    if (item.equalsIgnoreCase("Khulna")) {
                        ArrayAdapter<String> DistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, allDistrictKhulna);
                        districtSpin.setAdapter(DistrictGroupAdapter);

                        districtSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                if(i > 0){

                                    String itemKhulna = adapterView.getItemAtPosition(i).toString();
                                    if(itemKhulna.equalsIgnoreCase("Bagerhat")){
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictBagerhat);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                    }
                                    if(itemKhulna.equalsIgnoreCase("Chuadanga")){
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictChuadanga);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                    }
                                    if(itemKhulna.equalsIgnoreCase("Jashore")){
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictJashore);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                    }
                                    if(itemKhulna.equalsIgnoreCase("Jhenaidah")){
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictJhenaidah);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                    }
                                    if(itemKhulna.equalsIgnoreCase("Khulna")){
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictKhulna);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                    }
                                    if(itemKhulna.equalsIgnoreCase("Kushtia")){
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictKushtia);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                    }
                                    if(itemKhulna.equalsIgnoreCase("Magura")){
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictMagura);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                    }
                                    if(itemKhulna.equalsIgnoreCase("Meherpur")){
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictMeherpur);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                    }
                                    if(itemKhulna.equalsIgnoreCase("Narail")){
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictNarail);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                    }
                                    if(itemKhulna.equalsIgnoreCase("Satkhira")){
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictSatkhira);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                    }

                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }
                    if (item.equalsIgnoreCase("Rajshahi")) {
                        ArrayAdapter<String> DistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, allDistrictRajshahi);
                        districtSpin.setAdapter(DistrictGroupAdapter);

                        districtSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                if(i > 0){

                                    String itemRajshahi = adapterView.getItemAtPosition(i).toString();
                                    if(itemRajshahi.equalsIgnoreCase("Bogura")){
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictBogura);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                    }
                                    if(itemRajshahi.equalsIgnoreCase("Joypurhat")){
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictJoypurhat);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                    }
                                    if(itemRajshahi.equalsIgnoreCase("Naogaon")){
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictNaogaon);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                    }
                                    if(itemRajshahi.equalsIgnoreCase("Natore")){
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictNatore);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                    }
                                    if(itemRajshahi.equalsIgnoreCase("Nawabganj")){
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictNawabganj);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                    }
                                    if(itemRajshahi.equalsIgnoreCase("Pabna")){
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictPabna);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                    }
                                    if(itemRajshahi.equalsIgnoreCase("Rajshahi")){
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictRajshahi);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);


                                    }
                                    if(itemRajshahi.equalsIgnoreCase("Sirajgonj")){
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictSirajgonj);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                    }



                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }
                    if (item.equalsIgnoreCase("Rangpur")) {
                        ArrayAdapter<String> DistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, allDistrictRangpur);
                        districtSpin.setAdapter(DistrictGroupAdapter);

                        districtSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                if(i > 0){

                                    String itemRangpur = adapterView.getItemAtPosition(i).toString();
                                    if(itemRangpur.equalsIgnoreCase("Dinajpur")){
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictDinajpur);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                    }
                                    if(itemRangpur.equalsIgnoreCase("Gaibandha")){
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictGaibandha);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);
                                    }
                                    if(itemRangpur.equalsIgnoreCase("Kurigram")){
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictKurigram);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);
                                    }
                                    if(itemRangpur.equalsIgnoreCase("Lalmonirhat")){
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictLalmonirhat);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                    }
                                    if(itemRangpur.equalsIgnoreCase("Nilphamari")){
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictNilphamari);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                    }
                                    if(itemRangpur.equalsIgnoreCase("Panchagarh")){
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictPanchagarh);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                    }
                                    if(itemRangpur.equalsIgnoreCase("Rangpur")){
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictRangpur);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                    }
                                    if(itemRangpur.equalsIgnoreCase("Thakurgaon")){
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictThakurgaon);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                    }

                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }
                    if (item.equalsIgnoreCase("Sylhet")) {
                        ArrayAdapter<String> DistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, allDistrictSylhet);
                        districtSpin.setAdapter(DistrictGroupAdapter);

                        districtSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                if(i > 0){

                                    String itemSylhet = adapterView.getItemAtPosition(i).toString();
                                    if(itemSylhet.equalsIgnoreCase("Habiganj")){
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictHabiganj);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                    }
                                    if(itemSylhet.equalsIgnoreCase("Maulvibazar")){
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictMaulvibazar);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                    }
                                    if(itemSylhet.equalsIgnoreCase("Sunamganj")){
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictSunamganj);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

                                    }
                                    if(itemSylhet.equalsIgnoreCase("Sylhet")){
                                        ArrayAdapter<String> SubDistrictGroupAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.bloodgroupspinview, R.id.bloodGroupItem, subDistrictSylhet);
                                        subDistrictSpin.setAdapter(SubDistrictGroupAdapter);

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






        birthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(RegistrationActivity.this,
                         android.R.style.Theme_Holo_Light_Dialog_MinWidth, showDate, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        showDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month+1;
                String date = day+"/"+month+"/"+year;
                birthDate.setText(date);
            }
        };

        regLastDonateDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(RegistrationActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, showLastDonate, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        showLastDonate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month+1;
                String date = day+"/"+month+"/"+year;
                regLastDonateDate.setText(date);
            }
        };

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    private void registerUser(){

        final int totalDonateInit;
        final String lastDonateDateInit;

        final String email = fullEmail.getText().toString().trim();
        String password = gpassword.getText().toString().trim();

        //userData
        final String name = fullName.getText().toString();
        final String phone = userContact.getText().toString();
        final String hidePhone;
        if(hideContact.isChecked()){
            hidePhone = "true";
        } else {
            hidePhone = "false";
        }

        final String dateOfBirth = birthDate.getText().toString();
        final String genderName;
        int selectedGenderId = gender.getCheckedRadioButtonId();
        if(selectedGenderId < 0){
            Toast.makeText(RegistrationActivity.this, "Select your gender", Toast.LENGTH_SHORT ).show();
            return;
        } else {
            btnGender = findViewById(selectedGenderId);
        }
        genderName = btnGender.getText().toString();

        final String userBloodGroupf;
        if(bloodGroupSpinner.getSelectedItemPosition() > 0){
                userBloodGroupf = bloodGroupSpinner.getSelectedItem().toString();

        } else {
            Toast.makeText(RegistrationActivity.this, "Select Your Blood Group", Toast.LENGTH_SHORT ).show();
            return;
        }
            final String userDivision;
            final String userDistrict;
            final String userThana;

        if(divisionSpin.getSelectedItemPosition() > 0){
            userDivision = divisionSpin.getSelectedItem().toString();

        } else {
            Toast.makeText(RegistrationActivity.this, "Select Your Division", Toast.LENGTH_SHORT ).show();
            return;
        }

        if(districtSpin.getSelectedItemPosition() > 0){
            userDistrict = districtSpin.getSelectedItem().toString();

        } else {
            Toast.makeText(RegistrationActivity.this, "Select Your District", Toast.LENGTH_SHORT ).show();
            return;
        }

        if(subDistrictSpin.getSelectedItemPosition() > 0){
            userThana = subDistrictSpin.getSelectedItem().toString();

        } else {
            Toast.makeText(RegistrationActivity.this, "Select Your Subdistrict", Toast.LENGTH_SHORT ).show();
            return;
        }

            final String userArea = area.getText().toString();
            final String donor;
            if(beDonor.isChecked()){

                donor = "true";

                totalDonateInit = Integer.valueOf(String.valueOf(regTotalDonate.getText()));

                lastDonateDateInit = regLastDonateDate.getText().toString();

                if(!lastDonateDateInit.isEmpty()){

                    SimpleDateFormat sdf3 = new SimpleDateFormat("dd/MM/yyyy");
                    Date newDonateDate = null;
                    try {
                        newDonateDate = sdf3.parse(lastDonateDateInit);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (System.currentTimeMillis() < newDonateDate.getTime()) {
                        regLastDonateDate.setError("Greater than current Date isn't allowed!");
                        regLastDonateDate.requestFocus();
                        return;
                    }
                }
            } else {
                donor = "false";
                totalDonateInit = 0;
                lastDonateDateInit = "Didnt donate yet";
            }

            if(name.isEmpty()){
                fullName.setError("Enter a Name");
                fullName.requestFocus();
                return;
            }

            //checking the validity of the email
            else if(email.isEmpty())
            {
                fullEmail.setError("Enter an email address");
                fullEmail.requestFocus();
                return;
            }

            else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
            {
                fullEmail.setError("Enter a valid email address");
                fullEmail.requestFocus();
                return;
            }

            else if(phone.length() < 11){
                userContact.setError("Enter a valid number");
                userContact.requestFocus();
                return;
            }



            //checking the validity of the password
            else if(password.isEmpty())
            {
                gpassword.setError("Enter a password");
                gpassword.requestFocus();
                return;
            }
            else if(password.length() < 6)
            {
                gpassword.setError("Password must be greater than 6 characters");
                gpassword.requestFocus();
                return;
            }

            else if(dateOfBirth.isEmpty()){
                birthDate.setError("Enter your Date of Birth");
                birthDate.requestFocus();
                return;
            }

            else if(genderName.isEmpty()){
                btnGender.setError("Enter your Date of Birth");
                btnGender.requestFocus();
                return;
            }

            else {
                if (userArea.isEmpty()) {
                    area.setError("Enter your Area");
                    area.requestFocus();
                    return;
                }
            }

            progressBar.setVisibility(View.VISIBLE);

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){

                        userID = mAuth.getCurrentUser().getUid();
                        final FirebaseUser cUser = mAuth.getCurrentUser();

                        DocumentReference documentReference = fStore.collection("users").document(userID);

                        Map<String, Object> newUser = new HashMap<>();
                        newUser.put("userName", name);
                        newUser.put("email", email);
                        newUser.put("contactNo", phone);
                        newUser.put("hideContact", hidePhone);
                        newUser.put("birthDate", dateOfBirth);
                        newUser.put("gender", genderName);
                        newUser.put("bloodGroup", userBloodGroupf);
                        newUser.put("uDivision", userDivision);
                        newUser.put("uDistrict", userDistrict);
                        newUser.put("policeStation", userThana);
                        newUser.put("uArea", userArea);
                        newUser.put("isDonor", donor);

                        documentReference.set(newUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                DocumentReference documentReferenceType = fStore.collection("userrole").document(userID);

                                Map<String, Object> newUserRole = new HashMap<>();
                                newUserRole.put("rolename", "user");

                                documentReferenceType.set(newUserRole).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        if(donor.equals("true")){
                                            int donate = 0;
                                            DocumentReference documentReference2 = fStore.collection("donors").document(userID);

                                            Map<String, Object> newDonor = new HashMap<>();
                                            newDonor.put("totalDonate", totalDonateInit);
                                            newDonor.put("lastDonate", lastDonateDateInit);
                                            newDonor.put("hasDisease", "false");
                                            newDonor.put("userName", name);


                                            documentReference2.set(newDonor).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                    cUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(RegistrationActivity.this, "Registration Successful!", Toast.LENGTH_SHORT ).show();
                                                            startActivity(new Intent(RegistrationActivity.this, VerifyEmailActivity.class));
                                                            finish();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(RegistrationActivity.this, "Email verification code not sent! Try Again.!", Toast.LENGTH_SHORT ).show();
                                                            startActivity(new Intent(RegistrationActivity.this, VerifyEmailActivity.class));
                                                            finish();
                                                        }
                                                    });


                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(RegistrationActivity.this, "Registration Failed!", Toast.LENGTH_SHORT ).show();
                                                }
                                            });
                                        }
                                        if(donor.equals("false")){
                                            int donate = 0;
                                            DocumentReference documentReference2 = fStore.collection("donors").document(userID);

                                            Map<String, Object> newDonor = new HashMap<>();
                                            newDonor.put("totalDonate", totalDonateInit);
                                            newDonor.put("lastDonate", lastDonateDateInit);
                                            newDonor.put("hasDisease", "false");
                                            newDonor.put("userName", name);


                                            documentReference2.set(newDonor).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    cUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            progressBar.setVisibility(View.GONE);
                                                            startActivity(new Intent(RegistrationActivity.this, VerifyEmailActivity.class));
                                                            Toast.makeText(RegistrationActivity.this, "Registration Successful!", Toast.LENGTH_SHORT ).show();
                                                            finish();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            progressBar.setVisibility(View.GONE);
                                                            startActivity(new Intent(RegistrationActivity.this, UserDashboard.class));
                                                            Toast.makeText(RegistrationActivity.this, "Verification mail sent Failed! Try Again", Toast.LENGTH_SHORT ).show();
                                                            finish();
                                                        }
                                                    });
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    progressBar.setVisibility(View.GONE);
                                                    Toast.makeText(RegistrationActivity.this, "Registration Failed!", Toast.LENGTH_SHORT ).show();
                                                }
                                            });
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(RegistrationActivity.this, "Registration Failed!", Toast.LENGTH_SHORT ).show();
                                    }
                                });

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(RegistrationActivity.this, "Registration Failed!", Toast.LENGTH_SHORT ).show();
                            }
                        });


                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(RegistrationActivity.this, "Registration Failed! User Name Exit!", Toast.LENGTH_SHORT ).show();
                    }
                }
            });

    }

}