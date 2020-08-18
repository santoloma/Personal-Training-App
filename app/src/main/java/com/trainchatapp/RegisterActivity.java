package com.trainchatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.trainchatapp.model.User;

public class RegisterActivity extends AppCompatActivity {

    private EditText username_text;
    private EditText email_text;
    private EditText password_text;
    private EditText confirm_password_text;
    private Button btn_register;
    private RadioButton staffTrue_btn;

    FirebaseAuth auth;
    DatabaseReference db_ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        FloatingActionButton back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, StartActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        username_text = findViewById(R.id.register_username);
        email_text = findViewById(R.id.register_email);
        password_text = findViewById(R.id.register_password);
        confirm_password_text = findViewById(R.id.register_confirm_password);
        btn_register = findViewById(R.id.btn_register);
        staffTrue_btn = findViewById(R.id.staffTrue_btn);

        db_ref = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = username_text.getText().toString();
                String email = email_text.getText().toString();
                String password = password_text.getText().toString();
                String confirm_password = confirm_password_text.getText().toString();
                String description = "I am " + username + "\n";

                boolean isStaff = false;
                boolean isVerified = false;
                if(staffTrue_btn.isChecked()){
                    isStaff = true;
                    description += "I am a personal trainer.";
                }else{
                    isVerified = true;
                    description += "I am looking for a personal trainer.";
                }

                if (!username.matches("^[a-zA-Z0-9._-]{3,}$")) {
                    Toast.makeText(RegisterActivity.this, "Invalid username.", Toast.LENGTH_SHORT).show();
                } else if (!email.matches("^(.+)@(.+)$")) {
                    Toast.makeText(RegisterActivity.this, "Invalid email.", Toast.LENGTH_SHORT).show();
                } else if (password.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Enter a valid password.", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirm_password)) {
                    Toast.makeText(RegisterActivity.this, "Password don't match.", Toast.LENGTH_SHORT).show();
                } else {
                    register(username, email, password, description, isStaff, isVerified);
                }
            }
        });
    }

    private void register(final String username, String email, String password, final String description, final boolean staff, final boolean verified) {

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            assert user != null;
                            String userid = user.getUid();


                            User newUser = new User(userid, username, "default", "default",
                                              username.toLowerCase(),
                                         description, staff, verified, "", "");


                            db_ref.child("Users").child(userid).setValue(newUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("oops", "Database transaction failed");
                                        }
                                    });
                        } else {
                            Toast.makeText(RegisterActivity.this, "Unsuccessful registration", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}