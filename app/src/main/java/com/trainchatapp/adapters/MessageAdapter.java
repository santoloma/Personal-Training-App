package com.trainchatapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AlignmentSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
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
import com.trainchatapp.utils.DateTimeUtils;
import com.trainchatapp.model.Message;
import com.trainchatapp.R;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MESSAGE_LEFT = 0;
    public static final int MESSAGE_RIGHT = 1;
    private Context mContext;
    private List<Message> mMessages;
    private String imageURL;

    FirebaseUser firebaseUser;


    public MessageAdapter(Context mContext, List<Message> mMessages, String imageURL){
        this.mContext = mContext;
        this.mMessages = mMessages;
        this.imageURL = imageURL;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MESSAGE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }else{
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        Message message = mMessages.get(position);
        SpannableString spannable = new SpannableString(message.getText() + "\n" + DateTimeUtils.formatDate(message.getTimeSent()));
        spannable.setSpan(new ForegroundColorSpan(Color.rgb(85,85,85)), message.getText().length(), spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new RelativeSizeSpan(0.8125f), message.getText().length(), spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_OPPOSITE), message.getText().length(), spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.show_message.setText(spannable);
        if(imageURL.equals("default")){
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        }else{
            Glide.with(mContext).load(imageURL).into(holder.profile_image);
        }

        if(position == mMessages.size()-1){
            if(message.getTimeRead() != 0l){
                holder.text_seen.setText("Seen " + DateTimeUtils.formatDate(message.getTimeRead()));
            }else{
                holder.text_seen.setText("Delivered");
            }
        }else{
            holder.text_seen.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView show_message;
        public ImageView profile_image;
        public TextView text_seen;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            show_message = itemView.findViewById(R.id.show_message);
            profile_image = itemView.findViewById(R.id.profile_image);
            text_seen = itemView.findViewById(R.id.text_seen);
        }
    }


    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(mMessages.get(position).getSender().equals(firebaseUser.getUid())){
            return MESSAGE_RIGHT;
        }else{
            return MESSAGE_LEFT;
        }
    }
}

