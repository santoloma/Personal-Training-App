package com.trainchatapp.utils;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.trainchatapp.notifications.Token;


public class DBUtils {


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
