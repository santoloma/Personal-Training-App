package com.trainchatapp.fragments;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trainchatapp.MainActivity;
import com.trainchatapp.R;
import com.trainchatapp.model.User;
import com.trainchatapp.utils.DBUtils;
import com.trainchatapp.utils.RandomQuotesGenerator;


public class HomeFragment extends Fragment {

    private ImageButton bookingsBtn, profileBtn, introBtn;
    private ImageSwitcher imageSwitcher;
    private int animationCounter = 1;
    private Handler imageSwitcherHandler;
    private TextView welcomeText, quoteText;
    FirebaseUser firebaseUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);
        final MainActivity mainActivity = (MainActivity) getActivity();
        bookingsBtn = view.findViewById(R.id.bookingsBtn);
        profileBtn = view.findViewById(R.id.profileBtn);
        introBtn = view.findViewById(R.id.introBtn);
        imageSwitcher = view.findViewById(R.id.imageSwitcher);
        welcomeText = view.findViewById(R.id.welcome_text);
        quoteText = view.findViewById(R.id.quote_text);
        quoteText.setText(RandomQuotesGenerator.getQuote());
        firebaseUser = DBUtils.currentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                welcomeText.setText("Hello, " + user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        Animation in  = AnimationUtils.loadAnimation(getContext(), R.anim.left_to_right_in);
        Animation out = AnimationUtils.loadAnimation(getContext(), R.anim.left_to_right_out);
        imageSwitcher.setInAnimation(in);
        imageSwitcher.setOutAnimation(out);
        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
                                     public View makeView() {
                                         ImageView myView = new ImageView(getContext());
                                         return myView;
                                     }
                                 });



        imageSwitcherHandler = new Handler(Looper.getMainLooper());
        imageSwitcherHandler.post(new Runnable() {
            @Override
            public void run() {
                switch (animationCounter++) {
                    case 1:
                        imageSwitcher.setImageResource(R.drawable.img1);
                        break;
                    case 2:
                        imageSwitcher.setImageResource(R.drawable.img2);
                        break;
                    case 3:
                        imageSwitcher.setImageResource(R.drawable.img3);
                        break;
                    case 4:
                        imageSwitcher.setImageResource(R.drawable.img4);
                        break;
                    case 5:
                        imageSwitcher.setImageResource(R.drawable.img5);
                        break;
                    case 6:
                        imageSwitcher.setImageResource(R.drawable.img6);
                        break;
                }

               if(animationCounter == 7 ) animationCounter = 1;

                imageSwitcherHandler.postDelayed(this, 3500);
            }
        });
        bookingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.navigateToBookings();
            }
        });
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.navigateToProfile();
            }
        });
        introBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.navigateToIntro();
            }
        });
        return view;
    }
}