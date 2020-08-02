package com.trainchatapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trainchatapp.R;
import com.trainchatapp.adapters.BookingAdapter;
import com.trainchatapp.model.Booking;
import com.trainchatapp.utils.DateTimeUtils;
import java.util.ArrayList;
import java.util.List;


public class BookingsListFragment extends Fragment {

    private RecyclerView recyclerView;
    private BookingAdapter bookingAdapter;
    private List<Booking> bookings = new ArrayList<>();

    private TextView heading;


    FirebaseUser firebaseUser;
    DatabaseReference ref;

    private boolean old;
    private static final String OLD = "old";


    public BookingsListFragment() {
    }


    public static BookingsListFragment newInstance(boolean _old) {
        BookingsListFragment fragment = new BookingsListFragment();
        Bundle args = new Bundle();
        args.putBoolean(OLD, _old);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            old = getArguments().getBoolean(OLD);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bookings_list, container, false);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        heading = view.findViewById(R.id.heading);
        if(old) {
            heading.setText("Bookings History");
        }

        ref = FirebaseDatabase.getInstance().getReference("Bookings");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bookings.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Booking booking = snapshot.getValue(Booking.class);
                    if (booking.getClientId() != null && booking.getTrainerId() != null) {
                        if (booking.getClientId().equals(firebaseUser.getUid()) || booking.getTrainerId().equals(firebaseUser.getUid())) {
                            boolean inThePast = DateTimeUtils.isInThePast(booking.getDate(), booking.getDuration());
                            if (old) {
                                if(inThePast) {
                                    bookings.add(booking);
                                }
                            }else{
                                if(!inThePast){
                                    bookings.add(booking);
                                }
                            }
                        }
                    }
                }
                bookingAdapter = new BookingAdapter(getContext(), bookings, false);
                recyclerView.setAdapter(bookingAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }
}