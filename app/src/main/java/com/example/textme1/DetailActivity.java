package com.example.textme1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.textme1.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {

    TextView edtphoneNumber, imageUrlView;
    EditText edtName;
    ImageView profileImg;
    ImageButton selectImg;
    RadioGroup radioGroup;
    Button submitButton;
    RadioButton selectedRadioBtn;
    String gender, name, phoneNumbers, imagesUrl;
    Uri imageUri;
    DatabaseReference reference;
    StorageReference storageReference;
    String userId;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        edtphoneNumber = findViewById(R.id.edt_phone);
        radioGroup = findViewById(R.id.group);
        submitButton = findViewById(R.id.submit_btn);
        edtName = findViewById(R.id.edt_name);
        profileImg = findViewById(R.id.profile_img);
        selectImg = findViewById(R.id.profile_update_btn);
        imageUrlView = findViewById(R.id.img_url);

        progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog.setTitle("Data Uploading!");
        progressDialog.setMessage("Please wait your data is uploading!");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        edtphoneNumber.setText(String.format("%s", getIntent().getStringExtra("mob")));
        reference = FirebaseDatabase.getInstance().getReference("users");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        userId = user.getUid();

        storageReference = FirebaseStorage.getInstance().getReference();

        selectImg.setOnClickListener(view -> {
            selectImageFromPhone();
        });

        submitButton.setOnClickListener(view -> {
            selectedRadioBtn = radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
            if (selectedRadioBtn != null)
            {
                gender = selectedRadioBtn.getText().toString();
            }
            upDateProfile(userId);
        });
    }

    private void upDateProfile(String userId) {

//        progressDialog.show();
        StorageReference storageRef = storageReference.child("profileImages/")
                .child("img"+System.currentTimeMillis());
        storageRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        name = edtName.getText().toString();
                        phoneNumbers = edtphoneNumber.getText().toString();
                        String gen = gender;
                        imagesUrl = uri.toString();

                        Map<String, Object> map = new HashMap<>();
                        map.put("gender",gen);
                        map.put("imageUrl",imagesUrl);
                        map.put("id",userId);
                        map.put("phoneNumber",phoneNumbers);
                        map.put("username",name);
                        reference.child("personal_data")
                                .child(userId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    //reference.child("personal_data").child(userId).updateChildren(map);
//                                    progressDialog.dismiss();
                                    reference.child("personal_data").child(userId).setValue(map);
                                    startActivity(new Intent(DetailActivity.this,ChatScreen.class));
                                    finish();
                                }
                                else
                                    {
//                                  reference.child("personal_data").child(userId).setValue(map);
                                    }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(DetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

            }
        });

    }


    private void selectImageFromPhone() {
        Intent imageIntent = new Intent();
        imageIntent.setType("image/*");
        imageIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser
                (imageIntent, "Select Image from Phone"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            imageUri = data.getData();
            imageUrlView.setText(imageUri.toString());
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                profileImg.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}