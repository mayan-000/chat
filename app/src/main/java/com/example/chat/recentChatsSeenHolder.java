package com.example.chat;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class recentChatsSeenHolder extends RecyclerView.ViewHolder {

    private TextView nameText, messageText, timeText;
    private ImageView profileImage;

    public recentChatsSeenHolder(View itemView) {
        super(itemView);

        nameText = itemView.findViewById(R.id.friendNameRecentSeen);
        messageText = itemView.findViewById(R.id.friendMessageRecentSeen);
        timeText = itemView.findViewById(R.id.friendMessageTimeRecentSeen);
        profileImage = itemView.findViewById(R.id.friendProfilePicRecentSeen);
    }

    void bind(messageClassNew message){
//       message, read, time, date, type, Uid

        messageText.setText(message.getMessage());
        timeText.setText(message.getDate()+" "+message.getTime());


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(
                "users/"+message.getUid()+"/username/");

        reference.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                nameText.setText(task.getResult().getValue(String.class));
            }
        });

        reference = FirebaseDatabase.getInstance().getReference(
                "users/"+message.getUid()+"/ProfilePic/");

        reference.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Picasso.get().load(task.getResult().getValue(String.class))
                        .into(profileImage);
            }
        });
    }

}
