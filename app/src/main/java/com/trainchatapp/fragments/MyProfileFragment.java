package com.trainchatapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.trainchatapp.EditProfileActivity;
import com.trainchatapp.R;
import com.trainchatapp.model.User;
import com.trainchatapp.utils.DBUtils;
import com.trainchatapp.utils.ImageUtils;
import de.hdodenhof.circleimageview.CircleImageView;


public class MyProfileFragment extends Fragment {

    private CircleImageView profile_image;
    private TextView username_text;
    private ImageButton add_photo;
    private TextView profile_description;

    FirebaseUser firebaseUser = DBUtils.currentUser();
    DatabaseReference ref;
    StorageReference storageReference;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_myprofile, container, false);
        profile_image = view.findViewById(R.id.profile_image);
        username_text = view.findViewById(R.id.username);
        profile_description = view.findViewById(R.id.profile_description);
        add_photo = view.findViewById(R.id.add_photo);

        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        ref = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                if (user.getId().equals(firebaseUser.getUid())) {
                    username_text.setText(user.getUsername());
                }
                ImageUtils.setProfileImage(getActivity(), user.getImageURL(), profile_image);
                profile_description.setText(user.getDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getContext(), EditProfileActivity.class));
            }
        });

        return view;
    }
}