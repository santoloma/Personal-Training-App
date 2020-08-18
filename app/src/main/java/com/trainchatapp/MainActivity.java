package com.trainchatapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trainchatapp.adapters.ViewPagerAdapter;
import com.trainchatapp.model.Message;
import com.trainchatapp.model.User;
import com.trainchatapp.utils.DBUtils;
import com.trainchatapp.utils.ImageUtils;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private TextView badge_message;
    private TextView badge_notification;
    private RelativeLayout badge_message_layout;
    private RelativeLayout badge_notification_layout;
    FirebaseUser firebaseUser =  DBUtils.currentUser();
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.nav_view);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,
                R.id.nav_bookings,
                R.id.nav_myprofile,
                R.id.nav_users,
                R.id.nav_messages,
                R.id.nav_notifications,
                R.id.nav_intro,
                R.id.nav_application,
                R.id.nav_schedule)
                .setDrawerLayout(drawer)
                .build();

        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navigationView.removeHeaderView(navigationView.findViewById(R.id.nav_home));
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        LinearLayout sideNavLayout = (LinearLayout)navigationView.getHeaderView(0);

        final CircleImageView profile_picture = sideNavLayout.findViewById(R.id.imageView);
        final TextView username_text = sideNavLayout.findViewById(R.id.textView);

        badge_message = findViewById(R.id.badge_message);
        badge_notification = findViewById(R.id.badge_notification);
        badge_message_layout = findViewById(R.id.badge_message_layout);
        badge_notification_layout = findViewById(R.id.badge_notification_layout);

        badge_message_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.nav_messages);
            }
        });

        badge_notification_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.nav_notifications);
            }
        });

        ref = FirebaseDatabase.getInstance().getReference("Messages");


        DatabaseReference r = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        r.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User currentUser = snapshot.getValue(User.class);
                if(currentUser.isStaff()){
                    navigationView.getMenu().getItem(8).setVisible(true);
                }else{
                    navigationView.getMenu().getItem(4).setVisible(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
                int unread = 0;
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Message message = snapshot.getValue(Message.class);
                    if(message.getReceiver().equals(firebaseUser.getUid()) && message.getTimeRead() == 0){
                        ++unread;
                    }
                }
                if(unread == 0){
                    badge_message.setVisibility(View.GONE);
                }else{
                    badge_message.setVisibility(View.VISIBLE);
                    badge_message.setText(unread+"");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        ref = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                username_text.setText(user.getUsername());
                System.out.println(user);
                ImageUtils.setProfileImage(getApplicationContext(), user.getFacePic(), profile_picture);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.logout:
                startActivity(new Intent(MainActivity.this, StartActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                FirebaseAuth.getInstance().signOut();
                return true;
            case R.id.action_terms_and_conditions:
                startActivity(new Intent(MainActivity.this, GeneralTermsAndConditions.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                return true;
            case R.id.action_settings:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }
        return false;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}