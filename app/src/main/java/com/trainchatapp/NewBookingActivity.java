package com.trainchatapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.trainchatapp.fragments.APIService;
import com.trainchatapp.fragments.subfragments.DatePickerFragment;
import com.trainchatapp.fragments.subfragments.DurationPickerFragment;
import com.trainchatapp.fragments.subfragments.TimePickerFragment;
import com.trainchatapp.model.Booking;
import com.trainchatapp.model.User;
import com.trainchatapp.notifications.Client;
import com.trainchatapp.notifications.Data;
import com.trainchatapp.notifications.MyResponse;
import com.trainchatapp.notifications.Sender;
import com.trainchatapp.notifications.Token;
import com.trainchatapp.utils.DBUtils;

import java.util.Date;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewBookingActivity extends AppCompatActivity {

    private CircleImageView instructor_photo;
    private TextView date;
    private TextView time_from;
    private TextView duration;
    private EditText location;
    private EditText notes;
    private Button book_btn;
    private String instructor_id;

    APIService apiService;
    FirebaseUser firebaseUser;
    DatabaseReference ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_booking);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewBookingActivity.this, MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });


        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);


        instructor_photo = findViewById(R.id.instructor_photo);
        date = findViewById(R.id.date);
        time_from = findViewById(R.id.time_from);
        duration = findViewById(R.id.duration);
        location = findViewById(R.id.location);
        notes = findViewById(R.id.notes);
        book_btn = findViewById(R.id.book_btn);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        instructor_id = getIntent().getStringExtra("instructor_id");
        ref = FirebaseDatabase.getInstance().getReference("Users").child(instructor_id);

        ref.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User instructor = dataSnapshot.getValue(User.class);
                if(instructor.getImageURL().equals("default")){
                    instructor_photo.setImageResource(R.mipmap.ic_launcher);
                }else{
                    Glide.with(getApplicationContext()).load(instructor.getImageURL()).into(instructor_photo);
                }
                getSupportActionBar().setTitle("New Booking with " + instructor.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment dateFragment = new DatePickerFragment(date);
                dateFragment.showNow(getSupportFragmentManager(), "datePicker");

            }
        });

        time_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment timeFragment = new TimePickerFragment(time_from);
                timeFragment.showNow(getSupportFragmentManager(), "timePicker");
            }
        });

        duration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DurationPickerFragment durationFragment = new DurationPickerFragment(duration);
                durationFragment.showNow(getSupportFragmentManager(), "timePicker");
            }
        });

        notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notes.requestFocus();
            }
        });


        book_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                String _date = date.getText().toString();
                String timeFrom = time_from.getText().toString();
                String _duration = duration.getText().toString();
                String _location = location.getText().toString();
                String notes_str = notes.getText().toString();

                sendBookingRequest(_date, timeFrom, _duration, notes_str, _location);
            }
        });

        DBUtils.updateToken(FirebaseInstanceId.getInstance().getToken());
    }

    private void sendBookingRequest(String date, String timeFrom, String duration, String notes, String location){
        long time = new Date().getTime();
        Booking booking = new Booking(instructor_id, firebaseUser.getUid(), time, date, timeFrom, duration,
                            false, 0l, notes, false, location);
        ref = FirebaseDatabase.getInstance().getReference("Bookings")
                .child((instructor_id + firebaseUser.getUid() + date + timeFrom + duration).hashCode()+"");
        ref.setValue(booking).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Booking successfully created.", Toast.LENGTH_LONG);
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }else{
                    Toast.makeText(getApplicationContext(), task.getException()+"", Toast.LENGTH_LONG);
                }
            }
        });
        sendNotifiaction(instructor_id, firebaseUser.getUid());
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


    private void sendNotifiaction(final String receiver, final String username){
        System.out.println("send notif");
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(firebaseUser.getUid(), R.mipmap.send_icon_round, username +" sent you a booking request.", "Booking Request",
                            receiver);

                    Sender sender = new Sender(data, token.getToken());
                    System.out.println(sender);

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code() == 200){
                                        if (response.body().success != 1){
                                            Toast.makeText(NewBookingActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                                            System.out.println("failed");
                                        }else{
                                            System.out.println("success");
                                        }
                                    }else{
                                        System.out.println(response.code()+"meow");
                                    }
                                }
                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {
                                    System.out.println("ooops");

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("cancelled " + databaseError.getDetails());

            }
        });
    }
}