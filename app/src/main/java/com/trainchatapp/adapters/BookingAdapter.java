package com.trainchatapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trainchatapp.model.Booking;
import com.trainchatapp.R;
import com.trainchatapp.model.User;
import com.trainchatapp.utils.DateTimeUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {

    private Context mContext;
    private List<Booking> bookings;
    private boolean old;

    FirebaseUser firebaseUser;

    public BookingAdapter(Context mContext, List<Booking> bookings, boolean old) {
        this.mContext = mContext;
        this.bookings = bookings;
        this.old = old;
    }

    @NonNull
    @Override
    public BookingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_booking_item, parent, false);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        return new BookingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BookingAdapter.ViewHolder holder, int position) {
        final Booking booking = bookings.get(position);
        DatabaseReference trainerRef = FirebaseDatabase.getInstance().getReference("Users").child(booking.getTrainerId());
        final DatabaseReference clientRef = FirebaseDatabase.getInstance().getReference("Users").child(booking.getClientId());
        if (booking.getTimeCancelled() != 0) {
            holder.cancel_btn.setText("Rebook");
            holder.amend_btn.setVisibility(View.GONE);
        }
        holder.location.setText("Location: " + booking.getLocation());
        holder.notes.setText(booking.getNotes());
        final DatabaseReference _booking = FirebaseDatabase.getInstance().getReference("Bookings")
                .child((booking.getTrainerId() + booking.getClientId() + booking.getDate()
                        + booking.getTimeFrom() + booking.getDuration()).hashCode() + "");
        holder.cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> map = new HashMap<>();
                if (booking.getTimeCancelled() != 0) {
                    map.put("timeCancelled", 0);
                    map.put("accepted", false);
                    sendNotification(true);
                    holder.cancel_btn.setText("Book");
                    holder.amend_btn.setVisibility(View.VISIBLE);
                } else {
                    map.put("timeCancelled", new Date().getTime());
                    sendNotification(false);
                    holder.cancel_btn.setText("Rebook");
                    holder.amend_btn.setVisibility(View.GONE);
                }
                _booking.updateChildren(map);

            }
        });

        holder.delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _booking.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            System.out.println("successfully deleted");
                        }

                    }
                });

            }
        });

        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.more.setVisibility(View.GONE);
                holder.moreLayout.setVisibility(View.VISIBLE);
            }
        });

        holder.less.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.more.setVisibility(View.VISIBLE);
                holder.moreLayout.setVisibility(View.GONE);
            }
        });


        trainerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final User trainer = dataSnapshot.getValue(User.class);
                if(!firebaseUser.getUid().equals(trainer.getId())){
                    holder.booking_text.setText("Booking with " + trainer.getUsername());
                }else{
                    clientRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User client = snapshot.getValue(User.class);
                            holder.booking_text.setText("Booking with " + client.getUsername());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                holder.date.setText("Date: " + booking.getDate());
                holder.time.setText("Time: " + booking.getTimeFrom() + "\nDuration:" + booking.getDuration());

                if (old) {
                    if (booking.getTimeCancelled() != 0) {
                        SpannableString spannable = new SpannableString("Status: Cancelled");
                        spannable.setSpan(new ForegroundColorSpan(Color.rgb(255, 51, 0)), 8, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        holder.status.setText(spannable);
                    } else {
                        SpannableString spannable = new SpannableString("Status: Completed");
                        spannable.setSpan(new ForegroundColorSpan(Color.rgb(0, 102, 0)), 8, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        holder.status.setText(spannable);
                    }
                    holder.cancel_btn.setVisibility(View.GONE);
                    holder.amend_btn.setVisibility(View.GONE);
                } else {
                    if (booking.getTimeCancelled() != 0) {
                        SpannableString spannable = new SpannableString("Status: Cancelled");
                        spannable.setSpan(new ForegroundColorSpan(Color.rgb(255, 51, 0)), 8, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        holder.status.setText(spannable);
                    } else {
                        System.out.println("Booking is declined" + booking.isDeclined());
                        if (!booking.isDeclined()) {
                            if (booking.isAccepted()) {
                                SpannableString spannable = new SpannableString("Status: Confirmed");
                                spannable.setSpan(new ForegroundColorSpan(Color.rgb(46, 184, 46)), 8, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                holder.status.setText(spannable);
                            } else {
                                SpannableString spannable = new SpannableString("Status: Requested");
                                spannable.setSpan(new ForegroundColorSpan(Color.rgb(0, 102, 204)), 8, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                holder.status.setText(spannable);
                                holder.receipt_btn.setVisibility(View.GONE);
                            }
                        }
                        else{
                            SpannableString spannable = new SpannableString("Status: Declined");
                            spannable.setSpan(new ForegroundColorSpan(Color.rgb(255, 0, 0)), 8, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            holder.status.setText(spannable);
                            holder.amend_btn.setVisibility(View.GONE);
                            holder.cancel_btn.setVisibility(View.GONE);
                            holder.receipt_btn.setVisibility(View.GONE);
                            holder.delete_btn.setVisibility(View.VISIBLE);
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendNotification(boolean b) {
    }


    @Override
    public int getItemCount() {
        return bookings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public  TextView booking_text;
        public  TextView date;
        public  TextView time;
        public  TextView status;
        public  TextView location;
        public  TextView notes;
        public  Button cancel_btn;
        public  Button amend_btn;
        public  Button receipt_btn;
        public  Button delete_btn;
        public  TextView more;
        public  TextView less;
        public LinearLayout moreLayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            booking_text = itemView.findViewById(R.id.booking_text);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            status = itemView.findViewById(R.id.status);
            location =itemView.findViewById(R.id.location);
            notes = itemView.findViewById(R.id.notes);
            cancel_btn = itemView.findViewById(R.id.cancel_btn);
            amend_btn = itemView.findViewById(R.id.amend_btn);
            receipt_btn = itemView.findViewById(R.id.receipt_btn);
            delete_btn = itemView.findViewById(R.id.delete_btn);
            more = itemView.findViewById(R.id.more);
            less = itemView.findViewById(R.id.less);
            moreLayout = itemView.findViewById(R.id.moreLayout);
        }
    }
}

