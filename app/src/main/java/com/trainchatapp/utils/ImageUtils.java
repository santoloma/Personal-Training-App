package com.trainchatapp.utils;

import android.content.Context;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.trainchatapp.R;


public class ImageUtils {

    public static void setProfileImage(Context context, String url, ImageView imgView){

        if(url.equals("default")){
            imgView.setImageResource(R.mipmap.ic_launcher);
        }else{
            if(context != null) {
                Glide.with(context).load(url).into(imgView);
            }
        }

    }
}
