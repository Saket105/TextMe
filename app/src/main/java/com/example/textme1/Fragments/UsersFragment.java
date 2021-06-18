package com.example.textme1.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.textme1.Adapter.UserAdapter;
import com.example.textme1.Model.User;
import com.example.textme1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class UsersFragment extends Fragment {

     RecyclerView recyclerView;
     List<User> mUsers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        mUsers = new ArrayList<>();
        
        readUsers();
        return view;
    }

    private void readUsers() {
        mUsers.clear();
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.child("personal_data").child(firebaseUser.getUid())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists())
//                {
//                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
//                        User user = snapshot.getValue(User.class);
//                        mUsers.add(user);
//                        Toast.makeText(getContext(), user.getId(), Toast.LENGTH_SHORT).show();
//                    }
//                    UserAdapter userAdapter1 = new UserAdapter(getContext(), mUsers);
////                    recyclerView.setAdapter(userAdapter1);
//                }else {
//                    Toast.makeText(getContext(),"No data to show",Toast.LENGTH_SHORT).show();
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
