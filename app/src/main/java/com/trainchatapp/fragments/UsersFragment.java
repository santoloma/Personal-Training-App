package com.trainchatapp.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.trainchatapp.R;
import com.trainchatapp.adapters.UserProfileAdapter;
import com.trainchatapp.model.User;
import com.trainchatapp.utils.DBUtils;
import java.util.ArrayList;
import java.util.List;

public class UsersFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserProfileAdapter userAdapter;
    private List<User> mUsers;
    private EditText search_users;

    DatabaseReference users_ref = FirebaseDatabase.getInstance().getReference("Users");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mUsers = new ArrayList<>();

        readUsers();

        search_users = view.findViewById(R.id.search_users);
        search_users.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchUsers(s.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        return view;
    }

    private void searchUsers(String s) {
        final FirebaseUser firebaseUser = DBUtils.currentUser();

        final Query query = users_ref
                .orderByChild("search")
                .startAt(s)
                .endAt(s + "\uf8ff");
        users_ref.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final User currentUser = snapshot.getValue(User.class);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        mUsers.clear();
                        for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                            User u = snapshot.getValue(User.class);
                            if(firebaseUser != null && u != null){
                                if (!firebaseUser.getUid().equals(u.getId()) && u.isVerified()
                                        && (currentUser.isStaff() ^ u.isStaff())) {
                                    mUsers.add(u);
                                }
                            }
                        }
                        userAdapter = new UserProfileAdapter(getContext(), mUsers);
                        recyclerView.setAdapter(userAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void readUsers() {
        final FirebaseUser firebaseUser = DBUtils.currentUser();
        users_ref.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final User currentUser = snapshot.getValue(User.class);
                users_ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (search_users.getText().toString().equals("")) {
                            mUsers.clear();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                User user = snapshot.getValue(User.class);
                                if(firebaseUser != null && user != null) {
                                    if (!user.getId().equals(firebaseUser.getUid()) && user.isVerified()
                                            && (currentUser.isStaff() ^ user.isStaff())) {
                                        mUsers.add(user);
                                    }
                                }
                            }
                            userAdapter = new UserProfileAdapter(getContext(), mUsers);
                            recyclerView.setAdapter(userAdapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}