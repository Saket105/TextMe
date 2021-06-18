package com.example.textme1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.textme1.Fragments.ChatsFragment;
import com.example.textme1.Fragments.ProfileFragment;
import com.example.textme1.Fragments.UsersFragment;
import com.example.textme1.Model.User;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatScreen extends AppCompatActivity {

    CircleImageView profile_image;
    TextView username;
    DatabaseReference reference;
    List<User> userList;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("users");
        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.view_pager);
        userList = new ArrayList<>();

        ViewPageAdapter viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager());
        viewPageAdapter.addFragment(new ChatsFragment(),"Chats");
        viewPageAdapter.addFragment(new UsersFragment(),"Users");
        viewPageAdapter.addFragment(new ProfileFragment(),"Profile");

        viewPager.setAdapter(viewPageAdapter);

        tabLayout.setupWithViewPager(viewPager);

        Toolbar toolbar = findViewById(R.id.toolbarCustom);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");

        firebaseUser = mAuth.getCurrentUser();
        loaddatafromserver(firebaseUser);
    }

    private void loaddatafromserver(FirebaseUser firebaseUser1) {
        String uId = firebaseUser1.getUid();

       FirebaseDatabase.getInstance()
               .getReference("users")
               .child("personal_data")
               .child(uId)
               .addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                      /* if (snapshot.exists()){
                           for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                               User user1 = dataSnapshot.getValue(User.class);
                               assert user1 != null;
                               username.setText(user1.getUsername());
                               Glide.with(getApplicationContext()).load(user1.getImageUrl()).into(profile_image);
                               String id = user1.getId();
                               Toast.makeText(getApplicationContext(),id,Toast.LENGTH_SHORT).show();
                           }
                       }else{
                           Toast.makeText(ChatScreen.this, "No data to show!", Toast.LENGTH_SHORT).show();
                       }*/
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {

                   }
               });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ChatScreen.this,StartActivity.class));
                finish();
                Toast.makeText(this, "Logged Out.....", Toast.LENGTH_SHORT).show();
                return true;
        }
        return false;
    }

    class ViewPageAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private  ArrayList<String> titles;

        ViewPageAdapter(FragmentManager fm){
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment, String title){
            fragments.add(fragment);
            titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
}
