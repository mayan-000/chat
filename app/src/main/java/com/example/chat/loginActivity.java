package com.example.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.regex.Pattern;

public class loginActivity extends AppCompatActivity {

    private EditText email, password;
    private TextView logIn, signUp, forgotPassword;
    private View googleSignIn;
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getId();



//        Login button
        logIn.setOnClickListener(v -> {

            String userEmail = email.getText().toString();
            String userPassword = password.getText().toString();
//            Checks email password

            if(isValid(userEmail) && userPassword.length()>=8)
                setLogIn(userEmail,userPassword);
            else
                Toast.makeText(this, "Wrong Format of Credentials", Toast.LENGTH_SHORT).show();
        });



        signUp.setOnClickListener(v -> {
            startActivity(new Intent(this, signupActivity.class));
            finish();
        });



        forgotPassword.setOnClickListener(v -> {
            startActivity(new Intent(this, forgotActivity.class));
            finish();
        });




    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();

        if(user!=null){
//            Got to user's Chat section
            if(user.isEmailVerified()) {
                Intent i = new Intent(this, chatSectionActivity.class);
//                i.putExtra("friendUserUid","D8Zi9k27xdfvo9mQl3dVX6DKvyc2");
                startActivity(i);                finish();
            }
            else{
                {
//                    Delete name
                    FirebaseDatabase.getInstance().getReference()
                            .child("users").child(user.getUid()).removeValue();
//                    Delete profilePic
                    FirebaseStorage.getInstance().getReference(user.getUid()).delete();
                }
                auth.signOut();
                user.delete();
            }
        }

    }

    private void getId(){
        email = findViewById(R.id.emailLogin);
        password = findViewById(R.id.passwordLogin);
        logIn = findViewById(R.id.logInButton);
        signUp = findViewById(R.id.signUpLogin);
        forgotPassword = findViewById(R.id.forgotPasswordLogin);
        googleSignIn = findViewById(R.id.signInGoogleLogin);
    }

    private void setLogIn(String userEmail, String userPwd){
//        Log in
        auth.signInWithEmailAndPassword(userEmail,userPwd)
            .addOnCompleteListener(this,task -> {
                if(task.isSuccessful()){
                    FirebaseUser user = auth.getCurrentUser();
//                    Check is email verified is not delete account
                    if(user.isEmailVerified()){
                        Toast.makeText(this, "Login Done", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(this, friendChat.class);
                        i.putExtra("friendUserUid","ku0EFV3pg9gYUcvU2w1UPCFcyPy2");
                        startActivity(i);
                        finish();
                    }
                    else{
                        Toast.makeText(this, "No User Found", Toast.LENGTH_SHORT).show();
                        email.setText("");
                        password.setText("");
                        {
                            FirebaseDatabase.getInstance().getReference()
                                    .child("users").child(user.getUid()).removeValue();
                            FirebaseStorage.getInstance().getReference(user.getUid()).delete();
                        }
                        auth.signOut();
                        user.delete();
                    }
                }
                else{
                    Toast.makeText(this, "Wrong Password or Email",
                            Toast.LENGTH_SHORT).show();
                }
            }
        );
    }

    private boolean isValid(String email){
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }


}