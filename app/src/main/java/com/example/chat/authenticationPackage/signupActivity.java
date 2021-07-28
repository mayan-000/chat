package com.example.chat.authenticationPackage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.regex.Pattern;

public class signupActivity extends AppCompatActivity {

    private EditText email, password, userName;
    private Button verificationButton;
    private TextView login;
    private ImageView profilePic;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private Uri Imageuri;
    private String generatedFilePath;
    private int imageFlag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        getId();

        profilePic.setOnClickListener(v -> {
            chooseFile();
        });

        verificationButton.setOnClickListener(v -> {
            String userEmail = email.getText().toString();
            String userPassword = password.getText().toString();

            if(isValid(userEmail) && userPassword.length()>=8 && userName.length()>0)
                setSignUp(userEmail,userPassword);
            else
                Toast.makeText(this, "Wrong Format of Credentials", Toast.LENGTH_SHORT)
                        .show();
        });

        login.setOnClickListener(v -> {
            startActivity(new Intent(this, loginActivity.class));
            finish();
        });

    }

    private void getId(){
        email = findViewById(R.id.emailSignup);
        verificationButton = findViewById(R.id.verificationLinkSignup);
        login = findViewById(R.id.logInButtonSignup);
        password = findViewById(R.id.passwordSignup);
        userName = findViewById(R.id.usernameSignup);
        profilePic = findViewById(R.id.profileImageSignup);
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

    private void setSignUp(String userEmail, String userPwd){
        
        auth.createUserWithEmailAndPassword(userEmail,userPwd)
        .addOnCompleteListener(this,task -> {
            if(imageFlag==0){
                Toast.makeText(this, "Select A Profile Pic",
                        Toast.LENGTH_SHORT).show();
            }
            else if(task.isSuccessful()){
                uploadPic(userEmail);
            }
            else{
                if(task.getException() instanceof FirebaseAuthUserCollisionException){
                    Toast.makeText(this, "Email Address Already in Use",
                            Toast.LENGTH_SHORT).show();
                }
                else
                Toast.makeText(this, "Error! Please Try Again",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadPic(String userEmail){
        findViewById(R.id.progressBarSignUpActivity).setVisibility(View.VISIBLE);
        findViewById(R.id.viewScrollSignUpActivity).setVisibility(View.INVISIBLE);
        findViewById(R.id.textViewSignUpActivity).setVisibility(View.VISIBLE);

        StorageReference storageReference = FirebaseStorage.getInstance()
                .getReference(auth.getCurrentUser().getUid());
        StorageReference reference = storageReference.child("ProfilePic");
        reference.putFile(Imageuri).addOnSuccessListener(taskSnapshot ->
                reference.getDownloadUrl().addOnCompleteListener(task -> {
            generatedFilePath = task.getResult().toString();

                    sendLink(userEmail);
        }));
    }

    private void sendLink(String userEmail){
        FirebaseUser user = auth.getCurrentUser();

        user.sendEmailVerification()
            .addOnCompleteListener(this, task -> {
                if(task.isSuccessful()) {
                    Toast.makeText(this, "Verification Link Sent to " + userEmail,
                            Toast.LENGTH_LONG).show();

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference reference = database.getReference("users/"+user.getUid()
                            +"/username");
                    reference.setValue(userName.getText().toString());
                    reference = database.getReference("users/"+user.getUid()
                            +"/ProfilePic");
                    reference.setValue(generatedFilePath);
                    reference = database.getReference("users/"+user.getUid()
                            +"/email");
                    reference.setValue(userEmail);

                    auth.signOut();
                    startActivity(new Intent(this, loginActivity.class));
                    finish();
                }
                else
                    Toast.makeText(this, "Failed to Send Verification Email",
                            Toast.LENGTH_LONG).show();
            });
    }

    private void chooseFile(){
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK){
            Imageuri = data.getData();
            Picasso.get().load(Imageuri).into(profilePic);
            imageFlag=1;
        }
    }
}