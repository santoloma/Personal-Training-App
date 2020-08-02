package com.trainchatapp.adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.trainchatapp.NewBookingActivity;
import com.trainchatapp.ProfileActivity;
import com.trainchatapp.MessageActivity;
import com.trainchatapp.R;
import com.trainchatapp.model.User;
import com.trainchatapp.utils.RandomTags;

import java.util.List;

public class UserProfileAdapter extends RecyclerView.Adapter<UserProfileAdapter.ViewHolder> {

    private Context mContext;
    private List<User> mUsers;


    public UserProfileAdapter(Context mContext, List<User> mUsers){
        this.mContext = mContext;
        this.mUsers = mUsers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_profile_item, parent, false);
        return new UserProfileAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final User user = mUsers.get(position);
        holder.username_text.setText(user.getUsername());
        if (user.getImageURL().equals("default")) {
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(mContext).load(user.getImageURL()).into(holder.profile_image);
        }
        if (!user.isStaff()) {
            holder.star_rating.setVisibility(View.GONE);
            holder.book_button.setVisibility(View.GONE);
            holder.tags.setVisibility(View.GONE);
        }else{
            holder.book_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, NewBookingActivity.class);
                    intent.putExtra("instructor_id", user.getId());
                    mContext.startActivity(intent);
                }
            });
        }
        holder.message_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("userid", user.getId());
                mContext.startActivity(intent);
            }
        });
        holder.user_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ProfileActivity.class);
                intent.putExtra("userid", user.getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        public RelativeLayout user_item;
        public LinearLayout tags;
        public TextView username_text;
        public ImageView profile_image;
        public LinearLayout star_rating;
        public ImageButton message_button;
        public ImageButton book_button;
        public TextView tag1;
        public TextView tag2;
        public TextView tag3;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            user_item = itemView.findViewById(R.id.user_item);
            username_text = itemView.findViewById(R.id.username);
            profile_image = itemView.findViewById(R.id.profile_image);
            star_rating = itemView.findViewById(R.id.star_rating);
            message_button = itemView.findViewById(R.id.message_button);
            book_button = itemView.findViewById(R.id.book_button);
            tags = itemView.findViewById(R.id.tags);
            tag1 = itemView.findViewById(R.id.tag1);
            tag2 = itemView.findViewById(R.id.tag2);
            tag3 = itemView.findViewById(R.id.tag3);
            String[] tags = RandomTags.get3Tags();
            tag1.setText(tags[0]);
            tag2.setText(tags[1]);
            tag3.setText(tags[2]);
        }
    }
}
