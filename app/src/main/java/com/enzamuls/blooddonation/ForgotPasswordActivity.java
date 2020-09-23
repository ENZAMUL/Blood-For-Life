package com.enzamuls.blooddonation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText inputEmailForgetInfo;
    private Button sendMail;
    private ProgressBar forgetPassBar;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        inputEmailForgetInfo = findViewById(R.id.inputEmailForgotInfo);
        sendMail = findViewById(R.id.btnConfirmation);
        forgetPassBar = findViewById(R.id.progressBarForgetPass);

        fAuth = FirebaseAuth.getInstance();

        sendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String userEmail = inputEmailForgetInfo.getText().toString().trim();

                forgetPassBar.setVisibility(View.VISIBLE);

                if(!userEmail.isEmpty()){
                    fAuth.sendPasswordResetEmail(userEmail).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            forgetPassBar.setVisibility(View.GONE);
                            Toast.makeText(ForgotPasswordActivity.this, "Password reset link sent successfully!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ForgotPasswordActivity.this, MainActivity.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            forgetPassBar.setVisibility(View.GONE);
                            Toast.makeText(ForgotPasswordActivity.this, "Password reset link sent failed!", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });
    }
}