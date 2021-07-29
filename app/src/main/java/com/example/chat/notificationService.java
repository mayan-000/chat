package com.example.chat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;

import com.example.chat.friendChatPackage.messageClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class notificationService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        checkMessages check = new checkMessages();
        check.start();


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }


    public class checkMessages extends Thread {
        private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        private DatabaseReference reference = FirebaseDatabase.getInstance().getReference(
                "users/" + user.getUid() + "/LastMessages/"
        );
        public final String CHANNEL_ID = "1";

        @Override
        public void run() {
            super.run();

            while (true) {
                try {
                    Thread.sleep(1000);
//                    Log.d("Msg","Running");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                reference.get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        for (DataSnapshot data : task.getResult().getChildren()) {
                            messageClass msg = data.getValue(messageClass.class);
                                Log.d("Msg","Running");

                            if(msg.getRead()==0) {
//                                Log.d("Msg","Running");
//                                loadNotification();
                            }
                        }
                    }
                });

            }

        }

        void loadNotification(){
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,
                    "1", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager = (NotificationManager)notificationService.
                    this.getSystemService(notificationService.this.NOTIFICATION_SERVICE);

            manager.createNotificationChannel(notificationChannel);

            Notification.Builder builder = new Notification.Builder(notificationService.this,CHANNEL_ID);
            builder.setContentTitle("Title")
                    .setContentText("Messages Unread")
                    .setPriority(Notification.PRIORITY_DEFAULT);

            NotificationManagerCompat compat = NotificationManagerCompat.from(notificationService.this);
            compat.notify(1,builder.build());

        }
    }
}

