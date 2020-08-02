package com.trainchatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trainchatapp.model.User;
import com.trainchatapp.utils.DBUtils;
import com.trainchatapp.utils.ImageUtils;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileActivity extends AppCompatActivity {

    private TextView username_text;
    private ImageView profile_image;
    private TextView profile_description;
    private Button message_btn;
    private Button book_btn;
    private TextView profile_heading;

    DatabaseReference ref;
    FirebaseUser firebaseUser = DBUtils.currentUser();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
        if(firebaseUser == null){
            startActivity(new Intent(getApplicationContext(), StartActivity.class));
        }
        username_text = findViewById(R.id.username);
        profile_image = findViewById(R.id.profile_image);
        profile_description = findViewById(R.id.profile_description);
        message_btn = findViewById(R.id.message_btn);
        book_btn = findViewById(R.id.book_btn);
        profile_heading = findViewById(R.id.profile_heading);

        final String userid = getIntent().getStringExtra("userid");

        ref = FirebaseDatabase.getInstance().getReference("Users").child(userid);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                username_text.setText(user.getUsername());
                profile_description.setText(user.getDescription());
                ImageUtils.setProfileImage(getApplicationContext(), user.getImageURL(), profile_image);
                if(user.isStaff()){
                    profile_heading.setText("Personal Trainer Profile");
                    FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User currentUser = dataSnapshot.getValue(User.class);
                            if(!currentUser.isStaff()){
                                book_btn.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        message_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MessageActivity.class);
                intent.putExtra("userid", userid);
                startActivity(intent);
            }
        });

        book_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewBookingActivity.class);
                intent.putExtra("instructor_id", userid);
                startActivity(intent);
            }
        });

    }


    @Override
    protected void onPause() {
        super.onPause();
        DBUtils.changeStatus(this, "offline");
    }

    @Override
    protected void onResume() {
        super.onResume();
        DBUtils.changeStatus(this, "online");
    }

}