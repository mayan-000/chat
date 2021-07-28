package com.example.chat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.Context.NOTIFICATION_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

public class checkMessages extends Thread {
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference(
            "users/"+user.getUid()+"/"
    );
    public final String CHANNEL_ID = "1";

    @Override
    public void run() {
        super.run();

        while (true){
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }



        }

    }

    

}
