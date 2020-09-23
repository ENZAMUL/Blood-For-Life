package com.enzamuls.blooddonation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class VerifyEmailActivity extends AppCompatActivity {

    private Button verifyNow, verifyLater, reLogin;

    private FirebaseUser cuurentUser;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);

        verifyNow = findViewById(R.id.btnVerifyNow);
        verifyLater = findViewById(R.id.btnVerifLater);
        reLogin = findViewById(R.id.btnVerifyRelogin);

        fAuth = FirebaseAuth.getInstance();
        cuurentUser = fAuth.getCurrentUser();

        if(cuurentUser.isEmailVerified() && cuurentUser != null){
            startActivity(new Intent(VerifyEmailActivity.this, UserDashboard.class));
            finish();
        }

        verifyLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( cuurentUser != null){
                    startActivity(new Intent(VerifyEmailActivity.this, UserDashboard.class));
                    finish();
                }
            }
        });

        verifyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cuurentUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(VerifyEmailActivity.this, "Verification mail sent Successfully!", Toast.LENGTH_SHORT ).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(VerifyEmailActivity.this, "Verification mail sent Failed! Try Again", Toast.LENGTH_SHORT ).show();
                    }
                });
            }
        });

        reLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fAuth.signOut();
                startActivity(new Intent(VerifyEmailActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}