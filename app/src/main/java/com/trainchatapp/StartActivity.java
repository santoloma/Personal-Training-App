package com.trainchatapp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class StartActivity extends AppCompatActivity {

    private Button btn_register, btn_login, btn_info;
    private static int count = 0;

    FirebaseUser firebaseUser;

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Dialog d = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        d.setContentView(R.layout.splash_screen);
        ++count;
        if (count == 1) {
            d.show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setUp(d);
                }
            }, 2000);
        }else{
            setUp(d);
        }
    }

    private void setUp(Dialog d){
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            if (count == 1) {
                d.dismiss();
            }
            setContentView(R.layout.activity_start);
            btn_register = findViewById(R.id.btn_register);
            btn_login = findViewById(R.id.btn_login);
            btn_info = findViewById(R.id.btn_info);
            btn_register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(StartActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
            });
            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });
            btn_info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(StartActivity.this, InfoActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

}