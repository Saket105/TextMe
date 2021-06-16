package com.example.textme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;

import java.util.concurrent.TimeUnit;

public class OtpVarificationActivity extends AppCompatActivity {

    FirebaseAuth auth;
    DatabaseReference reference;
    EditText input1, input2, input3, input4, input5, input6;
    TextView showNumber, againRequest;
    Button signUp;
    String getOtp, enterCodeOtp;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        input1 = findViewById(R.id.input1);
        input2 = findViewById(R.id.input2);
        input3 = findViewById(R.id.input3);
        input4 = findViewById(R.id.input4);
        input5 = findViewById(R.id.input5);
        input6 = findViewById(R.id.input6);
        showNumber = findViewById(R.id.showNumber);
        signUp = findViewById(R.id.register);
        againRequest = findViewById(R.id.again_request);
        progressBar = findViewById(R.id.progressBar1);
        auth = FirebaseAuth.getInstance();

        showNumber.setText(String.format("+91-%s", getIntent().getStringExtra("mobile")));

        getOtp = getIntent().getStringExtra("backendOtp");

        signUp.setOnClickListener(view -> {
            if (!input1.getText().toString().trim().isEmpty() &&
                    !input2.getText().toString().trim().isEmpty() &&
                    !input3.getText().toString().trim().isEmpty() &&
                    !input4.getText().toString().trim().isEmpty() &&
                    !input5.getText().toString().trim().isEmpty() &&
                    !input6.getText().toString().trim().isEmpty()) {
                //Toast.makeText(this, "Otp Verify", Toast.LENGTH_SHORT).show();

                 enterCodeOtp = input1.getText().toString() +
                        input2.getText().toString() +
                        input3.getText().toString() +
                        input4.getText().toString() +
                        input5.getText().toString() +
                        input6.getText().toString();

                if (getOtp != null) {
                    progressBar.setVisibility(View.VISIBLE);
                    signUp.setVisibility(View.INVISIBLE);
                    SigningUser();

                } else {
                    Toast.makeText(this, "Check Internet Connection!", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(this, "Please enter correct OTP!", Toast.LENGTH_SHORT).show();
            }
        });
        numberOtpMoved();
        againRequest.setOnClickListener(view -> {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    "+91" + getIntent().getStringExtra("mobile"),
                    60,
                    TimeUnit.SECONDS,
                    OtpVarificationActivity.this,
                    new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        @Override
                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        }

                        @Override
                        public void onVerificationFailed(@NonNull FirebaseException e) {

                            Toast.makeText(OtpVarificationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCodeSent(@NonNull String s1, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//                                    super.onCodeSent(s, forceResendingToken);
                            getOtp = s1;
                            Toast.makeText(getApplicationContext(),"Otp Send Successfully",Toast.LENGTH_SHORT).show();
                        }
                    }
            );
        });
    }

    private void SigningUser() {
        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(getOtp, enterCodeOtp);
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                signUp.setVisibility(View.VISIBLE);
                if (task.isSuccessful()) {
                    startActivity(new Intent(getApplicationContext(), ChatScreen.class));
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(OtpVarificationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void numberOtpMoved() {

        input1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    input2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        input2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    input3.requestFocus();
                } else if (charSequence.toString().trim().isEmpty()) {
                    input1.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        input3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    input4.requestFocus();
                } else if (charSequence.toString().trim().isEmpty()) {
                    input2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        input4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    input5.requestFocus();
                } else if (charSequence.toString().trim().isEmpty()) {
                    input3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        input5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    input6.requestFocus();
                } else if (charSequence.toString().trim().isEmpty()) {
                    input4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        input6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().isEmpty()) {
                    input5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
