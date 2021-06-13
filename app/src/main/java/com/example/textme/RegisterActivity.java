package com.example.textme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    EditText username, email, password;
    Button register;
    FirebaseAuth auth;
    DatabaseReference reference;
    TextView signInPage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.btn_register);
        signInPage = findViewById(R.id.signInPage);

        signInPage.setOnClickListener(view -> {
            startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
        });

        auth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uname = username.getText().toString();
                String emailid = email.getText().toString();
                String pass = password.getText().toString();

                if (TextUtils.isEmpty(uname) || TextUtils.isEmpty(emailid) || TextUtils.isEmpty(pass)){
                    Toast.makeText(RegisterActivity.this, "All Fields Are Required", Toast.LENGTH_SHORT).show();
                } else if (pass.length() < 6){
                    Toast.makeText(RegisterActivity.this, "Password must be At least 6 character", Toast.LENGTH_SHORT).show();
                } else {
                    setRegister(uname,emailid,pass);
                }
            }
        });
    }
    private void setRegister(final String username, String email, String password){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
           if (task.isSuccessful()){
               FirebaseUser firebaseUser = auth.getCurrentUser();
               assert firebaseUser != null;
               String userId = firebaseUser.getUid();

               reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

               HashMap<String, String> hashMap = new HashMap<>();
               hashMap.put("id",userId);
               hashMap.put("username",username);
               hashMap.put("imageUrl","default");

               reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       if (task.isSuccessful()){
                           Intent i = new Intent(RegisterActivity.this, ChatScreen.class);
                           Toast.makeText(RegisterActivity.this, "Register Successful......", Toast.LENGTH_SHORT).show();
                           i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                           startActivity(i);
                           finish();
                       }
                   }
               });
           } else {
               Toast.makeText(RegisterActivity.this, "Logging You In......", Toast.LENGTH_SHORT).show();
           }
            }
        });
    }
}
