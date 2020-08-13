package com.trainchatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.trainchatapp.adapters.MessageAdapter;
import com.trainchatapp.fragments.APIService;
import com.trainchatapp.model.Message;
import com.trainchatapp.model.User;
import com.trainchatapp.notifications.Client;
import com.trainchatapp.notifications.Data;
import com.trainchatapp.notifications.MyResponse;
import com.trainchatapp.notifications.Sender;
import com.trainchatapp.notifications.Token;
import com.trainchatapp.utils.DBUtils;
import com.trainchatapp.utils.ImageUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity {

    private CircleImageView profile_image;
    private TextView username_text;
    private ImageButton btn_send;
    private EditText text_send;

    private FirebaseUser firebaseUser = DBUtils.currentUser();
    private DatabaseReference ref;

    private Intent intent;
    private MessageAdapter messageAdapter;
    private List<Message> mMessages;
    private RecyclerView recyclerView;
    private ValueEventListener seenListener;

    String userid;
    boolean notify = false;


    APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        if(firebaseUser == null){
            startActivity(new Intent(getApplicationContext(), StartActivity.class));
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(MessageActivity.this, MainActivity.class)
               .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        profile_image = findViewById(R.id.profile_image);
        username_text = findViewById(R.id.username);
        btn_send = findViewById(R.id.btn_send);
        text_send = findViewById(R.id.text_send);

        intent = getIntent();

        userid = intent.getStringExtra("userid");

        ref = FirebaseDatabase.getInstance().getReference("Users").child(userid);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                username_text.setText(user.getUsername());
                ImageUtils.setProfileImage(getApplicationContext(), user.getFacePic(), profile_image);
                readMessage(firebaseUser.getUid(), userid, user.getFacePic());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startProfileActivity(userid);
            }
        });

        username_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startProfileActivity(userid);
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify = true;
                String message = text_send.getText().toString();
                if(!message.isEmpty()){
                    sendMessage(firebaseUser.getUid(), userid, message);
                    text_send.setText("");
                }else{
                    Toast.makeText(MessageActivity.this, "Message body is empty.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        seenMessage(userid);
    }

    private void startProfileActivity(String userid){
        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
        intent.putExtra("userid", userid);
        startActivity(intent);
    }

    private void seenMessage(final String userid){
        ref = FirebaseDatabase.getInstance().getReference("Messages");
        final long timeRead = new Date().getTime();
        seenListener = ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Message message = snapshot.getValue(Message.class);
                    if (message.getReceiver().equals(firebaseUser.getUid())
                            && message.getSender().equals(userid) && message.getTimeRead()==0l) {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("timeRead", timeRead);
                        snapshot.getRef().updateChildren(map);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    private void sendMessage(final String from, final String to, String message){
        text_send.getText();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        final String userid = intent.getStringExtra("userid");
        Date date = new Date();
        long time = date.getTime();
        Message msg = new Message(from, to, message, time, 0l);   //not seen yet
        ref.child("Messages").push().setValue(msg);
        recordChat(firebaseUser.getUid(), userid, time);
        recordChat(userid, firebaseUser.getUid(), time);

        final String mssg = message;

        ref = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (notify) {
                    System.out.println("notifying");
                    sendNotifiaction(to, user.getUsername(), mssg);
                }else{
                    System.out.println("not notifying " + user.getStatus());

                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void recordChat(final String id1, final String id2, final  long time) {
        final DatabaseReference chatlistRef = FirebaseDatabase.getInstance().getReference("Messagelist")
                .child(id1)
                .child(id2);

        chatlistRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", id2);
                    map.put("lastMsgTime", time);
                    chatlistRef.setValue(map);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void readMessage(final String myid, final String userid, final String imageURL){
        mMessages = new ArrayList<>();
        System.out.println("readMessage method");

        ref = FirebaseDatabase.getInstance().getReference("Messages");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println("Reading messages");
                mMessages.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                    final Message message = snapshot.getValue(Message.class);
                    if((message.getReceiver().equals(myid) && message.getSender().equals(userid)) ||
                            (message.getReceiver().equals(userid) && message.getSender().equals(myid))){
                        mMessages.add(message);
                    }
                    messageAdapter = new MessageAdapter(MessageActivity.this, mMessages, imageURL);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void sendNotifiaction(String receiver, final String username, final String message){
        System.out.println("send notif");
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(firebaseUser.getUid(), R.mipmap.send_icon_round, username+": "+message, "New Message",
                            userid);

                    Sender sender = new Sender(data, token.getToken());
                    System.out.println(sender);

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code() == 200){
                                        if (response.body().success != 1){
                                            Toast.makeText(MessageActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onResume() {
        super.onResume();
        DBUtils.changeStatus(this, "online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        ref.removeEventListener(seenListener);
        DBUtils.changeStatus(this, "offline");
    }
}