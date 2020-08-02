package com.trainchatapp.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.trainchatapp.model.Messagelist;
import com.trainchatapp.R;
import com.trainchatapp.model.User;
import com.trainchatapp.adapters.UserMessageAdapter;
import com.trainchatapp.utils.DBUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


public class ChatsFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserMessageAdapter userMessageAdapter;
    private final HashMap<User, Long> mUsers = new HashMap<>();
    private List<Messagelist> chatList = new ArrayList<>();

    FirebaseUser firebaseUser;
    DatabaseReference ref;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chats, container, false);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        ref = FirebaseDatabase.getInstance().getReference("Messagelist").child(firebaseUser.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Messagelist messagelist = snapshot.getValue(Messagelist.class);
                    chatList.add(messagelist);
                }
                chatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DBUtils.updateToken(FirebaseInstanceId.getInstance().getToken());

        return view;
    }

    private void chatList() {
        ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    for(Messagelist messagelist : chatList){

                        if(user.getId().equals(messagelist.getId())){
                            mUsers.put(user, messagelist.getLastMsgTime());
                        }
                    }
                }
                ArrayList<User> usrs = new ArrayList<User>(mUsers.keySet());

                Collections.sort(usrs, new Comparator<User>() {
                     @Override
                     public int compare(User o1, User o2) {
                        return mUsers.get(o2).compareTo(mUsers.get(o1));
                     }
                 });

                userMessageAdapter = new UserMessageAdapter(getContext(), usrs, true);
                recyclerView.setAdapter(userMessageAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}