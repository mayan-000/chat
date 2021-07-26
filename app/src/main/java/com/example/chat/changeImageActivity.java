package com.example.chat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class changeImageActivity extends AppCompatActivity {

    private ImageView pic;
    private ImageButton button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_image);

        pic = findViewById(R.id.profilePicImageActivity);
        button = findViewById(R.id.changeProfilePicButtonImageActivity);
        setPic();


        button.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
        });

    }

    void setPic(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(
                "users/"+user.getUid()+"/ProfilePic"
        );

        reference.get().addOnCompleteListener(task -> {
            if(task.isSuccessful())
                Picasso.get().load(task.getResult().getValue(String.class)).into(pic);
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode==RESULT_OK){
            Uri ImageUri = data.getData();
            Picasso.get().load(ImageUri).into(pic);

            StorageReference storageReference = FirebaseStorage.getInstance()
                    .getReference(FirebaseAuth.getInstance().getCurrentUser().getUid());
            StorageReference reference = storageReference.child("ProfilePic");
            reference.putFile(ImageUri).addOnSuccessListener(taskSnapshot ->
                    reference.getDownloadUrl().addOnCompleteListener(task -> {

                        changeLocation(task.getResult().toString());
            }));

        }
    }

    void changeLocation(String url){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(
                "users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()
                        +"/ProfilePic"
        );

        ref.setValue(url);
    }

}