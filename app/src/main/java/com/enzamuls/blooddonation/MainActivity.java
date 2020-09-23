package com.enzamuls.blooddonation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private Button btnRegister, btnLogin;
    private EditText userEmail, inputPassword;
    private ProgressBar progressBar;
    private TextView forgotPass;

    private FirebaseAuth mAuth;
    private FirebaseUser cuser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        cuser = mAuth.getCurrentUser();

        if (mAuth.getCurrentUser() != null && !cuser.isEmailVerified()) {
            startActivity(new Intent(MainActivity.this, VerifyEmailActivity.class));
            finish();
        }
        if(mAuth.getCurrentUser() != null && cuser.isEmailVerified()){
            startActivity(new Intent(MainActivity.this, UserDashboard.class));
            finish();
        }

            btnRegister = findViewById(R.id.btnRegister);
            btnLogin = findViewById(R.id.btnLogin);

            userEmail = findViewById(R.id.inputEmail);
            inputPassword = findViewById(R.id.inputPassword);
            progressBar = findViewById(R.id.progressBarLogin);

        forgotPass = findViewById(R.id.forgotPassword);

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });


            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String email = userEmail.getText().toString().trim();
                    String password = inputPassword.getText().toString().trim();

                    if (email.isEmpty()) {
                        userEmail.setError("Enter an email address!");
                        userEmail.requestFocus();
                        return;
                    }

                    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        userEmail.setError("Enter a valid email address!");
                        userEmail.requestFocus();
                        return;
                    }

                    if (password.isEmpty()) {
                        inputPassword.setError("Enter your password!");
                        inputPassword.requestFocus();
                        return;
                    }

                    progressBar.setVisibility(View.VISIBLE);

                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                startActivity(new Intent(MainActivity.this, UserDashboard.class));
                                Toast.makeText(MainActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(MainActivity.this, "Login Failed! Please check your email and password!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });

        }

}