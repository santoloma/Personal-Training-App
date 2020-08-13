package com.trainchatapp.utils;
import android.content.Context;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.trainchatapp.R;

public class ImageUtils {

    private static void setImage(Context context, String url, ImageView imgView, boolean isBody){
        if(url.equals("default")){
            System.out.println("default image used");
            if(isBody){
                System.out.println("body image");
                imgView.setImageResource(R.drawable.ic_body);
            }else{
                System.out.println("face image");
                imgView.setImageResource(R.drawable.ic_face);
            }
        }else{
            if(context != null) {
                Glide.with(context).load(url).into(imgView);
            }
        }
    }

    public static void setProfileImage(Context context, String url, ImageView imgView){
        setImage(context, url, imgView, false);
    }

    public static void setBodyImage(Context context, String url, ImageView imgView){
        setImage(context, url, imgView, true);
    }
}
