package com.example.textme;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth auth;
    TextInputLayout numberInputLayout;
    Button continueButton;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        numberInputLayout = findViewById(R.id.number);
        continueButton = findViewById(R.id.continue1);
        progressBar = findViewById(R.id.progressBar);
        auth = FirebaseAuth.getInstance();

        continueButton.setOnClickListener(view -> {
            if (!numberInputLayout.getEditText().getText().toString().trim().isEmpty()) {
                if (numberInputLayout.getEditText().getText().toString().trim().length() == 10) {
//                    startActivity(new Intent(this,OtpVarificationActivity.class)
//                            .putExtra("mobile",numberInputLayout.getEditText().getText().toString()));

                    progressBar.setVisibility(View.VISIBLE);
                    continueButton.setVisibility(View.INVISIBLE);
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            "+91" + numberInputLayout.getEditText().getText().toString(),
                            60,
                            TimeUnit.SECONDS,
                            LoginActivity.this,
                            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                @Override
                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                    progressBar.setVisibility(View.GONE);
                                    continueButton.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onVerificationFailed(@NonNull FirebaseException e) {
                                    progressBar.setVisibility(View.GONE);
                                    continueButton.setVisibility(View.VISIBLE);
                                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//                                    super.onCodeSent(s, forceResendingToken);
                                    progressBar.setVisibility(View.GONE);
                                    continueButton.setVisibility(View.VISIBLE);
                                    startActivity(new Intent(LoginActivity.this, OtpVarificationActivity.class)
                                            .putExtra("mobile", numberInputLayout.getEditText().getText().toString())
                                            .putExtra("backendOtp",s));
                                }
                            }
                    );
                } else {
                    numberInputLayout.getEditText().setError("Invalid Number!");
                    return;
                }
            } else {
                Toast.makeText(this, "First enter mobile number", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
