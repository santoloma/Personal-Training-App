package com.trainchatapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.trainchatapp.model.Booking;
import com.trainchatapp.R;
import java.util.List;

public class BookingRequestAdapter extends RecyclerView.Adapter<BookingRequestAdapter.ViewHolder> {

    private Context mContext;
    private List<Booking> bookings;


    FirebaseUser firebaseUser;

    public BookingRequestAdapter(Context mContext, List<Booking> bookings) {
        this.mContext = mContext;
        this.bookings = bookings;

    }

    @NonNull
    @Override
    public BookingRequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_booking_request_item, parent, false);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        return new BookingRequestAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BookingRequestAdapter.ViewHolder holder, int position) {

    }



    @Override
    public int getItemCount() {
        return bookings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }
}

