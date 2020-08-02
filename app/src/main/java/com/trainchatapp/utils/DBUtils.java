package com.trainchatapp.utils;

import android.app.NotificationManager;
import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.trainchatapp.notifications.Token;

import java.util.HashMap;

public class DBUtils {

    public static void changeStatus(Context context, String status){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
            if(ref != null) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("status", status);
                ref.updateChildren(map);
                if (status.equals("online")) {
                    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancelAll();

                }
            }
        }
    }


    public static FirebaseUser currentUser(){
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public static void updateToken(String token){
        System.out.println("Updating token");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(currentUser().getUid()).setValue(token1);
    }

}
