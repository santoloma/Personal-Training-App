package com.trainchatapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trainchatapp.MessageActivity;
import com.trainchatapp.R;
import com.trainchatapp.model.Message;
import com.trainchatapp.model.User;
import com.trainchatapp.utils.DateTimeUtils;

import java.util.List;

public class UserMessageAdapter extends RecyclerView.Adapter<UserMessageAdapter.ViewHolder> {

    private Context mContext;
    private List<User> mUsers;
    String theLastMsg;
    long theLastMsgTime;


    public UserMessageAdapter(Context mContext, List<User> mUsers, boolean isChat) {
        this.mContext = mContext;
        this.mUsers = mUsers;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_message_item, parent, false);
        return new UserMessageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final User user = mUsers.get(position);
        holder.username_text.setText(user.getUsername());
        if (user.getFacePic().equals("default")) {
            holder.profile_image_on.setImageResource(R.mipmap.ic_launcher);
            holder.profile_image_off.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(mContext).load(user.getFacePic()).into(holder.profile_image_on);
            Glide.with(mContext).load(user.getFacePic()).into(holder.profile_image_off);
        }

        if (user.getStatus().equals("online")) {
            holder.profile_image_off.setVisibility(View.VISIBLE);
            holder.profile_image_on.setVisibility(View.GONE);
        } else {
            holder.profile_image_off.setVisibility(View.GONE);
            holder.profile_image_on.setVisibility(View.VISIBLE);
        }

        lastMessage(user.getId(), holder.last_msg, holder.last_msg_time, holder.badge_unread);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("userid", user.getId());
                mContext.startActivity(intent);

            }
        });

    }


    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView username_text;
        public ImageView profile_image_on;
        public ImageView profile_image_off;
        public TextView last_msg;
        public TextView last_msg_time;
        public TextView badge_unread;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username_text = itemView.findViewById(R.id.username);
            profile_image_on = itemView.findViewById(R.id.profile_image_on);
            profile_image_off = itemView.findViewById(R.id.profile_image_off);
            last_msg = itemView.findViewById(R.id.last_msg);
            last_msg_time = itemView.findViewById(R.id.last_msg_time);
            badge_unread = itemView.findViewById(R.id.badge_unread);
        }
    }

    private void lastMessage(final String userid, final TextView last_message, final TextView last_message_time, final TextView badge_unread) {
        theLastMsg = "";
        theLastMsgTime = 0;
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Messages");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int unread = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Message message = snapshot.getValue(Message.class);
                    assert firebaseUser != null;
                    boolean a = message.getReceiver().equals(firebaseUser.getUid()) && message.getSender().equals(userid);
                    boolean b = message.getReceiver().equals(userid) && message.getSender().equals(firebaseUser.getUid());
                    if (a || b) {
                        theLastMsg = message.getText();
                        theLastMsgTime = message.getTimeSent();
                    }
                    if (a && message.getTimeRead() == 0) {
                        ++unread;
                    }
                }
                if (unread == 0) {
                    badge_unread.setVisibility(View.GONE);
                } else {
                    badge_unread.setText(unread + "");
                    badge_unread.setVisibility(View.VISIBLE);
                }
                last_message.setText(theLastMsg);
                theLastMsg = "";
                last_message_time.setText(DateTimeUtils.formatDate(theLastMsgTime));
                theLastMsgTime = 0;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
