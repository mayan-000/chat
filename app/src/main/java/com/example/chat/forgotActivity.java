package com.example.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class forgotActivity extends AppCompatActivity {

    private Button forgotButton;
    private EditText email;
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        getId();
    
        forgotButton.setOnClickListener(v -> {
            auth.sendPasswordResetEmail(email.getText().toString())
                    .addOnCompleteListener(task -> {
                       if (task.isSuccessful()){
                           Toast.makeText(this, "Email Sent", Toast.LENGTH_SHORT).show();
                           startActivity(new Intent(this, loginActivity.class));
                           finish();
                       }
                       else{
                           Toast.makeText(this, "Error Occurred", Toast.LENGTH_SHORT).show();
                       }
                    });
        });

    }

    private void getId(){
        email = findViewById(R.id.emailForgot);
        forgotButton = findViewById(R.id.forgotPasswordForgot);
    }
}