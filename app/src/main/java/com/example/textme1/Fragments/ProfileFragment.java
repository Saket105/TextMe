package com.example.textme1.Fragments;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.textme1.ChatScreen;
import com.example.textme1.DetailActivity;
import com.example.textme1.Model.User;
import com.example.textme1.R;
import com.google.android.gms.tasks.Continuation;
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
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    ImageView image_profile;
    TextView username;
    Button openGallery;

    DatabaseReference reference;
    FirebaseUser fuser;
    String userId,name,imageUrl;

    StorageReference storageReference;

    private Uri imageUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        image_profile = view.findViewById(R.id.profile_image);
        username = view.findViewById(R.id.username);
        openGallery = view.findViewById(R.id.update_photo_name);

        storageReference = FirebaseStorage.getInstance().getReference();

        fuser = FirebaseAuth.getInstance().getCurrentUser();
//        String userId = fuser.getUid();
////        username.setText(userId);
        reference = FirebaseDatabase.getInstance().getReference("users");
        FirebaseDatabase.getInstance()
                .getReference("users")
                .child("personal_data")
                .child(fuser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                /*User user = dataSnapshot.getValue(User.class);
                username.setText(user.getUsername());
                if (user.getImageUrl().equals("default"))
                {
                    image_profile.setImageResource(R.mipmap.ic_launcher);
                } else{
                    Glide.with(getContext()).load(user.getImageUrl()).into(image_profile);
                }*/
                if (dataSnapshot.exists()){
                    String name = dataSnapshot.child("username").getValue().toString();
                    username.setText(name);
                    Glide.with(getContext())
                            .load(dataSnapshot.child("imageUrl").getValue().toString())
                            .into(image_profile);
                }else {
                    Toast.makeText(getContext(), "No data to show!", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        image_profile.setOnClickListener(v -> {
//            openImage();
        });

        openGallery.setOnClickListener(v -> {
            userId = fuser.getUid();
//            updateData(userId);
        });

        return view;
    }

    private void updateData(String userId) {
        StorageReference storageRef = storageReference.child("profileImages/")
                .child("img"+System.currentTimeMillis());

        storageRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
               storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                   @Override
                   public void onSuccess(Uri uri) {
                       name = username.getText().toString();
                       imageUrl = uri.toString();

                       Map<String, Object> map = new HashMap<>();
                       map.put("username",username);
                       map.put("imageUrl",imageUrl);

                       reference.child("personal_data")
                               .child(userId).addValueEventListener(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot snapshot) {
                               if (snapshot.exists()){
                                   reference.child("personal_data").child(userId).updateChildren(map);
//                                    progressDialog.dismiss();
//                                   reference.setValue(map);
                                   /*startActivity(new Intent(DetailActivity.this, ChatScreen.class));
                                   finish();*/
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
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openImage() {

        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser
                (i, "Select Image from Phone"), 101);
    }

   /* private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }*/



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == RESULT_OK)
        {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                image_profile.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
